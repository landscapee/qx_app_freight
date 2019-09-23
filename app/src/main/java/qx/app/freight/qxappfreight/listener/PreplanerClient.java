package qx.app.freight.qxappfreight.listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.activity.LoginActivity;
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.WebSocketMessageBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.ActManager;
import qx.app.freight.qxappfreight.utils.AppUtil;
import qx.app.freight.qxappfreight.utils.NetworkUtils;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.utils.WebSocketUtils;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.provider.ConnectionProvider;

public class PreplanerClient extends StompClient {

    public static final String TAG = "websocket";
    private Gson mGson = new Gson();
    private CompositeDisposable compositeDisposable;
    private Context mContext;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Timer mTimerReConnect;
    private TimerTask mTimerTaskReConnect;
    public PreplanerClient(String uri, Context mContext) {
        super(new CollectionClient.GetConnectionProvider());
        this.mContext = mContext;
        connect(uri);
    }

    @SuppressLint("CheckResult")
    public void connect(String uri) {
        StompClient my = Stomp.over(Stomp.ConnectionProvider.OKHTTP, uri);
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(TAG, "guest"));
        //超时连接
        withClientHeartbeat(1000).withServerHeartbeat(1000);
        resetSubscriptions();
        Disposable dispLifecycle = my.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            WebSocketService.isTopic = true;
                            WebSocketService.mStompClient.add(my);
                            WebSocketUtils.sendHeartBeat(mTimer,mTimerTask);
                            WebSocketUtils.stopTimer(mTimerReConnect,mTimerTaskReConnect);
//                            sendMess(my);
//                            if (mTimerReConnect != null)
//                                mTimerReConnect.cancel();
                            Log.e(TAG, "webSocket  组板 打开");
                            break;
                        case ERROR:
                            Log.e(TAG, "websocket 组板 出错", lifecycleEvent.getException());
                            WebSocketUtils.stopTimer(mTimer,mTimerTask);
//                            if (mTimer != null)
//                                mTimer.cancel();
                            WebSocketService.isTopic = false;
                            connect(uri);
                            break;
                        case CLOSED:
                            Log.e(TAG, "websocket 组板 关闭");
                            WebSocketUtils.stopTimer(mTimer,mTimerTask);
//                            if (mTimer != null)
//                                mTimer.cancel();
                            WebSocketService.isTopic = false;
                            resetSubscriptions();
//                            connect(uri);
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.e(TAG, "Stomp failed server heartbeat");
                            WebSocketUtils.stopTimer(mTimer,mTimerTask);
//                            if (mTimer != null)
//                                mTimer.cancel();
                            WebSocketService.isTopic = false;
                            break;
                    }
                });
        compositeDisposable.add(dispLifecycle);
        if (!WebSocketService.isTopic) {
            //订阅   待办
            Disposable dispTopic1 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/taskTodo/taskTodoList")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.d(TAG, "websocket-->代办 " + topicMessage.getPayload());
                        // 消息回执
                        WebSocketUtils.pushReceipt(my,compositeDisposable,topicMessage.getStompHeaders().get(0).getValue());

                        WebSocketResultBean mWebSocketBean = mGson.fromJson(topicMessage.getPayload(), WebSocketResultBean.class);
                        sendReshEventBus(mWebSocketBean);
                    }, throwable -> Log.e(TAG, "websocket-->代办失败", throwable));

            compositeDisposable.add(dispTopic1);
            //订阅  登录地址
            Disposable dispTopic = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/" + UserInfoSingle.getInstance().getUserToken() + "/MT/message")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.d(TAG, "websocket-->登录 " + topicMessage.getPayload());
                        if (null != topicMessage.getPayload()) {
                            Tools.showDialog(mContext);
                        }
                    }, throwable -> {
                        Log.e(TAG, "websocket-->登录失败", throwable);
                    });
            compositeDisposable.add(dispTopic);
            //订阅   消息中心地址
            Disposable dispTopic2 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/MT/msMsg")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.d(TAG, "websocket-->消息中心 " + topicMessage.getPayload());
                        WebSocketMessageBean mWebSocketMessBean = mGson.fromJson(topicMessage.getPayload(), WebSocketMessageBean.class);
                        sendMessageEventBus(mWebSocketMessBean);
                    }, throwable -> Log.e(TAG, "websocket-->消息中心失败", throwable));

            compositeDisposable.add(dispTopic2);
        }
        my.connect();
    }

    //用于代办刷新
    public static void sendReshEventBus(WebSocketResultBean bean) {
        EventBus.getDefault().post(bean);
    }

    public void sendMess(StompClient my) {
        mTimer = new Timer();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("json", "123");
        mTimerTask = new TimerTask() {
            public void run() {
                compositeDisposable.add(my.send("/app/heartbeat", jsonObject.toJSONString()).subscribe(() -> Log.d(TAG, "websocket 消息发送成功"), throwable -> Log.e(TAG, "websocket 消息发送失败")));
                Log.e("websocket", "发送消息" + jsonObject.toJSONString());
            }
        };
        mTimer.schedule(mTimerTask, 20000, 30000);
    }
    public void reConnect(String uri) {
        WebSocketService.subList.clear();
        if (mTimerReConnect != null)
            mTimerReConnect.cancel();
        mTimerReConnect = new Timer();
        mTimerTaskReConnect = new TimerTask() {
            public void run() {
                if (NetworkUtils.isNetWorkAvailable(mContext))
                    connect(uri);
            }
        };
        mTimerReConnect.schedule(mTimerTaskReConnect, 1000, 1000);
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    public static class GetConnectionProvider implements ConnectionProvider {

        @Override
        public Observable<String> messages() {
            return null;
        }

        @Override
        public Completable send(String stompMessage) {
            Log.d(TAG, "send" + stompMessage);
            return null;
        }

        @Override
        public Observable<LifecycleEvent> lifecycle() {
            return null;
        }

        @Override
        public Completable disconnect() {
            return null;
        }
    }



    //消息推送
    public void sendMessageEventBus(WebSocketMessageBean bean) {
        EventBus.getDefault().post(bean);
    }
}
