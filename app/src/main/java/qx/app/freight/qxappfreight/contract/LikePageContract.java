package qx.app.freight.qxappfreight.contract;


import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.LikePageBean;

public class LikePageContract {
    public interface likePageModel {
        void likePage(BaseFilterEntity entity, IResultLisenter lisenter);

    }

    public interface likePageView extends IBaseView {
        void likePageResult(List<LikePageBean> result);

    }
}
