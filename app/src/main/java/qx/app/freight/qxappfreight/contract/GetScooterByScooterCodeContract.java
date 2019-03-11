package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.ReturnWeighingEntity;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;

public class GetScooterByScooterCodeContract {

    public interface getScooterByScooterCodeModel {
        void getInfosByFlightId(BaseFilterEntity entity, IResultLisenter lisenter);

        void getScooterByScooterCode(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);

        void saveScooter(GetInfosByFlightIdBean getInfosByFlightIdBean, IResultLisenter lisenter);

        void returnWeighing(ReturnWeighingEntity returnWeighingEntity, IResultLisenter lisenter);

        void getWeight(String pbName, IResultLisenter lisenter);
    }

    public interface GetScooterByScooterCodeView extends IBaseView {
        void getInfosByFlightIdResult(List<GetInfosByFlightIdBean> getInfosByFlightIdBeans);

        void getScooterByScooterCodeResult(GetInfosByFlightIdBean getInfosByFlightIdBean);

        void saveScooterResult(String result);

        void returnWeighingResult(String result);

        void getWeightResult(String result);
    }

}
