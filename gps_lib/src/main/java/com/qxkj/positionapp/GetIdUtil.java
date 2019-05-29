package com.qxkj.positionapp;

import android.Manifest;
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

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GetIdUtil {


    //单例
    private static GetIdUtil getIdUtil = new GetIdUtil();

    private GetIdUtil() {

    }

    /**
     * 单例
     *
     * @return
     */
    public static GetIdUtil getSingleInstance() {
        return getIdUtil;
    }


    private CellInfo getCellInfo(Context mContext) {
        CellInfo result = new CellInfo();
        TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = manager.getNetworkOperator();
        int mcc = Integer.parseInt(operator.substring(0, 3));
        int mnc = Integer.parseInt(operator.substring(3));
        result.setMcc(mcc);
        result.setMnc(mnc);
        int type = manager.getNetworkType();
        //需要判断网络类型，因为获取数据的方法不一样
        if (type == TelephonyManager.NETWORK_TYPE_CDMA        // 电信cdma网
                || type == TelephonyManager.NETWORK_TYPE_1xRTT
                || type == TelephonyManager.NETWORK_TYPE_EVDO_0
                || type == TelephonyManager.NETWORK_TYPE_EVDO_A
                || type == TelephonyManager.NETWORK_TYPE_EVDO_B) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "权限错误", Toast.LENGTH_SHORT).show();
            }
            CdmaCellLocation cdma = (CdmaCellLocation) manager.getCellLocation();
            result.setCid(cdma.getBaseStationId());
            result.setLac(cdma.getNetworkId());
        } else if (type == TelephonyManager.NETWORK_TYPE_GPRS         // 移动和联通GSM网
                || type == TelephonyManager.NETWORK_TYPE_EDGE
                || type == TelephonyManager.NETWORK_TYPE_HSDPA
                || type == TelephonyManager.NETWORK_TYPE_UMTS
                || type == TelephonyManager.NETWORK_TYPE_LTE) {
            GsmCellLocation location = (GsmCellLocation) manager.getCellLocation();
            result.setCid(location.getCid());
            result.setLac(location.getLac());
        } else if (type == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
            Toast.makeText(mContext, "电话卡不可用！", Toast.LENGTH_LONG).show();
        }
        return result;
    }

    public void getLocationInfo(Context context) {

        try {
            CellInfo cellInfo = getCellInfo(context);
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
                    Log.e("GPS Service", "SUCCES:获取到位置信息");
                    CellLocationResponse result = response.body();
                    GPSUtils.getInstance().notifyLocationUpdate(new LocationEntity(result.getLon(), result.getLat()));
                    Log.e("    [GPS info]", "经度：" + result.getLat() + ", 维度:" + result.getLon());
                }

                @Override
                public void onFailure(Call<CellLocationResponse> call, Throwable t) {
                    Log.e("GPS Service", "ERROR:" + t.toString());
                }
            });
        } catch (Exception e) {
            Log.e("GPS", "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

}