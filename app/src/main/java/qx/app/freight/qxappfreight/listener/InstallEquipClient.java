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
import qx.app.freight.qxappfreight.bean.LockEventbusEntity;
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
 * 装卸机推送
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
                            sendMess(my, uri);
                            Log.e(TAG, "webSocket  装卸机 打开");
                            break;
                        case ERROR:
                            WebSocketService.mStompClient.remove(my);
                            WebSocketService.isTopic = false;
                            Log.e(TAG, "websocket 装卸机 出错", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.e(TAG, "websocket 装卸机 关闭");
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
            //订阅   运输 装卸机
            Disposable dispTopic3 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/aiSchTask/outFileTask")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.e(TAG, topicMessage.getPayload());
                        // 消息回执
                        WebSocketUtils.pushReceipt(my, compositeDisposable, topicMessage.getStompHeaders().get(0).getValue());
                        if (topicMessage.getPayload().trim().contains("\"cancelFlag\":true")) {//任务取消的推送
                            if (topicMessage.getPayload().contains("\"taskType\":1")) {//装卸机
                                CommonJson4List <LoadAndUnloadTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                                sendLoadUnLoadGroupBoard(data);
                            } else if (topicMessage.getPayload().contains("\"taskType\":2")) {//运输
                                CommonJson4List <AcceptTerminalTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <AcceptTerminalTodoBean> data = gson.fromJson(topicMessage.getPayload(), AcceptTerminalTodoBean.class);
                                sendLoadUnLoadGroupBoard(data);
                            } else if (topicMessage.getPayload().contains("\"taskType\":-9")) {//航班不保障了
                                CommonJson4List <LoadAndUnloadTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                                data.setConfirmTask(false);//航班不保障了
                                sendLoadUnLoadGroupBoard(data);
                            } else if (topicMessage.getPayload().contains("\"taskFlag\":4")) {//装卸机任务超时自动完成
                                CommonJson4List <LoadAndUnloadTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                                sendLoadUnLoadGroupBoard(data);
                            }
                        } else {
                            if (topicMessage.getPayload().contains("\"taskType\":1") || topicMessage.getPayload().contains("\"taskType\":2") || topicMessage.getPayload().contains("\"taskType\":3") || topicMessage.getPayload().contains("\"taskType\":5") || topicMessage.getPayload().contains("\"taskType\":6") || topicMessage.getPayload().contains("\"taskType\":7") || topicMessage.getPayload().contains("\"taskType\":8")) {//装卸机
                                CommonJson4List <LoadAndUnloadTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                                sendLoadUnLoadGroupBoard(data);

                            } else if (topicMessage.getPayload().contains("\"taskType\":0")) {//运输
                                CommonJson4List <AcceptTerminalTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <AcceptTerminalTodoBean> data = gson.fromJson(topicMessage.getPayload(), AcceptTerminalTodoBean.class);
                                sendLoadUnLoadGroupBoard(data);
                            } else if (topicMessage.getPayload().contains("\"stevedoresStaffChange\":true")) {//装卸员任务变换
                                Gson gson = new Gson();
                                LoadUnLoadTaskPushBean data = gson.fromJson(topicMessage.getPayload(), LoadUnLoadTaskPushBean.class);
                                pushLoadUnLoadTask(data);
                            } else if (topicMessage.getPayload().contains("\"loadUnloadStatusChg\":true")) {//机位变更
                                Gson gson = new Gson();
                                SeatChangeEntity data = gson.fromJson(topicMessage.getPayload(), SeatChangeEntity.class);
                                pushFlightInfo(data);
                            } else if (topicMessage.getPayload().contains("\"passengerLoadUnSend\":true")) {//结载15分钟推送
                                Gson gson = new Gson();
                                SeatChangeEntity data = gson.fromJson(topicMessage.getPayload(), SeatChangeEntity.class);
                                pushFlightInfo(data);
                            }
                            else if (topicMessage.getPayload().contains("\"passengerLoadStatusChg\":true")) {//结载任务步骤 状态变更
                                EventBus.getDefault().post("JunctionLoadFragment_refresh");
                            }
                            else {
                                CommonJson4List <LoadAndUnloadTodoBean> gson = new CommonJson4List <>();
                                CommonJson4List <LoadAndUnloadTodoBean> data = gson.fromJson(topicMessage.getPayload(), LoadAndUnloadTodoBean.class);
                                sendLoadUnLoadGroupBoard(data);
                            }
                        }
                    }, throwable -> Log.e(TAG, "运输装卸机 订阅", throwable));

            compositeDisposable.add(dispTopic3);

            //订阅   待办
            Disposable dispTopic1 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + WebSocketService.ToList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        //
                        Log.e("msgId", topicMessage.getStompHeaders().get(0).getValue());
                        WebSocketUtils.pushReceipt(my, compositeDisposable, topicMessage.getStompHeaders().get(0).getValue());
                        Log.d(TAG, "结载websocket-->代办 " + topicMessage.getPayload());
                        WebSocketResultBean mWebSocketBean = mGson.fromJson(topicMessage.getPayload(), WebSocketResultBean.class);
                        sendReshEventBus(mWebSocketBean);
                    }, throwable -> Log.e(TAG, "websocket-->代办失败", throwable));
            compositeDisposable.add(dispTopic1);
            WebSocketService.subList.add(WebSocketService.ToList);

            compositeDisposable.add(dispTopic1);

            //订阅  装机单变更推送
            Log.e("tagPush", "userid=====" + UserInfoSingle.getInstance().getUserId());
            Disposable loadingListPush = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/departure/preloadedCargo")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.d(TAG, "收到装机单变更推送： " + topicMessage.getPayload());
                        // 消息回执
                        WebSocketUtils.pushReceipt(my, compositeDisposable, topicMessage.getStompHeaders().get(0).getValue());
                        if (null != topicMessage.getPayload()) {
                            sendLoadingListPush(topicMessage.getPayload());
                        }
//                        Tools.wakeupScreen(mContext);//唤醒
                    }, throwable -> {
                        Log.e(TAG, "收到装机单变更推送失败", throwable);
                    });
            compositeDisposable.add(loadingListPush);
            //结载收到 通知录入的新装机单
            Disposable loadingListPush1 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/departure/newInstalledSingleto")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.d(TAG, "通知录入的新装机单： " + topicMessage.getPayload());
                        // 消息回执
                        WebSocketUtils.pushReceipt(my, compositeDisposable, topicMessage.getStompHeaders().get(0).getValue());
                        if (null != topicMessage.getPayload()) {
                            InstallNotifyEventBusEntity installNotifyEventBusEntity = JSON.parseObject(topicMessage.getPayload(), InstallNotifyEventBusEntity.class);
//                            installNotifyEventBusEntity.setFlighNo(topicMessage.getPayload());
                            sendLoadingListPushNotify(installNotifyEventBusEntity);
                        }
//                        Tools.wakeupScreen(mContext);//唤醒
                    }, throwable -> {
                        Log.e(TAG, "通知录入的新装机单", throwable);
                    });
            compositeDisposable.add(loadingListPush1);


            Disposable dispTopic4 = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + WebSocketService.Install)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.d(TAG, "websocket-->发送至结载的建议装机单 " + topicMessage.getPayload());
                        // 消息回执
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
//                        Tools.wakeupScreen(mContext);//唤醒
                    }, throwable -> Log.e(TAG, "websocket-->发送至结载的建议装机单", throwable));
            compositeDisposable.add(dispTopic4);
            //订阅  登录地址
            Disposable dispTopic = my.topic("/user/" + UserInfoSingle.getInstance().getUserId() + "/" + UserInfoSingle.getInstance().getUserToken() + "/MT/message")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(topicMessage -> {
                        Log.d(TAG, "websocket-->登录 " + topicMessage.getPayload());
                        if (null != topicMessage.getPayload()) {
                            Tools.showDialog(mContext);
                        }
//                        Tools.wakeupScreen(mContext);//唤醒
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
                        if ("CLIPPING_WEIGHTER_ERROR_NOTICE".equals(mWebSocketMessBean.getMessageName())) { //负重异常
                            String string = mWebSocketMessBean.getContent().replace("\\", "");
                            AfterHeavyExceptionBean afterHeavyExceptionBean = mGson.fromJson(string, AfterHeavyExceptionBean.class);
                            sendMessageEventBus(afterHeavyExceptionBean);
                        } else {
                            sendMessageEventBus(mWebSocketMessBean);
                        }

                    }, throwable -> Log.e(TAG, "websocket-->消息中心失败", throwable));

            compositeDisposable.add(dispTopic2);
        }
        Log.e(TAG, "websocket-->结载代办地址：" + "/user/" + UserInfoSingle.getInstance().getUserId() + "/taskTodo/taskTodoList");
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
                compositeDisposable.add(my.send("/app/heartbeat", jsonObject.toJSONString()).subscribe(() -> Log.d(TAG, "websocket 消息发送成功"), throwable -> Log.e(TAG, "websocket 消息发送失败")));
                if (!WebSocketService.isTopic) {
                    reConnect1(uri);
                }
                Log.e("websocket", "发送消息" + jsonObject.toJSONString());

            }
        };
        mTimer.schedule(mTimerTask, Constants.TIME_HEART, Constants.TIME_HEART);
    }

    public void reConnect1(String uri) {
        WebSocketService.subList.clear();
        connect(uri);
        Log.e("websocket", "心跳失败 正在重连……");
    }


    //用于代办刷新
    public static void sendReshEventBus(WebSocketResultBean bean) {
        EventBus.getDefault().post(bean);
    }

    //用于通知结载 装机单 建议
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

    //用于装卸机运输推送刷新弹窗
    public static void sendLoadUnLoadGroupBoard(CommonJson4List bean) {
        EventBus.getDefault().post(bean);
    }

    //用于装卸员任务人变换通知
    public static void pushLoadUnLoadTask(LoadUnLoadTaskPushBean bean) {
        EventBus.getDefault().post(bean);
    }

    //信息变更
    public static void pushFlightInfo(SeatChangeEntity bean) {
        EventBus.getDefault().post(bean);
    }

    //消息推送
    public void sendMessageEventBus(WebSocketMessageBean bean) {
        EventBus.getDefault().post(bean);
    }

    //负重异常
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
//        dialog.setTitle("提示")
//                .setMessage("你的账号在其他地方登陆！请重新登陆")
//                .setNegativeButton("确定")
//                .isCanceledOnTouchOutside(false)
//                .isCanceled(true)
//                .setOnClickListener((dialog1, confirm) -> loginOut());
//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(() -> dialog.show());
//    }
//
//    //强制登出
//    private void loginOut() {
//        UserInfoSingle.setUserNil();
//        ActManager.getAppManager().finishAllActivity();
//        WebSocketService.stopServer(MyApplication.getContext());
//        Intent intent = new Intent(MyApplication.getContext(), LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        mContext.startActivity(intent);
//    }

}
