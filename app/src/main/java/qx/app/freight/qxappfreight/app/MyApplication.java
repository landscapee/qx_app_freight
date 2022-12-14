package qx.app.freight.qxappfreight.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.beidouapp.imlibapi.IMLIBContext;
import com.liys.doubleclicklibrary.ViewDoubleHelper;

import qx.app.freight.qxappfreight.BuildConfig;
import qx.app.freight.qxappfreight.constant.HttpConstant;
import qx.app.freight.qxappfreight.utils.CrashHandler;
import qx.app.freight.qxappfreight.utils.IMUtils;


public class MyApplication extends Application  {

    private static Context appContext;

    public static String currentView; //当前顶层View的类名

    public static boolean isNeedIm = false;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();

        CrashHandler.getsInstance().init(appContext);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        Log.e("Application：运行模式>>>", BuildConfig.Model);
//        CrashReport.initCrashReport(getApplicationContext(), "5884b765c7", true); //bugly 异常统计

           //处理点击事件重复点击问题
        ViewDoubleHelper.init(this);

        initIM();//初始化IM服务配置
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("tag", "onActivityCreated===" + activity.getLocalClassName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("tag", "onActivityStarted===" + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("tag", "onActivityResumed===" + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("tag", "onActivityPaused===" + activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("tag", "onActivityCreated===" + activity.getLocalClassName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("tag", "onActivitySaveInstanceState===" + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("tag", "onActivityDestroyed===" + activity.getLocalClassName());
            }
        });
    }



    public static void initIM() {
        try {
            IMUtils.initIM();
//            IMLIBContext.getInstance().setSchedualNoticeDetailInterfaceUrl(AppIPConfig.getSchduleApiHost() + HttpConstant.IM_NOTICE_URL);
            IMLIBContext.getInstance().setSchedualNoticeDetailInterfaceUrl("http://" + HttpConstant.CMCC + ":86/api/schedule/" + HttpConstant.IM_NOTICE_URL);
        } catch (Exception e) {
            Log.d("tagMsg", "im初始化失败");
        }
    }


    public static Context getContext() {
        return appContext;
    }
}
