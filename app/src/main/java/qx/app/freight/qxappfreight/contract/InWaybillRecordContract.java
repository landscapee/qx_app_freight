package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordGetEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitNewEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.InWaybillRecordBean;
import qx.app.freight.qxappfreight.bean.response.WaybillQuickGetBean;

/**
 * 进港分拣
 * create by guohao - 2018/4/25
 */
public class InWaybillRecordContract {

    public interface inWaybillRecordModel {
        void getList(InWaybillRecordGetEntity entity, IResultLisenter lisenter);

        void submit(InWaybillRecordSubmitNewEntity entity, IResultLisenter<BaseEntity<Object>> lisenter);

        void deleteById(String id, IResultLisenter lisenter);

        void allGoodsArrived(InWaybillRecordSubmitNewEntity.SingleLineBean data, IResultLisenter lisenter);
    }

    public interface inWaybillRecordView extends IBaseView {
        void resultGetList(InWaybillRecordBean bean);

        void resultSubmit(BaseEntity<Object> result);

        void resultDeleteById(Object o);

        void allGoodsArrivedResult(WaybillQuickGetBean o);
    }
}
