package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.QueryAviationRequireBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class GetQueryAviationRequireContract {
    public interface getQueryAviationRequireModel {
        void getQueryAviationRequire(String airLineId, IResultLisenter lisenter);
    }

    public interface getQueryAviationRequireView extends IBaseView {
        void getQueryAviationRequireResult(List<QueryAviationRequireBean> queryAviationRequireBean);
    }
}
