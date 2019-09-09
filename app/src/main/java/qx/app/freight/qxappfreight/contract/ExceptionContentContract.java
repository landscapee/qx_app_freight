package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;

public class ExceptionContentContract {
    public interface exceptionContentModel {
        void exceptionContent(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface exceptionContentView extends IBaseView {
        void exceptionContentResult(String result);
    }
}
