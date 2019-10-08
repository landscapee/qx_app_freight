package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.AddInfoEntity;
import qx.app.freight.qxappfreight.bean.response.ExistBean;
import qx.app.freight.qxappfreight.bean.response.ForkliftWorkingCostBean;

/**
 * TODO : 叉车使用费用
 * Created by zyy
 */
public class ForkliftCostContract {

    public interface forkliftModel {
        void addForklift(List<ForkliftWorkingCostBean> forkliftWorkingCostBeans, IResultLisenter lisenter);
        void getForklifts(String waybillId, IResultLisenter lisenter);
    }

    public interface forkliftView extends IBaseView {
        void addForkliftResult(String result);
        void getForkliftsResult(List<ForkliftWorkingCostBean> result);
    }
}
