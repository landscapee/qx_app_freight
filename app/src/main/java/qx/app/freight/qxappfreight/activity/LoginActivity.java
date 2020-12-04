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
import qx.app.freight.qxappfreight.utils.RsaCoder;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.pad.utils.VersionUtils;

import static qx.app.freight.qxappfreight.app.MyApplication.isNeedIm;

/**
 * 登录页面
 * by 郭浩
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
        toolbar.setMainTitle(Color.WHITE, "登录");
        tvCopyVersion.setText(" @成都双流国际机场版权所有（v" + BuildConfig.VERSION_NAME + "）");
        //版本升级
        VersionUtils versionUtils = new VersionUtils();
        versionUtils.checkVersion(this, AppUtil.getPackageInfo(this), "newphone2", Constants.APP_NAME);

        mEtUserName.setText(UserInfoSingle.getInstance().getLoginName());
        mEtUserName.setText(Tools.getLoginNameForLogin());
        mEtPassWord.setText(Tools.getPassword());

        mBtnLogin.setOnClickListener(v -> {
            if (!Tools.isFastClick()) {
                return;
            }
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

        //输入法 下载安装
//        DownloadUtils downloadUtils = new DownloadUtils(this);
//        //是否按安装输入法
//        if (!downloadUtils.isInstall()) {
//            //是否已经下载
//            if (downloadUtils.isDownload()) {
//                //直接安装
//                downloadUtils.showDialogInstall();
//            } else {
//                //下载
//                downloadUtils.showDialogDownload();
//            }
//        } else {
//            Log.e("输入法：", "输入法已经安装！");
//        }

    }


//    private void getDeviceInfo(){
//        Log.e("22222", "getDeviceInfo: "+ DeviceInfoUtil.getDeviceInfo(this));
//        Log.e("22222", "getDeviceInfo: "+ DeviceInfoUtil.getPhoneBrand());
//        Log.e("22222", "getDeviceInfo: "+ DeviceInfoUtil.getPhoneModel());
//        Log.e("22222", "getDeviceInfo: "+ DeviceInfoUtil.getPhoneDevice());
//    }


    /**
     * 登录方法
     */
    private void login() {
        if (TextUtils.isEmpty(mEtUserName.getText().toString()) || TextUtils.isEmpty(mEtPassWord.getText().toString())) {
            ToastUtil.showToast("账号或者密码不能为空");
        } else {
            showProgessDialog("正在登录……");
            mPresenter = new LoginPresenter(this);
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

    /**
     * 组装登录参数
     *
     * @return
     */
    private LoginEntity getLoginEntity() {
        LoginEntity mLoginEntity = new LoginEntity();
        mLoginEntity.setUsername(RsaCoder.encryptByPublicKey(mEtUserName.getText().toString().trim(), RsaCoder.P_KEY));
        mLoginEntity.setPassword(RsaCoder.encryptByPublicKey(mEtPassWord.getText().toString().trim(), RsaCoder.P_KEY));
        mLoginEntity.setType("MT");
        List <String> syss = new ArrayList <>();
        syss.add("10040000");//外场
        syss.add("10080000");//货运
        mLoginEntity.setSysCode(syss);
        return mLoginEntity;
    }

    /**
     * 获取登录智能调度请求体
     *
     * @return
     */
    private ReqLoginBean getLoginQxAiEntity() {
        ReqLoginBean bean = new ReqLoginBean();
        bean.setUserName(mEtUserName.getText().toString().trim());
        bean.setPwd(mEtPassWord.getText().toString().trim());
        bean.setPhoneNo(DeviceInfoUtil.getPhoneNumber(this));
        bean.setDeviceId(StringUtil.isEmpty(DeviceInfoUtil.getIMEI(this)) ? UUID.randomUUID().toString() : DeviceInfoUtil.getIMEI(this));
        // 设备类型
        bean.setDeviceType("phone");

        bean.setEptModel(StringUtil.isEmpty(Build.MODEL) ? "未获取到设备型号" : Build.MODEL);
        bean.setSystemName("Android");
        bean.setSystemCode(StringUtil.isEmpty(Build.VERSION.RELEASE) ? "未获取到版本信息" : Build.VERSION.RELEASE);
        return bean;
    }

    @Override
    public void loginResult(LoginResponseBean loginBean) {
        Log.e("isNeedIm", "isNeedIm:" + isNeedIm);
        if (loginBean != null) {
            isNeedIm = false;
            for (LoginResponseBean.RoleRSBean mRoleRSBean : loginBean.getRoleRS()) {
                if (Constants.PREPLANER.equals(mRoleRSBean.getRoleCode())) {
                    ToastUtil.showToast(this, "组板只能使用PAD登录");
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
                    ToastUtil.showToast(this, "站坪角色不能登录该应用");
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
            ToastUtil.showToast(this, "数据错误");
        }
    }

    /**
     * 通知运输装卸机PC 监听 该用户已经上线
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
            Log.e("通知运输监控已登录", result);
        }

    }

    /**
     * 登录成功 跳转到主页
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
