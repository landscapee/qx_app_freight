package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * 激光扫码界面
 */
public class LaserScanActivity extends BaseActivity {

    private String mScooterCode;
    private String flag;
    private boolean laserAndZxing; //是否是从二维码扫描界面过来的

    @Override
    public int getLayoutId() {
        return R.layout.activity_laser_scan;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        initTitle();
        flag = getIntent().getStringExtra("flag");
        laserAndZxing = getIntent().getBooleanExtra("laserAndZxing",false);

        LinearLayout llZxing = findViewById(R.id.ll_zxing);
        LinearLayout llInput = findViewById(R.id.ll_input);
        llZxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果laserAndZxing 是true 就是从二维码扫码跳转过来的，就直接关闭 回到二维码扫码界面
                if (laserAndZxing){
                    finish();
                }else {
                    ScanManagerActivity.startActivityFromLaser(LaserScanActivity.this,flag);
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
     * 普通启动
     */
    public static void startActivity(Context mContext) {
        Intent starter = new Intent(mContext, LaserScanActivity.class);
        ((Activity) mContext).startActivityForResult(starter, 0);
    }

    /**
     * 带参数启动
     */
    public static void startActivity(Context mContext, String flag) {
        Intent starter = new Intent(mContext, LaserScanActivity.class);
        starter.putExtra("flag", flag);
        ((Activity) mContext).startActivityForResult(starter, 0);
    }

    /**
     *从二维码扫码界面跳转过来
     */
    public static void startActivityFromZxing(Context mContext, String flag){
        Intent starter = new Intent(mContext, LaserScanActivity.class);
        starter.putExtra("flag", flag)
                .putExtra("laserAndZxing",true);
        mContext.startActivity(starter);
    }

    /**
     * CommonDialog 的用法
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
                            if (TextUtils.isEmpty(dialog1.getMessage())){
                                ToastUtil.showToast("输入为空");
                            }else {
                                getBackMessage(dialog1.getMessage());
                            }

                        }
                    }
                })
                .show();
    }

    /**
     * 设置页面标题
     */
    private void initTitle() {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "激光扫码");
    }

    /**
     * 获取回调的扫码信息
     * @param result
     */
    private void getBackMessage(String result){
        if (laserAndZxing){
            EventBus.getDefault().post(new LaserAndZxingBean(result,"scan"));
        }else {
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
            Tools.startShortVibrator(this);// 扫码成功 短暂震动
        }

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.SCAN_RESULT == resultCode) {
            mScooterCode = data.getStringExtra(Constants.SACN_DATA);
            getBackMessage(mScooterCode);
        } else {
            Log.e("resultCode", "收货页面不是200");
        }
    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (!TextUtils.isEmpty(result.getData())) {
                mScooterCode = result.getData();
                getBackMessage(mScooterCode);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LaserAndZxingBean result) {
        if (!TextUtils.isEmpty(result.getData()) && result.getTypeName().equals("laser")) {
            mScooterCode = result.getData();
            getBackMessage(mScooterCode);
        }
    }
}
