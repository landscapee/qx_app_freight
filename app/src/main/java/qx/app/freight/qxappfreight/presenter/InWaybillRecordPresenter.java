package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordGetEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitEntity;
import qx.app.freight.qxappfreight.bean.response.InWaybillRecordBean;
import qx.app.freight.qxappfreight.contract.InWaybillRecordContract;
import qx.app.freight.qxappfreight.model.InWaybillRecordModel;

/**
 * 进港分拣
 *
 * create by guohao - 2019/4/25
 */
public class InWaybillRecordPresenter extends BasePresenter {

    public InWaybillRecordPresenter(InWaybillRecordContract.inWaybillRecordView inWaybillRecordView) {
        mRequestView = inWaybillRecordView;
        mRequestModel = new InWaybillRecordModel();
    }

    public void getList(InWaybillRecordGetEntity entity){
        mRequestView.showNetDialog();
        ((InWaybillRecordModel)mRequestModel).getList(entity, new IResultLisenter<InWaybillRecordBean>() {
            @Override
            public void onSuccess(InWaybillRecordBean bean) {
                ((InWaybillRecordContract.inWaybillRecordView)mRequestView).resultGetList(bean);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void submit(InWaybillRecordSubmitEntity entity){
        ((InWaybillRecordModel)mRequestModel).submit(entity, new IResultLisenter() {
            @Override
            public void onSuccess(Object o) {
                ((InWaybillRecordContract.inWaybillRecordView)mRequestView).resultSubmit(o);
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void deleteById(String id){
        ((InWaybillRecordModel)mRequestModel).deleteById(id, new IResultLisenter() {
            @Override
            public void onSuccess(Object o) {
                ((InWaybillRecordContract.inWaybillRecordView)mRequestView).resultDeleteById(o);
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void allGoodsArrived(InWaybillRecord data){
        ((InWaybillRecordModel)mRequestModel).allGoodsArrived(data, new IResultLisenter() {
            @Override
            public void onSuccess(Object o) {
                ((InWaybillRecordContract.inWaybillRecordView)mRequestView).allGoodsArrivedResult(o);
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
}
