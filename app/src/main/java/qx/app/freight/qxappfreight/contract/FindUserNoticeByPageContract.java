package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.NoticeBean;
import qx.app.freight.qxappfreight.bean.response.NoticeViewBean;

public class FindUserNoticeByPageContract {
    public interface findUserNoticeByPageModel {
        void notice(BaseFilterEntity entity, IResultLisenter lisenter);

        void noticeView(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface findUserNoticeByPageView extends IBaseView {
        void noticeResult(NoticeBean result);

        void noticeViewResult(NoticeViewBean result);
    }
}
