package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jwenfeng.library.pulltorefresh.util.DisplayUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.devilsen.czxing.code.BarcodeFormat;
import me.devilsen.czxing.view.ScanActivityDelegate;
import me.devilsen.czxing.view.ScanListener;
import me.devilsen.czxing.view.ScanView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.CabinStrAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.LaserAndZxingBean;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.response.GroundAgentBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.dialog.InputDialog;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * activity  通过intent回传
 * <p>
 * fragment 通过 EventBus 回传（传入的flag 就是 接收EventBus 判断的 flag）
 * <p>
 * by zyy
 * <p>
 * 2019/2/26
 */
public class ScanManagerActivity extends BaseActivity implements ScanListener, SensorEventListener {
    @BindView(R.id.zx_view)
    ScanView mScanView;
    @BindView(R.id.btn_open_flash_light)
    Button btnOpen;
    @BindView(R.id.btn_again)
    Button btnAgain;
    @BindView(R.id.ll_zxing)
    LinearLayout llZxing;
    @BindView(R.id.ll_input)
    LinearLayout llInput;
    @BindView(R.id.ll_agent)
    LinearLayout llAgent;
    @BindView(R.id.tv_agent)
    TextView tvAgent;

    @BindView(R.id.flashlight_close)
    ImageView flashlightClose;
    @BindView(R.id.tv_special)
    TextView tvSpecial;

    private Boolean isOpen = false;
    private String flag = null;
    //特殊字符，显示在上面
    private String special = null;
    private String groundAgentCode = null;
    private List <String> groundAgentBeansStr = new ArrayList <>();
    private boolean laserAndZxing;//是否是从激光扫描界面过来的
    private ScanActivityDelegate.OnScanDelegate scanDelegate;
    private SensorManager mSensroMgr;//传感器管理类

    private int codeType = -1; // 0 需要显示 地面代理选项

    /**
     * 普通启动
     */
    public static void startActivity(Context mContext) {
        Intent starter = new Intent(mContext, ScanManagerActivity.class);
        ((Activity) mContext).startActivityForResult(starter, 0);
    }

    /**
     * 带特殊字符启动
     */
    public static void startActivity(Context mContext, String flag, String special) {
        Intent starter = new Intent(mContext, ScanManagerActivity.class);
        starter.putExtra("special", special);
        starter.putExtra("flag", flag);
        ((Activity) mContext).startActivityForResult(starter, 0);
    }

    /**
     * 带参数启动
     */
    public static void startActivity(Context mContext, String flag) {
        Intent starter = new Intent(mContext, ScanManagerActivity.class);
        starter.putExtra("flag", flag);
        ((Activity) mContext).startActivityForResult(starter, 0);
    }
    /**
     * 带参数启动
     */
    public static void startActivityForAgent(Context mContext, String flag) {
        Intent starter = new Intent(mContext, ScanManagerActivity.class);
        starter.putExtra("flag", flag);
        starter.putExtra("codeType", 0);
        ((Activity) mContext).startActivityForResult(starter, 0);
    }
    /**
     * 从激光扫码界面跳转过来
     */
    public static void startActivityFromLaser(Context mContext, String flag) {
        Intent starter = new Intent(mContext, ScanManagerActivity.class);
        starter.putExtra("flag", flag).putExtra("laserAndZxing", true);
        mContext.startActivity(starter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_manager;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        mSensroMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "普通扫码");
        flag = getIntent().getStringExtra("flag");
        special = getIntent().getStringExtra("special");
        codeType = getIntent().getIntExtra("codeType",-1);
        laserAndZxing = getIntent().getBooleanExtra("laserAndZxing", false);
        mScanView.setScanMode(ScanView.SCAN_MODE_BIG);
        mScanView.setScanListener(this);
        if (!TextUtils.isEmpty(special)) {
            tvSpecial.setText(special);
        }
        btnOpen.setOnClickListener(v -> {
            if (isOpen) {
                isOpen = false;
                btnOpen.setText("打开闪光灯");
            } else {
                mScanView.onFlashLightClick();
                isOpen = true;
                btnOpen.setText("关闭闪光灯");
            }
        });
        flashlightClose.setOnClickListener(v -> {
            if (isOpen) {
                isOpen = false;
                btnOpen.setText("打开闪光灯");
            } else {
                isOpen = true;
                btnOpen.setText("关闭闪光灯");
            }
        });
        llZxing.setOnClickListener(v -> {
            //如果laserAndZxing 是true 就是从激光扫码跳转过来的，就直接关闭 回到激光扫码界面
            if (laserAndZxing) {
                finish();
            } else {
                LaserScanActivity.startActivityFromZxing(ScanManagerActivity.this, flag);
            }
        });
        llInput.setOnClickListener(v -> {
            mScanView.onAnalysisBrightness(true);
            mScanView.onFlashLightClick();
            showDialog();
        });
        for (GroundAgentBean groundAgentBean : MainActivity.groundAgentBeans) {
            groundAgentBeansStr.add(groundAgentBean.getShortName());
        }
        if (MainActivity.groundAgentBeans.size() > 0) {
            groundAgentCode = MainActivity.groundAgentBeans.get(0).getAgentCode();
            tvAgent.setText(MainActivity.groundAgentBeans.get(0).getShortName());
        }
        if (codeType == 0){
            llAgent.setVisibility(View.VISIBLE);
            llAgent.setOnClickListener(v -> {
                showPopList(groundAgentBeansStr, MainActivity.getAgentName(groundAgentCode), llAgent, 1);
            });
        }else {
            llAgent.setVisibility(View.GONE);
        }

    }

    /**
     * InputDialog 的用法
     */
    private void showDialog() {
        InputDialog dialog1 = new InputDialog(this);
        dialog1.setTitle("手动输入")
                .setHint("请输入......")
                .setPositiveButton("确定")
                .setNegativeButton("取消")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener((dialog, confirm) -> {
                    if (confirm) {
                        if (TextUtils.isEmpty(dialog1.getMessage())) {
                            ToastUtil.showToast("输入为空");
                        } else {
                            getBackMessage(dialog1.getMessage());
                        }
                    } else {

                    }
                }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensroMgr.registerListener(this, mSensroMgr.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);//开启监听传感器
        mScanView.openCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mScanView.startScan();  // 显示扫描框，并开始识别
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScanView.stopScan();
        mScanView.closeCamera(); // 关闭摄像头预览，并且隐藏扫描框
        mSensroMgr.unregisterListener(this);//断开监听传感器
    }

    @Override
    public void onDestroy() {
        mScanView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    @Override
    public void onScanSuccess(String result, BarcodeFormat format) {
        if (result != null) {
            getBackMessage(result);
        }
        finish();
    }

    @Override
    public void onOpenCameraError() {
        ToastUtil.showToast("扫描失败，请重试。");
        Log.e("onOpenCameraError", "onOpenCameraError");
    }

    /**
     * 获取回调的扫码信息
     *
     * @param result
     */
    private void getBackMessage(String result) {
        if (laserAndZxing) {
            EventBus.getDefault().post(new LaserAndZxingBean(result, "laser"));
        } else {
            if (flag == null) {
                Intent intent = new Intent();
                intent.putExtra(Constants.SACN_DATA, result);
                setResult(Constants.SCAN_RESULT, intent);
            } else {
                ScanDataBean dataBean = new ScanDataBean();
                dataBean.setFunctionFlag(flag);
                dataBean.setData(result);
                dataBean.setGroundAgentCode(groundAgentCode);
                EventBus.getDefault().post(dataBean);
            }
            Tools.startShortVibrator(this);// 扫码成功 短暂   震动
        }
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LaserAndZxingBean result) {
        if (!TextUtils.isEmpty(result.getData()) && result.getTypeName().equals("scan")) {
            getBackMessage(result.getData());
        }
//        ToastUtil.showToast("扫码数据为空请重新扫码");
    }

    private boolean openedLight = false;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightStrength = event.values[0];//光线强度
            if (lightStrength < 10 && !openedLight) {
                Log.e("tagTest", "亮度==" + lightStrength + ";打开闪光灯");
                mScanView.getCameraSurface().openFlashlight();
                openedLight = true;
            } else {
                Log.e("tagTest", "亮度==" + lightStrength + ";关闭闪光灯");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * @param
     * @return
     * @method
     * @description 弹出选择框
     * @date: 2020/4/10 11:46
     * @author: 张耀
     */
    private PopupWindow windowPopList;
    private View listView;
    private RecyclerView rcList;
    private CabinStrAdapter berthAdapter;

    private void showPopList(List <String> list, String oldStr, View llClick, int flag) {
        if (list == null || list.size() == 0) {
            ToastUtil.showToast("没有可选地面代理");
            return;
        }
        if (windowPopList == null) {
            listView = getLayoutInflater().inflate(R.layout.popup_berth_list, null);
            windowPopList = new PopupWindow(listView, DisplayUtil.dp2Px(this, 80), DisplayUtil.dp2Px(this, 150));
        }
        //软键盘不会挡着popupwindow
        windowPopList.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        windowPopList.setBackgroundDrawable(getDrawable());//设置背景透明以便点击外部消失
        windowPopList.setOutsideTouchable(true);//点击外部收起
        windowPopList.setFocusable(true);
        rcList = listView.findViewById(R.id.rc_berth);
        rcList.setLayoutManager(new LinearLayoutManager(this));
        berthAdapter = new CabinStrAdapter(list);
        rcList.setAdapter(berthAdapter);
        berthAdapter.setOnItemClickListener((adapter, view, position1) -> {
            if (!list.get(position1).equals(oldStr)) {
                if (flag == 1) {
                    groundAgentCode = MainActivity.getAgentCode(list.get(position1));
                    tvAgent.setText(MainActivity.getAgentName(groundAgentCode));
                }
            }
            windowPopList.dismiss();
        });
        windowPopList.setAnimationStyle(R.style.anim_drop_down);
        windowPopList.showAsDropDown(llClick, 20, 5);
    }

    /**
     * 生成一个 透明的背景图片
     *
     * @return
     */
    private Drawable getDrawable() {
        ShapeDrawable bgdrawable = new ShapeDrawable(new OvalShape());
        bgdrawable.getPaint().setColor(getResources().getColor(android.R.color.transparent));
        return bgdrawable;
    }
}
