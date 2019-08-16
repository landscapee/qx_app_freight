package qx.app.freight.qxappfreight.contract;

import java.util.List;

import io.reactivex.annotations.Nullable;
import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.AddInfoEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ExistBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class ScooterInfoListContract {
    public interface scooterInfoListModel {
        void scooterInfoList(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
        void scooterInfoListForReceive(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
        void exist(String scooterId, IResultLisenter lisenter);
        void addInfo(MyAgentListBean myAgentListBean, IResultLisenter lisenter);
    }

    public interface scooterInfoListView extends IBaseView {
        void scooterInfoListResult(List<ScooterInfoListBean> scooterInfoListBeans);
        void scooterInfoListForReceiveResult(List<ScooterInfoListBean> scooterInfoListBeans);
        void existResult(MyAgentListBean existBean);
        void addInfoResult(MyAgentListBean result);
    }
}
