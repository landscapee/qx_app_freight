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

import com.qxkj.positionapp.GPSService;
import com.qxkj.positionapp.LocationEntity;
import com.qxkj.positionapp.observer.LocationObservable;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.fragment.ClearStorageFragment;
import qx.app.freight.qxappfreight.fragment.DynamicFragment;
import qx.app.freight.qxappfreight.fragment.MineFragment;
import qx.app.freight.qxappfreight.fragment.TaskFragment;
import qx.app.freight.qxappfreight.fragment.TestFragment;
import qx.app.freight.qxappfreight.reciver.MessageReciver;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.ToastUtil;

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


    private TaskFragment mTaskFragment;
    private DynamicFragment mDynamicFragment;
    private ClearStorageFragment mCSFragment;
    //    private TaskPutCargoFragment mTaskPutCargoFragment;
    private MineFragment mMineFragment;
    private Fragment nowFragment;
    private TestFragment testFragment;

    private int taskAssignType = 0;
    private MessageReciver mMessageReciver;//聊天消息广播接收器

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
        initServices();
        setToolbarShow(View.GONE);
        mMessageReciver = new MessageReciver(this);
        IntentFilter filter3 = new IntentFilter(Constants.IMLIB_BROADCAST_CHAT_NEWMESSAGE);
        registerReceiver(mMessageReciver, filter3);
        initFragment();
    }


    private void initServices() {
        //开启定位服务
        GPSService.startGPSService(this);
        WebSocketService.startService(this);
    }


    private void initFragment() {

        mTaskFragment = new TaskFragment();
        mDynamicFragment = new DynamicFragment();
        mCSFragment = new ClearStorageFragment();
        testFragment = new TestFragment();
        mMineFragment = new MineFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mTaskFragment)
                .add(R.id.content, mDynamicFragment)
                .add(R.id.content, mCSFragment)
                .add(R.id.content, testFragment)
//                .add(R.id.content, mTaskPutCargoFragment)
                .add(R.id.content, mMineFragment)
                .commit();
        nowFragment = mTaskFragment;

        switchFragment(0, mTaskFragment);
    }

    private void switchFragment(int index, Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .hide(mTaskFragment)
                .hide(mDynamicFragment)
                .hide(mCSFragment)
                .hide(testFragment)
//                .hide(mTaskPutCargoFragment)
                .hide(mMineFragment);

        nowFragment = fragment; //替换当前fragment

        mIvTask.setImageResource(R.mipmap.backlog_normal);
        mTvTask.setTextColor(getResources().getColor(R.color.main_tv_normal));
        mIvTest.setImageResource(R.mipmap.dynamics_normal);
        mTvTest.setTextColor(getResources().getColor(R.color.main_tv_normal));
        mIvSearch.setImageResource(R.mipmap.clear_normal);
        mTvSearch.setTextColor(getResources().getColor(R.color.main_tv_normal));
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
                mIvTest.setImageResource(R.mipmap.dynamics_selected);
                mTvTest.setTextColor(getResources().getColor(R.color.main_tv_press));
                break;
            case 2:
                mIvSearch.setImageResource(R.mipmap.clear_selected);
                mTvSearch.setTextColor(getResources().getColor(R.color.main_tv_press));
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
                switchFragment(0, mTaskFragment);
                break;
            case R.id.ll_flight:
                switchFragment(1, mDynamicFragment);
                break;
            case R.id.ll_search:
                switchFragment(2, mCSFragment);
                break;
            case R.id.ll_message:
                switchFragment(3, testFragment);
                break;
            case R.id.ll_mine:
                switchFragment(4, mMineFragment);
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
}
