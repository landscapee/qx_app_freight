package qx.app.freight.qxappfreight.presenter;


import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.UldLikeBean;
import qx.app.freight.qxappfreight.contract.LikePageContract;
import qx.app.freight.qxappfreight.model.LikePageModel;

public class LikePagePresenter extends BasePresenter {

    public LikePagePresenter(LikePageContract.likePageView likePageView) {
        mRequestView = likePageView;
        mRequestModel = new LikePageModel();
    }

    public void likePage(BaseFilterEntity entity) {
        ((LikePageModel) mRequestModel).likePage(entity, new IResultLisenter<UldLikeBean>() {
            @Override
            public void onSuccess(UldLikeBean likePageBean) {
                ((LikePageContract.likePageView) mRequestView).likePageResult(likePageBean.getRecords());
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
            }
        });
    }
}