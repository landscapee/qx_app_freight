package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.OnlineStutasEntity;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.contract.OnlineStatusContract;
import qx.app.freight.qxappfreight.model.OnlineStatusModel;

/**
 * @ProjectName:
 * @Package: qx.app.freight.qxappfreight.presenter
 * @ClassName: OnlineStatusPresenter
 * @Description: java类作用描述
 * @Author: 张耀
 * @CreateDate: 2021/3/17 14:48
 * @UpdateUser: 更新者：
 * @UpdateDate: 2021/3/17 14:48
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class OnlineStatusPresenter extends BasePresenter {
    public OnlineStatusPresenter(OnlineStatusContract.OnlineStatusView onlineStatusView) {
        mRequestView = onlineStatusView;
        mRequestModel = new OnlineStatusModel();
    }

    public void onlineStatus(OnlineStutasEntity userInfo) {
        mRequestView.showNetDialog();
        ((OnlineStatusModel) mRequestModel).onlineStatus(userInfo, new IResultLisenter <String>() {
            @Override
            public void onSuccess(String result) {
                ((OnlineStatusContract.OnlineStatusView) mRequestView).onlineStatusResult(result);
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
