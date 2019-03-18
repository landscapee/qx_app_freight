package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 激光扫码界面
 */
public class LaserScanActivity extends BaseActivity {

    private String chenNum;
    private String mScooterCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_laser_scan;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        chenNum = getIntent().getStringExtra("chenNum");
        initTitle();

    }

    private void initTitle() {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "扫一扫");
        toolbar.setRightIconView(View.VISIBLE, R.mipmap.switch_scan, v -> {
            ScanManagerActivity.startActivity(this);
        });
    }

    private void startAllocaaateScanActivity(String chenNum, String mScooterCode) {
        Intent intent = new Intent(this, AllocaaateScanActivity.class);
        intent.putExtra("chenNum", chenNum);
        intent.putExtra("scooterCode", mScooterCode);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.SCAN_RESULT == resultCode) {
            mScooterCode = data.getStringExtra(Constants.SACN_DATA);
            startAllocaaateScanActivity(chenNum, mScooterCode);
        } else {
            Log.e("resultCode", "收货页面不是200");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (result.getFunctionFlag().equals("ChooseWeighScanActivity")) {
            //板车号
            mScooterCode = result.getData();
            if (!"".equals(mScooterCode)) {
                startAllocaaateScanActivity(chenNum, mScooterCode);
            } else {
                ToastUtil.showToast("扫码数据为空请重新扫码");
            }
        } else {
            Log.e("resultCode", "收货页面不是200");
        }
    }
}
