package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;

public class PrintRequestContract {
    public interface printRequestModel {
        void printRequest(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface printRequestView extends IBaseView {
        void printRequestResult(String result);
    }
}
