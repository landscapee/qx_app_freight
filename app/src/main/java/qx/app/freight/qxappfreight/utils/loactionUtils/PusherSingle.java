package qx.app.freight.qxappfreight.utils.loactionUtils;


public class PusherSingle {
    private static Pusher pusher = null;

    public synchronized static Pusher getPusher(String ip, int port, int timeoutsecond) throws Exception {
        if (pusher == null) {
            pusher = new Pusher(ip, port, timeoutsecond * 1000);
        }

        return pusher;
    }
}
