package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ErrorFilingEntity;

public class InPortTallyErrorFilingContract {
    public interface InPortErrorFilingModel {
        void errorFiling(ErrorFilingEntity entity, IResultLisenter lisenter);
    }

    public interface InPortErrorFilingView extends IBaseView {
        void errorFilingResult(String result);
    }
}
