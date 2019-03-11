package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.GetQualificationsBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class GetQualificationsContract {
    public interface getQualificationsModel {
        void getQualifications(String freightCode, IResultLisenter lisenter);
    }

    public interface getQualificationsView extends IBaseView {
        void getQualificationsResult(GetQualificationsBean getQualificationsBean);
    }
}
