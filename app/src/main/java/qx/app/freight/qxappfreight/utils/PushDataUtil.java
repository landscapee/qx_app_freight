package qx.app.freight.qxappfreight.utils;

import android.app.Activity;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PushDataUtil {
    /**
     * 处理推送信息数据，进行对应的提示和操作
     *
     * @param result         推送数据
     * @param mCurrentTaskId 当前页面的任务id
     * @param context        上下文
     */
    public static void handlePushInfo(CommonJson4List result, String mCurrentTaskId, Context context) {
        if (result != null) {
            String taskId = result.getTaskId();
            if (result.isCancelFlag()&&taskId.equals(mCurrentTaskId)) {
                if (!result.isConfirmTask()){
                    ToastUtil.showToast("当前装卸机任务已取消保障");
                    backToLastView(context);
                }else  {
                    ToastUtil.showToast("当前装卸机任务已取消");
                    backToLastView(context);
                }
            } else if (result.isSplitTask()) {
                if (taskId.equals(mCurrentTaskId)) {
                    ToastUtil.showToast("当前装卸机任务已被拆分，即将回到代办首页");
                    backToLastView(context);
                }
            } else if (result.isChangeWorkerUser()) {
                if (taskId.equals(mCurrentTaskId)) {
                    ToastUtil.showToast("当前装卸机任务已被系统切换操作人，即将回到代办首页");
                    backToLastView(context);
                }
            }
        }
    }

    /**
     * 回退到上一界面
     *
     * @param context 上下文
     */
    private static void backToLastView(Context context) {
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // timer 默认在新线程，所以需要切换回主线程
                .subscribe(aLong -> ((Activity) context).finish());
    }
}
