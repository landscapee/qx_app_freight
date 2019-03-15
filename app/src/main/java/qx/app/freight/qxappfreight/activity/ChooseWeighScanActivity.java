package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

public class ChooseWeighScanActivity extends BaseActivity {

    @BindView(R.id.ll_chen_1)
    LinearLayout llChen1;
    @BindView(R.id.ll_chen_2)
    LinearLayout llChen2;
    @BindView(R.id.ll_chen_3)
    LinearLayout llChen3;
    @BindView(R.id.ll_chen_4)
    LinearLayout llChen4;
    private String chenNum;
    private String mScooterCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_weigh_scan;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        EventBus.getDefault().register(this);
        toolbar.setMainTitle(Color.WHITE, "选择扫码称");
        //点击新增跳转
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        initView();
    }

    private void initView() {

    }

    @OnClick({R.id.ll_chen_1, R.id.ll_chen_2, R.id.ll_chen_3, R.id.ll_chen_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_chen_1:
                chenNum = "1#称";
                ScanManagerActivity.startActivity(this);
                break;
            case R.id.ll_chen_2:
                chenNum = "2#称";
                ScanManagerActivity.startActivity(this);
                break;
            case R.id.ll_chen_3:
                chenNum = "3#称";
                ScanManagerActivity.startActivity(this);
                break;
            case R.id.ll_chen_4:
                chenNum = "4#称";
                ScanManagerActivity.startActivity(this);
                break;
        }
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
                ToastUtil.showToast(ChooseWeighScanActivity.this, "扫码数据为空请重新扫码");
            }
        } else {
            Log.e("resultCode", "收货页面不是200");
        }
    }
}
