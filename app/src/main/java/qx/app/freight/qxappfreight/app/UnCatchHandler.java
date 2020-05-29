package qx.app.freight.qxappfreight.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import qx.app.freight.qxappfreight.bean.ScooterMapSingle;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.ExceptionContentContract;
import qx.app.freight.qxappfreight.presenter.ExceptionContentPresenter;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.ActManager;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * 全局捕获异常类，实现Thread.UncaughtExceptionHandler
 *
 * @author hasee
 */
public class UnCatchHandler implements Thread.UncaughtExceptionHandler, ExceptionContentContract.exceptionContentView {
    private static UnCatchHandler mUnCatchHandler;
    private Context context;

    /**
     * 一定要写个单例
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
        this.context = context;
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
    public void uncaughtException(Thread t, Throwable ex) {
        ExceptionContentPresenter mPresenter = new ExceptionContentPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setExceptionContent(ex.getStackTrace().toString());
        mPresenter.exceptionContent(entity);
        doSomething(t, ex);
    }

    private void doSomething(Thread thread, Throwable ex) {
        boolean isHandle = handleException(ex);
        if (!isHandle && mUnCatchHandler != null) {
            // 如果我们没有处理则让系统默认的异常处理器来处理
            mUnCatchHandler.uncaughtException(thread, ex);
        } else {
            try {
                //给Toast留出时间
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e("22222", "uncaughtException() InterruptedException:" + e);
            }
            UserInfoSingle.setUserNil();
            ScooterMapSingle.getInstance().clear();
            ActManager.getAppManager().finishAllActivity();
            WebSocketService.stopServer(context);
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis(), restartIntent);
            android.os.Process.killProcess(android.os.Process.myPid());


        }

    }


    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    Toast toast;
                    toast = Toast.makeText(context, "程序出现异常，即将退出", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);

                    toast.show();
                    Looper.loop();
//                    hasToast = true;
                } catch (Exception e) {
                    Log.e("22222", "handleException Toast error" + e);
                }

            }
        }).start();


        if (ex == null) {
            return false;
        }
//
//        if (mIsDebug) {
//            // 收集设备参数信息
//            collectDeviceInfo();
//            // 保存日志文件
//            saveCatchInfo2File(ex);
//        }

        return true;
    }


    @Override
    public void exceptionContentResult(String result) {
        ToastUtil.showToast(result);
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }
}
