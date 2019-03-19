package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;

public class BaggageAreaSubContract {
    public interface baggageAreaSubModel {
        void baggageAreaSub(BaseFilterEntity model, IResultLisenter lisenter);
    }

    public interface baggageAreaSubView extends IBaseView {
        void baggageAreaSubResult(String result);
    }
}
