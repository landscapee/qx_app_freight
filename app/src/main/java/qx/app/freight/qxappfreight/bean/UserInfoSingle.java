package qx.app.freight.qxappfreight.bean;

import android.util.Log;

import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.utils.Tools;

/**
 * 用户单列
 */
public class UserInfoSingle {

    private static volatile UserInfoSingle singleton;

    private UserInfoSingle() {
    }

    private static LoginResponseBean user = null;

    public static LoginResponseBean getInstance() {

        synchronized (UserInfoSingle.class) {
            if (user == null)
                return new LoginResponseBean();
        }
        return user;
    }

    public static void setUser(LoginResponseBean users) {
            try {
                user = Tools.IOclone(users) ;
            }
            catch (Exception e)
            {
                Log.e("setUser","users");
            }

    }

    public static void setUserNil() {
        user = null;
    }


}
