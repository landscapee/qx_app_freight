package qx.app.freight.qxappfreight.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private static Context appContext;




    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

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

    public static Context getContext() {

        return appContext;
    }



}
