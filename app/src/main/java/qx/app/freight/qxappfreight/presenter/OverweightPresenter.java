package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.AddInfoEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.OverweightBean;
import qx.app.freight.qxappfreight.contract.AddInfoContract;
import qx.app.freight.qxappfreight.contract.OverweightContract;
import qx.app.freight.qxappfreight.model.AddInfoModel;
import qx.app.freight.qxappfreight.model.OverweightModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class OverweightPresenter extends BasePresenter {

    public OverweightPresenter(OverweightContract.OverweightView overweightView) {
        mRequestView = overweightView;
        mRequestModel = new OverweightModel();
    }

    public void getOverweight( BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((OverweightModel) mRequestModel).getOverWeight(entity, new IResultLisenter <List <OverweightBean>>() {
            @Override
            public void onSuccess(List <OverweightBean> result) {
                ((OverweightContract.OverweightView) mRequestView).getOverWeightResult(result);
                mRequestView.dissMiss();
            }
            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void addOverweight(List<OverweightBean> entity) {
        mRequestView.showNetDialog();
        ((OverweightModel) mRequestModel).addOverWeight(entity, new IResultLisenter <String>() {
            @Override
            public void onSuccess(String result) {
                ((OverweightContract.OverweightView) mRequestView).addOverWeightResult(result);
                mRequestView.dissMiss();
            }
            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void deleteOverweight(OverweightBean entity) {
        mRequestView.showNetDialog();
        ((OverweightModel) mRequestModel).deleteOverWeight(entity, new IResultLisenter <String>() {
            @Override
            public void onSuccess(String result) {
                ((OverweightContract.OverweightView) mRequestView).deleteOverWeightResult(result);
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
