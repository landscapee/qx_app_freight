package qx.app.freight.qxappfreight.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.utils.ThreadPoolUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DownloadFileService extends Service {
    private static final String KEY_DOWNURL = "DOWNURL";
    private static final String KEY_BASE = "BASE";
    private static final String KEY_PATH = "PATH";
    private static final String KEY_FILENAME = "FILENAME";
    private String baseUrl;
    private String downUrl;
    private String savePath;
    private String fileName;
    private Context mContext;
    // 显示下载进度
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private int noticeId = 1012;

    public static void startService(Context context, String baseUrl, String downUrl, String fileName, String savePath) {
        Intent intent = new Intent(context, DownloadFileService.class);
        intent.putExtra(KEY_BASE, baseUrl);
        intent.putExtra(KEY_DOWNURL, downUrl);
        intent.putExtra(KEY_FILENAME, fileName);
        intent.putExtra(KEY_PATH, savePath);
        context.startService(intent);
    }

    public DownloadFileService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        baseUrl = intent.getStringExtra(KEY_BASE);
        downUrl = intent.getStringExtra(KEY_DOWNURL);
        savePath = intent.getStringExtra(KEY_PATH);
        fileName = intent.getStringExtra(KEY_FILENAME);
        mContext = this;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mContext);
        initNotice();
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initNotice() {
        mBuilder.setContentTitle(fileName.substring(0, fileName.lastIndexOf(".")));
        mBuilder.setTicker(fileName + "正在下载");
        mBuilder.setContentText(fileName.substring(0, fileName.lastIndexOf(".")) + "下载中");
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_logo));
        mBuilder.setSmallIcon(R.mipmap.ic_download);
        mNotificationManager.notify(noticeId, mBuilder.build());
    }

    private void startDownload() {
        Callback<ResponseBody> callback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    //获取文件总长度
                    long totalLength = response.body().contentLength();
                    File file = new File(savePath + fileName);
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    long currentLength = 0;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        //此处进行更新操作
                        //len即可理解为已下载的字节数
                        currentLength += len;
                        onLoading(currentLength, totalLength);
                    }
                    fos.flush();
                    fos.close();
                    bis.close();
                    is.close();
                    //此处就代表更新结束
                    mNotificationManager.cancel(noticeId);
                    InstallApp(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastUtil.showToast("下载安装文件出错");
            }
        };
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).callbackExecutor(ThreadPoolUtil.threadPool).build();
        DownloadService service = retrofit.create(DownloadService.class);
        Call<ResponseBody> call = service.download(downUrl);
        call.enqueue(callback);
    }

    private void onLoading(long len, long totalLength) {
        double currentPro = ((double) len / (double) totalLength) * 100;
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_logo));
        mBuilder.setSmallIcon(R.mipmap.ic_download);
        mBuilder.setContentTitle(fileName.substring(0, fileName.lastIndexOf(".")));
        mBuilder.setContentText((int) currentPro + "%");
        mBuilder.setProgress(100, (int) currentPro, false);
        mNotificationManager.notify(noticeId, mBuilder.build());
    }

    /**
     * @param file 安装文件
     */
    private void InstallApp(File file) {
        if (file == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type;
        if (Build.VERSION.SDK_INT < 23) {
            type = "application/vnd.android.package-archive";
        } else {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getFileExtension(file));
        }
        intent.setDataAndType(Uri.fromFile(file), type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private String getFileExtension(File file) {
        String filePath = file.getPath();
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastPoi == -1 || lastSep >= lastPoi) {
            return "";
        }
        return filePath.substring(lastPoi + 1);
    }
}
