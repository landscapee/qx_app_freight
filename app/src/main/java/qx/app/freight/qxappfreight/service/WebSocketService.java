package qx.app.freight.qxappfreight.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.qxkj.positionapp.GPSUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.GpsInfoEntity;
import qx.app.freight.qxappfreight.bean.request.OnlineStutasEntity;
import qx.app.freight.qxappfreight.constant.HttpConstant;
import qx.app.freight.qxappfreight.contract.OnlineStatusContract;
import qx.app.freight.qxappfreight.contract.SaveGpsInfoContract;
import qx.app.freight.qxappfreight.listener.BaggageClient;
import qx.app.freight.qxappfreight.listener.BeforehandClient;
import qx.app.freight.qxappfreight.listener.CollectionClient;
import qx.app.freight.qxappfreight.listener.DeliveryClient;
import qx.app.freight.qxappfreight.listener.InstallEquipClient;
import qx.app.freight.qxappfreight.listener.NewspaperClient;
import qx.app.freight.qxappfreight.listener.OffSiteEscortClient;
import qx.app.freight.qxappfreight.listener.PreplanerClient;
import qx.app.freight.qxappfreight.listener.ReceiveClient;
import qx.app.freight.qxappfreight.listener.WeighterClient;
import qx.app.freight.qxappfreight.presenter.OnlineStatusPresenter;
import qx.app.freight.qxappfreight.presenter.SaveGpsInfoPresenter;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.RxTimer;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import ua.naiksoftware.stomp.StompClient;

public class WebSocketService extends Service implements SaveGpsInfoContract.saveGpsInfoView, OnlineStatusContract.OnlineStatusView {
    public Context mContext;
    //    private static StompClient mStompClient;
    public static final String TAG = "websocket";
    private GpsInfoEntity gpsInfoEntity; // gps 上传实体
    private SaveGpsInfoPresenter saveGpsInfoPresenter;
    private OnlineStatusPresenter onlineStatusPresenter;
    private int taskAssignType = 0;
    public static boolean isTopic = false;
    public static List <StompClient> mStompClient;
    public static String ToList = "/taskTodo/taskTodoList";
    public static String Message = "/MT/msMsg";
    public static String Login = "/MT/message";
    public static String Install = "/departure/installedAdvice";

    public static List <String> subList = new ArrayList <>();

    public  OnlineStutasEntity onlineStutasEntity;

    @Override
    public void onCreate() {
        super.onCreate();
        List <String> ary = Arrays.asList("cargoAgency", "receive", "securityCheck", "collection", "charge");
        mStompClient = new ArrayList <>();
        if (null == UserInfoSingle.getInstance().getRoleRS()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            backGroundStartForeground();
        }
        for (int i = 0; i < UserInfoSingle.getInstance().getRoleRS().size(); i++) {
            if (UserInfoSingle.getInstance().getRoleRS() != null && UserInfoSingle.getInstance().getRoleRS().size() > 0) {
                if (ary.contains(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                    taskAssignType = 1;
                } else if ("delivery_in".equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                    taskAssignType = 3;
                } else {
                    taskAssignType = 2;
                }
            }
            //多角色 需要保持多个 web socket 链接

            switch (UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode()) {
                case "collection": //收运
                    Collection(HttpConstant.WEBSOCKETURL
                            + "userId=" + UserInfoSingle.getInstance().getUserId()
                            + "&taskAssignType=" + taskAssignType
                            + "&type=MT"
                            + "&role=collection");
                    break;
                case "receive": //收验
                    ReceiveClient(HttpConstant.WEBSOCKETURL
                            + "userId=" + UserInfoSingle.getInstance().getUserId()
                            + "&taskAssignType=" + taskAssignType
                            + "&type=MT"
                            + "&role=receive");
                    break;
                case "supervision":  //运输 -装卸机
                    InstallEquipClient(HttpConstant.WEBSOCKETURL
                            + "userId=" + UserInfoSingle.getInstance().getUserId()
                            + "&taskAssignType=" + taskAssignType
                            + "&type=MT"
                            + "&role=supervision");
                    break;
                case "stevedores":  //装卸员小组长任务推送
                    InstallEquipClient(HttpConstant.WEBSOCKETURL
                            + "userId=" + UserInfoSingle.getInstance().getUserId()
                            + "&taskAssignType=" + taskAssignType
                            + "&type=MT"
                            + "&role=supervision");
                    break;
                case "clipping":  //结载
                    InstallEquipClient(HttpConstant.WEBSOCKETURL
                            + "userId=" + UserInfoSingle.getInstance().getUserId()
                            + "&taskAssignType=" + taskAssignType
                            + "&type=MT"
                            + "&role=clipping");
                    break;
                case "offSiteEscort":  //运输
                    OffSiteEscortClient(HttpConstant.WEBSOCKETURL
                            + "userId=" + UserInfoSingle.getInstance().getUserId()
                            + "&taskAssignType=" + taskAssignType
                            + "&type=MT"
                            + "&role=offSiteEscort");
                    break;
                case "preplaner"://预配-组板
                    PreplanerClient(HttpConstant.WEBSOCKETURL
                            + "userId=" + UserInfoSingle.getInstance().getUserId()
                            + "&taskAssignType=" + taskAssignType
                            + "&type=MT"
                            + "&role=preplaner");
                    break;
                case "weighter"://复重
                    WeighterClient(HttpConstant.WEBSOCKETURL
                            + "userId=" + UserInfoSingle.getInstance().getUserId()
                            + "&taskAssignType=" + taskAssignType
                            + "&type=MT"
                            + "&role=weighter");
                    break;
                case "delivery_in"://进港提货
                    DeliveryClient(HttpConstant.WEBSOCKETURL
                            + "userId=" + UserInfoSingle.getInstance().getUserId()
                            + "&taskAssignType=" + taskAssignType
                            + "&type=MT"
                            + "&role=delivery_in");
                    break;
                case "beforehand_in"://进港理货
                    BeforehandClient(HttpConstant.WEBSOCKETURL
                            + "userId=" + UserInfoSingle.getInstance().getUserId()
                            + "&taskAssignType=" + taskAssignType
                            + "&type=MT"
                            + "&role=beforehand_in");
                    break;
                case "porter"://行李员
                    BaggageClient(HttpConstant.WEBSOCKETURL
                            + "userId=" + UserInfoSingle.getInstance().getUserId()
                            + "&taskAssignType=" + taskAssignType
                            + "&type=MT"
                            + "&role=porter");
                    break;
//                case "report"://报载
//                    NewspaperClient(HttpConstant.WEBSOCKETURL
//                            + "userId=" + UserInfoSingle.getInstance().getUserId()
//                            + "&taskAssignType=" + taskAssignType
//                            + "&type=MT"
//                            + "&role=report");
//                    break;

            }
        }
        saveGpsInfoPresenter = new SaveGpsInfoPresenter(this);
        onlineStatusPresenter = new OnlineStatusPresenter(this);
        startSendThread();
    }

    private RxTimer rxTimer;

    private void endThread() {
        if (rxTimer != null) {
            rxTimer.cancel();
        }
    }

    private void startSendThread() {
        endThread();
        if (rxTimer == null) {
            rxTimer = new RxTimer();
        }
        rxTimer.interval(10000, new RxTimer.RxAction() {
            @Override
            public void action(long number) {
                sendGps();
//                sendOnlineStatus();
            }
        });
    }

    private void sendGps() {
        Log.e("GPS位置：", GPSUtils.getInstance().getCurrentLocation().getLatitude() + "");
        if (GPSUtils.getInstance().getCurrentLocation().getLatitude() <= 0.0) {
            Log.e("GPS位置：", "位置为0 不提交");
        } else {
            gpsInfoEntity = new GpsInfoEntity();
            gpsInfoEntity.setLatitude(String.valueOf(GPSUtils.getInstance().getCurrentLocation().getLatitude()));
            gpsInfoEntity.setLongitude(String.valueOf(GPSUtils.getInstance().getCurrentLocation().getLongitude()));
            if (UserInfoSingle.getInstance() != null) {
                gpsInfoEntity.setUserId(UserInfoSingle.getInstance().getUserId());
            } else {
                gpsInfoEntity.setUserId("admin");
            }
            gpsInfoEntity.setTerminalId(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
            saveGpsInfoPresenter.saveGpsInfo(gpsInfoEntity);
        }
    }

    private void sendOnlineStatus() {
        Log.e("登录状态上传：", "已发送" );
        if (onlineStutasEntity == null){
            onlineStutasEntity = new OnlineStutasEntity();
            onlineStutasEntity.setPlatform("android");
            onlineStutasEntity.setUser(UserInfoSingle.getInstance());
        }
        onlineStatusPresenter.onlineStatus(onlineStutasEntity);
    }

    public static void startService(Context activtity) {
        actionStart(activtity);
    }

    private static void actionStart(Context ctx) {
        Intent i = new Intent(ctx, WebSocketService.class);
        ctx.startService(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void backGroundStartForeground() {

        /**
         *通过通知启动服务
         */
        //设定的通知渠道名称
        String channelName = "background_qx_freight_app";
        //设置通知的重要程度
        int importance = NotificationManager.IMPORTANCE_LOW;
        //构建通知渠道
        NotificationChannel channel = new NotificationChannel(channelName, channelName, importance);
        channel.setDescription("");
        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelName);
        builder.setSmallIcon(R.mipmap.ic_logo) //设置通知图标
                .setContentTitle("后台服务")//设置通知标题
                .setContentText("推送已连接")//设置通知内容
                .setAutoCancel(true) //用户触摸时，自动关闭
                .setOngoing(true);//设置处于运行状态
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(2, builder.build());
    }

    public void Collection(String uri) {
        new CollectionClient(uri, this);
    }

    public void InstallEquipClient(String uri) {
        new InstallEquipClient(uri, this);
    }

    public void PreplanerClient(String uri) {
        new PreplanerClient(uri, this);
    }

    public void WeighterClient(String uri) {
        new WeighterClient(uri, this);
    }

    public void DeliveryClient(String uri) {
        new DeliveryClient(uri, this);
    }

    public void BeforehandClient(String uri) {
        new BeforehandClient(uri, this);
    }

    public void OffSiteEscortClient(String uri) {
        new OffSiteEscortClient(uri, this);
    }

    public void ReceiveClient(String uri) {
        new ReceiveClient(uri, this);
    }

    public void NewspaperClient(String uri) {
        new NewspaperClient(uri, this);
    }

    public void BaggageClient(String uri) {
        new BaggageClient(uri, this);
    }


    @Override
    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= 26) {
            stopForeground(true);
        }
        endThread();
        Log.e("WebSocketService：", "onDestroy");
        super.onDestroy();

    }


    @Override
    public void saveGpsInfoResult(String result) {
        Log.e("GPS上传：", result);
    }

    @Override
    public void toastView(String error) {
        if (!StringUtil.isEmpty(error)) {
            Log.e("GPS上传：", error);
        }
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }

    @Override
    public void onlineStatusResult(String result) {
        if (!StringUtil.isEmpty(result)) {
            Log.e("登录状态上传：", "成功：" + result);
        }
    }

    public class WebSocketBinder extends Binder {
        WebSocketService getService() {
            return WebSocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new WebSocketBinder();
    }

    //停止连接 并关闭服务
    public static void stopServer(Context context) {
        //顺便 关闭可能没关闭的声音 和震动
        Tools.closeVibrator(context.getApplicationContext());
        closeLink();
        Intent startSrv = new Intent(context, WebSocketService.class);
        context.stopService(startSrv);
    }

    //停止连接
    public static void closeLink() {
        if (subList != null) {
            subList.clear();
        }
        if (mStompClient != null) {
            if (mStompClient.size() != 0) {
                for (int i = 0; i < mStompClient.size(); i++) {
                    Log.e(TAG, "关闭了" + i + "个连接");
                    mStompClient.get(i).disconnect();
                }
            }
        }

    }

    public synchronized static void setIsTopic(boolean isTopic1) {
        isTopic = isTopic1;
    }

    /**
     * 如果已经订阅 返回 false
     *
     * @param sub
     * @return
     */
    public static boolean isExist(String sub) {
        boolean isExit = true;
        for (String string : subList) {
            if (sub.equals(string)) {
                isExit = false;
            }
        }
        return isExit;
    }

}
