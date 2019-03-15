package qx.app.freight.qxappfreight.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class WebSocketService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new WebSocketBinder();
    }

    public class WebSocketBinder extends Binder {
        WebSocketService getService() {
            return WebSocketService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
