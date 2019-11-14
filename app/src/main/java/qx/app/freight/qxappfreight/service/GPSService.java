package qx.app.freight.qxappfreight.service;

import android.Manifest;
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
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import qx.app.freight.qxappfreight.bean.PositionBean;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.utils.loactionUtils.BSLoactionUtil;

/**
 * TODO: GPS 获取地址
 */
public class GPSService extends Service {
    private static final String TAG = "GPSService";
    private LocationManager locationManager = null;
    private String provider;
    private int time = 1000;
    private boolean isGetGPS;
    private BSLoactionUtil mBSLoactionUtil;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startLocation();
            if (isGetGPS) {
                mHandler.postDelayed(mRunnable, time);
            }

        }
    };

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.obtainMessage(0).sendToTarget();
        }
    };

    /**
     * TODO: 保存上传位置
     */
    private void saveLocation(Location l) {
        // 数据
        PositionBean bean = new PositionBean();
        if (l == null) {
            bean.setAltitude(0.0);//海拔
            bean.setLongitude(0.0);//经度
            bean.setLatitude(0.0);// 维度
            bean.setTime(-1);
        } else {
            bean.setAltitude(l.getAltitude());//海拔
            bean.setLongitude(l.getLongitude());//经度
            bean.setLatitude(l.getLatitude());// 维度
            bean.setTime(l.getTime());
        }
        // 事件上报 使用最新的位置
        Tools.saveGPSPosition(bean);
    }

    public static void gpsStart(Activity act) {
        if (!isGpsEnabled((LocationManager) act
                .getSystemService(Context.LOCATION_SERVICE))) {
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
     *
     * @param ctx
     */
    private static void actionStart(Context ctx) {
        Intent i = new Intent(ctx, GPSService.class);
        ctx.startService(i);
    }

    /**
     * 停止一个服务
     *
     * @param ctx
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
        if (isOpenGPS || isOpenNetwork) {
            return true;
        }
        return false;
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
        mBSLoactionUtil = BSLoactionUtil.newInstance(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = getProvider();
        startLocation();
        Log.d(TAG, "--定位服务启动--");
        //        isGetGPS = true;
        //        mHandler.postDelayed(mRunnable, time);// 启动线程控制扫描
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
        locationManager.requestLocationUpdates(provider, time, 1,
                locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isGetGPS = false;
        mHandler.removeCallbacks(mRunnable);
        mBSLoactionUtil.destroyBS();
        if (locationManager != null && locationListener != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);
            locationListener = null;
            locationManager = null;
        }
    }

    public class GPSBinder extends Binder {
        GPSService getService() {
            return GPSService.this;
        }
    }

}
