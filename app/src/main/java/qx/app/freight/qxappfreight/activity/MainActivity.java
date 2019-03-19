package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.constant.HttpConstant;
import qx.app.freight.qxappfreight.contract.LoginContract;
import qx.app.freight.qxappfreight.fragment.TaskFragment;
import qx.app.freight.qxappfreight.fragment.TaskPutCargoFragment;
import qx.app.freight.qxappfreight.fragment.TaskStowageFragment;
import qx.app.freight.qxappfreight.fragment.TestFragment;
import qx.app.freight.qxappfreight.presenter.LoginPresenter;
import qx.app.freight.qxappfreight.service.GPSService;
import qx.app.freight.qxappfreight.service.WebSocketService;
import qx.app.freight.qxappfreight.utils.ToastUtil;

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
    private TestFragment mTestFragment;
    private TaskPutCargoFragment mTaskStowageFragment;
    private TaskPutCargoFragment mTaskPutCargoFragment;
    private Fragment nowFragment;

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

    //, HttpConstant.WEBSOCKETURL+"userId="+ UserInfoSingle.getInstance().getUserId()+"&type=MT&role=collection"

    private void initServices() {
        GPSService.gpsStart(this);
        WebSocketService.startService(this, HttpConstant.WEBSOCKETURL+"userId="+ UserInfoSingle.getInstance().getUserId()+"&type=MT&role=collection");
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
        mTestFragment = new TestFragment();
        mTaskStowageFragment = new TaskPutCargoFragment();
        mTaskPutCargoFragment = new TaskPutCargoFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mTaskFragment)
                .add(R.id.content, mTestFragment)
                .add(R.id.content, mTaskStowageFragment)
                .add(R.id.content, mTaskPutCargoFragment)
                .commit();
        nowFragment = mTaskFragment;
        switchFragment(0, mTaskFragment);
    }

    private void switchFragment(int index, Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment)
                .commit();

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
    }

    @OnClick({R.id.ll_task, R.id.ll_flight, R.id.ll_search, R.id.ll_message, R.id.ll_mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_task:
                switchFragment(0, mTaskFragment);
                break;
            case R.id.ll_flight:
                switchFragment(1, mTestFragment);
                break;
            case R.id.ll_search:
                switchFragment(2, mTaskStowageFragment);
                break;
            case R.id.ll_message:
                switchFragment(3, mTaskPutCargoFragment);
                break;
            case R.id.ll_mine:
                switchFragment(4, mTaskPutCargoFragment);
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
        ToastUtil.showToast(this, broadString);
    }
}
