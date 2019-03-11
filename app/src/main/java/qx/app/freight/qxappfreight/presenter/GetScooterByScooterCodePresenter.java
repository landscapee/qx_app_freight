package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.ReturnWeighingEntity;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.contract.GetScooterByScooterCodeContract;
import qx.app.freight.qxappfreight.model.GetScooterByScooterCodeModel;


public class GetScooterByScooterCodePresenter extends BasePresenter {
    public GetScooterByScooterCodePresenter(GetScooterByScooterCodeContract.GetScooterByScooterCodeView getQualificationsView) {
        mRequestView = getQualificationsView;
        mRequestModel = new GetScooterByScooterCodeModel();
    }

    public void getInfosByFlightId(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((GetScooterByScooterCodeModel) mRequestModel).getInfosByFlightId(entity, new IResultLisenter<List<GetInfosByFlightIdBean>>() {
            @Override
            public void onSuccess(List<GetInfosByFlightIdBean> getInfosByFlightIdBeans) {
                ((GetScooterByScooterCodeContract.GetScooterByScooterCodeView) mRequestView).getInfosByFlightIdResult(getInfosByFlightIdBeans);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void getScooterByScooterCode(BaseFilterEntity airLineId) {
        mRequestView.showNetDialog();
        ((GetScooterByScooterCodeModel) mRequestModel).getScooterByScooterCode(airLineId, new IResultLisenter<GetInfosByFlightIdBean>() {
            @Override
            public void onSuccess(GetInfosByFlightIdBean queryAviationRequireBeans) {
                ((GetScooterByScooterCodeContract.GetScooterByScooterCodeView) mRequestView).getScooterByScooterCodeResult(queryAviationRequireBeans);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void saveScooter(GetInfosByFlightIdBean getInfosByFlightIdBean) {
        mRequestView.showNetDialog();
        ((GetScooterByScooterCodeModel) mRequestModel).saveScooter(getInfosByFlightIdBean, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((GetScooterByScooterCodeContract.GetScooterByScooterCodeView) mRequestView).saveScooterResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void returnWeighing(ReturnWeighingEntity getInfosByFlightIdBean) {
        mRequestView.showNetDialog();
        ((GetScooterByScooterCodeModel) mRequestModel).returnWeighing(getInfosByFlightIdBean, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((GetScooterByScooterCodeContract.GetScooterByScooterCodeView) mRequestView).returnWeighingResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void getWeight(String pbName) {
        mRequestView.showNetDialog();
        ((GetScooterByScooterCodeModel) mRequestModel).getWeight(pbName, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((GetScooterByScooterCodeContract.GetScooterByScooterCodeView) mRequestView).getWeightResult(result);
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
