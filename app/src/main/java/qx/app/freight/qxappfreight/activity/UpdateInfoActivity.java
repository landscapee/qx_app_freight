package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScooterMapSingle;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.UpdatePwdEntity;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.contract.UpdatePWDContract;
import qx.app.freight.qxappfreight.presenter.UpdatePWDPresenter;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.ActManager;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

public class UpdateInfoActivity extends BaseActivity {

    @BindView(R.id.tv_update_info)
    TextView tvUpdateInfo;

    private CustomToolbar toolbar;

    public static void startActivity(Activity context
    ) {
        Intent intent = new Intent(context, UpdateInfoActivity.class);
        context.startActivityForResult(intent, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_info;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "更新公告");
        initView();
        initData();
    }


    private void initView() {



    }

    private void initData() {
    }

}
