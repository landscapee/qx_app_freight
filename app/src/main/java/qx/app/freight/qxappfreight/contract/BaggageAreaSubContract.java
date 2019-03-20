package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;

public class BaggageAreaSubContract {
    public interface baggageAreaSubModel {
        void baggageAreaSub(BaseFilterEntity model, IResultLisenter lisenter);
        void scooterInfoList(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
    }

    public interface baggageAreaSubView extends IBaseView {
        void scooterInfoListResult(List<ScooterInfoListBean> scooterInfoListBeans);
        void baggageAreaSubResult(String result);
    }
}
