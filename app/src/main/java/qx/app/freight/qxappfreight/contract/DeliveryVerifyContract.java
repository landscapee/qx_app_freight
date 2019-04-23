package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ChangeWaybillEntity;
import qx.app.freight.qxappfreight.bean.request.DeclareWaybillEntity;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;

public class DeliveryVerifyContract {

    public interface deliveryVerifyModel{
        //获取数据
        void deliveryVerify(DeclareWaybillEntity declareWaybillEntity, IResultLisenter lisenter);
        //提交
        void changeSubmit(ChangeWaybillEntity changeWaybillEntity, IResultLisenter lisenter);
    }

    public interface deliveryVerifyView extends IBaseView {

        void deliveryVerifyResult(DeclareWaybillBean transportListBean);
        void changeSubmitResult(Object object);
    }

}
