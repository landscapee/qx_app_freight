package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.AirlineRequireBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class AirlineRequireContract {
    public interface airlineRequireModel {
        void airlineRequire(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
    }

    public interface airlineRequireView extends IBaseView {
        void airlineRequireResult(List<AirlineRequireBean> airlineRequireBeans);
    }

}
