package qx.app.freight.qxappfreight.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * 全局捕获异常类，实现Thread.UncaughtExceptionHandler
 *
 * @author hasee
 */
public class UnCatchHandler implements Thread.UncaughtExceptionHandler {
    private static UnCatchHandler mUnCatchHandler;
    private Context context;
    /**
     * 一定要写个单例
     *
     */
    public static UnCatchHandler getInstance(Context context) {
        if (mUnCatchHandler == null) {
            synchronized (UnCatchHandler.class) {
                mUnCatchHandler = new UnCatchHandler(context);
            }
        }
        return mUnCatchHandler;
    }

    private UnCatchHandler(Context context) {
        this.context=context;
        init();
    }

    void init() {
        //获取默认的系统异常捕获器
        //把当前的crash捕获器设置成默认的crash捕获器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 保存我们抛出的异常至SD卡
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            ToastUtil.showToast(e.getLocalizedMessage());
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            Log.i("tagError", e.getLocalizedMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
