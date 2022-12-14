package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordGetEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitNewEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.InWaybillRecordBean;
import qx.app.freight.qxappfreight.bean.response.WaybillQuickGetBean;
import qx.app.freight.qxappfreight.contract.InWaybillRecordContract;
import qx.app.freight.qxappfreight.model.InWaybillRecordModel;

/**
 * 进港分拣
 * <p>
 * create by guohao - 2019/4/25
 */
public class InWaybillRecordPresenter extends BasePresenter {

    public InWaybillRecordPresenter(InWaybillRecordContract.inWaybillRecordView inWaybillRecordView) {
        mRequestView = inWaybillRecordView;
        mRequestModel = new InWaybillRecordModel();
    }

    public void getList(InWaybillRecordGetEntity entity) {
        mRequestView.showNetDialog();
        ((InWaybillRecordModel) mRequestModel).getList(entity, new IResultLisenter<InWaybillRecordBean>() {
            @Override
            public void onSuccess(InWaybillRecordBean bean) {
                ((InWaybillRecordContract.inWaybillRecordView) mRequestView).resultGetList(bean);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void submit(InWaybillRecordSubmitNewEntity entity) {
        ((InWaybillRecordModel) mRequestModel).submit(entity, new IResultLisenter<BaseEntity<Object>>() {
            @Override
            public void onSuccess(BaseEntity<Object> o) {
                ((InWaybillRecordContract.inWaybillRecordView) mRequestView).resultSubmit(o);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void deleteById(String id) {
        ((InWaybillRecordModel) mRequestModel).deleteById(id, new IResultLisenter() {
            @Override
            public void onSuccess(Object o) {
                ((InWaybillRecordContract.inWaybillRecordView) mRequestView).resultDeleteById(o);
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void allGoodsArrived(InWaybillRecordSubmitNewEntity.SingleLineBean data) {
        ((InWaybillRecordModel) mRequestModel).allGoodsArrived(data, new IResultLisenter<WaybillQuickGetBean>() {
            @Override
            public void onSuccess(WaybillQuickGetBean result) {
                ((InWaybillRecordContract.inWaybillRecordView) mRequestView).allGoodsArrivedResult(result);
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
}
