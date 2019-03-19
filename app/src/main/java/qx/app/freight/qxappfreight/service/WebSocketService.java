package qx.app.freight.qxappfreight.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketBean;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class WebSocketService extends Service {
    public Context mContext;
    private StompClient mStompClient;
    private Timer mTimer;
    private long nowdate;
    public static final String TAG = "websocket";
    private CompositeDisposable compositeDisposable;
    //张硕地址
//    private String uri = "ws://192.168.1.129:8080/taskAssignCenter?userId=uefaa7789c18845c2921b717a41d2da3a";
    //小猪地址
    private String uri = "ws://192.168.0.171:7004/socketServer?userId=pengrui&type=1&role=admin";
    private Gson mGson = new Gson();


    @Override
    public void onCreate() {
        super.onCreate();
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, uri);
        //请求头
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(TAG, "guest"));
        headers.add(new StompHeader(TAG, "guest"));
        //超时连接
        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        resetSubscriptions();
        //创建连接
        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.e(TAG, "webSocket 打开");
                            break;
                        case ERROR:
                            Log.e(TAG, "websocket 出错", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.e(TAG, "websocket 关闭");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.e(TAG, "Stomp failed server heartbeat");
                            break;
                    }
                });
        compositeDisposable.add(dispLifecycle);

        //订阅
        //小猪地址  user/123/MT/message
        //张硕地址  /taskTodoUser/uefaa7789c18845c2921b717a41d2da3a/taskTodo/taskTodoList
        Disposable dispTopic = mStompClient.topic("/user/pengrui/MT/message")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "订阅成功 " + topicMessage.getPayload());
                    if (null != topicMessage.getPayload()) {
                        //将json字符串转为bean对象
                        WebSocketBean mWebSocketBean = mGson.fromJson(topicMessage.getPayload(), WebSocketBean.class);
                        mWebSocketBean.getContent();
                        Log.d(TAG, "订阅返回信息 " + mWebSocketBean.toString());
                    }

                }, throwable -> {
                    Log.e(TAG, "订阅失败", throwable);
                });

        compositeDisposable.add(dispTopic);
        mStompClient.connect(headers);
    }

    public static void startService(Activity activtity) {
        actionStart(activtity);
    }


    //创建长连接，服务器端没有心跳机制的情况下，启动timer来检查长连接是否断开，如果断开就执行重连
    private void createStompClient() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
//                sendMassage("");
                Log.e("websocket 心跳包", "发送中");
            }
        }, 1000, 1000);
        //每次发送心跳包，服务器接收到响应就会返回一个值，如果查过5s还没有收到返回值，
        // 那么就判定是断网
        if ((System.currentTimeMillis() - nowdate) > 5000 && nowdate != 0) {
            mStompClient.disconnect();
            mTimer.cancel();
            onCreate();
            Log.e("websocket", "" + System.currentTimeMillis() + "nowdate:" + nowdate);
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static void actionStart(Context ctx) {
        Intent i = new Intent(ctx, WebSocketService.class);
        ctx.startService(i);
    }

    public static void actionStop(Context ctx) {
        Intent i = new Intent(ctx, WebSocketService.class);
        ctx.stopService(i);
    }

    public class WebSocketBinder extends Binder {
        WebSocketService getService() {
            return WebSocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new WebSocketBinder();
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    //停止连接
    public void stop() {
        mStompClient.disconnect();
    }
}
