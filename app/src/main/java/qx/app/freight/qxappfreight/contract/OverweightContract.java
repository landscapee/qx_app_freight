package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.AddInfoEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ExistBean;
import qx.app.freight.qxappfreight.bean.response.OverweightBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class OverweightContract {

    public interface OverweightModel {
        void getOverWeight(BaseFilterEntity entity, IResultLisenter lisenter);
        void addOverWeight(List<OverweightBean> entity, IResultLisenter lisenter);
        void deleteOverWeight(OverweightBean entity, IResultLisenter lisenter);
    }

    public interface OverweightView extends IBaseView {
        void getOverWeightResult(List <OverweightBean> result);
        void addOverWeightResult(String result);
        void deleteOverWeightResult(String result);
    }
}
