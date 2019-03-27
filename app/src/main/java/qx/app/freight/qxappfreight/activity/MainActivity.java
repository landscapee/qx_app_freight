package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidouapp.imlibapi.activity.ImLibSpecialHomeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.constant.HttpConstant;
import qx.app.freight.qxappfreight.fragment.DynamicFragment;
import qx.app.freight.qxappfreight.fragment.MineFragment;
import qx.app.freight.qxappfreight.fragment.TaskFragment;
import qx.app.freight.qxappfreight.fragment.TaskPutCargoFragment;
import qx.app.freight.qxappfreight.service.GPSService;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.IMUtils;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity {
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
    private ImLibSpecialHomeFragment mIMFragment;
    private TaskPutCargoFragment mTaskPutCargoFragment;
    private MineFragment mMineFragment;
    private Fragment nowFragment;

    private int taskAssignType = 0;

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
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        initServices();
        setToolbarShow(View.GONE);
        initFragment();
    }


    private void initServices() {
        GPSService.gpsStart(this);
        //根据登录返回的
        List<String> ary = Arrays.asList("cargoAgency", "receive", "securityCheck", "collection", "charge");
        if (UserInfoSingle.getInstance().getRoleRS() != null && UserInfoSingle.getInstance().getRoleRS().size()>0){
            if (ary.contains(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode())) {
                taskAssignType = 1;
            } else if ("delivery_in".equals(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode())) {
                taskAssignType = 3;
            } else
                taskAssignType = 2;
            WebSocketService.startService(this, HttpConstant.WEBSOCKETURL
                    + "userId=" + UserInfoSingle.getInstance().getUserId()
                    + "&taskAssignType=" + taskAssignType
                    + "&type=MT"
                    + "&role=" + UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
        }

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

        mTaskFragment = new TaskFragment();
        mDynamicFragment = new DynamicFragment();
        mIMFragment = new ImLibSpecialHomeFragment();
        mTaskPutCargoFragment = new TaskPutCargoFragment();
        mMineFragment = new MineFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mTaskFragment)
                .add(R.id.content, mDynamicFragment)
                .add(R.id.content, mIMFragment)
                .add(R.id.content, mTaskPutCargoFragment)
                .add(R.id.content, mMineFragment)
                .commit();
        nowFragment = mTaskFragment;
        switchFragment(0, mTaskFragment);
//        IMLIBContext.getInstance().setDeviceIdentify(DeviceInfoUtil.getIMEI(this));
        IMUtils.imLibLogin("lizhong", "李忠", "eyJhbGciOiJIUzI1NiJ9.eyJjcmVhdGVfdGltZSI6MTU1MzUwMTUwMDk1MCwidXNlcl9pbmZvIjoie1wiZGVwdENvZGVcIjpcImNzZ2xcIixcImRlcHRJZFwiOlwiN2IzMTZjYjhjMTgxNDhiOGFiYTUxNmRlODVmNzZlYWVcIixcImlkXCI6XCI2MjQwNjg4NzBjMGM0ZGNiOTUyYTRkNDAyZjdjZDg5N1wiLFwibG9naW5OYW1lXCI6XCJsaXpob25nXCIsXCJuYW1lXCI6XCLmnY7lv6BcIixcInJvbGVzXCI6W3tcImNvZGVcIjpcImdyb3VwX2xlYWRlclwiLFwiaWRcIjpcImYzZmEwNmM2ZmU3MDRhOTRiZWIxYzlmMDMxMjYyNDdhXCJ9LHtcImNvZGVcIjpcIlN5c3RlbU1hbmFnZXJcIixcImlkXCI6XCJTeXN0ZW1NYW5hZ2VyXCJ9LHtcImNvZGVcIjpcImFsbF9yZXBvcnRcIixcImlkXCI6XCI1ZWQ3OWUyY2NmMWQ0MWJhYTRhNTE3Nzg1MDdiMjFiN1wifSx7XCJjb2RlXCI6XCJBT0NfUkVBRFwiLFwiaWRcIjpcIjUyMmM3ODY5NjJkNzQzNGJhN2VhY2FmOTM2YjMzYzQ3XCJ9LHtcImNvZGVcIjpcIkFPQ19TRlwiLFwiaWRcIjpcIjExNDA5ZDhkODU1NjQ4NTRiZTk4ZTQxY2Y5MTAzZmY2XCJ9LHtcImNvZGVcIjpcIkFPQ19DWVwiLFwiaWRcIjpcIjg1ZmJiNjQ1NDA2OTQ4NzRiZGU3NDFjYjU3MjE2ODE5XCJ9LHtcImNvZGVcIjpcIkFPQ19XUklURVwiLFwiaWRcIjpcImY3NTdhYmQxNmExZDRkNzNhMTU2YmU0MjZmMmIzMmJlXCJ9LHtcImNvZGVcIjpcImRlcHRNYW5hZ2VyXCIsXCJpZFwiOlwiZGVwdE1hbmFnZXJcIn0se1wiY29kZVwiOlwiMVwiLFwiaWRcIjpcImFkODI4MjgwZDI4MzRjNzI4ODkxMmZjY2VlOTYyNTg0XCJ9LHtcImNvZGVcIjpcIkFQUEhUXCIsXCJpZFwiOlwiYjUwMTQ5NTEwODMxNDhlN2IzY2E3NjY5MjRjNzFiNTVcIn1dLFwic3RhdGVcIjpcIjFcIn0iLCJ1c2VyX25hbWUiOiLmnY7lv6AiLCJ1c2VyX2tleSI6IjQyODk5ODU0YmU2ZGRlYTA4OTVlNjMwNGYzMTE5OGQ2IiwidGltZW91dCI6Mjg4MDB9.uCx9MCGIfESaeKy5z4DnS70nfMz6fRWAGl52i2hJR5w");
    }

    //    public void setDeviceIdentify(String deviceIdentify) {
//        if (deviceIdentify != null && !"null".equals(deviceIdentify) && !"".equals(deviceIdentify)) {
//            ImLibConstants.IMLIB_DEVICE_IDENTIFY = deviceIdentify;
//        } else {
//            Toast.makeText(c, "必须设置设备标识码", 1).show();
//        }
//
//    }
    private void switchFragment(int index, Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .hide(mTaskFragment)
                .hide(mDynamicFragment)
                .hide(mIMFragment)
                .hide(mTaskPutCargoFragment)
                .hide(mMineFragment);

        nowFragment = fragment; //替换当前fragment

        mIvTask.setImageResource(R.mipmap.backlog_normal);
        mTvTask.setTextColor(getResources().getColor(R.color.main_tv_normal));
        mIvTest.setImageResource(R.mipmap.dynamics_normal);
        mTvTest.setTextColor(getResources().getColor(R.color.main_tv_normal));
        mIvSearch.setImageResource(R.mipmap.statistics_normal);
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
                mIvSearch.setImageResource(R.mipmap.statistics_selected);
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
                switchFragment(2, mTaskPutCargoFragment);
                break;
            case R.id.ll_message:
                switchFragment(3, mIMFragment);
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

    /**
     * 接收激光扫码广播内容， 进行业务处理。
     *
     * @param broadString 广播内容
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecBroad(String broadString) {
//        ToastUtil.showToast(this, broadString);
    }

    @Override
    public void onBackPressed() {
        quitApp();
    }
}
