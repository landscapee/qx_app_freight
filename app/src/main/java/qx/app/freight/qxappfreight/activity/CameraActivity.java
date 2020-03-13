package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.utils.ToastUtil;


/**
 * Created by guohao On 2019/6/10 15:01
 * 相机公共activity
 */
public class CameraActivity extends AppCompatActivity {

    /**
     * 相机
     */
    public static final int CAMERA = 1000;

    /**
     * 相册
     */
    public static final int ALBUM = 1001;

    /**
     * 结果
     */
    public static final String RESULT = "RESULT";

    public static final int MY_REQUEST_CODE = 9999;

    /**
     * 静态常量 类型
     */
    public static String TYPE = "TYPE";

    /**
     * 静态常量 请求code
     */
    public static String CODE = "CODE";

    public String filePath = "";


    /**
     * requestCode
     */
    int REQUEST_CODE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filePath = getExternalCacheDir().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
        Log.e("dime", filePath);
        Intent intent = getIntent();
        REQUEST_CODE = intent.getIntExtra(CODE, 0);
        int type = intent.getIntExtra(TYPE, CAMERA);
        if (type == CAMERA) {
            camera();
        } else if (type == ALBUM) {
            album();
        } else {
            ToastUtil.showToast("选择照片的类型错误，退出！");
            finish();
        }
    }

    /**
     * 页面跳转
     *
     * @param context
     * @param requestCode
     * @param type
     */
    public static void startCameraActivity(Context context, int requestCode, int type) {
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(CODE, requestCode);
        ((BaseActivity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 相机选择
     */
    public void camera() {
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(filePath);
        file.deleteOnExit();

        Uri mUri = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mUri = FileProvider.getUriForFile(this, "qx.app.freight.qxappfreight.fileProvider", file);
        } else {
            mUri = Uri.fromFile(file);
        }
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intentCamera, MY_REQUEST_CODE);
    }

    /**
     * 相册选择
     */
    public void album() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //相机
            if (requestCode == MY_REQUEST_CODE) {
                Intent intent = new Intent();
                intent.putExtra(RESULT, filePath);
                setResult(RESULT_OK, intent);
            }
        }
    }
}
