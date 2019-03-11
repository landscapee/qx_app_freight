package qx.app.freight.qxappfreight.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.utils.ActManager;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 基础封装页面
 */
public abstract class BaseActivity extends AppCompatActivity {
    public BasePresenter mPresenter;
    public MaterialDialog mProgessbarDialog;
    public TextView mTextView = null;
    private CustomToolbar mToolbar;

    private boolean isBack = false;

    private IsBackListener mIsBackListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        ActManager.getAppManager().addActivity(this);
        initDialog();
        FrameLayout contentView = findViewById(android.R.id.content);
        ViewGroup viewGroup = (ViewGroup) contentView.getChildAt(0);
        mToolbar = new CustomToolbar(this);
        viewGroup.addView(mToolbar, 0);
        businessLogic(savedInstanceState);
    }

    /**
     * 获取layout的资源id
     *
     * @return 结果
     */
    public abstract int getLayoutId();

    /**
     * 后续的业务逻辑
     */
    public abstract void businessLogic(Bundle savedInstanceState);

    /**
     * 控制toolbar显示与隐藏
     *
     * @param show 是否显示
     */
    public void setToolbarShow(int show) {
        if (mToolbar != null) {
            if (show == View.VISIBLE) {
                mToolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v ->
                {
                    if (!isBack)
                        this.finish();
                    else {
                        mIsBackListener.interceptBack();
                    }
                });
            }
            mToolbar.setVisibility(show);
        }
    }


    /**
     * 返回toolbar的引用
     *
     * @return toolbar
     */
    public CustomToolbar getToolbar() {
        if (mToolbar != null) {
            return mToolbar;
        }
        return null;
    }

    public void initDialog() {
        mProgessbarDialog = new MaterialDialog(this);
        View mView = LayoutInflater.from(this).inflate(R.layout.dialog_progressbar, null);
        mTextView = mView.findViewById(R.id.progressbar_tv);
        mProgessbarDialog.setCanceledOnTouchOutside(true);
        mProgessbarDialog.setContentView(mView);
    }

    public void showProgessDialog(String message) {
        if (!TextUtils.isEmpty(message))
            mTextView.setText(message);
        mProgessbarDialog.show();
    }

    public void setProgressText(String message){
        if (!TextUtils.isEmpty(message))
            mTextView.setText(message);
    }

    public void dismissProgessDialog() {
        mProgessbarDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (mToolbar != null) {
            mToolbar = null;
        }
        if (mProgessbarDialog != null) {
            mProgessbarDialog = null;
        }
        if (mPresenter != null) {
            mPresenter.detach();
            mPresenter.interruptHttp();
        }
        ActManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

    /**
     * TODO: 检测imei权限
     *
     * @return
     */
    @SuppressLint("CheckResult")
    public void checkPermissions() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
        ).subscribe(aBoolean -> {
            if (!aBoolean) {
                showPermissionsDialog();
            }
        });
    }

    public void checkPermissionsForWindow() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Tools.applyCommonPermission(this);
            }
        }
    }

    private void showPermissionsDialog() {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.setTitle("系统提示").setMessage("您没有授予该应用相关权限,需去设置界面开启后才能使用")
                .setCanceledOnTouchOutside(false)
                .setNegativeButton("去设置", v -> {
                    dialog.dismiss();
                    Uri packageURI = Uri.parse("package:" + getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    startActivity(intent);
                }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(isBack && keyCode == KeyEvent.KEYCODE_BACK){
            mIsBackListener.interceptBack();
            return isBack;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setIsBack(boolean isBack,IsBackListener mIsBackListener){
        this.isBack = isBack;
        this.mIsBackListener = mIsBackListener;
    }

    public interface IsBackListener{

        void interceptBack();

    }


}
