package qx.app.freight.qxappfreight.presenter;



import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ScooterTransitBean;
import qx.app.freight.qxappfreight.bean.response.GroundAgentBean;
import qx.app.freight.qxappfreight.contract.GroundAgentContract;
import qx.app.freight.qxappfreight.model.GroundAgentModel;

public class GroundAgentPresenter extends BasePresenter {
    public GroundAgentPresenter(GroundAgentContract.GroundAgentView groundAgentView) {
        mRequestView = groundAgentView;
        mRequestModel = new GroundAgentModel();
    }

    /**
     * 新增基础板车信息
     * @param scooter
     */
    public void newScooter(ScooterTransitBean scooter) {
        mRequestView.showNetDialog();
        ((GroundAgentModel) mRequestModel).newScooter(scooter, new IResultLisenter <String>() {
            @Override
            public void onSuccess(String result) {
                ((GroundAgentContract.GroundAgentView) mRequestView).newScooterResult();
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }

        });
    }

    /**
     * 获取 地面代理 配置信息
     */
    public void getAllAgent() {
        mRequestView.showNetDialog();
        ((GroundAgentModel) mRequestModel).getAllAgent(new IResultLisenter <List <GroundAgentBean>>() {
            @Override
            public void onSuccess(List <GroundAgentBean> result) {
                ((GroundAgentContract.GroundAgentView) mRequestView).getAllAgentResult(result);
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
