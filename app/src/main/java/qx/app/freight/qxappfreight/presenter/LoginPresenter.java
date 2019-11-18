package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.LoginEntity;
import qx.app.freight.qxappfreight.bean.request.ReqLoginBean;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.bean.response.RespLoginBean;
import qx.app.freight.qxappfreight.contract.LoginContract;
import qx.app.freight.qxappfreight.model.LoginModel;

public class LoginPresenter extends BasePresenter {

    public LoginPresenter(LoginContract.loginView loginView) {
        mRequestView = loginView;
        mRequestModel = new LoginModel();
    }

    public void login(LoginEntity loginEntity) {
        mRequestView.showNetDialog();
        ((LoginModel) mRequestModel).login(loginEntity, new IResultLisenter<LoginResponseBean>() {
            @Override
            public void onSuccess(LoginResponseBean loginBean) {
                ((LoginContract.loginView) mRequestView).loginResult(loginBean);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void loginQxAi(ReqLoginBean loginEntity) {
        mRequestView.showNetDialog();
        ((LoginModel) mRequestModel).loginQxAi(loginEntity, new IResultLisenter<RespLoginBean>() {
            @Override
            public void onSuccess(RespLoginBean loginBean) {
                if (loginBean.isSucc())
                    ((LoginContract.loginView) mRequestView).loginQxAiResult(loginBean);
                else
                    mRequestView.toastView(loginBean.getMsg());
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }


}
