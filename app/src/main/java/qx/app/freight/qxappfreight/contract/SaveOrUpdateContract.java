package qx.app.freight.qxappfreight.contract;


import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.SaveOrUpdateEntity;

public class SaveOrUpdateContract {
    public interface saveOrUpdateModel {
        void saveOrUpdate(SaveOrUpdateEntity entity, IResultLisenter lisenter);
    }

    public interface saveOrUpdateView extends IBaseView {
        void saveOrUpdateResult(String result);
    }
}
