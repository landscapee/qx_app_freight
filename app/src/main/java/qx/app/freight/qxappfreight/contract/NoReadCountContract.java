package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.bean.request.UserBean;
import qx.app.freight.qxappfreight.bean.response.RespBean;

public class NoReadCountContract {
    public interface noReadCountModel {
        void noReadCount(PageListEntity pageListEntity, IResultLisenter lisenter);

        void noReadNoticeCount(String userId,IResultLisenter lisenter);

        void loginOut(UserBean userBean,IResultLisenter lisenter);
    }

    public interface noReadCountView extends IBaseView {
        void noReadCountResult(String result);

        void noReadNoticeCountResult(String result);

        void loginOutResult(RespBean respBean);
    }
}
