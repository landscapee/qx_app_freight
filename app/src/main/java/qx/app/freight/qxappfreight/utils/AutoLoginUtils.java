package qx.app.freight.qxappfreight.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.LoginEntity;
import qx.app.freight.qxappfreight.bean.request.PhoneParametersEntity;
import qx.app.freight.qxappfreight.bean.request.ReqLoginBean;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.bean.response.RespLoginBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetPhoneParametersContract;
import qx.app.freight.qxappfreight.contract.LoginContract;
import qx.app.freight.qxappfreight.presenter.GetPhoneParametersPresenter;
import qx.app.freight.qxappfreight.presenter.LoginPresenter;

import static qx.app.freight.qxappfreight.app.MyApplication.isNeedIm;

public class AutoLoginUtils implements LoginContract.loginView, GetPhoneParametersContract.getPhoneParametersView{




    BasePresenter mPresenter;
    private Context mContext;
    /**
     * 登录方法
     */
    public void login(Context context) {
        mContext = context;
        if (TextUtils.isEmpty("user") || TextUtils.isEmpty("psw")) {
            ToastUtil.showToast("账号或者密码不能为空");
        } else {
            mPresenter = new LoginPresenter(this);
            ((LoginPresenter) mPresenter).login(getLoginEntity());
        }
    }

    private void loginIm(LoginResponseBean loginBean) {
        mPresenter = new LoginPresenter(this);
        ((LoginPresenter) mPresenter).loginQxAi(getLoginQxAiEntity());
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
    /**
     * 组装登录参数
     *
     * @return
     */
    private LoginEntity getLoginEntity() {
        LoginEntity mLoginEntity = new LoginEntity();
        mLoginEntity.setUsername("");
        mLoginEntity.setPassword("");
        mLoginEntity.setType("MT");
        List <String> syss = new ArrayList <>();
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
        bean.setUserName("");
        bean.setPwd("");
        bean.setPhoneNo(DeviceInfoUtil.getPhoneNumber(mContext));
        bean.setDeviceId(StringUtil.isEmpty(DeviceInfoUtil.getIMEI(mContext)) ? UUID.randomUUID().toString() : DeviceInfoUtil.getIMEI(mContext));
        // 设备类型
        bean.setDeviceType("phone");
        bean.setEptModel(StringUtil.isEmpty(Build.MODEL) ? "未获取到设备型号" : Build.MODEL);
        bean.setSystemName("Android");
        bean.setSystemCode(StringUtil.isEmpty(Build.VERSION.RELEASE) ? "未获取到版本信息" : Build.VERSION.RELEASE);
        return bean;
    }
    @Override
    public void getPhoneParametersResult(String result) {

    }

    @Override
    public void loginResult(LoginResponseBean loginBean) {
        if (loginBean != null) {
            isNeedIm = false;
            for (LoginResponseBean.RoleRSBean mRoleRSBean : loginBean.getRoleRS()) {
                if (Constants.PREPLANER.equals(mRoleRSBean.getRoleCode())) {
                    ToastUtil.showToast(mContext, "组板只能使用PAD登录");
                    return;
                }
                if (Constants.INSTALL_UNLOAD_EQUIP.equals(mRoleRSBean.getRoleCode())||
                        Constants.JUNCTION_LOAD.equals(mRoleRSBean.getRoleCode())||
                        Constants.DRIVEROUT.equals(mRoleRSBean.getRoleCode())||
                        Constants.INSTALL_EQUIP_LEADER.equals(mRoleRSBean.getRoleCode())
                ) {
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
            }
        } else {
            ToastUtil.showToast(mContext, "数据错误");
        }
    }

    @Override
    public void loginQxAiResult(RespLoginBean loginBean) {

    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }
}
