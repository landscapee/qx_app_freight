package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.ScooterOperateContract;
import qx.app.freight.qxappfreight.model.ScooterOperateModel;

/**
 * Created by guohao on 2019/5/8 15:41 @COPYRIGHT 青霄科技
 *
 * @title：出港配载 理货
 * @description：
 */
public class ScooterOperatePresenter extends BasePresenter {

    public ScooterOperatePresenter(ScooterOperateContract.scooterOperateView scooterOperateView){
        mRequestView = scooterOperateView;
        mRequestModel = new ScooterOperateModel();

    }

    /**
     * 回退到预配 -- guohao
     * @param entity
     *              {
     *                  flightId : 航班业务id
     *                  taskId : 任务id
     *                  userId : 登录用户id
     *              }
     */
    public void returnToPrematching(BaseFilterEntity entity){
        mRequestView.showNetDialog();
        ((ScooterOperateModel)mRequestModel).returnToPrematching(entity, new IResultLisenter() {
            @Override
            public void onSuccess(Object o) {
                ((ScooterOperateContract.scooterOperateView)mRequestView).returnToPrematching(o);
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
