package qx.app.freight.qxappfreight.presenter;

import android.util.Log;

import java.util.List;

import io.reactivex.annotations.Nullable;
import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.AddInfoEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ExistBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.contract.AddInfoContract;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.model.AddInfoModel;
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
        ((ScooterInfoListModel) mRequestModel).scooterInfoList(model, new IResultLisenter<List<ScooterInfoListBean>>() {
            @Override
            public void onSuccess(List<ScooterInfoListBean> scooterInfoListBeans) {
                ((ScooterInfoListContract.scooterInfoListView) mRequestView).scooterInfoListResult(scooterInfoListBeans);
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
