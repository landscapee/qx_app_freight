package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.TestInfoListBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class TestInfoContract {
    public interface testInfoModel {
        void testInfo(String waybillId, IResultLisenter lisenter);
    }

    public interface testInfoView extends IBaseView {
        void testInfoResult(TestInfoListBean testInfoListBeanList);
    }
}
