package qx.app.freight.qxappfreight.listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

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
import qx.app.freight.qxappfreight.bean.LoadUnLoadTaskPushBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.WebSocketMessageBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.utils.WebSocketUtils;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.provider.ConnectionProvider;

public class NewspaperClient extends StompClient {


    public static final String TAG = "websocket";
    private Gson mGson = new Gson();
    private CompositeDisposable compositeDisposable;
    private Context mContext;
    private Timer mTimer;
    private TimerTask mTimerTask;

    public NewspaperClient(String uri, Context mContext) {
        super(new CollectionClient.GetConnectionProvider());
        this.mContext = mContext;
        connect(uri);
    }

    @SuppressLint("CheckResult")
    public void connect(String uri) {
        StompClient my = Stomp.over(Stomp.ConnectionProvider.OKHTTP, uri);
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(TAG, "guest"));
        //????????????
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
                            sendMess(my,uri);
                            Log.e(TAG, "webSocket  ?????? ??????");
                            break;
                        case ERROR:
                            Log.e(TAG, "websocket ?????? ??????", lifecycleEvent.getException());
                            WebSocketService.mStompClient.remove(my);
                            WebSocketService.isTopic = false;
                            break;
                        case CLOSED:
                            Log.e(TAG, "websocket ?????? ??????");
                            WebSocketService.isTopic = false;
                            if (UserInfoSingle.getInstance().getUserId() == null|| StringUtil.isEmpty(UserInfoSingle.getInstance().getUserId())){
                                if (mTimerTask!= null){
                                    mTimerTask.cancel();
                                    mTimerTask= null;
                                }
                                if (mTimer != null){
                                    mTimer.purge();
                                    mTimer.cancel();
                                    mTimer = null;
                                }
                            }
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.e(TAG, "Stomp failed server heartbeat");
                            WebSocketService.isTopic = false;
                            break;
                    }
                });
        compositeDisposable.add(dispLifecycle);
        if (!WebSocketService.isTopic) {
            WebSocketService.isTopic = true;
            //??????   ??????
            if (WebSocketService.isExist(WebSocketService.ToList)) {
                Disposable dispTopic1 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + WebSocketService.ToList)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(topicMessage -> {
                            // ????????????
                            WebSocketUtils.pushReceipt(my,compositeDisposable,topicMessage.getStompHeaders().get(0).getValue());
                            Log.d(TAG, "websocket-->?????? " + topicMessage.getPayload());
                            WebSocketResultBean mWebSocketBean = mGson.fromJson(topicMessage.getPayload(), WebSocketResultBean.class);
                            sendReshEventBus(mWebSocketBean);
                        }, throwable -> Log.e(TAG, "websocket-->????????????", throwable));
                compositeDisposable.add(dispTopic1);
                WebSocketService.subList.add(WebSocketService.ToList);

            }
            if (WebSocketService.isExist(WebSocketService.Login)) {
                //??????  ????????????
                Disposable dispTopic = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/" + UserInfoSingle.getInstance().getUserToken() + WebSocketService.Login)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(topicMessage -> {
                            Log.d(TAG, "websocket-->?????? " + topicMessage.getPayload());
                            if (null != topicMessage.getPayload()) {
                                Tools.showDialog(mContext);
                            }
                        }, throwable -> {
                            Log.e(TAG, "websocket-->????????????", throwable);
                        });
                compositeDisposable.add(dispTopic);
                WebSocketService.subList.add(WebSocketService.Login);
            }
            if (WebSocketService.isExist(WebSocketService.Message)) {
                //??????   ??????????????????
                Disposable dispTopic2 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + WebSocketService.Message)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(topicMessage -> {
                            Log.d(TAG, "websocket-->???????????? " + topicMessage.getPayload());
                            WebSocketMessageBean mWebSocketMessBean = mGson.fromJson(topicMessage.getPayload(), WebSocketMessageBean.class);
                            sendMessageEventBus(mWebSocketMessBean);
                        }, throwable -> Log.e(TAG, "websocket-->??????????????????", throwable));
                compositeDisposable.add(dispTopic2);
                WebSocketService.subList.add(WebSocketService.Message);
            }
        }
        my.connect();
    }


    public void sendMess(StompClient my,String uri) {
        if (mTimerTask!= null){
            mTimerTask.cancel();
            mTimerTask= null;
        }
        if (mTimer != null){
            mTimer.purge();
            mTimer.cancel();
            mTimer = null;
        }
        mTimer = new Timer();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("json", "123");
        mTimerTask = new TimerTask() {
            public void run() {
                compositeDisposable.add(my.send("/app/heartbeat", jsonObject.toJSONString()).subscribe(() -> Log.d(TAG, "websocket ??????????????????"), throwable -> Log.e(TAG, "websocket ??????????????????")));
                if (!WebSocketService.isTopic){
                    reConnect1(uri);
                }
                Log.e("websocket", "????????????" + jsonObject.toJSONString());

            }
        };
        mTimer.schedule(mTimerTask, Constants.TIME_HEART, Constants.TIME_HEART);
    }
    public void reConnect1(String uri) {
        WebSocketService.subList.clear();
        connect(uri);
        Log.e("websocket", "???????????? ??????????????????");
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

    //???????????????????????????????????????
    public static void sendLoadUnLoadGroupBoard(CommonJson4List bean) {
        EventBus.getDefault().post(bean);
    }

    //????????????????????????????????????
    public static void pushLoadUnLoadTask(LoadUnLoadTaskPushBean bean) {
        EventBus.getDefault().post(bean);
    }

    //??????????????????
    public static void sendReshEventBus(WebSocketResultBean bean) {
        EventBus.getDefault().post(bean);
    }


    //????????????
    public void sendMessageEventBus(WebSocketMessageBean bean) {
        EventBus.getDefault().post(bean);
    }

    private void sendLoadingListPush(String result) {
        EventBus.getDefault().post(result);
    }

//    private void showDialog() {
//        CommonDialog dialog = new CommonDialog(mContext);
//        dialog.setTitle("??????")
//                .setMessage("???????????????????????????????????????????????????")
//                .setNegativeButton("??????")
//                .isCanceledOnTouchOutside(false)
//                .isCanceled(true)
//                .setOnClickListener((dialog1, confirm) -> loginOut());
//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(() -> dialog.show());
//    }
//
//    //????????????
//    private void loginOut() {
//        UserInfoSingle.setUserNil();
//        ActManager.getAppManager().finishAllActivity();
//        WebSocketService.stopServer(MyApplication.getContext());
//        Intent intent = new Intent(MyApplication.getContext(), LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        mContext.startActivity(intent);
//    }
}
