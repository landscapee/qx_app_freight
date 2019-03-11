package qx.app.freight.qxappfreight.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.java_websocket.WebSocket;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

/**
 * TODO : xxx
 * Created by pr
 */
public class WebSocketSTOMPManager {
    public Context mContext;
    private StompClient mStompClient;
    private Timer mTimer;
    private long nowdate;
    private String uri = "ws://myyx.nat123.cc:24010/taskAssignCenter?userId=ud8eecd98a3ea4e7aaa2f24ab2808680e";
    //myyx.nat123.cc:24010
    //private String uri = "http://myyx.nat123.cc:24010/taskAssignCenter?userId=ua1a81dd438b748dc9ddf76896b6a11fb";
    private CompositeDisposable compositeDisposable;
    public WebSocketSTOMPManager(Context context) {
        this.mContext = context;
        mTimer = new Timer();
    }

    //创建连接
    public void connect(Scheduler scheduler) {
        mStompClient = Stomp.over(WebSocket.class, uri);
        Disposable dispLifecycle = (Disposable) mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case ERROR:
                            Log.e("websocket", "连接出错。。。。。" + lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.e("websocket", "连接关闭。。。。。");
                            break;
                        case OPENED:
                            Log.e("websocket", "连接已开启。。。。。");
//                    createStompClient();
                            break;
                    }
                });
        compositeDisposable.add(dispLifecycle);
        mStompClient.connect();

        Log.e("websocket", "连接中。。。。。");
        mStompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case ERROR:
                    Log.e("websocket", "连接出错。。。。。" + lifecycleEvent.getException());
                    break;
                case CLOSED:
                    Log.e("websocket", "连接关闭。。。。。");
                    break;
                case OPENED:
                    Log.e("websocket", "连接已开启。。。。。");
//                    createStompClient();
                    break;
            }
        });
    }


    //订阅消息
    public void registerStompTopic(String registerMassage) {
        mStompClient.topic(registerMassage).subscribe(new Action1<StompMessage>() {
            @Override
            public void call(StompMessage stompMessage) {
                Toast.makeText(mContext, "订阅成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //创建长连接，服务器端没有心跳机制的情况下，启动timer来检查长连接是否断开，如果断开就执行重连
    private void createStompClient() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
//                sendMassage("");
//                Log.e("websocket 心跳包", "发送中");
            }
        }, 1000, 1000);
        //每次发送心跳包，服务器接收到响应就会返回一个值，如果查过5s还没有收到返回值，
        // 那么就判定是断网
        if ((System.currentTimeMillis() - nowdate) > 5000 && nowdate != 0) {
            mStompClient.disconnect();
            mTimer.cancel();
//            connect();
            Log.e("websocket", "" + System.currentTimeMillis() + "nowdate:" + nowdate);
            return;
        }
    }


//    //发送消息
//    public void sendMassage(String massage) {
//        mStompClient.send(massage).subscribe(new Subscriber<Void>() {
//            @Override
//            public void onCompleted() {
//                Log.e("websocket send", "发送成功");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e("websocket send", "失败" + e.getMessage());
//            }
//
//            @Override
//            public void onNext(Void aVoid) {
//                Log.e("websocket send", "next");
//            }
//        });
//    }

    //停止连接
    public void stop() {
        mStompClient.disconnect();
    }
}
