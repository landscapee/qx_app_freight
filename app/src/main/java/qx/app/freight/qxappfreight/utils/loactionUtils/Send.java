package qx.app.freight.qxappfreight.utils.loactionUtils;

import android.util.Log;

import qx.app.freight.qxappfreight.utils.TimeUtils;

public class Send {
    private String ip;
    private int port;
    private int timeoutsecond;
    private Pusher pusher = null;

    public Send(String ip, int port, int timeoutsecond) {
        this.ip = ip;
        this.port = port;
        this.timeoutsecond = timeoutsecond * 1000;
    }

    public void SendToGisServer(final String content, final OnSendLisenter lisenter) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag = false;
                byte[] uuid = null;

                byte[] msg = null;
                Pusher pusher = null;
                try {
                    boolean result;
                    msg = content.getBytes("UTF-8");
                    pusher = PusherSingle.getPusher(ip, port, timeoutsecond);
                    Log.d("SendLocationService", "push0x20Message: 发送"+ TimeUtils.getTime2());
                    result = pusher.push0x20Message(msg);

                    if (result) {
                        flag = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (lisenter != null) {
                    lisenter.onSendResult(flag);
                }
            }

        }).start();
    }

    public interface OnSendLisenter {
        void onSendResult(boolean isSuc);
    }
}

