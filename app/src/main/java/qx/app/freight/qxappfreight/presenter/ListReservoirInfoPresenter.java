package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.ReservoirAreaBean;
import qx.app.freight.qxappfreight.bean.response.ReservoirBean;
import qx.app.freight.qxappfreight.contract.ListReservoirInfoContract;
import qx.app.freight.qxappfreight.contract.LoginContract;
import qx.app.freight.qxappfreight.model.ListReservoirInfoModel;
import qx.app.freight.qxappfreight.model.LoginModel;

/**
 * Created by guohao on 2019/5/21 23：19
 * 库区接口
 */
public class ListReservoirInfoPresenter extends BasePresenter {


    public ListReservoirInfoPresenter(ListReservoirInfoContract.listReservoirInfoView view) {
        mRequestView = view;
        mRequestModel = new ListReservoirInfoModel();
    }

    public void listReservoirInfoByCode(String deptCode){

        mRequestView.showNetDialog();
        ((ListReservoirInfoModel)mRequestModel).listReservoirInfo(deptCode, new IResultLisenter<ReservoirAreaBean>() {
            @Override
            public void onSuccess(ReservoirAreaBean bean) {
                ((ListReservoirInfoContract.listReservoirInfoView)mRequestView).listReservoirInfoResult(bean);
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
