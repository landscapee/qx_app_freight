package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.bean.response.MsMessageViewBean;
import qx.app.freight.qxappfreight.bean.response.PageListBean;

public class MessageContract {

    public interface messageModel {
        void pageList(BaseFilterEntity baseEntity, IResultLisenter lisenter);
        void msMessageView(BaseFilterEntity baseEntity, IResultLisenter lisenter);
    }

    public interface messageView extends IBaseView {
        void pageListResult(PageListBean pageListBean);
        void msMessageViewResult(MsMessageViewBean msMessageViewBean);
    }
}
