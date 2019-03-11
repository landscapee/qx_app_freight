package qx.app.freight.qxappfreight.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

/**
 * TODO : 获取基站定位信息
 * Created by owen
 * on 2017-01-05.
 */
public class BSLoactionUtil {
    @SuppressLint("StaticFieldLeak")
    private static BSLoactionUtil mLoactionUtil;
    private BSLocationBean mBSLocationBean;
    private Context mContext;
    private TelephonyManager telephonyManager;

    public static BSLoactionUtil newInstance(Context context) {
        if (mLoactionUtil == null)
            mLoactionUtil = new BSLoactionUtil(context);
        return mLoactionUtil;
    }

    private BSLoactionUtil(Context context) {
        mContext = context.getApplicationContext();
        mBSLocationBean = new BSLocationBean();
        reGetBS();
    }

    public void reGetBS() {
        telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        //监听基站信息变化
        try {
            telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CELL_LOCATION);
            telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        } catch (SecurityException e) {
            Log.d("tagBSLocation", "reGetBS: " + e.getMessage());
        }
    }

    public void destroyBS() {
        mPhoneStateListener = null;
        telephonyManager = null;
        mLoactionUtil = null;
    }

    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            try {
                GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
                if (cellLocation!=null){
                    mBSLocationBean.setLac(cellLocation.getLac() + "");
                    mBSLocationBean.setCid(cellLocation.getCid() + "");
                }
                else
                    Log.d("BSLoactionUtil", "程序异常: 没有插入手机卡");
            } catch (SecurityException e) {
                Log.d("BSLoactionUtil", "程序异常: 手机是电信");
            }
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            String s = signalStrength.toString();
            String[] Strength = s.split(" ");
            //4G 信号强度在 第10位，且小于0；
            if (Strength[9].contains("-")) {
                mBSLocationBean.setRssi(Strength[9]);
            } else if (Strength[13].contains("-")) {// 3G信号强度在第14位且小于0
                mBSLocationBean.setRssi(Strength[13]);
            }
            Log.d("BSLoactionUtil", "基站信息: " + mBSLocationBean.toString());
            Tools.saveBSLocation(mBSLocationBean);
        }
    };

    public static class BSLocationBean {
        private String Lac;
        private String Cid;
        private String Rssi;

        @NonNull
        @Override
        public String toString() {
            return "lac:" + Lac + ", cid:" + Cid + ", rssi:" + Rssi;
        }

        String getCid() {
            return Cid;
        }

        void setCid(String cid) {
            Cid = cid;
        }

        String getLac() {
            return Lac;
        }

        void setLac(String lac) {
            Lac = lac;
        }

        String getRssi() {
            return Rssi;
        }

        void setRssi(String rssi) {
            Rssi = rssi;
        }
    }

}
