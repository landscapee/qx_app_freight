package qx.app.freight.qxappfreight.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.TimeUtils;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import qx.app.freight.qxappfreight.bean.PositionBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.GpsInfoEntity;
import qx.app.freight.qxappfreight.contract.SaveGpsInfoContract;
import qx.app.freight.qxappfreight.presenter.SaveGpsInfoPresenter;
import qx.app.freight.qxappfreight.utils.BSLoactionUtil;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.Tools;

/**
 * TODO: GPS 获取地址
 */
public class GPSService extends Service implements SaveGpsInfoContract.saveGpsInfoView {
    private static final String TAG = "tagGPS";
    private LocationManager locationManager = null;
    private String provider;
    private int time = 1000;
    private boolean isGetGPS;
    private BSLoactionUtil mBSLoactionUtil;

    private GpsInfoEntity gpsInfoEntity;

    private SaveGpsInfoPresenter saveGpsInfoPresenter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startLocation();
            if (isGetGPS) {
                mHandler.postDelayed(mRunnable, time);
            }
        }
    };
    Runnable mRunnable = () -> mHandler.obtainMessage(0).sendToTarget();

    /**
     * TODO: 保存上传位置
     */
    private void saveLocation(Location l) {
        // 数据
        PositionBean bean = new PositionBean();
        if (l == null) {
            bean.setAltitude(String.valueOf(0.0));//海拔
            bean.setLongitude(String.valueOf(0.0));//经度
            bean.setLatitude(String.valueOf(0.0));// 纬度
            bean.setTime(-1);
        } else {
            bean.setAltitude(String.valueOf(l.getAltitude()));//海拔
            bean.setLongitude(String.valueOf(l.getLongitude()));//经度
            bean.setLatitude(String.valueOf(l.getLatitude()));// 纬度
            bean.setTime(l.getTime());
        }
        // 事件上报 使用最新的位置
        Tools.saveGPSPosition(bean);
        EventBus.getDefault().post("经度"+bean.getLongitude()+"纬度:"+bean.getLatitude());
        Log.e("saveGPSPosition====","经度"+bean.getLongitude()+"纬度:"+bean.getLatitude());

        sendGps(bean);

    }

    /**
     * 发送GPS
     * @param bean
     */
    private void sendGps(PositionBean bean){

        //临时使用 没有GPS 传0
        PositionBean bean1 = new PositionBean();
        bean1.setAltitude(String.valueOf(0.0));//海拔
        bean1.setLongitude(String.valueOf(0.0));//经度
        bean1.setLatitude(String.valueOf(0.0));// 纬度
        bean1.setTime(-1);
        // 事件上报 使用最新的位置
        Tools.saveGPSPosition(bean1);

        if(null == bean.getLatitude())
        {
            bean.setLatitude("102551.2");
            bean.setLongitude("225412.3");
        }
        gpsInfoEntity.setLatitude(bean.getLatitude());
        gpsInfoEntity.setLongitude(bean.getLongitude());
        if (UserInfoSingle.getInstance() != null)
            gpsInfoEntity.setUserId(UserInfoSingle.getInstance().getUserId());
        else
            gpsInfoEntity.setUserId("admin");
        gpsInfoEntity.setTerminalId(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
        saveGpsInfoPresenter.saveGpsInfo(gpsInfoEntity);
    }

    public static void gpsStart(Activity act) {
        if (!isGpsEnabled((LocationManager) act.getSystemService(Context.LOCATION_SERVICE))) {
            Toast.makeText(act, "GPS导航未开启，请设置！", Toast.LENGTH_SHORT).show();
            // 返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            act.startActivityForResult(intent, 1);
            return;
        }
        // 启动服务
        actionStart(act);
    }

    /**
     * 启动一个服务执行指定的方法
     */
    private static void actionStart(Context ctx) {
        Intent i = new Intent(ctx, GPSService.class);
        ctx.startService(i);
    }

    /**
     * 停止一个服务
     */
    public static void actionStop(Context ctx) {
        Intent i = new Intent(ctx, GPSService.class);
        ctx.stopService(i);
    }

    public static boolean isGpsEnabled(LocationManager locationManager) {
        boolean isOpenGPS = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isOpenNetwork = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isOpenGPS || isOpenNetwork;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new GPSBinder();
    }

    // 获取Location Provider
    private String getProvider() {
        // 构建位置查询条件
        Criteria criteria = new Criteria();
        // 查询精度：高
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 是否查询海拨：是
        criteria.setAltitudeRequired(true);
        // 是否查询方位角 : 否
        criteria.setBearingRequired(false);
        // 是否允许付费：是
        criteria.setCostAllowed(false);
        // 电量要求：低
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // 返回最合适的符合条件的provider，第2个参数为true说明 , 如果只有一个provider是有效的,则返回当前provider
        return locationManager.getBestProvider(criteria, true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSend();
        mBSLoactionUtil = BSLoactionUtil.newInstance(this);

        saveGpsInfoPresenter = new SaveGpsInfoPresenter(this);
        gpsInfoEntity = new GpsInfoEntity();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        在室内时 GPS 无法使用
//        provider = LocationManager.NETWORK_PROVIDER;
        provider = getProvider();
        startLocation();
        Log.d(TAG, "--定位服务启动--");

        //测试
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    sendGps(new PositionBean());
//                    TimeUnit.SECONDS.sleep(30000);
//
//                }catch (Exception e){
//
//                }
//
//            }
//        });
//        thread.start();

    }

    private void initSend() {


    }

    /**
     * TODO: 位置改变监听
     */
    private LocationListener locationListener = new LocationListener() {
        // 位置发生改变后调用
        public void onLocationChanged(Location l) {
            Log.d(TAG, "onLocationChanged: ");
            if (l != null) {
                // 数据
                saveLocation(l);
            }
        }

        // provider 被用户关闭后调用
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: 用户关闭");
//            EventBus.getDefault().post(new EventGPSBean("open_gps", "打开GPS"));
        }

        // provider 被用户开启后调用
        public void onProviderEnabled(String provider) {
            startLocation();
            Log.d(TAG, "onProviderEnabled: 用户开启");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: 状态变化");
        }
    };

    private void startLocation() {
        // 启动基站定位
        mBSLoactionUtil.reGetBS();
        // 启动GPS定位
        if (ActivityCompat.checkSelfPermission(GPSService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //TODO:  一秒 一米
        locationManager.requestLocationUpdates(provider, time, 1, locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isGetGPS = false;
        mHandler.removeCallbacks(mRunnable);
//        mBSLoactionUtil.destroyBS();
        if (locationManager != null && locationListener != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);
            locationListener = null;
            locationManager = null;
        }
    }

    @Override
    public void saveGpsInfoResult(String result) {
        Log.e("saveGpsInfoResult===",result);
    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }

    public class GPSBinder extends Binder {
        GPSService getService() {
            return GPSService.this;
        }
    }
}
