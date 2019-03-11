package qx.app.freight.qxappfreight.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * TODO : xxx
 * Created by pr
 */
public class WebSocketManager {

    private WebSocketConnection mWebSocketConnection;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private WebSocketOptions mWebSocketOptions;
    public static List<String> list = new ArrayList<String>();
    private String url = "ws://173.100.1.75:9008/taskAssignCenter?userId=ud8eecd98a3ea4e7aaa2f24ab2808680e";
    private long nowdate;
    private String TAG = "websocket";
    private Context mContext;

    public WebSocketManager(Context context) {
        this.mContext = context;
        mWebSocketConnection = new WebSocketConnection();
        mTimer = new Timer();
        mWebSocketOptions = new WebSocketOptions();
        setwebsocketoptions();
        //心跳包，不停地发送消息给服务器
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mWebSocketConnection.sendTextMessage("");
                Log.e("websocket", "连接中。。。。。");
            }
        };
    }


    //调整链接是否超时的时间限制
    public void setwebsocketoptions() {
        mWebSocketOptions.setSocketConnectTimeout(30000);
        mWebSocketOptions.setSocketReceiveTimeout(10000);
    }

    //链接服务器端的代码
    public void connect() {
        try {
            mWebSocketConnection.connect(url, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    Log.e(TAG, "Status: Connected to " + url);
                    sendHB();
                }

                @Override
                public void onTextMessage(String payload) {
                    if (payload.equals("用户刷新-已收到")) {
                        nowdate = System.currentTimeMillis();
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.e(TAG, "Connection lost." + reason);
                }
            }, mWebSocketOptions);

        } catch (WebSocketException e) {
            Log.e(TAG, "Connection lost." + e.toString());
        }
    }

    //开启心跳包，每一秒发送一次消息，如果返回lost再重连
    public void sendHB() {
        mTimer.schedule(mTimerTask, 1000, 1000);
        //每次发送心跳包，服务器接收到响应就会返回一个值，如果查过5s还没有收到返回值，
        // 那么就判定是断网。
        if ((System.currentTimeMillis() - nowdate) > 5000 && nowdate != 0) {
            mWebSocketConnection.disconnect();
            mTimer.cancel();
//            connect();
            Log.e(TAG, "" + System.currentTimeMillis() + "nowdate:" + nowdate + mWebSocketConnection.isConnected());
            return;
        }
    }

    //发送信息
    public void sendMessage(String data) {
        if (!"".equals(data))
            mWebSocketConnection.sendTextMessage(data);
        else
            ToastUtil.showToast(mContext, "不能为空", Toast.LENGTH_SHORT);
    }

}