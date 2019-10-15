package qx.app.freight.qxappfreight.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import qx.app.freight.qxappfreight.BuildConfig;
import qx.app.freight.qxappfreight.activity.CustomCaptureActivity;
import qx.app.freight.qxappfreight.activity.LoginActivity;
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.bean.PositionBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.RespLoginBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.fragment.IOManifestFragment;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.widget.CommonDialog;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * TODO : 程序特有工具类
 * Created by owen
 * on 2016-09-08.
 */
public class Tools {
    public static String getFilePath() {
        return Objects.requireNonNull(Objects.requireNonNull(MyApplication.getContext()).getExternalCacheDir()).getAbsolutePath() + "/";
    }

    /**
     * 获取当前登录用户得角色名称
     *
     * @param context
     * @return
     */
    public static String getRoleName(Context context) {

        String roleName = "";
        if (Constants.SECURITY_CHECK.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "安检";
        } else if (Constants.COLLECTION.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "收运";
        } else if (Constants.INVENTORY_KEEPER.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "货运";
        } else if (Constants.CARGO_AGENCY.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "货代";
        } else if (Constants.RECEIVE.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "收验";
        }
        return roleName;
    }


    public static void setLoginUserBean(RespLoginBean userBean) {
        // token
        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.token, userBean.getToken());
        // userId
        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.userId, userBean.getUserId());//
        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.realName, userBean.getCnname());
        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.deptcode, userBean.getDeptcode());

        //当前登录的账号
        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.KEY_LOGIN_NAME, userBean.getLoginName());
        //当前登录账号的密码
//        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.KEY_LOGIN_PWD, userBean.get);
    }


    private static long lastClickTime;

    /**
     * 是否在一秒类 连续点击 ，规避误操作
     *
     * @return
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (lastClickTime > 0 && time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 设置顶部权限
     *
     * @param context
     */
    public static void applyCommonPermission(Context context) {
        try {
            Class clazz = Settings.class;
            Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
            Intent intent = new Intent(field.get(null).toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            //"files"与后台 沟通后 确定的 接收 key
            MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    /**
     * clone 类
     *
     * @param obj
     * @param <T>
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static <T extends Serializable> T IOclone(T obj) throws ClassNotFoundException, IOException {
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;

        ByteArrayInputStream bins = null;
        ObjectInputStream ojs = null;

        try {
            oos = new ObjectOutputStream(bous);
            oos.writeObject(obj);

            bins = new ByteArrayInputStream(bous.toByteArray());
            ojs = new ObjectInputStream(bins);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                oos.close();
                ojs.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return (T) ojs.readObject();
    }

    /**
     * 列表深拷贝
     *
     * @param src
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> ArrayList<T> deepCopy(ArrayList<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        ArrayList<T> dest = (ArrayList<T>) in.readObject();
        return dest;
    }


    /**
     * TODO: 保存 位置信息
     */
    public static void saveGPSPosition(PositionBean bean) {
        SaveUtils.getInstance().setValue(Constants.POSITION, bean);
    }

    /**
     * TODO: 得到 位置信息
     */
    public static PositionBean getGPSPosition() {
        PositionBean bean = (PositionBean) SaveUtils.getInstance().getValue(Constants.POSITION);
        if (bean == null)
            return null;
        return bean;
    }

    /**
     * TODO: 获取当前的位置信息是否有效
     */
    public static boolean isValidForLocasition(long time) {
        Long s = (System.currentTimeMillis() - time) / (1000 * 60);
        return s < Constants.ValidTime;
    }

    private static final String KEY_BSLoaction = "BSLoactionUtil";

    /**
     * TODO: 保存基站位置信息
     */
    public static void saveBSLocation(BSLoactionUtil.BSLocationBean bean) {
        SaveUtils.getInstance().setValue(KEY_BSLoaction, bean);
    }

    /**
     * TODO: 获取基站位置信息
     */
    public static BSLoactionUtil.BSLocationBean getBSLoaction() {
        return (BSLoactionUtil.BSLocationBean) SaveUtils.getInstance().getValue(KEY_BSLoaction);
    }

    public static String getToken() {
        String token = SharedPreferencesUtil.getString(MyApplication.getContext(), Constants.token, "");
        return token;
    }

    public static String getRealName() {
        String realName = SharedPreferencesUtil.getString(MyApplication.getContext(), Constants.realName, "");
        return realName;
    }

    public static String getLoginName() {
        String loginName = SharedPreferencesUtil.getString(MyApplication.getContext(), Constants.realName, "");
        return loginName;
    }

    /**
     * 判断当前程序是否在后台运行
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * TODO: 字符串为空返回 --
     */
    public static String returnTime(long s) {
        if (s == 0)
            return "- -";
        else
            return TimeUtils.date2Tasktime(s);
    }


    /**
     * 自动生成 板车业务id
     *
     * @return
     */
    public static String generateUniqueKey() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void showSoftKeyboard(View view) {
        Handler handle = new Handler();

        handle.postDelayed(() -> {
            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view, 0);
        }, 0);
    }

    /**
     * 关闭震动/铃音
     */
    public static void closeVibrator(Context context) {
        VibrationUtils.cancel(context);
        SoundConfigUtils.getInstance(context).stopMediaPlayer();
    }

    /**
     * 开启震动/铃音
     * 是否一直循环提醒
     * 指定铃音的资源id
     */
    public static void startVibrator(Context context, boolean isforcedispose, int rawId) {
        SoundConfigUtils.getInstance(context).playMediaPlayer(0, isforcedispose, rawId);
        VibrationUtils.openVibrator(context.getApplicationContext(), isforcedispose);//开启震动提醒，长时间震动和短时间震动
    }

    /**
     * 开启短震动
     */
    public static void startShortVibrator(Context context) {
        VibrationUtils.openShortVibrator(context.getApplicationContext());//开启震动提醒，短时间震动
        startShortSound(context);
    }

    /**
     * 短暂提示音
     */
    public static void startShortSound(Context context) {
//        try {
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
//            r.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
    }

    /**
     * 判断是否是生产环境
     *
     * @return
     */
    public static boolean isProduct() {
        if (BuildConfig.Model.equals("product"))
            return true;
        else
            return false;
    }
    /**
     * 判断是否是生产环境
     *
     * @return
     */
    public static boolean compareFist(String first,String second) {
        if (first!=null&&second!=null){
            if (first.equals(second))
                return  true;
            else {
                if (first.length()>0&&second.length()>0){
                    first = first.substring(0,1);
                    second = second.substring(0,1);
                    if (first.equals(second))
                        return true;
                    else
                        return false;
                }
                else {
                    return false;
                }
            }
        }
        else
            return false;
    }


    /**
     * 登录被挤下线 dialog
     * @param mContext
     */
    public static void showDialog(Context mContext) {

        loginOut(mContext);

        CommonDialog dialog = new CommonDialog(mContext);
        dialog.setTitle("提示")
                .setMessage("你的账号在其他地方登陆！请重新登陆")
                .setNegativeButton("确定")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener((dialog1, confirm) ->{});
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> dialog.show());
    }

    //强制登出
    public static void loginOut(Context mContext) {
        UserInfoSingle.setUserNil();
        ActManager.getAppManager().finishAllActivity();
        WebSocketService.stopServer(MyApplication.getContext());
        IMUtils.imLoginout();
        Intent intent = new Intent(MyApplication.getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);

        //清空 库房信息
        if (IOManifestFragment.iOqrcodeEntity != null){
            IOManifestFragment.iOqrcodeEntity = null;
        }

    }

    /**
     * 开启扫码
     * @param activity
     */
    public static void startScanCaptureAct(Activity activity){
        new IntentIntegrator(activity)
                // 自定义Activity，重点是这行----------------------------
                .setCaptureActivity(CustomCaptureActivity.class)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                .setPrompt("请对准二维码")// 设置提示语
                .setCameraId(0)// 选择摄像头,可使用前置或者后置
                .setBeepEnabled(true)// 是否开启声音,扫完码之后会"哔"的一声
                .setBarcodeImageEnabled(true)// 扫完码之后生成二维码的图片
                .initiateScan();// 初始化扫码
    }

    /**
     * 唤醒屏幕
     *
     * @param context
     */
    public static void wakeupScreen(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm.isScreenOn()) {
            return;
        }
        //屏锁管理器
        KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "bright");
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        //点亮屏幕
        wl.acquire();

        //释放
        wl.release();
    }


}
