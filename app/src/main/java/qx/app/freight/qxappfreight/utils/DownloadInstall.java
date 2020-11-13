package qx.app.freight.qxappfreight.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import qx.app.freight.qxappfreight.dialog.ProgressDialog;

/**
 * Created by guohao On 2019/6/4 15:57
 * <p>
 * 下载安装各种apk
 */
public class DownloadInstall {

    final String TAG = "download&install";

    String downloadUrl;
    String saveFilePath;
    ProgressDialog progressDialog;
    Context context;

    public DownloadInstall(Builder builder) {
        this.downloadUrl = builder.downloadUrl;
        this.saveFilePath = builder.saveFilePath;
        this.progressDialog = builder.progressDialog;
        this.context = builder.context;
    }

    public void start() {

        DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask();
        downloadAsyncTask.execute(downloadUrl);
    }


    /**
     * 子线程类
     */
    public class DownloadAsyncTask extends AsyncTask<String, Integer, File> {

        @Override
        protected File doInBackground(String... url) {
            File file = new File(saveFilePath);
            //下载
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url[0]).get().build();
            Call call = okHttpClient.newCall(request);
            Response response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
                publishProgress(-1);
                Log.e(TAG, "下载失败>>>>\n" + e.getMessage());
                return null;
            }

            //存储文件
            FileOutputStream fos = null;
            InputStream inputStream = null;
            try {

                file.deleteOnExit();
                fos = new FileOutputStream(file);

                long PROGRESS_TOTAL = response.body().contentLength();
                long PROGRESS_CURRENT = 0;

                byte[] bytes = new byte[2048];
                int len = 0;
                inputStream = response.body().byteStream();
                while ((len = inputStream.read(bytes)) > 0) {
                    fos.write(bytes, 0, len);
                    PROGRESS_CURRENT += len;
                    //更新进度
                    if (PROGRESS_TOTAL != -1) {
                        publishProgress((int) (PROGRESS_CURRENT * 100 / PROGRESS_TOTAL));
                    } else {
                        publishProgress(-2);//文件长度为-1  不显示进度条
                    }

                }
                return file;
            } catch (Exception e) {
                Log.e(TAG, "保存文件失败：>>>>\n" + e.getMessage());
                publishProgress(-1);
                progressDialog.dismiss();
                return null;
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    if (inputStream != null) {
                        fos.close();
                    }
                } catch (IOException ioe) {
                    Log.e(TAG, "流关闭失败");
                    publishProgress(-1);
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), TAG);
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            //完成下载，开始安装
            progressDialog.setProgress(100);
            progressDialog.dismiss();
            if (file != null) {
                //开始安装
                installApk(file);
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //更新进度条

            if (values[0] == 100) {
                progressDialog.dismiss();
            } else if (values[0] == -1) {
                ToastUtil.showToast("安装失败！");
                progressDialog.dismiss();
            }
            else if (values[0] == -2){
                progressDialog.hidePro();
            }
            else {
                progressDialog.setProgress(values[0]);
            }

        }
    }


    /**
     * 该apk是否安装
     *
     * @return
     */
    public boolean isAppInstall(String packageName) {
        for (PackageInfo info : context.getPackageManager().getInstalledPackages(0)) {
            if (info.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 安装
     *
     * @param file apk文件
     */
    public void installApk(File file) {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //分版本安装
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //使用fileProvider安装
            Uri apkUri = FileProvider.getUriForFile(context, "qx.app.freight.qxappfreight.fileProvider", file);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //普通安装
            installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        }
        context.startActivity(installIntent);
    }

    /**
     * 构建类
     */
    public static class Builder {
        public String saveFilePath;
        public ProgressDialog progressDialog;
        public Context context;
        public String packageName;
        String downloadUrl;

        public Builder Context(Context context) {
            this.context = context;
            return this;
        }

        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public Builder saveFilePath(String saveFilePath) {
            this.saveFilePath = saveFilePath;
            return this;
        }

        public Builder progressDialog(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
            return this;
        }

        public DownloadInstall build() {
            DownloadInstall downloadInstall = new DownloadInstall(this);
            return downloadInstall;
        }
    }

}
