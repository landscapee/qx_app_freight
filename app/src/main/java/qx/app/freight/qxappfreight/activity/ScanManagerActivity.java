package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.core.ScanBoxView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.LaserAndZxingBean;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.dialog.InputDialog;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * activity  通过intent回传
 * <p>
 * fragment 通过 EventBus 回传（传入的flag 就是 接收EventBus 判断的 flag）
 * <p>
 * by zyy
 * <p>
 * 2019/2/26
 */
public class ScanManagerActivity extends BaseActivity implements QRCodeView.Delegate {


    @BindView(R.id.zx_view)
    ZXingView mZXingView;
    @BindView(R.id.btn_open_flash_light)
    Button btnOpen;
    @BindView(R.id.btn_again)
    Button btnAgain;
    @BindView(R.id.ll_zxing)
    LinearLayout llZxing;
    @BindView(R.id.ll_input)
    LinearLayout llInput;
    @BindView(R.id.flashlight_close)
    ImageView flashlightClose;
    @BindView(R.id.tv_special)
    TextView tvSpecial;

    private Boolean isOpen = false;

    private String flag = null;
    //特殊字符，显示在上面
    private String special = null;

    private boolean laserAndZxing;//是否是从激光扫描界面过来的

    /**
     * 普通启动
     */
    public static void startActivity(Context mContext) {
        Intent starter = new Intent(mContext, ScanManagerActivity.class);
        ((Activity) mContext).startActivityForResult(starter, 0);
    }

    /**
     * 带特殊字符启动
     */
    public static void startActivity(Context mContext, String flag, String special) {
        Intent starter = new Intent(mContext, ScanManagerActivity.class);
        starter.putExtra("special", special);
        starter.putExtra("flag", flag);
        ((Activity) mContext).startActivityForResult(starter, 0);
    }

    /**
     * 带参数启动
     */
    public static void startActivity(Context mContext, String flag) {
        Intent starter = new Intent(mContext, ScanManagerActivity.class);
        starter.putExtra("flag", flag);
        ((Activity) mContext).startActivityForResult(starter, 0);
    }

    /**
     * 从激光扫码界面跳转过来
     */
    public static void startActivityFromLaser(Context mContext, String flag) {
        Intent starter = new Intent(mContext, ScanManagerActivity.class);
        starter.putExtra("flag", flag)
                .putExtra("laserAndZxing", true);
        mContext.startActivity(starter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_manager;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {

        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "普通扫码");

        flag = getIntent().getStringExtra("flag");
        special = getIntent().getStringExtra("special");
        laserAndZxing = getIntent().getBooleanExtra("laserAndZxing", false);

        if (!TextUtils.isEmpty(special)) {
            tvSpecial.setText(special);
        }

        mZXingView.setDelegate(this);
        btnAgain.setOnClickListener(v -> {
            startZXing();
        });
        btnOpen.setOnClickListener(v -> {
            if (isOpen) {
                mZXingView.closeFlashlight();
                isOpen = false;
                btnOpen.setText("打开闪光灯");
            } else {
                mZXingView.openFlashlight();
                isOpen = true;
                btnOpen.setText("关闭闪光灯");
            }
        });
        flashlightClose.setOnClickListener(v -> {
            if (isOpen) {
                mZXingView.closeFlashlight();
                isOpen = false;
                btnOpen.setText("打开闪光灯");
            } else {
                mZXingView.openFlashlight();
                isOpen = true;
                btnOpen.setText("关闭闪光灯");
            }
        });

        llZxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果laserAndZxing 是true 就是从激光扫码跳转过来的，就直接关闭 回到激光扫码界面
                if (laserAndZxing) {
                    finish();
                } else {
                    LaserScanActivity.startActivityFromZxing(ScanManagerActivity.this, flag);
                }

            }
        });
        llInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    /**
     * InputDialog 的用法
     */
    private void showDialog() {
        InputDialog dialog1 = new InputDialog(this);
        dialog1.setTitle("手动输入")
                .setHint("请输入......")
                .setPositiveButton("取消")
                .setNegativeButton("确定")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new InputDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {

                        } else {
                            if (TextUtils.isEmpty(dialog1.getMessage())) {
                                ToastUtil.showToast("输入为空");
                            } else {
                                getBackMessage(dialog1.getMessage());
                            }

                        }
                    }
                })
                .show();
    }

    private void startZXing() {
        mZXingView.startCamera();
        mZXingView.changeToScanQRCodeStyle();// 切换成扫描二维码样式z
        mZXingView.startSpotAndShowRect();//调用zxing扫描
    }

    @Override
    protected void onStart() {
        startZXing();
        if (mZXingView.getIsScanBarcodeStyle())
            ToastUtil.showToast("1111");
        else
            ToastUtil.showToast("2222");
        super.onStart();
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mZXingView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        getBackMessage(result);
    }
    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        if (isDark){
            ToastUtil.showToast("光线太暗请打开闪光灯！");
        }
    }


    /**
     * 获取回调的扫码信息
     *
     * @param result
     */
    private void getBackMessage(String result) {
        if (laserAndZxing) {
            EventBus.getDefault().post(new LaserAndZxingBean(result, "laser"));

        } else {
            if (flag == null) {
                Intent intent = new Intent();
                intent.putExtra(Constants.SACN_DATA, result);
                setResult(Constants.SCAN_RESULT, intent);
            } else {
                ScanDataBean dataBean = new ScanDataBean();
                dataBean.setFunctionFlag(flag);
                dataBean.setData(result);
                EventBus.getDefault().post(dataBean);
            }
            Tools.startShortVibrator(this);// 扫码成功 短暂   震动
        }
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtil.showToast("扫描失败，请重试。");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LaserAndZxingBean result) {
        if (!TextUtils.isEmpty(result.getData()) && result.getTypeName().equals("scan")) {
            getBackMessage(result.getData());
        }
//        ToastUtil.showToast("扫码数据为空请重新扫码");
    }
}
