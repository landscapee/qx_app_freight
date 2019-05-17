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
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.concurrent.Executor;

import qx.app.freight.qxappfreight.R;

public class DownloadFileService extends Service {
    private static final String KEY_DOWNURL = "DOWNURL";
    private static final String KEY_PATH = "PATH";
    private static final String KEY_FILENAME = "FILENAME";
    private String TAG = "tagDownload";
    private String downUrl;
    private String savePath;
    private String fileName;
    private Context mContext;
    private final Executor executor = new PriorityExecutor(2, true);
    // 显示下载进度
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private int noticeId = 1012;

    public static void startService(Context context, String downUrl, String fileName, String savePath) {
        Intent intent = new Intent(context, DownloadFileService.class);
        intent.putExtra(KEY_DOWNURL, downUrl);
        intent.putExtra(KEY_FILENAME, fileName);
        intent.putExtra(KEY_PATH, savePath);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, DownloadFileService.class);
        context.stopService(intent);
    }

    public DownloadFileService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        Callback.ProgressCallback<File> callback = new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Log.e(TAG, "onSuccess: 下载完成");
                mNotificationManager.cancel(noticeId);
                InstallApp(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, "onCancelled: ");
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onWaiting() {
                Log.e(TAG, "onWaiting: ");
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                Log.e(TAG, "total: " + total + "---current: " + current);
                double currentPro = ((double) current / (double) total) * 100;
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_logo));
                mBuilder.setSmallIcon(R.mipmap.ic_download);
                mBuilder.setContentTitle(fileName.substring(0, fileName.lastIndexOf(".")));
                mBuilder.setContentText((int) currentPro + "%");
                mBuilder.setProgress(100, (int) currentPro, false);
                mNotificationManager.notify(noticeId, mBuilder.build());
            }
        };
        RequestParams params = new RequestParams(downUrl);
        params.setAutoRename(true);
        params.setExecutor(executor);
        params.setSaveFilePath(savePath + fileName);
        x.http().post(params, callback);
    }

    /**
     *
     * @param file 安装文件
     */
    private void InstallApp(File file) {
        if (file == null)
            return;
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
        if (lastPoi == -1 || lastSep >= lastPoi)
            return "";
        return filePath.substring(lastPoi + 1);
    }
}
