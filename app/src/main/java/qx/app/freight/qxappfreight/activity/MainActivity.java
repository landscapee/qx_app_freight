package qx.app.freight.qxappfreight.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beidouapp.imlibapi.activity.ImLibSpecialHomeFragment;
import com.qxkj.positionapp.LocationEntity;
import com.qxkj.positionapp.observer.LocationObservable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.bean.AfterHeavyExceptionBean;
import qx.app.freight.qxappfreight.bean.NFCDataEntity;
import qx.app.freight.qxappfreight.bean.ScooterConfiSingle;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.loadinglist.InstallNotifyEventBusEntity;
import qx.app.freight.qxappfreight.bean.loadinglist.NewInstallEventBusEntity;
import qx.app.freight.qxappfreight.bean.request.InstallChangeEntity;
import qx.app.freight.qxappfreight.bean.request.SeatChangeEntity;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.bean.response.ScooterConfBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketMessageBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.ScooterConfContract;
import qx.app.freight.qxappfreight.dialog.InstallSuggestPushDialog;
import qx.app.freight.qxappfreight.dialog.UpdatePushDialog;
import qx.app.freight.qxappfreight.fragment.CargoManifestFragment;
import qx.app.freight.qxappfreight.fragment.ClearStorageFragment;
import qx.app.freight.qxappfreight.fragment.DynamicFragment;
import qx.app.freight.qxappfreight.fragment.IOManifestFragment;
import qx.app.freight.qxappfreight.fragment.LnstallationFragment;
import qx.app.freight.qxappfreight.fragment.MineFragment;
import qx.app.freight.qxappfreight.fragment.TaskFragment;
import qx.app.freight.qxappfreight.fragment.TestFragment;
import qx.app.freight.qxappfreight.presenter.GetScooterConfPresenter;
import qx.app.freight.qxappfreight.reciver.MessageReciver;
import qx.app.freight.qxappfreight.reciver.ScanReceiver;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.NfcUtils;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity implements LocationObservable, ScooterConfContract.scooterConfView {
    //    @BindView(R.id.view_pager)
//    ViewPager mViewPager;
    @BindView(R.id.iv_task)
    ImageView mIvTask;
    @BindView(R.id.iv_test)
    ImageView mIvTest;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.image_message)
    ImageView mIvMessgae;
    @BindView(R.id.image_mine)
    ImageView mIvMine;

    @BindView(R.id.tv_task)
    TextView mTvTask;
    @BindView(R.id.tv_test)
    TextView mTvTest;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.tv_message)
    TextView mTvMessge;
    @BindView(R.id.tv_mine)
    TextView mTvMine;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;//第三个tab


//    List <LockEventbusEntity> pushList = new ArrayList <>();

    private Fragment nowFragment;
    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment fragment3;
    private Fragment fragment4;
    private Fragment fragment5;

    private MessageReciver mMessageReciver;//聊天消息广播接收器
    private ScreenStateReciver mScreenStateReciver;

    private ScanReceiver ScanReceiver; //激光扫码 广播接受

    private boolean isJunctionLoad = false;//是否是结载角色

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int wakeFlag) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        initServices();
        setToolbarShow(View.GONE);

        mMessageReciver = new MessageReciver(this);
        IntentFilter filter3 = new IntentFilter(Constants.IMLIB_BROADCAST_CHAT_NEWMESSAGE);
        registerReceiver(mMessageReciver, filter3);

//        if(Build.VERSION.SDK_INT >= 26){
            initBoardReceiver();
//        }
//        mScreenStateReciver = new ScreenStateReciver();
//        IntentFilter screenfilter = new IntentFilter(Intent.ACTION_USER_PRESENT);
//        registerReceiver(mScreenStateReciver, screenfilter);
//        mTaskFragment = new TaskFragment();
//        mDynamicFragment = new DynamicFragment();
//        mCSFragment = new ClearStorageFragment();
//        testFragment = new TestFragment();
//        mMineFragment = new MineFragment();

//        new NfcUtils(this);// 初始化 NFC 工具
        //结载角色修改 底部tab 第二和第三项 货邮舱单 装机单
        isJunctionLoad = false;
        for (LoginResponseBean.RoleRSBean roleRSBean : UserInfoSingle.getInstance().getRoleRS()) {
            if (Constants.JUNCTION_LOAD.equals(roleRSBean.getRoleCode())) {
                isJunctionLoad = true;
                break;
            }
        }

        if (isJunctionLoad) {
            fragment1 = new TaskFragment();
            fragment2 = new CargoManifestFragment();
            fragment3 = new LnstallationFragment();
            if (MyApplication.isNeedIm && Tools.isProduct())
                fragment4 = new ImLibSpecialHomeFragment();
            else
                fragment4 = new TestFragment();
            fragment5 = new MineFragment();
        } else {
            fragment1 = new TaskFragment();
            fragment2 = new DynamicFragment();
            fragment3 = new ClearStorageFragment();
            if (MyApplication.isNeedIm && Tools.isProduct())
                fragment4 = new ImLibSpecialHomeFragment();
            else
                fragment4 = new IOManifestFragment();
            fragment5 = new MineFragment();
        }
        initFragment();
    }
    /**
     * NFC 数据接收
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //当该Activity接收到NFC标签时，运行该方法
        //调用工具方法，读取NFC数据
//        try {
//            String str = NfcUtils.readNFCFromTag(intent);
//            if (!StringUtil.isEmpty(str) && str.length() >= 7) {
//                String scooterCode = str.substring(3);
//                NFCDataEntity nfcDataEntity = new NFCDataEntity();
//                nfcDataEntity.setScooterCode(scooterCode);
//                EventBus.getDefault().post(nfcDataEntity);
//            }
////            Toast.makeText(this,"读取到的数据是："+str,Toast.LENGTH_LONG).show();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        NfcUtils.mNfcAdapter.enableForegroundDispatch(this, NfcUtils.mPendingIntent, NfcUtils.mIntentFilter, NfcUtils.mTechList);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        NfcUtils.mNfcAdapter.disableForegroundDispatch(this);
    }
    /**
     * 获取 板车基础配置
     */
    private void getScooterConf() {
        mPresenter = new GetScooterConfPresenter(this);
        ((GetScooterConfPresenter) mPresenter).getScooterConf("0");

    }


    private void initServices() {
        //开启定位服务
//        GPSService.startGPSService(this);
        qx.app.freight.qxappfreight.service.GPSService.gpsStart(this);
        WebSocketService.startService(this);
    }


    private void initFragment() {
//        PagerAdapter pagerAdapter = new PagerAdapter(this, getSupportFragmentManager());
//        mViewPager.setAdapter(pagerAdapter);
//        mViewPager.setOffscreenPageLimit(2);
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                switchFragment(i);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//            }
//        });
//        mViewPager.setCurrentItem(0);
//        switchFragment(mViewPager.getCurrentItem());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, fragment1)
                .add(R.id.content, fragment2)
                .add(R.id.content, fragment3)
                .add(R.id.content, fragment4)
                .add(R.id.content, fragment5)
                .commitAllowingStateLoss();
        nowFragment = fragment1;
        switchFragment(0, fragment1);
    }

    //    public void setDeviceIdentify(String deviceIdentify) {
//        if (deviceIdentify != null && !"null".equals(deviceIdentify) && !"".equals(deviceIdentify)) {
//            ImLibConstants.IMLIB_DEVICE_IDENTIFY = deviceIdentify;
//        } else {
//            Toast.makeText(c, "必须设置设备标识码", 1).show();
//        }
//    }
    private void switchFragment(int index, Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .hide(fragment1)
                .hide(fragment2)
                .hide(fragment3)
                .hide(fragment4)
                .hide(fragment5);


        nowFragment = fragment; //替换当前fragment
        if (isJunctionLoad) {
            mIvTest.setImageResource(R.mipmap.mainfest);
            mTvTest.setTextColor(getResources().getColor(R.color.main_tv_normal));
            mTvTest.setText("货邮舱单");
            mIvSearch.setImageResource(R.mipmap.load_list);
            mTvSearch.setTextColor(getResources().getColor(R.color.main_tv_normal));
            mTvSearch.setText("装机单");
        } else {
            //判断 是否需要显示 清库 tab
            int rw = 0;
            if (UserInfoSingle.getInstance().getRoleRS() != null && UserInfoSingle.getInstance().getRoleRS().size() != 0) {
                for (int i = 0; i < UserInfoSingle.getInstance().getRoleRS().size(); i++) {
                    if (Constants.INPORTTALLY.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                        rw = 1;
                        break;
                    }
                }
            }
            if (rw == 1) {
                llSearch.setVisibility(View.VISIBLE);
            } else {
                llSearch.setVisibility(View.GONE);
            }
            mIvTest.setImageResource(R.mipmap.dynamics_normal);
            mTvTest.setTextColor(getResources().getColor(R.color.main_tv_normal));
            mTvTest.setText("航班动态");
            mIvSearch.setImageResource(R.mipmap.clear_normal);
            mTvSearch.setTextColor(getResources().getColor(R.color.main_tv_normal));
            mTvSearch.setText("清库");
        }
        mIvTask.setImageResource(R.mipmap.backlog_normal);
        mTvTask.setTextColor(getResources().getColor(R.color.main_tv_normal));
        if (MyApplication.isNeedIm && Tools.isProduct()) {
            mIvMessgae.setImageResource(R.mipmap.news_normal);
            mTvMessge.setTextColor(getResources().getColor(R.color.main_tv_normal));
            mTvMessge.setText("消息");
        } else {
            mIvMessgae.setImageResource(R.mipmap.warehouse_press);
            mTvMessge.setTextColor(getResources().getColor(R.color.main_tv_normal));
            mTvMessge.setText("库房管理");

        }
        mIvMine.setImageResource(R.mipmap.my_normal);
        mTvMine.setTextColor(getResources().getColor(R.color.main_tv_normal));
        switch (index) {
            case 0:
                mIvTask.setImageResource(R.mipmap.backlog_selected);
                mTvTask.setTextColor(getResources().getColor(R.color.main_tv_press));
                break;
            case 1:
                mTvTest.setTextColor(getResources().getColor(R.color.main_tv_press));
                if (isJunctionLoad) {
                    mIvTest.setImageResource(R.mipmap.mainfest_press);
                } else {
                    mIvTest.setImageResource(R.mipmap.dynamics_selected);
                }
                break;
            case 2:
                mTvSearch.setTextColor(getResources().getColor(R.color.main_tv_press));
                if (isJunctionLoad) {
                    mIvSearch.setImageResource(R.mipmap.load_list_press);
                } else {
                    mIvSearch.setImageResource(R.mipmap.clear_selected);
                }
                break;
            case 3:
                if (MyApplication.isNeedIm && Tools.isProduct()) {
                    mIvMessgae.setImageResource(R.mipmap.news_selected);
                    mTvMessge.setTextColor(getResources().getColor(R.color.main_tv_press));
                } else {
                    mIvMessgae.setImageResource(R.mipmap.warehouse);
                    mTvMessge.setTextColor(getResources().getColor(R.color.main_tv_press));

                }

                break;
            case 4:
                mIvMine.setImageResource(R.mipmap.my_selected);
                mTvMine.setTextColor(getResources().getColor(R.color.main_tv_press));
                break;
        }
        transaction.show(nowFragment).commit();

    }

    @OnClick({R.id.ll_task, R.id.ll_flight, R.id.ll_search, R.id.ll_message, R.id.ll_mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_task:
                switchFragment(0, fragment1);
                break;
            case R.id.ll_flight:
                switchFragment(1, fragment2);
                break;
            case R.id.ll_search:
                switchFragment(2, fragment3);
                break;
            case R.id.ll_message:
                switchFragment(3, fragment4);
                break;
            case R.id.ll_mine:
                switchFragment(4, fragment5);
                break;
        }

    }

//    class PagerAdapter extends FragmentPagerAdapter {
//        PagerAdapter(Context context, FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//            Fragment fragment = null;
//            switch (i) {
//                case 0:
//                    fragment = new TaskFragment();
//                    break;
//                case 1:
//                    fragment = new TestFragment();
//                    break;
//                case 2:
//                    fragment = new TaskStowageFragment();
//                    break;
//            }
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//    }

    /**
     * android 8  版本（包含） 以上 动态注册广播Action 才能收到隐士广播
     */
    private void initBoardReceiver() {
        ScanReceiver = new ScanReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION);
        registerReceiver(ScanReceiver, filter);
    }
    @Override
    public void onBackPressed() {
//        quitApp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        GetIdUtil.getSingleInstance().unRegisterIfAready(this);
        try {
            unregisterReceiver(mMessageReciver);
            if (ScanReceiver!=null)
                unregisterReceiver(ScanReceiver);
//            unregisterReceiver(mScreenStateReciver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveLocationUpdate(LocationEntity locationEntity) {
        if (locationEntity != null)
            ToastUtil.showToast("经度:" + locationEntity.getLongitude() + "纬度:" + locationEntity.getLatitude());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SeatChangeEntity result) {
//        if (!(result.getRemark()==null || result.getRemark().contains("CTOT") ||result.getRemark().contains("机位"))){
//            Log.e("dialog：","222222222222");
        UpdatePushDialog updatePushDialog = new UpdatePushDialog(this, R.style.custom_dialog, result.getRemark(), () -> {
        });
        updatePushDialog.show();
//        }
        EventBus.getDefault().post("refresh_data_update");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(InstallChangeEntity result) {
        String remark = result.getFlightNo();
        if (remark != null && !StringUtil.isEmpty(remark)) {
            UpdatePushDialog updatePushDialogInstall = new UpdatePushDialog(this, R.style.custom_dialog, remark, () -> {
            });
            updatePushDialogInstall.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(InstallNotifyEventBusEntity result) {
        String remark = "";
        if (result.getType() == 2) {
            remark = result.getFlightNo() + "监装已确认按此装机，版本：" + result.getVersion();
        } else if (result.getType() == 4) {
            remark = result.getFlightNo() + "监装已确认最终装机单，版本：" + result.getVersion();
        }
        if (remark != null && !StringUtil.isEmpty(remark)) {
            UpdatePushDialog updatePushDialogInstall = new UpdatePushDialog(this, R.style.custom_dialog, remark, () -> {
                EventBus.getDefault().post("LoadInstall_Sure_Update");
            });
            updatePushDialogInstall.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AfterHeavyExceptionBean result) {
        String remark = "航班" + result.getFlightNo() + "出现复重异常, 异常板车" + result.getScooter() + "，请查看！";
        if (remark != null && !StringUtil.isEmpty(remark)) {
            UpdatePushDialog updatePushDialogInstall = new UpdatePushDialog(this, R.style.custom_dialog, remark, () -> {
            });
            updatePushDialogInstall.show();
        }
    }
    /**
     * 消息提醒推送
     *
     * @param webSocketMessageBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebSocketMessageBean webSocketMessageBean) {
        if (webSocketMessageBean.getSpecialFlag() == 1) {
            String remark = webSocketMessageBean.getContent();
            if (remark != null && !StringUtil.isEmpty(remark)) {
                UpdatePushDialog updatePushDialogInstall = new UpdatePushDialog(this, R.style.custom_dialog, remark, () -> {
                });
                updatePushDialogInstall.show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NewInstallEventBusEntity result) {
        List <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> scooters = new ArrayList <>();
        List <LoadingListBean.DataBean.ContentObjectBean> objectBeans = result.getBeans();
        if (objectBeans != null && objectBeans.size() > 0) {
            for (LoadingListBean.DataBean.ContentObjectBean mContentObjectBean : objectBeans) {
                scooters.addAll(mContentObjectBean.getScooters());
            }
            InstallSuggestPushDialog updatePushDialog = new InstallSuggestPushDialog(this, R.style.custom_dialog, scooters, objectBeans.get(0).getFlightNo(), () -> {
                EventBus.getDefault().post("LoadInstall_Sure_Update");
                EventBus.getDefault().post("refresh_data_update");
            });

            updatePushDialog.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ScooterConfiSingle.getInstance().isEmpty()) {
            Log.e("MainActivity", "板车基础信息为空,从服务器请求板车基础数据");
            getScooterConf();
        }

    }

    @Override
    public void getScooterConfResult(List <ScooterConfBean.ScooterConf> result) {
        if (result != null && result.size() > 0) {
            HashMap <String, String> scooterMap = new HashMap <>();
            for (ScooterConfBean.ScooterConf scooterConf : result) {
                scooterMap.put(scooterConf.getValue(), scooterConf.getName());
            }
            if (!scooterMap.isEmpty())
                ScooterConfiSingle.setScooterMap(scooterMap);
        }

    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(LockEventbusEntity result) {
//       if (pushList!=null&&result!=null){
//           pushList.add(result);
//       }
//    }
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    LockEventbusEntity push = (LockEventbusEntity) msg.obj;
//                    if (push != null){//根据 flag 分发未解锁 收到的任务
//                        EventBus.getDefault().post(push.getCommonJson4List());
//                    }
//                    break;
//            }
//        }
//    };
//    /**
//     * 读取消息队列里面的消息并发送给handler处理
//     *
//     * @param pushList
//     */
//    private void checkPush(List <LockEventbusEntity> pushList) {
//        if (pushList != null && pushList.size() != 0) {
//            for (int i = 0; i < pushList.size(); i++) {
//                mHandler.obtainMessage(1, pushList.get(i)).sendToTarget();
//            }
//        }
//    }

    /**
     * 屏幕监听
     */
    class ScreenStateReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                Log.e("屏幕监听:", "屏幕亮了");
            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                Log.e("屏幕监听:", "屏幕黑了");
            } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                Log.e("屏幕监听:", "屏幕解锁了");
//                checkPush(pushList);//解锁后去读取消息队列的推送列表
            }
        }
    }
}

