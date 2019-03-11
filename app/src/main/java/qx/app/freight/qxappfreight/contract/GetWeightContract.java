package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.FreightInfoBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class GetWeightContract {
    public interface getWeightModel {
        void getWeight(String pbName, IResultLisenter lisenter);
    }

    public interface getWeightView extends IBaseView {
        void getWeightResult(String result);
    }
}
