package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.dialog.InputDialog;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;

public class CustomCaptureActivity extends Activity {
    private String flag;

    private DecoratedBarcodeView barcodeScannerView;
    @BindView(R.id.ll_input)
    LinearLayout llInput;
    /**
     * 带参数启动
     */
    public static void startActivity(Context mContext, String flag) {
        Intent starter = new Intent(mContext, CustomCaptureActivity.class);
        starter.putExtra("flag", flag);
        ((Activity) mContext).startActivityForResult(starter, 0);
    }
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(final BarcodeResult result) {
            barcodeScannerView.pause();
            ScanDataBean scanDataBean = new ScanDataBean();
            scanDataBean.setData(result.getResult().toString());
            scanDataBean.setFunctionFlag(flag);
            EventBus.getDefault().post(scanDataBean);
            Tools.startShortSound(CustomCaptureActivity.this);
            finish();
            Log.e("222222222222",result.getResult().toString());
        }

        @Override
        public void possibleResultPoints(List <ResultPoint> resultPoints) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_capture);// 自定义布局
        ButterKnife.bind(this);
        flag = getIntent().getStringExtra("flag");
        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.dbv_custom);
        llInput.setOnClickListener(v -> showDialog());
        decode();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /**
     * Start decoding.
     */
    public void decode() {
        barcodeScannerView.decodeSingle(callback);
    }
    /**
     * InputDialog 的用法
     */
    private void showDialog() {
        InputDialog dialog1 = new InputDialog(this);
        dialog1.setTitle("手动输入")
                .setHint("请输入......")
                .setPositiveButton("取消")
                .setNegativeButton("确定")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new InputDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {

                        } else {
                            if (TextUtils.isEmpty(dialog1.getMessage())) {
                                ToastUtil.showToast("输入为空");
                            } else {
                                ScanDataBean scanDataBean = new ScanDataBean();
                                scanDataBean.setData(dialog1.getMessage());
                                scanDataBean.setFunctionFlag(flag);
                                EventBus.getDefault().post(scanDataBean);
                                Tools.startShortSound(CustomCaptureActivity.this);
                                finish();
//                                getBackMessage(dialog1.getMessage());
                            }

                        }
                    }
                })
                .show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        barcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScannerView.pauseAndWait();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}

