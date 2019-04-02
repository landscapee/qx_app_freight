package qx.app.freight.qxappfreight.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.LoginEntity;
import qx.app.freight.qxappfreight.bean.response.LoginBean;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.LoginContract;
import qx.app.freight.qxappfreight.presenter.LoginPresenter;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.IMUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements LoginContract.loginView {
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.et_password)
    EditText mEtPassWord;
    @BindView(R.id.et_username)
    EditText mEtUserName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setMainTitle(Color.WHITE, "登录");
        mEtPassWord.setText("111111");
        mEtUserName.setText(UserInfoSingle.getInstance().getLoginName());
        mEtUserName.setText("fuzhongyuan");
        mPresenter = new LoginPresenter(this);
        mBtnLogin.setOnClickListener(v -> {
            login();
        });
        mEtPassWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_DONE ==actionId){
                    login();
                    return true;
                }
                return false;
            }
        });
        checkPermissions();
        checkPermissionsForWindow();
//        getDeviceInfo();
    }

    private void getDeviceInfo(){
        Log.e("22222", "getDeviceInfo: "+ DeviceInfoUtil.getDeviceInfo(this));
        Log.e("22222", "getDeviceInfo: "+ DeviceInfoUtil.getPhoneBrand());
        Log.e("22222", "getDeviceInfo: "+ DeviceInfoUtil.getPhoneModel());
        Log.e("22222", "getDeviceInfo: "+ DeviceInfoUtil.getPhoneDevice());
    }

    /**
     * 登录方法
     */
    private void login() {
        if (TextUtils.isEmpty(mEtUserName.getText().toString()) || TextUtils.isEmpty(mEtPassWord.getText().toString())) {
            ToastUtil.showToast("账号或者密码不能为空");
        } else {
            ((LoginPresenter) mPresenter).login(getLoginEntity());
        }
    }

    /**
     * CommonDialog 的用法
     */
    private void showDialog() {
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle("这个是标题")
                .setMessage("这个是提示内容")
                .setPositiveButton("左边按钮")
                .setNegativeButton("右边按钮")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            ToastUtil.showToast("点击了左边的按钮");
                        } else {
                            ToastUtil.showToast("点击了右边的按钮");
                        }
                    }
                })
                .show();
    }

    private LoginEntity getLoginEntity() {
        LoginEntity mLoginEntity = new LoginEntity();
        mLoginEntity.setUsername(mEtUserName.getText().toString().trim());
        mLoginEntity.setPassword(mEtPassWord.getText().toString().trim());
        mLoginEntity.setType("MT");
        return mLoginEntity;
    }
    private LoginEntity getLoginQxAiEntity() {
        LoginEntity mLoginEntity = new LoginEntity();
        mLoginEntity.setUsername(mEtUserName.getText().toString().trim());
        mLoginEntity.setPassword(mEtPassWord.getText().toString().trim());
        return mLoginEntity;
    }

    @Override
    public void loginResult(LoginResponseBean loginBean) {
        if (loginBean != null) {
            Tools.setLoginUserBean(loginBean);
            for (LoginResponseBean.RoleRSBean mRoleRSBean : loginBean.getRoleRS()) {
                if (Constants.INSTALL_UNLOAD_EQUIP.equals(mRoleRSBean.getRoleCode())) {
//                    loginBean.setUserId(loginBean.getLoginid());
                    loginIm(loginBean);
                }
            }
            UserInfoSingle.setUser(loginBean);
            MainActivity.startActivity(this);
            finish();
        } else {
            ToastUtil.showToast(this, "数据错误");
        }
    }

    @Override
    public void loginQxAiResult(LoginBean loginBean) {

        IMUtils.imLibLogin(loginBean.getLoginName(),loginBean.getUsername(),loginBean.getToken());
        MainActivity.startActivity(this);
        finish();

    }

    private void loginIm(LoginResponseBean loginBean) {
        if (loginBean == null)
            IMUtils.imLibLogin("lizhong", "李忠", "eyJhbGciOiJIUzI1NiJ9.eyJjcmVhdGVfdGltZSI6MTU1MzUwMTUwMDk1MCwidXNlcl9pbmZvIjoie1wiZGVwdENvZGVcIjpcImNzZ2xcIixcImRlcHRJZFwiOlwiN2IzMTZjYjhjMTgxNDhiOGFiYTUxNmRlODVmNzZlYWVcIixcImlkXCI6XCI2MjQwNjg4NzBjMGM0ZGNiOTUyYTRkNDAyZjdjZDg5N1wiLFwibG9naW5OYW1lXCI6XCJsaXpob25nXCIsXCJuYW1lXCI6XCLmnY7lv6BcIixcInJvbGVzXCI6W3tcImNvZGVcIjpcImdyb3VwX2xlYWRlclwiLFwiaWRcIjpcImYzZmEwNmM2ZmU3MDRhOTRiZWIxYzlmMDMxMjYyNDdhXCJ9LHtcImNvZGVcIjpcIlN5c3RlbU1hbmFnZXJcIixcImlkXCI6XCJTeXN0ZW1NYW5hZ2VyXCJ9LHtcImNvZGVcIjpcImFsbF9yZXBvcnRcIixcImlkXCI6XCI1ZWQ3OWUyY2NmMWQ0MWJhYTRhNTE3Nzg1MDdiMjFiN1wifSx7XCJjb2RlXCI6XCJBT0NfUkVBRFwiLFwiaWRcIjpcIjUyMmM3ODY5NjJkNzQzNGJhN2VhY2FmOTM2YjMzYzQ3XCJ9LHtcImNvZGVcIjpcIkFPQ19TRlwiLFwiaWRcIjpcIjExNDA5ZDhkODU1NjQ4NTRiZTk4ZTQxY2Y5MTAzZmY2XCJ9LHtcImNvZGVcIjpcIkFPQ19DWVwiLFwiaWRcIjpcIjg1ZmJiNjQ1NDA2OTQ4NzRiZGU3NDFjYjU3MjE2ODE5XCJ9LHtcImNvZGVcIjpcIkFPQ19XUklURVwiLFwiaWRcIjpcImY3NTdhYmQxNmExZDRkNzNhMTU2YmU0MjZmMmIzMmJlXCJ9LHtcImNvZGVcIjpcImRlcHRNYW5hZ2VyXCIsXCJpZFwiOlwiZGVwdE1hbmFnZXJcIn0se1wiY29kZVwiOlwiMVwiLFwiaWRcIjpcImFkODI4MjgwZDI4MzRjNzI4ODkxMmZjY2VlOTYyNTg0XCJ9LHtcImNvZGVcIjpcIkFQUEhUXCIsXCJpZFwiOlwiYjUwMTQ5NTEwODMxNDhlN2IzY2E3NjY5MjRjNzFiNTVcIn1dLFwic3RhdGVcIjpcIjFcIn0iLCJ1c2VyX25hbWUiOiLmnY7lv6AiLCJ1c2VyX2tleSI6IjQyODk5ODU0YmU2ZGRlYTA4OTVlNjMwNGYzMTE5OGQ2IiwidGltZW91dCI6Mjg4MDB9.uCx9MCGIfESaeKy5z4DnS70nfMz6fRWAGl52i2hJR5w");
        else
        {
            ((LoginPresenter) mPresenter).loginQxAi(getLoginQxAiEntity());

        }

    }


    @Override
    public void toastView(String error) {
        //登录接口 崩溃使用
//        LoginResponseBean loginBean = new LoginResponseBean();
////        loginBean.setUserId("ub76903ce9f8b4d14b7bdbdf90ef52a53");
//        loginBean.setUserId("bd1782b5fe11436493e62b09cafe845c");
//        List<LoginResponseBean.RoleRSBean> roleRSBeans = new ArrayList<>();
//        LoginResponseBean.RoleRSBean mRoleRSBean = new LoginResponseBean.RoleRSBean();
////        mRoleRSBean.setRoleCode(Constants.DRIVEROUT);
//        mRoleRSBean.setRoleCode(Constants.INSTALL_UNLOAD_EQUIP);
////        mRoleRSBean.setRoleCode(Constants.DRIVERIN);
//
//        roleRSBeans.add(mRoleRSBean);
//        loginBean.setRoleRS(roleRSBeans);
//        UserInfoSingle.setUser(loginBean);
        //**************************************************
        loginIm(null);
        MainActivity.startActivity(this);
        ToastUtil.showToast(error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("正在登录……");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void onBackPressed() {
        quitApp();
    }
}
