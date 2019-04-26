package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordGetEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitEntity;
import qx.app.freight.qxappfreight.bean.response.InWaybillRecordBean;

/**
 * 进港分拣
 * create by guohao - 2018/4/25
 */
public class InWaybillRecordContract {

    public interface inWaybillRecordModel{
        void getList(InWaybillRecordGetEntity entity, IResultLisenter lisenter);

        void submit(InWaybillRecordSubmitEntity entity, IResultLisenter lisenter);

        void deleteById(String id, IResultLisenter lisenter);
    }

    public interface inWaybillRecordView extends IBaseView{
        void resultGetList(InWaybillRecordBean bean);

        void resultSubmit(Object o);

        void resultDeleteById(Object o);
    }
}
