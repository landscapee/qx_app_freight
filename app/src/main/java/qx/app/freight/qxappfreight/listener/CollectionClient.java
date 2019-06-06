package qx.app.freight.qxappfreight.listener;

import android.annotation.SuppressLint;
import android.util.Log;

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
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.provider.ConnectionProvider;

public class CollectionClient extends StompClient {
    public static final String TAG = "websocket";
    private Gson mGson = new Gson();
    private CompositeDisposable compositeDisposable;

    @SuppressLint("CheckResult")
    public CollectionClient(String uri) {
        super(new GetConnectionProvider());
        Stomp.over(Stomp.ConnectionProvider.OKHTTP, uri);
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(TAG, "guest"));
        headers.add(new StompHeader(TAG, "guest"));
        //超时连接
        withClientHeartbeat(1000).withServerHeartbeat(1000);
        resetSubscriptions();
        lifecycle()
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

        //订阅   待办
        Disposable dispTopic1 = this.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/taskTodo/taskTodoList")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "张硕订阅成功 " + topicMessage.getPayload());
                    WebSocketResultBean mWebSocketBean = mGson.fromJson(topicMessage.getPayload(), WebSocketResultBean.class);
                    sendReshEventBus(mWebSocketBean);
                }, throwable -> Log.e(TAG, "张硕订阅失败", throwable));

        compositeDisposable.add(dispTopic1);
    }

    //用于代办刷新
    public static void sendReshEventBus(WebSocketResultBean bean) {
        EventBus.getDefault().post(bean);
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


}
