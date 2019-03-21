package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * activity  通过intent回传
 *
 * fragment 通过 EventBus 回传（传入的flag 就是 接收EventBus 判断的 flag）
 *
 * by zyy
 *
 * 2019/2/26
 */
public class ScanManagerActivity extends BaseActivity implements QRCodeView.Delegate{


    @BindView(R.id.zx_view)
    ZXingView mZXingView;
    @BindView(R.id.btn_open_flash_light)
    Button btnOpen;
    @BindView(R.id.btn_again)
    Button btnAgain;

    private Boolean isOpen  = false;

    private String flag  = null;
    /**
     * 普通启动
     */
   public static void startActivity(Context mContext){
            Intent starter = new Intent(mContext, ScanManagerActivity.class);
            ((Activity)mContext).startActivityForResult(starter,0);
    }
    /**
     * 带参数启动
     */
    public static void startActivity(Context mContext,String flag){
        Intent starter = new Intent(mContext, ScanManagerActivity.class);
        starter.putExtra("flag",flag);
        ((Activity)mContext).startActivityForResult(starter,0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_manager;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {

        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "扫一扫");

        flag = getIntent().getStringExtra("flag");

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
    }

    private void startZXing() {
        mZXingView.startCamera();
        mZXingView.changeToScanQRCodeStyle();// 切换成扫描二维码样式
        mZXingView.startSpotAndShowRect();//调用zxing扫描
    }

    @Override
    protected void onStart() {
        startZXing();
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



        if (flag == null){
            Intent intent = new Intent();
            intent.putExtra(Constants.SACN_DATA,result);
            setResult(Constants.SCAN_RESULT,intent);
        }
        else {
            ScanDataBean dataBean = new ScanDataBean();
            dataBean.setFunctionFlag(flag);
            dataBean.setData(result);
            EventBus.getDefault().post(dataBean);
        }
       finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtil.showToast("扫描失败，请重试。");


    }


}
