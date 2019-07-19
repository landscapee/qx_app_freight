package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.UpdatePwdEntity;

public class UpdatePWDContract {
    public interface updatePWDModel {
        void updatePWD(UpdatePwdEntity entity, IResultLisenter lisenter);
    }

    public interface updatePWDView extends IBaseView {
        void updatePWDResult(String result);
    }
}
