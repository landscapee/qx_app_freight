package qx.app.freight.qxappfreight.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import qx.app.freight.qxappfreight.activity.LoginActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.ActManager;
import qx.app.freight.qxappfreight.utils.ToastUtil;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

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
    public void uncaughtException(Thread t, Throwable e) {
        Toast.makeText(context,"系统正在重启，请稍候...",Toast.LENGTH_SHORT).show();
        UserInfoSingle.setUserNil();
        ActManager.getAppManager().finishAllActivity();
        WebSocketService.stopServer(context);
//        Activity activity = ActManager.getAppManager().currentActivity();
//        //获取activity对象，可以通过基类Activity的静态方法获取
//        if (activity != null && !(activity instanceof LoginActivity)) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }
//            restartApp(activity);
//        }
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis(), restartIntent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    private  void restartApp(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
//        @SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity( activity.getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
//        AlarmManager mgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis(), restartIntent);
        //杀死老线程
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
