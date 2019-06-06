package qx.app.freight.qxappfreight.listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
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
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.ActManager;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.provider.ConnectionProvider;

public class InstallEquipClient extends StompClient {

    public static final String TAG = "websocket";
    private Gson mGson = new Gson();
    private CompositeDisposable compositeDisposable;
    private Context mContext;

    @SuppressLint("CheckResult")
    public InstallEquipClient(String uri,Context mContext) {
        super(new CollectionClient.GetConnectionProvider());
        this.mContext = mContext;
        StompClient my=  Stomp.over(Stomp.ConnectionProvider.OKHTTP, uri);
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(TAG, "guest"));
        //超时连接
        withClientHeartbeat(1000).withServerHeartbeat(1000);
        resetSubscriptions();
        my.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.e(TAG, "webSocket  Collect 打开");
                            break;
                        case ERROR:
                            Log.e(TAG, "websocket Collect 出错", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.e(TAG, "websocket Collect 关闭");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.e(TAG, "Stomp failed server heartbeat");
                            break;
                    }
                });

        //订阅   运输 装卸机
        Disposable dispTopic3 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/aiSchTask/outFileTask")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.e(TAG, topicMessage.getPayload());
                    if (topicMessage.getPayload().contains("cancelFlag:true")) {//任务取消的推送
                        if (topicMessage.getPayload().contains("taskType:1")) {//装卸机
                            CommonJson4List<LoadAndUnloadTodoBean> gson = new CommonJson4List<>();
                            CommonJson4List<LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                            sendLoadUnLoadGroupBoard(data);
                        } else if (topicMessage.getPayload().contains("taskType:2")) {//运输
                            CommonJson4List<AcceptTerminalTodoBean> gson = new CommonJson4List<>();
                            CommonJson4List<AcceptTerminalTodoBean> data = gson.fromJson(topicMessage.getPayload(), AcceptTerminalTodoBean.class);
                            sendLoadUnLoadGroupBoard(data);
                        }
                    } else {
                        if (topicMessage.getPayload().contains("taskType:1") || topicMessage.getPayload().contains("taskType:2")|| topicMessage.getPayload().contains("taskType:3")|| topicMessage.getPayload().contains("taskType:5")) {//装卸机
                            CommonJson4List<LoadAndUnloadTodoBean> gson = new CommonJson4List<>();
                            CommonJson4List<LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                            sendLoadUnLoadGroupBoard(data);
                        } else if (topicMessage.getPayload().contains("taskType:0")) {//运输
                            CommonJson4List<AcceptTerminalTodoBean> gson = new CommonJson4List<>();
                            CommonJson4List<AcceptTerminalTodoBean> data = gson.fromJson(topicMessage.getPayload(), AcceptTerminalTodoBean.class);
                            sendLoadUnLoadGroupBoard(data);
                        }else {
                            CommonJson4List<LoadAndUnloadTodoBean> gson = new CommonJson4List<>();
                            CommonJson4List<LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                            sendLoadUnLoadGroupBoard(data);
                        }
                    }
                }, throwable -> Log.e(TAG, "运输装卸机 订阅", throwable));

        compositeDisposable.add(dispTopic3);
        my.connect();
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

    //用于装卸机运输推送刷新弹窗
    public static void sendLoadUnLoadGroupBoard(CommonJson4List bean) {
        EventBus.getDefault().post(bean);
    }

}
