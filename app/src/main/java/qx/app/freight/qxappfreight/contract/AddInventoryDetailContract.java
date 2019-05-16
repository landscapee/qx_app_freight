package qx.app.freight.qxappfreight.contract;

import java.util.List;

import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;
import qx.app.freight.qxappfreight.bean.response.ListWaybillCodeBean;

public class AddInventoryDetailContract {
    public interface addInventoryDetailModel {
        void addInventoryDetail(List<InventoryDetailEntity> list, IResultLisenter lisenter);
        void uploads(List<MultipartBody.Part> files, IResultLisenter lisenter);
        void listWaybillCode(String code, IResultLisenter lisenter);
    }

    public interface addInventoryDetailView extends IBaseView {
        void addInventoryDetailResult(String result);
        void uploadsResult(Object result);
        void listWaybillCodeResult(ListWaybillCodeBean listWaybillCodeBean);
    }
}
