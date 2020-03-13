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

public class UpdatePWDActivity extends BaseActivity implements UpdatePWDContract.updatePWDView {

    @BindView(R.id.tv_old_pwd)
    TextView tvOldPwd;
    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.tv_new_pwd_1)
    TextView tvNewPwd1;
    @BindView(R.id.et_new_pwd_1)
    EditText etNewPwd1;
    @BindView(R.id.tv_new_pwd_2)
    TextView tvNewPwd2;
    @BindView(R.id.et_new_pwd_2)
    EditText etNewPwd2;
    @BindView(R.id.cb_show)
    AppCompatCheckBox cbShow;
    @BindView(R.id.btn_update_pwd)
    Button btnUpdatePwd;
    @BindView(R.id.content_update_pwd)
    RelativeLayout contentUpdatePwd;

    private LoginResponseBean loginBean;

    private CustomToolbar toolbar;

    public static void startActivity(Activity context
    ) {
        Intent intent = new Intent(context, UpdatePWDActivity.class);
        context.startActivityForResult(intent, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_pwd;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "密码修改");
        initView();
        initData();
    }


    private void initView() {

        cbShow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etOldPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                etNewPwd1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                etNewPwd2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                etOldPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etNewPwd1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etNewPwd2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });


        mPresenter = new UpdatePWDPresenter(this);
        btnUpdatePwd.setOnClickListener(v -> {
            String oldPwd = etOldPwd.getText().toString();
            String newPwd1 = etNewPwd1.getText().toString();
            String newPwd2 = etNewPwd2.getText().toString();
            if (TextUtils.isEmpty(oldPwd)) {
                ToastUtil.showToast("旧密码不能为空");
                return;
            }
            if (TextUtils.isEmpty(newPwd1)) {
                ToastUtil.showToast("新密码不能为空");
                return;
            }
            if (TextUtils.isEmpty(newPwd2)) {
                ToastUtil.showToast("确认密码不能为空");
                return;
            }
            if (!TextUtils.equals(newPwd1, newPwd2)) {
                ToastUtil.showToast("确认密码与新密码不一致,请检查后操作");
                return;
            }
            if (TextUtils.equals(newPwd1, oldPwd)) {
                ToastUtil.showToast("旧密码与新密码不能一致");
                return;
            }

            UpdatePwdEntity entity = new UpdatePwdEntity();
            entity.setPassword(newPwd2);
            entity.setOldpassword(oldPwd);
            if (!TextUtils.isEmpty(loginBean.getUserId())) {
                entity.setUserId(loginBean.getUserId());
                entity.setUsername(loginBean.getLoginName());
            } else
                ToastUtil.showToast("UserId为空");
            ((UpdatePWDPresenter) mPresenter).updatePWD(entity);
        });
    }

    private void initData() {
        loginBean = UserInfoSingle.getInstance();
    }

    @Override
    public void updatePWDResult(String result) {
        ToastUtil.showToast("密码修改成功");
        UserInfoSingle.setUserNil();
        ScooterMapSingle.getInstance().clear();
        ActManager.getAppManager().finishAllActivity();
        WebSocketService.stopServer(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }
}
