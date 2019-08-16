package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidouapp.imlibapi.IMLIBContext;
import com.beidouapp.imlibapi.activity.ImLibSpecialHomeFragment;
import com.qxkj.positionapp.GPSService;
import com.qxkj.positionapp.LocationEntity;
import com.qxkj.positionapp.observer.LocationObservable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.SeatChangeEntity;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.dialog.UpdatePushDialog;
import qx.app.freight.qxappfreight.fragment.CargoManifestFragment;
import qx.app.freight.qxappfreight.fragment.ClearStorageFragment;
import qx.app.freight.qxappfreight.fragment.DynamicFragment;
import qx.app.freight.qxappfreight.fragment.LnstallationFragment;
import qx.app.freight.qxappfreight.fragment.MineFragment;
import qx.app.freight.qxappfreight.fragment.TaskFragment;
import qx.app.freight.qxappfreight.fragment.TestFragment;
import qx.app.freight.qxappfreight.reciver.MessageReciver;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity implements LocationObservable {
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


    private Fragment mTaskFragment;
    private Fragment mDynamicFragment;
    private Fragment mCSFragment;
    private Fragment mMineFragment;
    private Fragment nowFragment;
    private Fragment testFragment;
    private Fragment lnstallationFragment;
    private Fragment cargoManifestFragment;
    private int taskAssignType = 0;
    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment fragment3;
    private Fragment fragment4;
    private Fragment fragment5;

    private MessageReciver mMessageReciver;//聊天消息广播接收器

    private boolean isJunctionLoad = false;//是否是结载角色

    public static void startActivity(Context context) {
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
//        mTaskFragment = new TaskFragment();
//        mDynamicFragment = new DynamicFragment();
//        mCSFragment = new ClearStorageFragment();
//        testFragment = new TestFragment();
//        mMineFragment = new MineFragment();
        //结载角色修改 底部tab 第二和第三项 货邮舱单 装机单
        isJunctionLoad = Constants.JUNCTION_LOAD.equals(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
        if(isJunctionLoad){
            fragment1 =  new TaskFragment();
            fragment2 = new CargoManifestFragment();
            fragment3 =  new LnstallationFragment();
            fragment4 = new TestFragment();
            fragment5 = new MineFragment();
        }
        else
        {
            fragment1 =  new TaskFragment();
            fragment2 =  new DynamicFragment();
            fragment3 = new ClearStorageFragment();
            if (MyApplication.isNeedIm && Tools.isProduct())
                fragment4 = new ImLibSpecialHomeFragment();
            else
                fragment4 = new TestFragment();
            fragment5 = new MineFragment();
        }
        initFragment();

    }


    private void initServices() {
        //开启定位服务
        GPSService.startGPSService(this);
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
                .commit();
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
        if (isJunctionLoad){
            mIvTest.setImageResource(R.mipmap.mainfest);
            mTvTest.setTextColor(getResources().getColor(R.color.main_tv_normal));
            mTvTest.setText("货邮舱单");
            mIvSearch.setImageResource(R.mipmap.load_list);
            mTvSearch.setTextColor(getResources().getColor(R.color.main_tv_normal));
            mTvSearch.setText("装机单");
        }
        else {
            mIvTest.setImageResource(R.mipmap.dynamics_normal);
            mTvTest.setTextColor(getResources().getColor(R.color.main_tv_normal));
            mTvTest.setText("航班动态");
            mIvSearch.setImageResource(R.mipmap.clear_normal);
            mTvSearch.setTextColor(getResources().getColor(R.color.main_tv_normal));
            mTvSearch.setText("清库");
        }
        mIvTask.setImageResource(R.mipmap.backlog_normal);
        mTvTask.setTextColor(getResources().getColor(R.color.main_tv_normal));
        mIvMessgae.setImageResource(R.mipmap.news_normal);
        mTvMessge.setTextColor(getResources().getColor(R.color.main_tv_normal));
        mIvMine.setImageResource(R.mipmap.my_normal);
        mTvMine.setTextColor(getResources().getColor(R.color.main_tv_normal));
        switch (index) {
            case 0:
                mIvTask.setImageResource(R.mipmap.backlog_selected);
                mTvTask.setTextColor(getResources().getColor(R.color.main_tv_press));
                break;
            case 1:
                mTvTest.setTextColor(getResources().getColor(R.color.main_tv_press));
                if (isJunctionLoad){
                    mIvTest.setImageResource(R.mipmap.mainfest_press);
                }
                else {
                    mIvTest.setImageResource(R.mipmap.dynamics_selected);
                }
                break;
            case 2:
                mTvSearch.setTextColor(getResources().getColor(R.color.main_tv_press));
                if (isJunctionLoad){
                    mIvSearch.setImageResource(R.mipmap.load_list_press);
                }
                else {
                    mIvSearch.setImageResource(R.mipmap.clear_selected);
                }
                break;
            case 3:
                mIvMessgae.setImageResource(R.mipmap.news_selected);
                mTvMessge.setTextColor(getResources().getColor(R.color.main_tv_press));
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

    @Override
    public void onBackPressed() {
        quitApp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        GetIdUtil.getSingleInstance().unRegisterIfAready(this);
        try {
            unregisterReceiver(mMessageReciver);
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
        if (result.getRemark().contains("机位变更")){
//            UpdatePushDialog updatePushDialog = new UpdatePushDialog(this, R.style.custom_dialog,result.getRemark(),null, s -> {
//                EventBus.getDefault().post("refresh_data_update");
//            });
//            updatePushDialog.show();
        }

    }
}

