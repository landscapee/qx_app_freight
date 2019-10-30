package qx.app.freight.qxappfreight.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.ActManager;
import qx.app.freight.qxappfreight.utils.ToastUtil;
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
        else {
            mTextView.setText("加载中……");}
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
        if (Build.VERSION.SDK_INT >= 26) {
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

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.currentView = getClass().getSimpleName();
        Log.e("========="+getClass().getSimpleName(),"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("========="+getClass().getSimpleName(),"onPause");
    }

    //两次返回退出
    protected int isFirstBack;
    private Timer mQuitTimer;
    protected void quitApp() {
        if (this.isFirstBack == 0) {
            ToastUtil.showToast("是否退出程序？");
            this.isFirstBack = 1;
            if (null == this.mQuitTimer) {
                this.mQuitTimer = new Timer();
            }
            this.mQuitTimer.schedule(new TimerTask() {
                public void run() {
                    BaseActivity.this.isFirstBack = 0;
                }
            }, 1000L);
        } else if (this.isFirstBack == 1) {
            ActManager.getAppManager().finishAllActivity();
            WebSocketService.stopServer(this);
            this.finish();
            System.exit(0);
        }
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (UserInfoSingle.getInstance().getRoleRS() != null && UserInfoSingle.getInstance().getRoleRS().size() > 0)
            outState.putSerializable("static_user", UserInfoSingle.getInstance());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            if (UserInfoSingle.getInstance().getRoleRS() == null || UserInfoSingle.getInstance().getRoleRS().size() == 0) {
                UserInfoSingle.setUser((LoginResponseBean) savedInstanceState.getSerializable("static_user"));
            }
        }
    }
}
