package qx.app.freight.qxappfreight.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.CompositeDisposable;
import ua.naiksoftware.stomp.StompClient;

/**
 * websocket 工具类
 */
public class WebSocketUtils {
    public static final String TAG = "websocket";
    /**
     * 消失回执
     * @param my
     * @param compositeDisposable
     * @param content
     */
    public static void pushReceipt(StompClient my,CompositeDisposable compositeDisposable,String content){
        if (!StringUtil.isEmpty(content)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgId", content);
            compositeDisposable.add(my.send("/app/msgAck", jsonObject.toJSONString()).subscribe(() -> Log.d(TAG, "websocket 消息回执"), throwable -> Log.e(TAG, "websocket 消息回执失败")));
        }

    }

    /**
     * 开启定时器 发送心跳
     */
    public static void sendHeartBeat(Timer mTimer,TimerTask mTimerTask) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("json", "123");
//        mTimerTask = new TimerTask() {
//            public void run() {
//                compositeDisposable.add(my.send("/app/heartbeat", jsonObject.toJSONString()).subscribe(() -> Log.d(TAG, "websocket 消息发送成功"), throwable -> Log.e(TAG, "websocket 消息发送失败")));
//                Log.e("websocket", "发送消息" + jsonObject.toJSONString());
//            }
//        };
        mTimer.schedule(mTimerTask, 20000, 30000);
    }

    public static TimerTask newTimerTaskHeart(StompClient my,CompositeDisposable compositeDisposable ){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("json", "123");
        TimerTask mTimerTask = new TimerTask() {
            public void run() {
                compositeDisposable.add(my.send("/app/heartbeat", jsonObject.toJSONString()).subscribe(() -> Log.d(TAG, "websocket 消息发送成功"), throwable -> Log.e(TAG, "websocket 消息发送失败")));
                Log.e("websocket", "发送消息" + jsonObject.toJSONString());
            }
        };
        return mTimerTask;
    }

    /**
     * 关闭定时器
     * @param mTimer
     */
    public static void stopTimer(Timer mTimer,TimerTask mTimerTask){
        if (mTimerTask !=null){
            mTimerTask.cancel();
        }
        if (mTimer != null){
            mTimer.purge();
            mTimer.cancel();
        }


    }



}
