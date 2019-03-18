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

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_weigh_scan;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
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
                startLaserScanActivity("1#称");
                break;
            case R.id.ll_chen_2:
                startLaserScanActivity("2#称");
                break;
            case R.id.ll_chen_3:
                startLaserScanActivity("3#称");
                break;
            case R.id.ll_chen_4:
                startLaserScanActivity("4#称");
                break;
        }
    }
    private void startLaserScanActivity(String num){
        Intent intent = new Intent(this, LaserScanActivity.class);
        intent.putExtra("chenNum", num);
        startActivity(intent);
    }
}
