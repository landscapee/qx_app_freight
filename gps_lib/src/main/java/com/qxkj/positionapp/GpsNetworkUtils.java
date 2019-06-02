package com.qxkj.positionapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.qxkj.positionapp.http.CellInfo;
import com.qxkj.positionapp.http.CellLocationResponse;
import com.qxkj.positionapp.http.HttpService;

import java.io.IOException;
import java.util.List;

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
    public void getLocationBySIM(Context context) {
        try {
            //组装参数
            CellInfo cellInfo = new CellInfo();
            if (telephonyManager == null) {
                telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            }
            String operator = telephonyManager.getNetworkOperator();
            cellInfo.setMcc(Integer.parseInt(operator.substring(0, 3)));
            cellInfo.setMnc(Integer.parseInt(operator.substring(3)));
            List<android.telephony.CellInfo> cellList = telephonyManager.getAllCellInfo();
            for (android.telephony.CellInfo info : cellList) {
                if (info instanceof CellInfoCdma) {
                    CellInfoCdma cellInfoCdma = (CellInfoCdma) info;
                    CellIdentityCdma cellIdentityCdma = cellInfoCdma.getCellIdentity();
                    cellInfo.setLac(cellIdentityCdma.getNetworkId());
                    cellInfo.setCid(cellIdentityCdma.getBasestationId());
                } else if (info instanceof CellInfoGsm) {
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) info;
                    CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
                    cellInfo.setLac(cellIdentityGsm.getLac());
                    cellInfo.setCid(cellIdentityGsm.getCid());
                } else if (info instanceof CellInfoLte) {
                    CellInfoLte cellInfoLte = (CellInfoLte) info;
                    CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
                    cellInfo.setLac(cellIdentityLte.getTac());
                    cellInfo.setCid(cellIdentityLte.getCi());
                } else if (info instanceof CellInfoWcdma) {
                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) info;
                    CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                    cellInfo.setLac(cellIdentityWcdma.getLac());
                    cellInfo.setCid(cellIdentityWcdma.getCid());
                }
                Log.e("tagGps","cell=======lac:"+cellInfo.getLac()+";cid:"+cellInfo.getCid());
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

            call.enqueue(new Callback<CellLocationResponse>() {
                @Override
                public void onResponse(Call<CellLocationResponse> call, Response<CellLocationResponse> response) {
                    if (response.body().getErrcode() == 0) {
                        Log.e("GPS", "Net Success");
                        GPSUtils.getInstance().updateLocationEntity(response.body().getLon(), response.body().getLat());
                    } else if (response.body().getErrcode() == 10000) {
                        Log.e("GPS", "Net ERROR: 参数错误");
                    } else if (response.body().getErrcode() == 10001) {
                        Log.e("GPS", "Net ERROR: 无查询结果");
                    } else {
                        Log.e("GPS", "Net ERROR: 未知错误");
                    }
                }

                @Override
                public void onFailure(Call<CellLocationResponse> call, Throwable t) {
                    Log.e("GPS", "Net Fail:" + t.getMessage());
                }
            });
        }catch (Exception e){
            Log.e("tagGps","未插入SIM卡");
        }
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
        int type = telephonyManager.getPhoneType();
        //判断权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("GPS", "Permission Denied: ACCESS_FINE_LOCATION");
            return false;
        }
        //需要判断网络类型，因为获取数据的方法不一样
        //电信cdma网 || 移动和联通GSM网
        if (type == TelephonyManager.PHONE_TYPE_CDMA || type == TelephonyManager.PHONE_TYPE_GSM) {
            return true;
        } else {
            Log.e("GPS", "Network Unable");
            return false;
        }
    }

}
