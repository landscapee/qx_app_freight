package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.QueryContainerInfoEntity;
import qx.app.freight.qxappfreight.bean.response.QueryContainerInfoBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class QueryContainerInfoContract {
    public interface queryContainerInfoModel {
        void queryContainerInfo(QueryContainerInfoEntity queryContainerInfoEntity, IResultLisenter lisenter);
    }

    public interface queryContainerInfoView extends IBaseView {
        void queryContainerInfoResult(List<QueryContainerInfoBean> queryContainerInfoBean);
    }
}
