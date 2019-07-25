package qx.app.freight.qxappfreight.contract;


import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ListByTypeBean;

public class ListByTypeContract {
    public interface listByTypeModel {
        void listByType(BaseFilterEntity entity, IResultLisenter lisenter);

    }

    public interface listByTypeView extends IBaseView {
        void listByTypeResult(ListByTypeBean result);

    }
}
