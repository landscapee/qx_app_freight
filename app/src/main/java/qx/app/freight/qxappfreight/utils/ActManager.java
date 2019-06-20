package qx.app.freight.qxappfreight.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

import qx.app.freight.qxappfreight.activity.AddReceiveGoodActivity;
import qx.app.freight.qxappfreight.activity.CollectorDeclareActivity;
import qx.app.freight.qxappfreight.activity.ReceiveGoodsActivity;
import qx.app.freight.qxappfreight.activity.VerifyCargoActivity;
import qx.app.freight.qxappfreight.activity.VerifyFileActivity;
import qx.app.freight.qxappfreight.activity.VerifyStaffActivity;

/**
 * activity 单列
 */
public class ActManager {

    private static Stack<Activity> activityStack;
    private static volatile ActManager instance;

    private ActManager() {
    }

    /**
     * 单一实例
     */
    public static ActManager getAppManager() {
        if (instance == null) {
            synchronized (ActManager.class) {
                if (instance == null)
                    instance = new ActManager();
            }

        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


    //收运关闭
    public void finishReCollection() {
        if (activityStack != null) {
            for (int i = 0; i < activityStack.size(); i++) {
                if ("AddReceiveGoodActivity".equals(activityStack.get(i).getClass().getSimpleName())) {
                    finishActivity(AddReceiveGoodActivity.class);
                }else if("CollectorDeclareActivity".equals(activityStack.get(i).getClass().getSimpleName())){
                    finishActivity(CollectorDeclareActivity.class);
                }else if("ReceiveGoodsActivity".equals(activityStack.get(i).getClass().getSimpleName())){
                    finishActivity(CollectorDeclareActivity.class);
                }
            }
        }
    }

    //收验关闭
    public void finishReceive(){
        if (activityStack != null) {
            for (int i = 0; i < activityStack.size(); i++) {
                if ("VerifyStaffActivity".equals(activityStack.get(i).getClass().getSimpleName())) {
                    finishActivity(VerifyStaffActivity.class);
                }else if("VerifyFileActivity".equals(activityStack.get(i).getClass().getSimpleName())){
                    finishActivity(VerifyFileActivity.class);
                }else if("VerifyCargoActivity".equals(activityStack.get(i).getClass().getSimpleName())){
                    finishActivity(VerifyCargoActivity.class);
                }
            }
        }
    }


    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
