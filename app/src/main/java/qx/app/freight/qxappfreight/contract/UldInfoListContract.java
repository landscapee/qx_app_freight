package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.UldInfoListBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class UldInfoListContract {
    public interface uldInfoListModel {
        void uldInfoList(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
    }

    public interface uldInfoListView extends IBaseView {
        void uldInfoListResult(List<UldInfoListBean> scooterInfoListBeans);
    }
}
