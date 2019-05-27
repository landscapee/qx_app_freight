package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.FreightInfoBean;
import qx.app.freight.qxappfreight.bean.response.MarketCollectionRequireBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class FreightInfoContract {
    public interface freightInfoModel {
        void freightInfo(String id, IResultLisenter lisenter);
    }

    public interface freightInfoView extends IBaseView {
        void freightInfoResult(List<MarketCollectionRequireBean> beanList);
    }

}
