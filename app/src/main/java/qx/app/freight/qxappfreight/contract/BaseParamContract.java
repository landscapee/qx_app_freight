package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.BaseParamBean;

public class BaseParamContract {
    public interface baseParamModel {
        void baseParam(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface baseParamView extends IBaseView {
        void baseParamResult(BaseParamBean changeStorageBean);
    }
}
