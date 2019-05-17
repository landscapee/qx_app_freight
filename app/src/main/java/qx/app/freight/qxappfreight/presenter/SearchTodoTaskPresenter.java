package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.AddScooterBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.contract.AddScooterContract;
import qx.app.freight.qxappfreight.contract.SearchTodoTaskContract;
import qx.app.freight.qxappfreight.model.AddScooterModel;
import qx.app.freight.qxappfreight.model.SearchTodoTaskModel;

/**
 * Created by guohao on 2019/5/17 16:18 @COPYRIGHT 青霄科技
 *
 * @title：
 * @description：
 */
public class SearchTodoTaskPresenter extends BasePresenter {

    public SearchTodoTaskPresenter(SearchTodoTaskContract.searchTodoTaskView searchTodoTaskView) {
        mRequestView = searchTodoTaskView;
        mRequestModel = new SearchTodoTaskModel();
    }

    public void searchTodoTask(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((SearchTodoTaskModel) mRequestModel).searchTodoTask(entity,new IResultLisenter<TransportListBean>() {
            @Override
            public void onSuccess(TransportListBean result) {
                ((SearchTodoTaskContract.searchTodoTaskView) mRequestView).searchTodoTaskResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }

        });
    }
}
