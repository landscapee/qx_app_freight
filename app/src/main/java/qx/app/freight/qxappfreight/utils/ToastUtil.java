package qx.app.freight.qxappfreight.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import qx.app.freight.qxappfreight.app.MyApplication;

/**
 * toast 统一管理类
 *
 * @author ouyangbin
 */
public class ToastUtil {
    private static String oldMsg;
    protected static Toast toast   = null;
    private static long oneTime=0;
    private static long twoTime=0;

    private ToastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 显示toast
     */
    public static void showToast(Context c, String s) {

        if ("318".equals(s)){
            return;
        }
        oldMsg = s;
        if (toast == null) {
            int time = Toast.LENGTH_SHORT;
            toast = Toast.makeText(c, s, time);
            Log.e("toast==null","2个==========="+s);
        } else {
            toast.setText(s);
            Log.e("toast！=null","2个==========="+s);
        }
        toast.show();

    }

    public static void showToast( String s){
        Context context= MyApplication.getContext();
        if ("318".equals(s)){
           return;
        }
        if(toast==null){
            toast =Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime=System.currentTimeMillis();
//            Log.e("toast==null",s);
        }else{
            twoTime=System.currentTimeMillis();
            if(s.equals(oldMsg)){
                if(twoTime-oneTime>Toast.LENGTH_SHORT){
                    toast.show();
                    Log.e("toast!=null&&","twoTime-oneTime>Toast.LENGTH_SHORT===="+s);
                }
            }else{
                Log.e("toast!=null&&","twoTime-oneTime>Toast.LENGTH_SHORT222===="+s);
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime=twoTime;
    }


    /**
     * 自定义时间显示toast
     */
    public static void showToast(Context c, String s, int time) {
        if (toast == null) {
            if (time == 0) {
                toast = Toast.makeText(c.getApplicationContext(), s, Toast.LENGTH_SHORT);
            } else if (time == 1) {
                toast = Toast.makeText(c.getApplicationContext(), s, Toast.LENGTH_LONG);
            }
        } else {
            toast.setText(s);
        }
        if (time == 0) {
            toast.setDuration(Toast.LENGTH_SHORT);
        } else if (time == 1) {
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.show();
    }
}
