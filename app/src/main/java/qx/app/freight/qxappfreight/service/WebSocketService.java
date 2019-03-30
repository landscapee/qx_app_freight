package qx.app.freight.qxappfreight.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.LoadUnloadGroupBoardBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketMessageBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class WebSocketService extends Service {
    private static String uri;
    public Context mContext;
    private static StompClient mStompClient;
    private Timer mTimer;
    private long nowdate;
    public static final String TAG = "websocket";
    private CompositeDisposable compositeDisposable;
    private Gson mGson = new Gson();


    @Override
    public void onCreate() {
        super.onCreate();
//        EventBus.getDefault().isRegistered(this);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, uri);
//        Log.e(TAG, uri);
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

        //订阅  小猪登录地址
        Disposable dispTopic = mStompClient.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/MT/message")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "订阅成功 " + topicMessage.getPayload());
                    if (null != topicMessage.getPayload()) {
                        sendLoginEventBus(topicMessage.getPayload());
                    }
                }, throwable -> {
                    Log.e(TAG, "订阅失败", throwable);
                });

        compositeDisposable.add(dispTopic);

        //订阅   张硕地址待办
        Disposable dispTopic1 = mStompClient.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/taskTodo/taskTodoList")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "张硕订阅成功 " + topicMessage.getPayload());
                    WebSocketResultBean mWebSocketBean = mGson.fromJson(topicMessage.getPayload(), WebSocketResultBean.class);
                    sendReshEventBus(mWebSocketBean);
                }, throwable -> Log.e(TAG, "张硕订阅失败", throwable));

        compositeDisposable.add(dispTopic1);

        //周弦  消息中心地址
        Disposable dispTopic2 = mStompClient.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/MT/msMsg")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "周弦订阅成功 " + topicMessage.getPayload());
                    WebSocketMessageBean mWebSocketMessBean = mGson.fromJson(topicMessage.getPayload(), WebSocketMessageBean.class);
                    sendMessageEventBus(mWebSocketMessBean);
                }, throwable -> Log.e(TAG, "周弦订阅失败", throwable));

        compositeDisposable.add(dispTopic2);

        //订阅   运输 装卸机
        Disposable dispTopic3 = mStompClient.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/aiSchTask/outFileTask")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "运输装卸机 订阅 " + topicMessage.getPayload());
                    LoadUnloadGroupBoardBean mWebSocketBean = mGson.fromJson(topicMessage.getPayload(), LoadUnloadGroupBoardBean.class);
                    sendLoadUnLoadGroupBoard(mWebSocketBean);
                }, throwable -> Log.e(TAG, "运输装卸机 订阅", throwable));

        compositeDisposable.add(dispTopic3);

        mStompClient.connect(headers);
    }

    public static void startService(Activity activtity, String url) {
        uri = url;
        actionStart(activtity);
    }

    //用于登录
    public static void sendLoginEventBus(String str) {
        EventBus.getDefault().post(str);
    }

    //用于代办刷新
    public static void sendReshEventBus(WebSocketResultBean bean) {
        EventBus.getDefault().post(bean);
    }
    //用于装卸机组板推送刷新弹窗
    public static void sendLoadUnLoadGroupBoard(LoadUnloadGroupBoardBean bean) {
        EventBus.getDefault().post(bean);
    }

    //消息推送
    public static void sendMessageEventBus(WebSocketMessageBean bean) {
        EventBus.getDefault().post(bean);
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
    public static void stopServer(Context context) {
        if (mStompClient != null){
            mStompClient.disconnect();
            mStompClient = null;
        }
        Intent startSrv = new Intent(context, WebSocketService.class);
        context.stopService(startSrv);
    }
}
