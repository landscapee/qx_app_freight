package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;

public class SynchronousLoadingContract {
    public interface synchronousLoadingModel {
        void synchronousLoading(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface synchronousLoadingView extends IBaseView {
        void synchronousLoadingResult(String result);
    }
}
