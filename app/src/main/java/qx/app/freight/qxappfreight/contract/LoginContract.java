package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.LoginEntity;
import qx.app.freight.qxappfreight.bean.request.ReqLoginBean;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.bean.response.RespLoginBean;

public class LoginContract {
    public interface loginModel {
        void login(LoginEntity loginEntity, IResultLisenter lisenter);
        void loginQxAi(ReqLoginBean loginEntity, IResultLisenter lisenter);
    }

    public interface loginView extends IBaseView {
        void loginResult(LoginResponseBean loginBean);
        void loginQxAiResult(RespLoginBean loginBean);
    }
}

