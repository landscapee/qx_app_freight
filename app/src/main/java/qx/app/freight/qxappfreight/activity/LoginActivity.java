package qx.app.freight.qxappfreight.activity;

import android.app.Dialog;
import android.content.pm.PackageInfo;
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
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import qx.app.freight.qxappfreight.BuildConfig;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.LoginEntity;
import qx.app.freight.qxappfreight.bean.request.PhoneParametersEntity;
import qx.app.freight.qxappfreight.bean.request.ReqLoginBean;
import qx.app.freight.qxappfreight.bean.response.LoginBean;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.bean.response.RespLoginBean;
import qx.app.freight.qxappfreight.bean.response.UpdateVersionBean2;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.constant.HttpConstant;
import qx.app.freight.qxappfreight.contract.GetPhoneParametersContract;
import qx.app.freight.qxappfreight.contract.LoginContract;
import qx.app.freight.qxappfreight.dialog.ProgressDialog;
import qx.app.freight.qxappfreight.dialog.UpDateVersionDialog;
import qx.app.freight.qxappfreight.http.HttpApi;
import qx.app.freight.qxappfreight.presenter.GetPhoneParametersPresenter;
import qx.app.freight.qxappfreight.presenter.LoginPresenter;
import qx.app.freight.qxappfreight.utils.AppUtil;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.DownloadInstall;
import qx.app.freight.qxappfreight.utils.DownloadUtils;
import qx.app.freight.qxappfreight.utils.IMUtils;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static qx.app.freight.qxappfreight.app.MyApplication.isNeedIm;

/**
 * 登录页面
 *   by 郭浩
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
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setMainTitle(Color.WHITE, "登录");
        tvCopyVersion.setText(" @成都双流国际机场版权所有（v" + BuildConfig.VERSION_NAME + "）");
        checkVersion();
        mEtUserName.setText(UserInfoSingle.getInstance().getLoginName());

        mEtPassWord.setText("");
        mEtUserName.setText("");
        mBtnLogin.setOnClickListener(v -> {
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
        DownloadUtils downloadUtils = new DownloadUtils(this);
        //是否按安装输入法
        if (!downloadUtils.isInstall()) {
            //是否已经下载
            if (downloadUtils.isDownload()) {
                //直接安装
                downloadUtils.showDialogInstall();
            } else {
                //下载
                downloadUtils.showDialogDownload();
            }
        } else {
            Log.e("输入法：", "输入法已经安装！");
        }

    }

    private void checkVersion() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            //打印retrofit日志
            Log.e("tagRetrofit", "msg = " + message);
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()//okhttp设置部分，此处还可再设置网络参数
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpConstant.UPDATE_CHECK_VERSION_URL)
                .client(client)//此client是为了打印信息
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpApi httpService = retrofit.create(HttpApi.class);
        Call<UpdateVersionBean2> call = httpService.updateVersion("newphone2");
        call.enqueue(new Callback<UpdateVersionBean2>() {
            @Override
            public void onResponse(Call<UpdateVersionBean2> call, Response<UpdateVersionBean2> response) {
                UpdateVersionBean2 updataBean = response.body();
                if (updataBean == null) {
                    ToastUtil.showToast("获取应用更新信息失败");
                    return;
                }
                if (1000 != updataBean.getResponseCode()) {
                    ToastUtil.showToast("获取应用更新信息失败");
                    return;
                }

                mVersionBean = updataBean;
                PackageInfo appInfo = AppUtil.getPackageInfo(LoginActivity.this);
                int versionCode = 0;
                if (appInfo != null) {
                    versionCode = appInfo.versionCode;
                }
                if (versionCode < mVersionBean.getData().getVersionCodeRS()) {
                    showAppUpdateDialog();
                }
            }

            @Override
            public void onFailure(Call<UpdateVersionBean2> call, Throwable t) {
                Log.e("tagUpdate", "更新版本出错");
            }
        });
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
        mLoginEntity.setUsername(mEtUserName.getText().toString().trim());
        mLoginEntity.setPassword(mEtPassWord.getText().toString().trim());
        mLoginEntity.setType("MT");
        List<String> syss = new ArrayList<>();
        syss.add("10040000");//外场
        syss.add("10080000");//货运
        mLoginEntity.setSysCode(syss);
        return mLoginEntity;
    }

    /**
     * 获取登录智能调度请求体
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
        Log.e("isNeedIm","isNeedIm:"+isNeedIm);
        if (loginBean != null) {
            isNeedIm = false;
            for (LoginResponseBean.RoleRSBean mRoleRSBean : loginBean.getRoleRS()) {
                if (Constants.PREPLANER.equals(mRoleRSBean.getRoleCode())) {
                    ToastUtil.showToast(this, "组板只能使用PAD登录");
                    dismissProgessDialog();
                    return;
                }
                if (Constants.INSTALL_UNLOAD_EQUIP.equals(mRoleRSBean.getRoleCode())||
                        Constants.JUNCTION_LOAD.equals(mRoleRSBean.getRoleCode())||
                        Constants.DRIVEROUT.equals(mRoleRSBean.getRoleCode())) {
//                    loginBean.setUserId(loginBean.getLoginid());
                    isNeedIm = true;
                    break;

                }
            }

            if (isNeedIm&& Tools.isProduct()){
                UserInfoSingle.setUser(loginBean);
                loginIm(loginBean);
            }
            else {
                UserInfoSingle.setUser(loginBean);
                loginTpPC(loginBean);
                if (Constants.PSW_TYPE_NO.equals(loginBean.getCode())) {
                    dismissProgessDialog();
                    UpdatePWDActivity.startActivity(this);
                }
                toMainAct();
            }
        } else {
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
        MainActivity.startActivity(this);
        finish();

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

    /**
     * 弹出下载提示框
     * vtime.setText(appVersion.getData().getVersionCode());
     * vContent.setText(appVersion.getData().getUpdateMsg());
     */
    private void showAppUpdateDialog() {
        UpDateVersionDialog dialog = new UpDateVersionDialog(this);


        dialog.setTitle("版本更新")
                .setMessage("更新版本：" + mVersionBean.getData().getVersionCode() + "\n更新内容：" + mVersionBean.getData().getUpdateMsg())
                .setNegativeButton("立即更新")
                .isCanceledOnTouchOutside(false)
                .isCanceled(false)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (mVersionBean.getData().getDownloadUrl() == null || mVersionBean.getData().getDownloadUrl().length() == 0) {
                            ToastUtil.showToast("下载地址获取有误");
                        } else {
                            downLoadFile(mVersionBean);
                        }
                    }
                })
                .show();


//        final AppUpdateDailog appUpdateDailog = new AppUpdateDailog(this);
//        appUpdateDailog.setAppUpdateDialogData(mVersionBean,
//                new AppUpdateDailog.AppUpdateLinstener() {
//                    @Override
//                    public void sure() {
//                        // 下载app
//                        if (mVersionBean.getData().getDownloadUrl() == null || mVersionBean.getData().getDownloadUrl().length() == 0) {
//                            ToastUtil.showToast("下载地址获取有误");
//                        } else {
//                            downLoadFile(mVersionBean);
//                        }
//                        appUpdateDailog.dismiss();
//                    }
//
//                    @Override
//                    public void cancel() {
//                        appUpdateDailog.dismiss();
//                    }
//                });
//        appUpdateDailog.show();
    }

    /**
     * 下载apk
     * http://10.16.23.156:9082/acdm-api/sys/api/download/todownloadflie.json?name=84ed15bf10e641d186fd562b62610796&filePath=/root/uploadfile/3426b22648f348a6987b0796cdb716e7
     */
    public void downLoadFile(UpdateVersionBean2 version) {
//        ToastUtil.showToast("程序更新中...");
//        String wholeUrl = version.getData().getDownloadUrl();
////        String base = wholeUrl.substring(0, wholeUrl.lastIndexOf("/") + 1);
////        String left = wholeUrl.substring(wholeUrl.lastIndexOf("/") + 1);
//        String base = "http://10.16.23.156:9082";
//        String left = "acdm-api/sys/api/download/todownloadflie.json?name=84ed15bf10e641d186fd562b62610796&filePath=/root/uploadfile/3426b22648f348a6987b0796cdb716e7";
        String saveFilePath = Tools.getFilePath() + Constants.APP_NAME + version.getData().getVersionCode() + ".apk";
//        DownloadFileService.startService(this, base, left, Constants.APP_NAME + version.getData().getVersionCode() + ".apk", Tools.getFilePath());


        ProgressDialog progressDialog = new ProgressDialog();
        progressDialog.setData(this, null);
        DownloadInstall downloadInstall = new DownloadInstall.Builder()
                .Context(this)
                .downloadUrl(version.getData().getDownloadUrl())
                .saveFilePath(saveFilePath)
                .progressDialog(progressDialog).build();
        downloadInstall.start();
    }


}
