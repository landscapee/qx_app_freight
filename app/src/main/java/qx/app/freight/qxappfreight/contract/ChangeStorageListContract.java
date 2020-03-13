package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.DeclareApplyForRecords;

public class ChangeStorageListContract {
    public interface changeStorageListModel {
        void changeStorageList(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface changeStorageListView extends IBaseView {
        void changeStorageListResult(DeclareApplyForRecords result);
    }
}
