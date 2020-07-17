package qx.app.freight.qxappfreight.contract;

import java.util.List;

import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.ListWaybillCodeBean;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;

public class AddInventoryDetailContract {
    public interface addInventoryDetailModel {
        void addInventoryDetail(List <InventoryDetailEntity> list, IResultLisenter lisenter);

        void uploads(List <MultipartBody.Part> files, IResultLisenter lisenter);

        void listWaybillCode(String code, String taskId, IResultLisenter lisenter);

        void getWaybillCode(IResultLisenter lisenter);

        void getWaybillInfoByWaybillCode(String waybillCode, IResultLisenter lisenter);

    }

    public interface addInventoryDetailView extends IBaseView {
        void addInventoryDetailResult(BaseEntity result);

        void uploadsResult(Object result);

        void listWaybillCodeResult(ListWaybillCodeBean listWaybillCodeBean);

        void getWaybillCodeResult(String result);

        void getWaybillInfoByWaybillCodeResult(WaybillsBean result);

        void getWaybillInfoByWaybillCodeResultFail();
    }
}
