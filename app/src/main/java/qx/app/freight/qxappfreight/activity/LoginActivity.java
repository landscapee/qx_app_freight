package qx.app.freight.qxappfreight.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beidouapp.imlibapi.IMLIBContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import qx.app.freight.qxappfreight.BuildConfig;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.LoginEntity;
import qx.app.freight.qxappfreight.bean.request.PhoneParametersEntity;
import qx.app.freight.qxappfreight.bean.request.ReqLoginBean;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.bean.response.RespLoginBean;
import qx.app.freight.qxappfreight.bean.response.UpdateVersionBean2;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetPhoneParametersContract;
import qx.app.freight.qxappfreight.contract.LoginContract;
import qx.app.freight.qxappfreight.presenter.GetPhoneParametersPresenter;
import qx.app.freight.qxappfreight.presenter.LoginPresenter;
import qx.app.freight.qxappfreight.utils.AppRestartUtils;
import qx.app.freight.qxappfreight.utils.AppUtil;
import qx.app.freight.qxappfreight.utils.CrashHandler;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.IMUtils;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.pad.utils.VersionUtils;

import static qx.app.freight.qxappfreight.app.MyApplication.isNeedIm;

/**
 * ????????????
 * by ??????
 */
public class LoginActivity extends BaseActivity implements LoginContract.loginView, GetPhoneParametersContract.getPhoneParametersView {
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.et_password)
    EditText mEtPassWord;
    @BindView(R.id.et_username)
    EditText mEtUserName;
    @BindView(R.id.tv_copyright_version)
    TextView tvCopyVersion;
    private UpdateVersionBean2 mVersionBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        AppRestartUtils.init(this);

        CrashHandler.getsInstance().uploadExceptionToServer();

        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setMainTitle(Color.WHITE, "??????");
        tvCopyVersion.setText(" @???????????????????????????????????????v" + BuildConfig.VERSION_NAME + "???");
        //????????????
        VersionUtils versionUtils = new VersionUtils();
        versionUtils.checkVersion(this, AppUtil.getPackageInfo(this), "newphone2", Constants.APP_NAME);

        mEtUserName.setText(UserInfoSingle.getInstance().getLoginName());
        mEtUserName.setText(Tools.getLoginNameForLogin());
        mEtPassWord.setText(Tools.getPassword());

        mBtnLogin.setOnClickListener(v -> {
            if (!Tools.isFastClick())
                return;
            login();
        });
        mEtPassWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_DONE == actionId) {
                    login();
                    return true;
                }
                return false;
            }
        });
        checkPermissions();
        checkPermissionsForWindow();

        //????????? ????????????
//        DownloadUtils downloadUtils = new DownloadUtils(this);
//        //????????????????????????
//        if (!downloadUtils.isInstall()) {
//            //??????????????????
//            if (downloadUtils.isDownload()) {
//                //????????????
//                downloadUtils.showDialogInstall();
//            } else {
//                //??????
//                downloadUtils.showDialogDownload();
//            }
//        } else {
//            Log.e("????????????", "????????????????????????");
//        }

    }


//    private void getDeviceInfo(){
//        Log.e("22222", "getDeviceInfo: "+ DeviceInfoUtil.getDeviceInfo(this));
//        Log.e("22222", "getDeviceInfo: "+ DeviceInfoUtil.getPhoneBrand());
//        Log.e("22222", "getDeviceInfo: "+ DeviceInfoUtil.getPhoneModel());
//        Log.e("22222", "getDeviceInfo: "+ DeviceInfoUtil.getPhoneDevice());
//    }


    /**
     * ????????????
     */
    private void login() {
        if (TextUtils.isEmpty(mEtUserName.getText().toString()) || TextUtils.isEmpty(mEtPassWord.getText().toString())) {
            ToastUtil.showToast("??????????????????????????????");
        } else {
            showProgessDialog("??????????????????");
            mPresenter = new LoginPresenter(this);
            ((LoginPresenter) mPresenter).login(getLoginEntity());
        }
    }

    /**
     * CommonDialog ?????????
     */
    private void showDialog() {
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle("???????????????")
                .setMessage("?????????????????????")
                .setPositiveButton("????????????")
                .setNegativeButton("????????????")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            ToastUtil.showToast("????????????????????????");
                        } else {
                            ToastUtil.showToast("????????????????????????");
                        }
                    }
                })
                .show();
    }

    /**
     * ??????????????????
     *
     * @return
     */
    private LoginEntity getLoginEntity() {
        LoginEntity mLoginEntity = new LoginEntity();
        mLoginEntity.setUsername(mEtUserName.getText().toString().trim());
        mLoginEntity.setPassword(mEtPassWord.getText().toString().trim());
        mLoginEntity.setType("MT");
        List <String> syss = new ArrayList <>();
        syss.add("10040000");//??????
        syss.add("10080000");//??????
        mLoginEntity.setSysCode(syss);
        return mLoginEntity;
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    private ReqLoginBean getLoginQxAiEntity() {
        ReqLoginBean bean = new ReqLoginBean();
        bean.setUserName(mEtUserName.getText().toString().trim());
        bean.setPwd(mEtPassWord.getText().toString().trim());
        bean.setPhoneNo(DeviceInfoUtil.getPhoneNumber(this));
        bean.setDeviceId(StringUtil.isEmpty(DeviceInfoUtil.getIMEI(this)) ? UUID.randomUUID().toString() : DeviceInfoUtil.getIMEI(this));
        // ????????????
        bean.setDeviceType("phone");

        bean.setEptModel(StringUtil.isEmpty(Build.MODEL) ? "????????????????????????" : Build.MODEL);
        bean.setSystemName("Android");
        bean.setSystemCode(StringUtil.isEmpty(Build.VERSION.RELEASE) ? "????????????????????????" : Build.VERSION.RELEASE);
        return bean;
    }

    @Override
    public void loginResult(LoginResponseBean loginBean) {
        Log.e("isNeedIm", "isNeedIm:" + isNeedIm);
        if (loginBean != null) {
            isNeedIm = false;
            for (LoginResponseBean.RoleRSBean mRoleRSBean : loginBean.getRoleRS()) {
                if (Constants.PREPLANER.equals(mRoleRSBean.getRoleCode())) {
                    ToastUtil.showToast(this, "??????????????????PAD??????");
                    dismissProgessDialog();
                    return;
                }
                if (Constants.INSTALL_UNLOAD_EQUIP.equals(mRoleRSBean.getRoleCode()) ||
                        Constants.JUNCTION_LOAD.equals(mRoleRSBean.getRoleCode()) ||
                        Constants.DRIVEROUT.equals(mRoleRSBean.getRoleCode()) ||
                        Constants.INSTALL_EQUIP_LEADER.equals(mRoleRSBean.getRoleCode()) ||
                        Constants.INTERNATIONAL_GOODS.equals(mRoleRSBean.getRoleCode()) ||
                        Constants.PORTER.equals(mRoleRSBean.getRoleCode())
                ) {
                    ToastUtil.showToast(this, "?????????????????????????????????");
                    dismissProgessDialog();
                    return;
//                    isNeedIm = true;
//                    break;
                }
            }

            if (isNeedIm && Tools.isProduct()) {
                UserInfoSingle.setUser(loginBean);
                loginIm(loginBean);
            } else {
                UserInfoSingle.setUser(loginBean);
                loginTpPC(loginBean);
                if (Constants.PSW_TYPE_NO.equals(loginBean.getCode())) {
                    dismissProgessDialog();
                    UpdatePWDActivity.startActivity(this);
                }
                toMainAct();
            }
        } else {
            dismissProgessDialog();
            ToastUtil.showToast(this, "????????????");
        }
    }

    /**
     * ?????????????????????PC ?????? ?????????????????????
     *
     * @param loginBean
     */
    private void loginTpPC(LoginResponseBean loginBean) {

        mPresenter = new GetPhoneParametersPresenter(this);
        PhoneParametersEntity mPhoneParametersEntity = new PhoneParametersEntity();
        mPhoneParametersEntity.setUserId(loginBean.getUserId());
        mPhoneParametersEntity.setPhoneNumber(loginBean.getPhone());
        mPhoneParametersEntity.setDeviceId(DeviceInfoUtil.getPhoneDevice());
        ((GetPhoneParametersPresenter) mPresenter).getPhoneParameters(mPhoneParametersEntity);
    }

    @Override
    public void getPhoneParametersResult(String result) {
        if (!"".equals(result)) {
//            toMainAct();
            Log.e("???????????????????????????", result);
        }

    }

    /**
     * ???????????? ???????????????
     */
    private void toMainAct() {
        dismissProgessDialog();
        Tools.saveLoginNameAndPassword(mEtUserName.getText().toString(), mEtPassWord.getText().toString());
        MainActivity.startActivity(this);
        finish();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == 444 && resultCode == RESULT_OK) {
//            ToastUtil.showToast(data.getStringExtra("query_result"));
//        }
//    }

    @Override
    public void loginQxAiResult(RespLoginBean loginBean) {
        Tools.setLoginUserBean(loginBean);
        IMLIBContext.getInstance().setDeviceIdentify(DeviceInfoUtil.getIMEI(this));
        IMUtils.imLibLogin(loginBean.getLoginName(), loginBean.getCnname(), loginBean.getToken());
        toMainAct();
    }

    private void loginIm(LoginResponseBean loginBean) {
        ((LoginPresenter) mPresenter).loginQxAi(getLoginQxAiEntity());
    }


    @Override
    public void toastView(String error) {
        //???????????? ????????????
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

//        loginIm(null);
//        MainActivity.startActivity(this);
        dismissProgessDialog();
        ToastUtil.showToast(error);
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }

    @Override
    public void onBackPressed() {
        quitApp();
    }

}
