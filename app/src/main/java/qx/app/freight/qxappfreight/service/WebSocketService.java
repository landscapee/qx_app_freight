package qx.app.freight.qxappfreight.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.activity.LoginActivity;
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketMessageBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.utils.ActManager;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class WebSocketService extends Service {
    private static String uri;
    public Context mContext;
    private static StompClient mStompClient;
    private Timer mTimer;
    public static final String TAG = "websocket";
    private CompositeDisposable compositeDisposable;
    private Gson mGson = new Gson();
    private int flag = 0;


    @Override
    public void onCreate() {
        super.onCreate();
//        EventBus.getDefault().isRegistered(this);
        mTimer = new Timer();
        if (uri == null) {
            Log.e(TAG, "推送服务url为null");
            return;
        }

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
                            flag = 0;
                            createStompClient(flag);
                            break;
                        case ERROR:
                            Log.e(TAG, "websocket 出错", lifecycleEvent.getException());
                            flag = 1;
                            createStompClient(flag);
                            break;
                        case CLOSED:
                            Log.e(TAG, "websocket 关闭");
                            flag = 1;
                            createStompClient(flag);
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.e(TAG, "Stomp failed server heartbeat");
                            break;
                    }
                });
        compositeDisposable.add(dispLifecycle);

        //订阅  登录地址
        Disposable dispTopic = mStompClient.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/" + UserInfoSingle.getInstance().getUserToken() + "/MT/message")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "订阅成功 " + topicMessage.getPayload());
                    if (null != topicMessage.getPayload()) {
//                        ToastUtil.showToast("你的账号在其他地方登陆，请重新登陆");
//                        showdialog1();
                        showDialog();
                    }
                }, throwable -> {
                    Log.e(TAG, "订阅失败", throwable);
                });

        compositeDisposable.add(dispTopic);
        //订阅  装机单变更推送
        Log.e("tagPush", "userid=====" + UserInfoSingle.getInstance().getUserId());
        Disposable loadingListPush = mStompClient.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/departure/preloadedCargo")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "订阅成功 " + topicMessage.getPayload());
                    if (null != topicMessage.getPayload()) {
                        sendLoadingListPush(topicMessage.getPayload());
                    }
                }, throwable -> {
                    Log.e(TAG, "订阅失败", throwable);
                });

        compositeDisposable.add(loadingListPush);
        //订阅   待办
        Disposable dispTopic1 = mStompClient.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/taskTodo/taskTodoList")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "张硕订阅成功 " + topicMessage.getPayload());
                    WebSocketResultBean mWebSocketBean = mGson.fromJson(topicMessage.getPayload(), WebSocketResultBean.class);
                    sendReshEventBus(mWebSocketBean);
                }, throwable -> Log.e(TAG, "张硕订阅失败", throwable));

        compositeDisposable.add(dispTopic1);

        //订阅   消息中心地址
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
                    Log.e(TAG, topicMessage.getPayload());
                    if (topicMessage.getPayload().contains("\"cancelFlag\":true")) {//任务取消的推送
                        if (topicMessage.getPayload().contains("\"taskType\":1")) {//装卸机
                            CommonJson4List<LoadAndUnloadTodoBean> gson = new CommonJson4List<>();
                            CommonJson4List<LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                            sendLoadUnLoadGroupBoard(data);
                        } else if (topicMessage.getPayload().contains("\"taskType\":2")) {//运输
                            CommonJson4List<AcceptTerminalTodoBean> gson = new CommonJson4List<>();
                            CommonJson4List<AcceptTerminalTodoBean> data = gson.fromJson(topicMessage.getPayload(), AcceptTerminalTodoBean.class);
                            sendLoadUnLoadGroupBoard(data);
                        }
                    } else {
                        if (topicMessage.getPayload().contains("\"taskType\":1") || topicMessage.getPayload().contains("\"taskType\":2")) {//装卸机
                            CommonJson4List<LoadAndUnloadTodoBean> gson = new CommonJson4List<>();
                            CommonJson4List<LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                            sendLoadUnLoadGroupBoard(data);
                        } else if (topicMessage.getPayload().contains("\"taskType\":0")) {//运输
                            CommonJson4List<AcceptTerminalTodoBean> gson = new CommonJson4List<>();
                            CommonJson4List<AcceptTerminalTodoBean> data = gson.fromJson(topicMessage.getPayload(), AcceptTerminalTodoBean.class);
                            sendLoadUnLoadGroupBoard(data);
                        }
                    }
                }, throwable -> Log.e(TAG, "运输装卸机 订阅", throwable));

        compositeDisposable.add(dispTopic3);

        mStompClient.connect(headers);
    }

    private void sendLoadingListPush(String result) {
        EventBus.getDefault().post(result);
    }

    public static void startService(Activity activtity, String url) {
        uri = url;
        actionStart(activtity);
    }

    //强制登出
    private void loginOut() {
        UserInfoSingle.setUserNil();
        ActManager.getAppManager().finishAllActivity();
        WebSocketService.stopServer(MyApplication.getContext());
        Intent intent = new Intent(MyApplication.getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    //用于登录
    public static void sendLoginEventBus(String str) {
        EventBus.getDefault().post(str);
    }

    //用于代办刷新
    public static void sendReshEventBus(WebSocketResultBean bean) {
        EventBus.getDefault().post(bean);
    }

    //用于装卸机运输推送刷新弹窗
    public static void sendLoadUnLoadGroupBoard(CommonJson4List bean) {
        EventBus.getDefault().post(bean);
    }

    //消息推送
    public static void sendMessageEventBus(WebSocketMessageBean bean) {
        EventBus.getDefault().post(bean);
    }


    //创建长连接，服务器端没有心跳机制的情况下，启动timer来检查长连接是否断开，如果断开就执行重连
    private void createStompClient(int flag) {
        if (1 == flag)
            onCreate();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mStompClient.send("websocket 心跳包");
                Log.e("websocket 心跳包", "发送中");
            }
        }, 5000, 5000);
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
        if (mStompClient != null) {
            mStompClient.disconnect();
            mStompClient = null;
        }
        Intent startSrv = new Intent(context, WebSocketService.class);
        context.stopService(startSrv);
    }

    private void showDialog() {
        CommonDialog dialog = new CommonDialog(getApplicationContext());
        dialog.setTitle("提示")
                .setMessage("你的账号在其他地方登陆！请重新登陆")
                .setNegativeButton("确定")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener((dialog1, confirm) -> loginOut());
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> dialog.show());
    }
}
