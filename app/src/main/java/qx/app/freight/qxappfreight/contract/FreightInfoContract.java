package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.FreightInfoBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class FreightInfoContract {
    public interface freightInfoModel {
        void freightInfo(String id, IResultLisenter lisenter);
    }

    public interface freightInfoView extends IBaseView {
        void freightInfoResult(FreightInfoBean freightInfoBean);
    }

}
