package qx.app.freight.qxappfreight.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import qx.app.freight.qxappfreight.dialog.ProgressDialog;
import qx.app.freight.qxappfreight.widget.CommonDialog;

public class DownloadUtils {

    final String DownloadUrl = "https://raw.githubusercontent.com/dimoge/test/master/WiInput_sign.apk";
    String apkFilPath = "";
    ProgressDialog progressDialog;

    Context context;

    boolean download_finish = false;

    public DownloadUtils(Context context) {
        this.context = context;
        apkFilPath = context.getExternalCacheDir().getAbsolutePath() + File.separator + "WiInput_sign.apk";
    }

    public void downloadApk() {

        File file = new File(apkFilPath);
        file.deleteOnExit();

        //开始下载
        Request request = new Request.Builder().url(DownloadUrl).get().build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("dime", "下载失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.e("dime", "下载Total=" + response.body().contentLength());

                long total = response.body().contentLength();
                FileOutputStream fos = null;
                InputStream inputStream = null;
                progressDialog = new ProgressDialog();
                progressDialog.setData(context, new ProgressDialog.OnDismissListener() {
                    @Override
                    public void refreshUI(boolean isLocal) {
                        if(download_finish){
                            call.cancel();
                        }
                    }
                });
                progressDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "download");
                try {
                    fos = new FileOutputStream(file);
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    long sum = 0;
                    inputStream = response.body().byteStream();
                    while ((len = inputStream.read(bytes)) > 0) {
                        fos.write(bytes, 0, len);
                        sum += len;
                        Log.e("dime", "进度：sum=" + sum + ", total=" + total);
                        progressDialog.setProgress((int) (100 * sum / total));
                    }
                    download_finish = true;
                    progressDialog.dismiss();
                    //是否开始安装
                    showDialogInstall();
                    Log.e("dime", "下载完成");
                } catch (IOException e) {
                    Log.e("dime", "下载错误:" + e.getMessage());
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    public void showDialogInstall() {
        CommonDialog dialog = new CommonDialog(context);
        dialog.setTitle("输入法")
                .setMessage("下载完成，安装输入法？")
                .setPositiveButton("确定")
                .setNegativeButton("取消")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            Log.e("dime", "开始安装");
                            installAPK();
                        } else {
                            Log.e("dime", "取消安装");
                        }
                    }
                })
                .show();
    }

    /**
     * 开始下载的弹框提示
     */
    public void showDialogDownload() {
        CommonDialog dialog = new CommonDialog(context);
        dialog.setTitle("输入法")
                .setMessage("开始下载输入法？")
                .setPositiveButton("确定")
                .setNegativeButton("取消")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            //开始下载
                            downloadApk();
                        } else {
                            //取消下载
                            Log.e("dime", "取消下载");
                        }
                    }
                })
                .show();
    }

    /**
     * 安装apk
     */
    public void installAPK() {

        //完成拷贝后，开始安装
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        File file = new File(apkFilPath);

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
     * 输入法是否安装
     *
     * @return
     */
    public boolean isInstall() {
        for (PackageInfo info : context.getPackageManager().getInstalledPackages(0)) {
            if ("com.hit.wi".equals(info.packageName)) {
                ToastUtil.showToast("已经安装WI输入法！");
                Log.e("dime", "已经装过了");
                return true;
            }
        }
        return false;
    }

    /**
     * 是否已经下载成功
     *
     * @return
     */
    public boolean isDownload() {
        File file = new File(apkFilPath);
        return file.exists();
    }


}