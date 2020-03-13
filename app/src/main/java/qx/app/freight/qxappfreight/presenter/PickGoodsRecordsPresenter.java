package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.AddInfoEntity;
import qx.app.freight.qxappfreight.bean.response.PickGoodsRecordsBean;
import qx.app.freight.qxappfreight.contract.AddInfoContract;
import qx.app.freight.qxappfreight.contract.PickGoodsRecordsContract;
import qx.app.freight.qxappfreight.model.AddInfoModel;
import qx.app.freight.qxappfreight.model.PickGoodsRecordsModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class PickGoodsRecordsPresenter extends BasePresenter {

    public PickGoodsRecordsPresenter(PickGoodsRecordsContract.pickGoodsView pickGoodsView) {
        mRequestView = pickGoodsView;
        mRequestModel = new PickGoodsRecordsModel();
    }

    public void getOutboundList(String waybillId) {
        mRequestView.showNetDialog();
        ((PickGoodsRecordsModel) mRequestModel).getOutboundList(waybillId, new IResultLisenter <List <PickGoodsRecordsBean>>() {
            @Override
            public void onSuccess(List <PickGoodsRecordsBean> result) {
                ((PickGoodsRecordsContract.pickGoodsView) mRequestView).getOutboundListResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void revokeInboundDelevery(PickGoodsRecordsBean pickGoodsRecordsBean) {
        mRequestView.showNetDialog();
        ((PickGoodsRecordsModel) mRequestModel).revokeInboundDelevery(pickGoodsRecordsBean, new IResultLisenter <String>() {
            @Override
            public void onSuccess(String result) {
                ((PickGoodsRecordsContract.pickGoodsView) mRequestView).revokeInboundDeleveryResult(result);
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
