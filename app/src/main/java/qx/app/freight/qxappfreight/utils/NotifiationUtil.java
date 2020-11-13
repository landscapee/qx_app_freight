package qx.app.freight.qxappfreight.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.LoginActivity;
import qx.app.freight.qxappfreight.activity.MainActivity;
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.constant.Constants;

/**
 * Created by mm on 2016/11/21.
 */
public class NotifiationUtil {

    private static NotificationManager mManager;

//    /**
//     * TODO: 显示通知栏
//     *
//     * @param message
//     */
//    public static void showNotification(Context context, String title, String ContentTitle, String message, int id, Intent intent) {
//        NotificationManager manger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setTicker(title);
//        builder.setContentText(message);
//        builder.setContentTitle(ContentTitle);
//        builder.setAutoCancel(true);//可以点击通知栏的删除按钮删除
//        //系统状态栏显示的小图标
//        builder.setSmallIcon(R.mipmap.ic_logo);
//        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_logo));
//        if (intent == null) {
//            intent = new Intent(context, MainAct.class);
//        }
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pIntent = PendingIntent.getActivity(context, 1, intent, 0);
//        //点击跳转的intent
//        builder.setContentIntent(pIntent);
//        //通知默认的声音 震动 呼吸灯
//        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
//        Notification notification = builder.build();
//        manger.notify(id, notification);
//    }


    public static void showNotification(Context mContext, String title, String ContentTitle, String message, int id, int rawId, Intent intent) {
        if (mManager == null) {
            mManager = (NotificationManager) mContext.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
//        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_notification);
//        remoteViews.setTextViewText(R.id.tv_notify_title, ContentTitle);
//        remoteViews.setTextViewText(R.id.tv_notify_content, message);
//        remoteViews.setTextViewText(R.id.tv_notify_time, TimeUtils.dateDayHourMin(System.currentTimeMillis()));
        NotificationCompat.Builder notifyBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(mContext).setContentTitle(title)
                        .setContentText(message)
                        // 点击消失
                        .setAutoCancel(true)
//                        .setContent(remoteViews)
                        .setContentTitle(ContentTitle)
                        // 设置该通知优先级
                        .setPriority(Notification.PRIORITY_MAX)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_logo))
                        .setSmallIcon(R.mipmap.plane)
                        .setTicker(title)// 通知首次出现在通知栏，带上升动画效果的
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setWhen(System.currentTimeMillis());
        // 通知产生的时间，会在通知信息里显示
        try {
            Uri uri = null;
            switch (rawId) {
                case R.raw.flight_notice:
                    uri = Uri.parse("android.resource://" + MyApplication.getContext().getPackageName() + "/" + R.raw.flight_notice);
                    break;
                case R.raw.im_notice:
                    uri = Uri.parse("android.resource://" + MyApplication.getContext().getPackageName() + "/" + R.raw.im_notice);
                    break;
                case R.raw.im_special:
                    uri = Uri.parse("android.resource://" + MyApplication.getContext().getPackageName() + "/" + R.raw.im_special);
                    break;
            }
            if (uri != null) {
                notifyBuilder.setSound(uri).setDefaults(Notification.DEFAULT_VIBRATE);
            } else if (id == Constants.NOTIFY_ID_PUSH_QUIET)//eta变更，通知不给声音和震动提示
            {
                notifyBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
            } else
                // 向通知添加声音、闪灯和振动效果的最简单
            {
                notifyBuilder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND);
            }
        } catch (Exception e) {
            notifyBuilder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND);
        }
        Intent resultIntent = null;
        if (StringUtil.isEmpty(Tools.getToken())) {
            resultIntent = new Intent(mContext, LoginActivity.class);
        } else if (intent == null) {
            resultIntent = new Intent(mContext, MainActivity.class);
        } else {
            resultIntent = intent;
        }
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notifyBuilder.setContentIntent(resultPendingIntent);
        mManager.notify(id, notifyBuilder.build());
    }

    /**
     * 关闭某个通知
     *
     * @param mContext
     * @param id
     */
    public static void cancelNotification(Context mContext, int id) {
        if (mManager == null) {
            mManager = (NotificationManager) mContext.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        mManager.cancel(id);
    }


    /**
     * 关闭应用所有通知
     *
     * @param mContext
     */
    public static void cancelAllNotify(Context mContext) {
        if (mManager == null) {
            mManager = (NotificationManager) mContext.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        mManager.cancelAll();
    }
}
