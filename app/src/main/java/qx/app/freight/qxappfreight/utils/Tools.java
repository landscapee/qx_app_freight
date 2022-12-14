package qx.app.freight.qxappfreight.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jwenfeng.library.pulltorefresh.util.DisplayUtil;

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
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.LoginActivity;
import qx.app.freight.qxappfreight.activity.MainActivity;
import qx.app.freight.qxappfreight.activity.MsgDialogAct;
import qx.app.freight.qxappfreight.activity.MsgDialogVisibleAct;
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.bean.PositionBean;
import qx.app.freight.qxappfreight.bean.ScooterMapSingle;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldFlightBean;
import qx.app.freight.qxappfreight.bean.response.RespLoginBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.fragment.IOManifestFragment;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.loactionUtils.BSLoactionUtil;
import qx.app.freight.qxappfreight.widget.CommonDialog;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * TODO : ?????????????????????
 * Created by owen
 * on 2016-09-08.
 */
public class Tools {
    public static String getFilePath() {
        return Objects.requireNonNull(Objects.requireNonNull(MyApplication.getContext()).getExternalCacheDir()).getAbsolutePath() + "/";
    }

    /**
     * ???????????????????????????????????????
     *
     * @param context
     * @return
     */
    public static String getRoleName(Context context) {

        String roleName = "";
        if (Constants.SECURITY_CHECK.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "??????";
        } else if (Constants.COLLECTION.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "??????";
        } else if (Constants.INVENTORY_KEEPER.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "??????";
        } else if (Constants.CARGO_AGENCY.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "??????";
        } else if (Constants.RECEIVE.equals(SharedPreferencesUtil.getString(context, "role", null))) {
            roleName = "??????";
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

    }

    public static void saveLoginNameAndPassword(String login, String password) {
        //?????????????????????
        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.KEY_LOGIN_NAME, login);
        //???????????????????????????
        SharedPreferencesUtil.setString(MyApplication.getContext(), Constants.KEY_LOGIN_PWD, password);
    }

    public static String getLoginNameForLogin() {
        String loginName = SharedPreferencesUtil.getString(MyApplication.getContext(), Constants.KEY_LOGIN_NAME, "");
        return loginName;
    }

    public static String getPassword() {
        String password = SharedPreferencesUtil.getString(MyApplication.getContext(), Constants.KEY_LOGIN_PWD, "");
        return password;
    }

    public static String getLoginName() {
        String loginName = SharedPreferencesUtil.getString(MyApplication.getContext(), Constants.realName, "");
        return loginName;
    }

    private static long lastClickTime;

    /**
     * ?????????????????? ???????????? ??????????????????
     *
     * @return
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (lastClickTime >= 0 && time - lastClickTime < 1000) {
            return false;
        }
        lastClickTime = time;
        return true;
    }

    /**
     * ??????????????????
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
            Toast.makeText(context, "??????????????????????????????????????????", Toast.LENGTH_LONG).show();
        }
    }

    public static List <MultipartBody.Part> filesToMultipartBodyParts(List <File> files) {
        List <MultipartBody.Part> parts = new ArrayList <>(files.size());
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            //"files"????????? ????????? ????????? ?????? key
            MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    /**
     * clone ???
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
     * ???????????????
     *
     * @param src
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> ArrayList <T> deepCopy(ArrayList <T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        ArrayList <T> dest = (ArrayList <T>) in.readObject();
        return dest;
    }


    /**
     * TODO: ?????? ????????????
     */
    public static void saveGPSPosition(PositionBean bean) {
        SaveUtils.getInstance().setValue(Constants.POSITION, bean);
    }

    /**
     * TODO: ?????? ????????????
     */
    public static PositionBean getGPSPosition() {
        PositionBean bean = (PositionBean) SaveUtils.getInstance().getValue(Constants.POSITION);
        if (bean == null)
            return null;
        return bean;
    }

    /**
     * TODO: ???????????????????????????????????????
     */
    public static boolean isValidForLocasition(long time) {
        Long s = (System.currentTimeMillis() - time) / (1000 * 60);
        return s < Constants.ValidTime;
    }

    private static final String KEY_BSLoaction = "BSLoactionUtil";

    /**
     * TODO: ????????????????????????
     */
    public static void saveBSLocation(qx.app.freight.qxappfreight.utils.loactionUtils.BSLoactionUtil.BSLocationBean bean) {
        SaveUtils.getInstance().setValue(KEY_BSLoaction, bean);
    }

    /**
     * TODO: ????????????????????????
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

    /**
     * ???????????????????????????????????????
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(ACTIVITY_SERVICE);
        List <ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "???appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "????????????"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "????????????"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * TODO: ????????????????????? --
     */
    public static String returnTime(long s) {
        if (s == 0)
            return "- -";
        else
            return TimeUtils.date2Tasktime(s);
    }


    /**
     * ???????????? ????????????id
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
     * ????????????/??????
     */
    public static void closeVibrator(Context context) {
        VibrationUtils.cancel(context);
        SoundConfigUtils.getInstance(context).stopMediaPlayer();
    }

    /**
     * ????????????/??????
     * ????????????????????????
     * ?????????????????????id
     */
    public static void startVibrator(Context context, boolean isforcedispose, int rawId) {
        SoundConfigUtils.getInstance(context).playMediaPlayer(0, isforcedispose, rawId);
        VibrationUtils.openVibrator(context.getApplicationContext(), isforcedispose);//??????????????????????????????????????????????????????
    }

    /**
     * ???????????????
     */
    public static void startShortVibrator(Context context) {
        VibrationUtils.openShortVibrator(context.getApplicationContext());//????????????????????????????????????
        startShortSound(context);
    }

    /**
     * ???????????????
     */
    public static void startShortSound(Context context) {
        try {
            Uri uri = Uri.parse("android.resource://" + MyApplication.getContext().getPackageName() + "/" + R.raw.beep);
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), uri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
//        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
    }

    /**
     * ???????????????????????????
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
     * ???????????????????????????
     *
     * @return
     */
    public static boolean compareFist(String first, String second) {
        if (first != null && second != null) {
            if (first.equals(second))
                return true;
            else {
                if (first.length() > 0 && second.length() > 0) {
                    first = first.substring(0, 1);
                    second = second.substring(0, 1);
                    if (first.equals(second))
                        return true;
                    else
                        return false;
                } else {
                    return false;
                }
            }
        } else
            return false;
    }


    /**
     * ?????????????????? dialog
     *
     * @param mContext
     */
    public static void showDialog(Context mContext) {

        loginOut(mContext);

        CommonDialog dialog = new CommonDialog(mContext);
        dialog.setTitle("??????")
                .setMessage("???????????????????????????????????????????????????")
                .setNegativeButton("??????")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener((dialog1, confirm) -> {
                });
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> dialog.show());
    }

    //????????????
    public static void loginOut(Context mContext) {
        UserInfoSingle.setUserNil();
        ScooterMapSingle.getInstance().clear();
        ActManager.getAppManager().finishAllActivity();
        WebSocketService.stopServer(MyApplication.getContext());
        IMUtils.imLoginout();
        Intent intent = new Intent(MyApplication.getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);

        //?????? ????????????
        if (IOManifestFragment.iOqrcodeEntity != null) {
            IOManifestFragment.iOqrcodeEntity = null;
        }

    }

    /**
     * ????????????
     *
     * @param context
     */
    public static void wakeupScreen(Context context) {
        if (isBackground(context) || !isScreenOn(context)) {
            if (isScreenOn(context)) {
                wakeupApp(context);
                return;
            }
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //???????????????????????????
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            //??????PowerManager.WakeLock??????,???????????????|???????????????????????????,????????????LogCat?????????Tag
            //????????????
            wl.acquire();
            //??????
            wl.release();
            wakeupApp(context);
            Log.e("?????????", "??????");
            if (Build.VERSION.SDK_INT >= 26) {
                if (isLocked(context)) {
                    MsgDialogAct.startActivity(context);
                }
            } else {
                if (isLocked(context)) {
                    MsgDialogVisibleAct.startActivity(context);
                }
            }
        }
    }

    public static void wakeupApp(Context context) {
//        Intent intent;
//        intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
////        if (intent != null) {
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        }
//        context.startActivity(intent);
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(intent);
        Log.e("?????????", "??????app");
    }

    /**
     * ????????????????????????
     *
     * @param context
     * @return
     */
    public static boolean isLocked(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }

    /**
     * ??????????????????????????????
     *
     * @param context
     * @return
     */
    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();//?????????true????????????????????????????????????????????????????????????
        return isScreenOn;
    }

    public static void unLock(Context context) {
        //???????????????
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //??????
        kl.disableKeyguard();
        Log.e("?????????", "??????");
    }

    public static int groupImlibUid(OutFieldFlightBean flights) {
        if ("D".equals(flights.getMovement())) {
            if (flights.getSuccessionId() != 0) {

                return flights.getSuccessionId();
            } else {
                return flights.getFlightId();
            }
        } else
            return flights.getFlightId();

    }

    public static int groupImlibUid(LoadAndUnloadTodoBean flights) {
        if (flights.getMovement() == 1 || flights.getMovement() == 4) {
            return Integer.valueOf(flights.getFlightId());
        } else if (flights.getLoadingAndUnloadBean() != null && flights.getLoadingAndUnloadBean().getSuccessionId() != null && !StringUtil.isEmpty(flights.getLoadingAndUnloadBean().getSuccessionId())) {
            if (Integer.valueOf(flights.getLoadingAndUnloadBean().getSuccessionId()) > 0) {
                return Integer.valueOf(flights.getLoadingAndUnloadBean().getSuccessionId());
            } else
                return Integer.valueOf(flights.getFlightId());
        } else
            return Integer.valueOf(flights.getFlightId());
    }

    /**
     * ?????????????????? ?????? ??????
     *
     * @param specialCode
     * @return
     */
    public static String getVolumeForSpCode(String specialCode) {
        String volume = specialCode;
        if (specialCode.contains("/")) {
            volume = specialCode.substring(0, specialCode.indexOf("/"));
        }
        return volume;
    }

    /**
     * ?????????????????? ?????? ?????????????????????
     *
     * @param specialCode
     * @return
     */
    public static String getSpCodeForSpCode(String specialCode) {
        String spCode = specialCode;
        if (specialCode.contains("/")) {
            spCode = specialCode.substring(specialCode.indexOf("/"), specialCode.length() - 1);
        } else
            spCode = "";
        return spCode;
    }

    /**
     * ????????????????????????RecylerView??????????????????
     *
     * @param num  list???size,??????????????????item
     * @param view recyclerView
     */
    public static void setRecyclerViewCenterHor(Activity context, int num, View view) {
        int interval = DisplayUtil.dp2Px(context, 20);//??????item?????????????????????(px)?????????????????????
        int itemWidth = DisplayUtil.dp2Px(context, 30);//??????item?????????????????????(px)?????????????????????
        WindowManager wm = context.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        if (num < 6) {
            int padding = (width - num * (interval + itemWidth)) / 2 - 30;
            view.setPadding(padding, 0, padding, 0);
        } else {
            view.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param view ??????
     * @param n    ??????
     */
    public static void scroll2Position(RecyclerView view, int n) {
        LinearLayoutManager manager = (LinearLayoutManager) view.getLayoutManager();
        if (manager == null) {
            return;
        }
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            view.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = view.getChildAt(n - firstItem).getTop();
            view.scrollBy(0, top);
        } else {
            view.scrollToPosition(n);
        }
    }

    /**
     * 0.??????????????????; 1.??????????????????; 2.??????????????????; 3.??????????????????; 4.???????????????; 5.?????????; 6.?????????; 7.???????????????; 8.?????????; -1????????????(??????); -2.??????(????????????); -10.????????????
     *
     * @param status
     * @return
     */
    public static String getWaybillStatus(int status) {
        String strStatus = "??????";
        switch (status) {
            case 0:
                strStatus = "??????????????????";
                break;
            case 1:
                strStatus = "??????????????????";
                break;
            case 2:
                strStatus = "??????????????????";
                break;
            case 3:
                strStatus = "??????????????????";
                break;
            case 4:
                strStatus = "???????????????";
                break;
            case 5:
                strStatus = "?????????";
                break;
            case 6:
                strStatus = "?????????";
                break;
            case 7:
                strStatus = "???????????????";
                break;
            case 8:
                strStatus = "?????????";
                break;
            case -1:
                strStatus = "????????????";
                break;
            case -2:
                strStatus = "??????";
                break;
            case -10:
                strStatus = "????????????";
                break;


        }
        return strStatus;
    }
}
