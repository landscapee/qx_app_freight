package qx.app.freight.qxappfreight.listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
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
import qx.app.freight.qxappfreight.bean.AfterHeavyExceptionBean;
import qx.app.freight.qxappfreight.bean.LoadUnLoadTaskPushBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.loadinglist.InstallNotifyEventBusEntity;
import qx.app.freight.qxappfreight.bean.loadinglist.NewInstallEventBusEntity;
import qx.app.freight.qxappfreight.bean.request.InstallChangeEntity;
import qx.app.freight.qxappfreight.bean.request.SeatChangeEntity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
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


/****
 * ???????????????
 *
 */
public class InstallEquipClient extends StompClient {

    public static final String TAG = "websocket";
    private Gson mGson = new Gson();
    private CompositeDisposable compositeDisposable;
    private Context mContext;
    private Timer mTimer;
    private TimerTask mTimerTask;


    public InstallEquipClient(String uri, Context mContext) {
        super(new CollectionClient.GetConnectionProvider());
        this.mContext = mContext;
        connect(uri);
    }

    @SuppressLint("CheckResult")
    public void connect(String uri) {
        StompClient my = Stomp.over(Stomp.ConnectionProvider.OKHTTP, uri);
        List <StompHeader> headers = new ArrayList <>();
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
                            sendMess(my, uri);
                            Log.e(TAG, "webSocket  ????????? ??????");
                            break;
                        case ERROR:
                            WebSocketService.mStompClient.remove(my);
                            WebSocketService.isTopic = false;
                            Log.e(TAG, "websocket ????????? ??????", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.e(TAG, "websocket ????????? ??????");
                            WebSocketService.isTopic = false;
                            if (UserInfoSingle.getInstance().getUserId() == null || StringUtil.isEmpty(UserInfoSingle.getInstance().getUserId())) {
                                if (mTimerTask != null) {
                                    mTimerTask.cancel();
                                    mTimerTask = null;
                                }
                                if (mTimer != null) {
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
            //??????   ?????? ?????????
            Disposable dispTopic3 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/aiSchTask/outFileTask")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.e(TAG, topicMessage.getPayload());
                        // ????????????
                        WebSocketUtils.pushReceipt(my, compositeDisposable, topicMessage.getStompHeaders().get(0).getValue());
                        if (topicMessage.getPayload().trim().contains("\"cancelFlag\":true")) {//?????????????????????
                            if (topicMessage.getPayload().contains("\"taskType\":1")) {//?????????
                                CommonJson4List <LoadAndUnloadTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                                sendLoadUnLoadGroupBoard(data);
                            } else if (topicMessage.getPayload().contains("\"taskType\":2")) {//??????
                                CommonJson4List <AcceptTerminalTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <AcceptTerminalTodoBean> data = gson.fromJson(topicMessage.getPayload(), AcceptTerminalTodoBean.class);
                                sendLoadUnLoadGroupBoard(data);
                            } else if (topicMessage.getPayload().contains("\"taskType\":-9")) {//??????????????????
                                CommonJson4List <LoadAndUnloadTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                                data.setConfirmTask(false);//??????????????????
                                sendLoadUnLoadGroupBoard(data);
                            } else if (topicMessage.getPayload().contains("\"taskFlag\":4")) {//?????????????????????????????????
                                CommonJson4List <LoadAndUnloadTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                                sendLoadUnLoadGroupBoard(data);
                            }
                        } else {
                            if (topicMessage.getPayload().contains("\"taskType\":1") || topicMessage.getPayload().contains("\"taskType\":2") || topicMessage.getPayload().contains("\"taskType\":3") || topicMessage.getPayload().contains("\"taskType\":5") || topicMessage.getPayload().contains("\"taskType\":6") || topicMessage.getPayload().contains("\"taskType\":7") || topicMessage.getPayload().contains("\"taskType\":8")) {//?????????
                                CommonJson4List <LoadAndUnloadTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                                sendLoadUnLoadGroupBoard(data);

                            } else if (topicMessage.getPayload().contains("\"taskType\":0")) {//??????
                                CommonJson4List <AcceptTerminalTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <AcceptTerminalTodoBean> data = gson.fromJson(topicMessage.getPayload(), AcceptTerminalTodoBean.class);
                                sendLoadUnLoadGroupBoard(data);
                            } else if (topicMessage.getPayload().contains("\"stevedoresStaffChange\":true")) {//?????????????????????
                                Gson gson = new Gson();
                                LoadUnLoadTaskPushBean data = gson.fromJson(topicMessage.getPayload(), LoadUnLoadTaskPushBean.class);
                                pushLoadUnLoadTask(data);
                            } else if (topicMessage.getPayload().contains("\"loadUnloadStatusChg\":true")) {//????????????
                                Gson gson = new Gson();
                                SeatChangeEntity data = gson.fromJson(topicMessage.getPayload(), SeatChangeEntity.class);
                                pushFlightInfo(data);
                            } else if (topicMessage.getPayload().contains("\"passengerLoadUnSend\":true")) {//??????15????????????
                                Gson gson = new Gson();
                                SeatChangeEntity data = gson.fromJson(topicMessage.getPayload(), SeatChangeEntity.class);
                                pushFlightInfo(data);
                            }
                            else if (topicMessage.getPayload().contains("\"passengerLoadStatusChg\":true")) {//?????????????????? ????????????
                                EventBus.getDefault().post("JunctionLoadFragment_refresh");
                            }
                            else {
                                CommonJson4List <LoadAndUnloadTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                                sendLoadUnLoadGroupBoard(data);
                            }
                        }
                    }, throwable -> Log.e(TAG, "??????????????? ??????", throwable));

            compositeDisposable.add(dispTopic3);

            //??????   ??????
            Disposable dispTopic1 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + WebSocketService.ToList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        //
                        Log.e("msgId", topicMessage.getStompHeaders().get(0).getValue());
                        WebSocketUtils.pushReceipt(my, compositeDisposable, topicMessage.getStompHeaders().get(0).getValue());
                        Log.d(TAG, "??????websocket-->?????? " + topicMessage.getPayload());
                        WebSocketResultBean mWebSocketBean = mGson.fromJson(topicMessage.getPayload(), WebSocketResultBean.class);
                        sendReshEventBus(mWebSocketBean);
                    }, throwable -> Log.e(TAG, "websocket-->????????????", throwable));
            compositeDisposable.add(dispTopic1);
            WebSocketService.subList.add(WebSocketService.ToList);

            compositeDisposable.add(dispTopic1);

            //??????  ?????????????????????
            Log.e("tagPush", "userid=====" + UserInfoSingle.getInstance().getUserId());
            Disposable loadingListPush = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/departure/preloadedCargo")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.d(TAG, "?????????????????????????????? " + topicMessage.getPayload());
                        // ????????????
                        WebSocketUtils.pushReceipt(my, compositeDisposable, topicMessage.getStompHeaders().get(0).getValue());
                        if (null != topicMessage.getPayload()) {
                            sendLoadingListPush(topicMessage.getPayload());
                        }
//                        Tools.wakeupScreen(mContext);//??????
                    }, throwable -> {
                        Log.e(TAG, "?????????????????????????????????", throwable);
                    });
            compositeDisposable.add(loadingListPush);
            //???????????? ???????????????????????????
            Disposable loadingListPush1 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/departure/newInstalledSingleto")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.d(TAG, "?????????????????????????????? " + topicMessage.getPayload());
                        // ????????????
                        WebSocketUtils.pushReceipt(my, compositeDisposable, topicMessage.getStompHeaders().get(0).getValue());
                        if (null != topicMessage.getPayload()) {
                            InstallNotifyEventBusEntity installNotifyEventBusEntity = JSON.parseObject(topicMessage.getPayload(), InstallNotifyEventBusEntity.class);
//                            installNotifyEventBusEntity.setFlighNo(topicMessage.getPayload());
                            sendLoadingListPushNotify(installNotifyEventBusEntity);
                        }
//                        Tools.wakeupScreen(mContext);//??????
                    }, throwable -> {
                        Log.e(TAG, "???????????????????????????", throwable);
                    });
            compositeDisposable.add(loadingListPush1);


            Disposable dispTopic4 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + WebSocketService.Install)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.d(TAG, "websocket-->????????????????????????????????? " + topicMessage.getPayload());
                        // ????????????
                        WebSocketUtils.pushReceipt(my, compositeDisposable, topicMessage.getStompHeaders().get(0).getValue());
                        JSONObject messObj = JSONObject.parseObject(topicMessage.getPayload(), JSONObject.class);
                        if (messObj != null) {
                            String str = messObj.getString("data");
                            JSONObject content = JSONObject.parseObject(str, JSONObject.class);
                            str = content.getString("content");
                            String flightNo = content.getString("flightNo");
                            if (str != null && str.length() > 2) {
                                str = str.replace("\\", "");
//                                str = str.substring(1,str.length()-1);
                                List <LoadingListBean.DataBean.ContentObjectBean> mWebSocketBean = JSONObject.parseArray(str, LoadingListBean.DataBean.ContentObjectBean.class);
                                if (mWebSocketBean != null && mWebSocketBean.size() > 0)
                                    mWebSocketBean.get(0).setFlightNo(flightNo);
                                sendInstallEventBus(mWebSocketBean);
                            }
                        }
//                        Tools.wakeupScreen(mContext);//??????
                    }, throwable -> Log.e(TAG, "websocket-->?????????????????????????????????", throwable));
            compositeDisposable.add(dispTopic4);
            //??????  ????????????
            Disposable dispTopic = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/" + UserInfoSingle.getInstance().getUserToken() + "/MT/message")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.d(TAG, "websocket-->?????? " + topicMessage.getPayload());
                        if (null != topicMessage.getPayload()) {
                            Tools.showDialog(mContext);
                        }
//                        Tools.wakeupScreen(mContext);//??????
                    }, throwable -> {
                        Log.e(TAG, "websocket-->????????????", throwable);
                    });
            compositeDisposable.add(dispTopic);
            //??????   ??????????????????
            Disposable dispTopic2 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/MT/msMsg")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.d(TAG, "websocket-->???????????? " + topicMessage.getPayload());
                        WebSocketMessageBean mWebSocketMessBean = mGson.fromJson(topicMessage.getPayload(), WebSocketMessageBean.class);
                        if ("CLIPPING_WEIGHTER_ERROR_NOTICE".equals(mWebSocketMessBean.getMessageName())) { //????????????
                            String string = mWebSocketMessBean.getContent().replace("\\", "");
                            AfterHeavyExceptionBean afterHeavyExceptionBean = mGson.fromJson(string, AfterHeavyExceptionBean.class);
                            sendMessageEventBus(afterHeavyExceptionBean);
                        } else {
                            sendMessageEventBus(mWebSocketMessBean);
                        }

                    }, throwable -> Log.e(TAG, "websocket-->??????????????????", throwable));

            compositeDisposable.add(dispTopic2);
        }
        Log.e(TAG, "websocket-->?????????????????????" + "/user/" + UserInfoSingle.getInstance().getUserId() + "/taskTodo/taskTodoList");
        my.connect();
    }

    public void sendMess(StompClient my, String uri) {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
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
                if (!WebSocketService.isTopic) {
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


    //??????????????????
    public static void sendReshEventBus(WebSocketResultBean bean) {
        EventBus.getDefault().post(bean);
    }

    //?????????????????? ????????? ??????
    public static void sendInstallEventBus(List <LoadingListBean.DataBean.ContentObjectBean> bean) {
        NewInstallEventBusEntity newInstallEventBusEntity = new NewInstallEventBusEntity(bean);
        EventBus.getDefault().post(newInstallEventBusEntity);
    }


    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    public static class GetConnectionProvider implements ConnectionProvider {

        @Override
        public Observable <String> messages() {
            return null;
        }

        @Override
        public Completable send(String stompMessage) {
            Log.d(TAG, "send" + stompMessage);
            return null;
        }

        @Override
        public Observable <LifecycleEvent> lifecycle() {
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

    //????????????
    public static void pushFlightInfo(SeatChangeEntity bean) {
        EventBus.getDefault().post(bean);
    }

    //????????????
    public void sendMessageEventBus(WebSocketMessageBean bean) {
        EventBus.getDefault().post(bean);
    }

    //????????????
    public void sendMessageEventBus(AfterHeavyExceptionBean bean) {
        EventBus.getDefault().post(bean);
    }

    private void sendLoadingListPush(String result) {
        InstallChangeEntity installChangeEntity = new InstallChangeEntity();
        installChangeEntity.setFlightNo(result);
        EventBus.getDefault().post(installChangeEntity);
        String flightNo = result.substring(0, result.indexOf(":"));
        EventBus.getDefault().post(flightNo);
    }

    private void sendLoadingListPushNotify(InstallNotifyEventBusEntity result) {
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
