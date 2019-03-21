package qx.app.freight.qxappfreight.utils;

import android.content.Context;
import android.util.Log;

import com.beidouapp.imlibapi.IMLIBContext;
import com.beidouapp.imlibapi.IMLIBInitializer;
import com.beidouapp.imlibapi.imlibinterface.ImLibGroupChatStartCallback;
import com.beidouapp.imlibapi.imlibinterface.ImLibLoginResultCallback;
import com.beidouapp.imlibapi.imlibinterface.ImLibLogoutResultCallback;
import com.beidouapp.imlibapi.imlibinterface.ImLibPersonalChatStartCallback;
import com.beidouapp.imlibapi.imlibinterface.ImLibSpecialListStartCallback;

import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.constant.HttpConstant;


/**
 * 即时聊天工具类
 * Created by mm on 2016/9/30.
 */
public class IMUtils {


    /**
     * 跳转到与某人聊天的界面
     *
     * @param context
     * @param friendImlibUid 好友userid
     * @param friendName     好友名字
     */
    public static void chatToFriend(final Context context, String friendImlibUid, String friendName) {
        NotifiationUtil.cancelNotification(context, Constants.NOTIFY_ID_CHAT);
        IMLIBContext.getInstance().startPersonalChat(context, friendImlibUid, friendName, new ImLibPersonalChatStartCallback() {
            @Override
            public void onResult(boolean result, String err) {
                if (result == false) {
                    //错误提示
                    ToastUtil.showToast(context, err);
                }
            }
        });
    }

    /**
     * 跳转到群组/保障组页面
     *
     * @param context
     * @param groupImlibUid 群id
     */
    public static void chatToGroup(final Context context, String groupImlibUid) {
        if (StringUtil.isEmpty(groupImlibUid)) {
            ToastUtil.showToast(context, "未获取到保障组相关信息");
            return;
        }
        NotifiationUtil.cancelNotification(context, Constants.NOTIFY_ID_CHAT);
        IMLIBContext.getInstance().startProtectGroupChat(context, groupImlibUid, new ImLibGroupChatStartCallback() {
            @Override
            public void onResult(boolean result, String err) {
                if (result == false) {
                    //错误提示
                    ToastUtil.showToast(context, err);
                }
            }
        });
    }

    /**
     * 跳转特情列表界面
     *
     * @param context
     */
    public static void openImLibSpecialList(final Context context) {
        IMLIBContext.getInstance().startImLibSpecialList(context, new ImLibSpecialListStartCallback() {
            @Override
            public void onResult(boolean b, String s) {
                if (!b)
                    ToastUtil.showToast(context, "页面打开失败,原因：" + s);
            }
        });
    }

    /**
     * 登出im
     */
    public static void imLoginout() {
        try {
            IMLIBContext.getInstance().imlibLogout(new ImLibLogoutResultCallback() {
                @Override
                public void onResponse(boolean b, String s) {
                    if (!b)
                       Log.d("tag","签退失败_im:" + s);
                    else
                        IMLIBContext.getInstance().destroy();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("tag","签退失败_im:" + e.toString());
        }
    }

    /**
     * 登录im服务器
     *
     * @param userAuthKey
     * @param token
     */
    public static void imLibLogin(String userAuthKey, String realName, String token) {
        try {
            IMLIBContext.getInstance().imLibLogin(userAuthKey, realName, token, new ImLibLoginResultCallback() {
                @Override
                public void onResponse(boolean result, String error) {
                    if (result == true) {
                        Log.d("tag","登录成功_im");
                    } else {
                        Log.d("tag","登录失败_im, error : " + error);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("tag",e.getMessage());
        }
    }

    /**
     * 初始化服务器
     */
    public static void initIM() {
        String host = AppIPConfig.getIMIP();
        IMLIBInitializer.initialize(MyApplication.getContext());//IM初始化
        if (HttpConstant.IM_WEB_HOST.equals(host)) {//如果是测试环境 地址不同
            IMLIBContext.getInstance().setServerInfo(host, 8080, host,
                    HttpConstant.IM_ILINK_PORT, HttpConstant.IM_APPKEY, HttpConstant.IM_SECUREKEY, host, HttpConstant.IM_FILE_PORT);
            Log.d("tag","登录初始化im：服务器" + host + "-端口号：8080");
        } else {
            IMLIBContext.getInstance().setServerInfo(host, HttpConstant.IM_WEB_PORT, host,
                    HttpConstant.IM_ILINK_PORT, HttpConstant.IM_APPKEY, HttpConstant.IM_SECUREKEY, host, HttpConstant.IM_FILE_PORT);
            Log.d("tag","登录初始化im_服务器：" + host + "-端口号：" + HttpConstant.IM_WEB_PORT);
        }
    }
}
