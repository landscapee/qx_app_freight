package qx.app.freight.qxappfreight.contract;

import java.util.List;

import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;

public class AddInventoryDetailContract {
    public interface addInventoryDetailModel {
        void addInventoryDetail(List<InventoryDetailEntity> list, IResultLisenter lisenter);
        void uploads(List<MultipartBody.Part> files, IResultLisenter lisenter);
    }

    public interface addInventoryDetailView extends IBaseView {
        void addInventoryDetailResult(String result);
        void uploadsResult(Object result);
    }
}
