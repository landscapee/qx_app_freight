package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ChangeStorageBean;

public class ChangeStorageContract {
    public interface changeStorageModel {
        void changeStorage(ChangeStorageBean entity, IResultLisenter lisenter);
    }

    public interface changeStorageView extends IBaseView {
        void changeStorageResult(String result);
    }
}
