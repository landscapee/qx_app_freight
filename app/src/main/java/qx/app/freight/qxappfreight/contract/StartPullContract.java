package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.PullGoodsEntity;

/**
 * TODO : xxx
 * Created by pr
 */
public class StartPullContract {

    public interface startPullModel {
        void startPull(PullGoodsEntity pullGoodsEntity, IResultLisenter lisenter);
    }

    public interface startPullView extends IBaseView {
        void startPullResult(String result);
    }
}
