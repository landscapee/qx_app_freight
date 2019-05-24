package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.TestInfoListBean;
import qx.app.freight.qxappfreight.contract.TestInfoContract;
import qx.app.freight.qxappfreight.model.TestInfoModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class TestInfoPresenter extends BasePresenter {

    public TestInfoPresenter(TestInfoContract.testInfoView testInfoView) {
        mRequestView = testInfoView;
        mRequestModel = new TestInfoModel();
    }

    public void testInfo(String waybillId,String freightId,String mTaskTypeCode) {
        mRequestView.showNetDialog();
        ((TestInfoModel) mRequestModel).testInfo(waybillId,freightId,mTaskTypeCode, new IResultLisenter<TestInfoListBean>() {
            @Override
            public void onSuccess(TestInfoListBean testInfoListBeans) {
                ((TestInfoContract.testInfoView) mRequestView).testInfoResult(testInfoListBeans);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
}
