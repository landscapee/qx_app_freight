package qx.app.freight.qxappfreight.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.qxkj.positionapp.GPSUtils;

import java.util.Arrays;
import java.util.List;

import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.GpsInfoEntity;
import qx.app.freight.qxappfreight.constant.HttpConstant;
import qx.app.freight.qxappfreight.contract.SaveGpsInfoContract;
import qx.app.freight.qxappfreight.listener.BeforehandClient;
import qx.app.freight.qxappfreight.listener.CollectionClient;
import qx.app.freight.qxappfreight.listener.DeliveryClient;
import qx.app.freight.qxappfreight.listener.InstallEquipClient;
import qx.app.freight.qxappfreight.listener.OffSiteEscortClient;
import qx.app.freight.qxappfreight.listener.PreplanerClient;
import qx.app.freight.qxappfreight.listener.ReceiveClient;
import qx.app.freight.qxappfreight.listener.WeighterClient;
import qx.app.freight.qxappfreight.presenter.SaveGpsInfoPresenter;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import ua.naiksoftware.stomp.StompClient;

public class WebSocketService extends Service implements SaveGpsInfoContract.saveGpsInfoView {
    public Context mContext;
    private static StompClient mStompClient;
    public static final String TAG = "websocket";
    private GpsInfoEntity gpsInfoEntity; // gps 上传实体
    private SaveGpsInfoPresenter saveGpsInfoPresenter;
    private int taskAssignType = 0;
    public static boolean isTopic = false;

    @Override
    public void onCreate() {
        super.onCreate();
        List<String> ary = Arrays.asList("cargoAgency", "receive", "securityCheck", "collection", "charge");
        for (int i = 0; i < UserInfoSingle.getInstance().getRoleRS().size(); i++) {
            if (UserInfoSingle.getInstance().getRoleRS() != null && UserInfoSingle.getInstance().getRoleRS().size() > 0) {
                if (ary.contains(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                    taskAssignType = 1;
                } else if ("delivery_in".equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                    taskAssignType = 3;
                } else
                    taskAssignType = 2;
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
                case "preplaner":  //预配-组板
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
            }
        }
        saveGpsInfoPresenter = new SaveGpsInfoPresenter(this);
        //GPS 数据提交线程
        Thread threadGps = new Thread(() -> {
            while (true) {
                try {
                    sendGps();
                    Thread.sleep(30000);

                } catch (Exception e) {
                    Log.e("GPS while (true)", e.getMessage());
                }
            }
        });
        threadGps.start();
    }

    private void sendGps() {
        Log.e("GPS位置：", GPSUtils.getInstance().getCurrentLocation().getLatitude() + "");
        if (GPSUtils.getInstance().getCurrentLocation().getLatitude() <= 0.0) {
            Log.e("GPS位置：", "位置为0 不提交");
        } else {
            gpsInfoEntity = new GpsInfoEntity();
            gpsInfoEntity.setLatitude(String.valueOf(GPSUtils.getInstance().getCurrentLocation().getLatitude()));
            gpsInfoEntity.setLongitude(String.valueOf(GPSUtils.getInstance().getCurrentLocation().getLongitude()));
            if (UserInfoSingle.getInstance() != null)
                gpsInfoEntity.setUserId(UserInfoSingle.getInstance().getUserId());
            else
                gpsInfoEntity.setUserId("admin");
            gpsInfoEntity.setTerminalId(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
            saveGpsInfoPresenter.saveGpsInfo(gpsInfoEntity);
        }
    }

    public static void startService(Activity activtity) {
        actionStart(activtity);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static void actionStart(Context ctx) {
        Intent i = new Intent(ctx, WebSocketService.class);
        ctx.startService(i);
    }

    @Override
    public void saveGpsInfoResult(String result) {
        Log.e("GPS上传：", result);
    }

    @Override
    public void toastView(String error) {
        Log.e("GPS上传：", error);
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

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

    //停止连接
    public static void stopServer(Context context) {
        if (mStompClient != null) {
            mStompClient.disconnect();
            mStompClient = null;
        }
        Intent startSrv = new Intent(context, WebSocketService.class);
        context.stopService(startSrv);
    }
}
