package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.PickGoodsRecordsBean;

/**
 * TODO : 进港提货 出库记录查询 和撤销
 * Created by zyy
 */
public class PickGoodsRecordsContract {

    public interface pickGoodsModel {
        void getOutboundList(String waybillId, IResultLisenter lisenter);
        void revokeInboundDelevery(PickGoodsRecordsBean pickGoodsRecordsBean,IResultLisenter lisenter);
    }

    public interface pickGoodsView extends IBaseView {
        void getOutboundListResult(List<PickGoodsRecordsBean> result);
        void revokeInboundDeleveryResult(String result);
    }
}
