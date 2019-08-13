package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.PullGoodsInfoBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class PullGoodsReportContract {
    public interface pullGoodsModel {
        void getPullGoodsInfo(String flightInfoId, IResultLisenter lisenter);

        void pullGoodsInfoCommit(PullGoodsInfoBean entity, IResultLisenter lisenter);
    }

    public interface pullGoodsView extends IBaseView {
        void getPullGoodsInfoResult(PullGoodsInfoBean result);

        void pullGoodsInfoCommitResult(String result);
    }

}
