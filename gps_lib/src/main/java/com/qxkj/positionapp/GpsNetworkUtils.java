package com.qxkj.positionapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import com.qxkj.positionapp.http.CellInfo;
import com.qxkj.positionapp.http.CellLocationResponse;
import com.qxkj.positionapp.http.HttpService;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GpsNetworkUtils {


    //单例
    private static GpsNetworkUtils gpsNetworkUtils = new GpsNetworkUtils();

    private GpsNetworkUtils() {

    }

    TelephonyManager telephonyManager;

    /**
     * 单例
     *
     * @return
     */
    public static GpsNetworkUtils getSingleInstance() {
        return gpsNetworkUtils;
    }


    /**
     * 通过网络请求获取gps
     * 请求文档地址：http://www.cellocation.com/interfac/
     *
     * @param context
     */
    @SuppressLint("MissingPermission")
    public LocationEntity getLocationBySIM(Context context) {
        //组装参数
        CellInfo cellInfo = new CellInfo();
        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        String operator = telephonyManager.getNetworkOperator();
        cellInfo.setMcc(Integer.parseInt(operator.substring(0, 3)));
        cellInfo.setMnc(Integer.parseInt(operator.substring(3)));
        int type = telephonyManager.getNetworkType();

        //需要判断网络类型，因为获取数据的方法不一样
        if (type == TelephonyManager.NETWORK_TYPE_CDMA
                || type == TelephonyManager.NETWORK_TYPE_1xRTT
                || type == TelephonyManager.NETWORK_TYPE_EVDO_0
                || type == TelephonyManager.NETWORK_TYPE_EVDO_A
                || type == TelephonyManager.NETWORK_TYPE_EVDO_B) {
            // 电信cdma网
            CdmaCellLocation cdma = (CdmaCellLocation) telephonyManager.getCellLocation();
            cellInfo.setCid(cdma.getBaseStationId());
            cellInfo.setLac(cdma.getNetworkId());
        } else if (type == TelephonyManager.NETWORK_TYPE_GPRS
                || type == TelephonyManager.NETWORK_TYPE_EDGE
                || type == TelephonyManager.NETWORK_TYPE_HSDPA
                || type == TelephonyManager.NETWORK_TYPE_UMTS
                || type == TelephonyManager.NETWORK_TYPE_LTE) {
            // 移动和联通GSM网
            GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
            cellInfo.setCid(location.getCid());
            cellInfo.setLac(location.getLac());
        }

        //网络请求
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            //打印retrofit日志
            Log.e("tagRetrofit", "msg = " + message);
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //okhttp设置部分，此处还可再设置网络参数
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.cellocation.com:81/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpService httpService = retrofit.create(HttpService.class);

        Call<CellLocationResponse> call = httpService.getLocationInfo(cellInfo.getMcc() + "", cellInfo.getMnc() + "", cellInfo.getLac() + "", cellInfo.getCid() + "");

        try {
            Response<CellLocationResponse> response = call.execute();
            if (response.body().getErrcode() == 0) {
                Log.e("GPS", "Net Success");
                return new LocationEntity(response.body().getLon(), response.body().getLat());
            } else if (response.body().getErrcode() == 10000) {
                Log.e("GPS", "Net ERROR: 参数错误");
                return null;
            } else if (response.body().getErrcode() == 10001) {
                Log.e("GPS", "Net ERROR: 无查询结果");
                return null;
            } else {
                Log.e("GPS", "Net ERROR: 未知错误");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("GPS", "Net Fail:" + e.toString());
        }
        return null;
    }


    /**
     * 网络请求获取pgs可用判断
     *
     * @param context
     * @return
     */
    public boolean isNetworkGpsEnable(Context context) {
        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        int type = telephonyManager.getNetworkType();
        //判断权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("GPS", "Permission Denied: ACCESS_FINE_LOCATION");
            return false;
        }
        //需要判断网络类型，因为获取数据的方法不一样
        if (
            //电信cdma网
                type == TelephonyManager.NETWORK_TYPE_CDMA
                        || type == TelephonyManager.NETWORK_TYPE_1xRTT
                        || type == TelephonyManager.NETWORK_TYPE_EVDO_0
                        || type == TelephonyManager.NETWORK_TYPE_EVDO_A
                        || type == TelephonyManager.NETWORK_TYPE_EVDO_B
                        // 移动和联通GSM网
                        || type == TelephonyManager.NETWORK_TYPE_GPRS
                        || type == TelephonyManager.NETWORK_TYPE_EDGE
                        || type == TelephonyManager.NETWORK_TYPE_HSDPA
                        || type == TelephonyManager.NETWORK_TYPE_UMTS
                        || type == TelephonyManager.NETWORK_TYPE_LTE) {
            return true;
        } else if (type == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
            Log.e("GPS", "电话卡不可用");
            return false;
        } else {
            Log.e("GPS", "Network Unable");
            return false;
        }
    }

}
