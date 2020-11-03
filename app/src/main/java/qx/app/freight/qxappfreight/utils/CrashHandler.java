package qx.app.freight.qxappfreight.utils;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class CrashHandler implements Thread.UncaughtExceptionHandler {


    private static final String TAG = "CrashHandler";

    private static final boolean DEBUG = true;


    private static final String PATH = Environment.getExternalStorageDirectory().getPath()

            + "/CrashTest/log/";

    private static final String FILE_NAME = "crash";

    private static final String FILE_NAME_SUFFIX = ".txt";
    private String uploadUrl = "http://10.16.23.156:6080/service-product-transport/tp-main-info/exceptionUpload";
//    private String uploadUrl = "http://173.101.2.8/service-product-transport/tp-main-info/exceptionUpload";
    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    private static CrashHandler sInstance = new CrashHandler();

    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private Context mContext;


    private CrashHandler() {


    }


    public static CrashHandler getsInstance() {

        return sInstance;

    }


    public void init(Context context) {

        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(this);

        mContext = context.getApplicationContext();

//        uploadExceptionToServer();

    }


    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用uncaughtException方法
     * <p>
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息
     *
     * @param thread
     * @param ex
     */


    @Override

    public void uncaughtException(Thread thread, Throwable ex) {
        boolean crash = catchCrashException(ex);
        if (!crash && mDefaultCrashHandler != null) {
            //没有自定义的CrashHandler的时候就调用系统默认的异常处理方式
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            ex.printStackTrace();
            //退出应用
            killProcess();
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex 未捕获的异常
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean catchCrashException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        dumpExceptionToSP(ex);
//        dumpExceptionToSDCard(ex);

        return true;
    }

    /**
     * 退出应用
     */
    private void killProcess() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e("application", "error : ", e);
        }

        // 退出程序
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);// 1秒钟后重启应用
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    /**
     * 将异常写入到sp
     *
     * @param ex
     */
    private void dumpExceptionToSP(Throwable ex) {
        SharedPreferences sp = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE);
        String exception = sp.getString("exception", "");
        StringBuilder sb = new StringBuilder();
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
            sb.append(exception).append("\n").append(time).append("\n")
                    .append("App Name: ").append(pi.packageName).append("\n")
                    .append("App Version: ").append(pi.versionName).append("_").append(pi.versionCode).append("\n")
                    .append("OS Version: ").append(Build.VERSION.RELEASE).append("_").append(Build.VERSION.SDK_INT).append("\n")
                    .append("制造商：").append(Build.MANUFACTURER).append("\n")
                    .append("型号：").append(Build.MODEL).append("\n")
                    .append("CPU ABI: ").append(Build.CPU_ABI).append("\n")
                    .append(ex.getClass().getName()).append(": ").append(ex.getMessage()).append("\n")
                    .append(Arrays.toString(ex.getStackTrace()));
            sp.edit().putString("exception", sb.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void dumpExceptionToSDCard(Throwable ex) throws IOException {

        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            if (DEBUG) {

                Log.e(TAG, "sdcard unmounted,skip dump exception");

                return;

            }

        }


        File dir = new File(PATH);

        if (!dir.exists()) {

            dir.mkdirs();

        }

        long current = System.currentTimeMillis();

        String time = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(new Date(current));

        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);

        try {

            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

            pw.println(time);

            dumpPhoneInfo(pw);

            pw.println();

            ex.printStackTrace(pw);

            pw.close();

            Log.e(TAG, "dump crash info seccess");

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());

            Log.e(TAG, "dump crash info failed");

        }


    }


    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {

        PackageManager pm = mContext.getPackageManager();

        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),

                PackageManager.GET_ACTIVITIES);

        pw.print("App Name: ");
        pw.println(pi.packageName);

        pw.print("App Version: ");

        pw.print(pi.versionName);

        pw.print('_');

        pw.println(pi.versionCode);


        //Android版本号

        pw.print("OS Version: ");

        pw.print(Build.VERSION.RELEASE);

        pw.print("_");

        pw.println(Build.VERSION.SDK_INT);

        //手机制造商

        pw.print("Vendor: ");

        pw.println(Build.MANUFACTURER);

        //手机型号

        pw.print(" Model: ");

        pw.println(Build.MODEL);

        //CPU架构

        pw.print("CPU ABI: ");

        pw.println(Build.CPU_ABI);

    }

    //将异常信息发送到服务器
    public void uploadExceptionToServer() {
        SharedPreferences sp = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE);
        String exception = sp.getString("exception", "");

        if (TextUtils.isEmpty(exception.trim())) return;

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor()).build();
        Map<String, String> map = new HashMap<>();
        map.put("exceptionContent", exception);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        Request request = new Request.Builder().url(uploadUrl).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call,  Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                        if ("200".equals(jsonObject.getString("status"))) {
                            sp.edit().putString("exception", "").apply();
                        }
                    }
                }
            }
        });

    }

}
