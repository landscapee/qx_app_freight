package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListDataBean;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.model.ScooterInfoListModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class ScooterInfoListPresenter extends BasePresenter {
    public ScooterInfoListPresenter(ScooterInfoListContract.scooterInfoListView scooterInfoListView) {
        mRequestView = scooterInfoListView;
        mRequestModel = new ScooterInfoListModel();
    }

    public void ScooterInfoList(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((ScooterInfoListModel) mRequestModel).scooterInfoList(model, new IResultLisenter<ScooterInfoListDataBean>() {
            @Override
            public void onSuccess(ScooterInfoListDataBean scooterInfoListBeans) {
                ((ScooterInfoListContract.scooterInfoListView) mRequestView).scooterInfoListResult(scooterInfoListBeans.getRecords());
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void ScooterInfoListForReceicve(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((ScooterInfoListModel) mRequestModel).scooterInfoListForReceive(model, new IResultLisenter<ScooterInfoListDataBean>() {
            @Override
            public void onSuccess(ScooterInfoListDataBean scooterInfoListBeans) {
                ((ScooterInfoListContract.scooterInfoListView) mRequestView).scooterInfoListForReceiveResult(scooterInfoListBeans.getRecords());
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void exist(String scooterid) {
        mRequestView.showNetDialog();
        ((ScooterInfoListModel) mRequestModel).exist(scooterid, new IResultLisenter<MyAgentListBean>() {
            @Override
            public void onSuccess(MyAgentListBean result) {
                ((ScooterInfoListContract.scooterInfoListView) mRequestView).existResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void addInfo(MyAgentListBean myAgentListBean) {
        mRequestView.showNetDialog();
        ((ScooterInfoListModel) mRequestModel).addInfo(myAgentListBean, new IResultLisenter<MyAgentListBean>() {
            @Override
            public void onSuccess(MyAgentListBean result) {
                ((ScooterInfoListContract.scooterInfoListView) mRequestView).addInfoResult(result);
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
