package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.BaseParamBean;

public class BaseParamTypeContract {
    public interface baseParamTypeModel {
        void baseParamType(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface baseParamTypeView extends IBaseView {
        void baseParamTypeResult(BaseParamBean changeStorageBean);
    }
}
