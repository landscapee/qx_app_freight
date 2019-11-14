package qx.app.freight.qxappfreight.utils.loactionUtils;

import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Pusher {

    private int timeout;
    private String host;
    private int port;
    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public Pusher(String host, int port, int timeoutMillis) throws Exception {

        this.host = host;
        this.port = port;
        this.timeout = timeoutMillis;
        initSocket();
    }


    public Pusher(Socket socket) throws Exception {
        this.socket = socket;
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    private void initSocket() throws Exception {
        socket = new Socket(this.host, this.port);
        socket.setSoTimeout(timeout);
        in = socket.getInputStream();
        out = socket.getOutputStream();
        Log.d(SendLocationService.TAG, "initSocket: 已重连");
    }

    public void close() throws Exception {
        if (socket == null) {
            return;
        }
        socket.close();
    }


    public boolean push0x20Message(byte[] data) {

        boolean flag = false;
        try {
            if (data == null) {
                throw new NullPointerException("data array is null");
            }

            out.write(data);
            out.flush();

            byte[] b = new byte[1];
            in.read(b);
            Log.d("SendLocationService", "push0x20Message: " + (int) (b[0]));

            if ((int) b[0] == 49) {
                flag = true;
            }/* else {
                socket.close();
                initSocket();
            }*/
        } catch (Exception e) {
            try {
                socket.close();
                initSocket();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return flag;
    }


}
