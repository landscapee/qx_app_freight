package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.ListWaybillCodeBean;
import qx.app.freight.qxappfreight.contract.ListWaybillCodeContract;
import qx.app.freight.qxappfreight.model.ListWaybillCodeModel;

public class ListWaybillCodePresenter extends BasePresenter {

    public ListWaybillCodePresenter(ListWaybillCodeContract.listWaybillCodeView listWaybillCodeView) {
        mRequestView = listWaybillCodeView;
        mRequestModel = new ListWaybillCodeModel();
    }

    public void listWaybillCode(String code) {
        mRequestView.showNetDialog();
        ((ListWaybillCodeModel) mRequestModel).listWaybillCode(code, new IResultLisenter<ListWaybillCodeBean>() {
            @Override
            public void onSuccess(ListWaybillCodeBean result) {
                ((ListWaybillCodeContract.listWaybillCodeView) mRequestView).listWaybillCodeResult(result);
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
