package qx.app.freight.qxappfreight.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.ScanManagerActivity;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

public class TaskDoneFragment extends BaseFragment {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    CustomToolbar mToolBar;
    @BindView(R.id.search_toolbar)
    SearchToolbar mSearchBar;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    private String nowRoleCode; //当前角色code
    private List<Fragment> fragmentList;
    private List<String> list_Title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        unbinder = ButterKnife.bind(this, view);
        mToolBar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> getActivity().finish());
        mToolBar.setRightIconView(View.VISIBLE, R.mipmap.search, v -> {
            mToolBar.setVisibility(View.GONE);
            mSearchBar.setVisibility(View.VISIBLE);
            // 向左边移入
            mSearchBar.setAnimation(AnimationUtils.makeInAnimation(getContext(), true));
            // 向右边移出
            mToolBar.setAnimation(AnimationUtils.makeOutAnimation(getContext(), true));
        });
        mSearchBar.setVisibility(View.GONE);
            mSearchBar.getCloseView().setOnClickListener(v->{
                mSearchBar.getSearchView().setText("");

                    mToolBar.setVisibility(View.VISIBLE);
                    mSearchBar.setVisibility(View.GONE);
                    // 向左边移入
                    mToolBar.setAnimation(AnimationUtils.makeInAnimation(getContext(), false));
                    // 向右边移出
                    mSearchBar.setAnimation(AnimationUtils.makeOutAnimation(getContext(), false));
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);


            });
        return view;
    }
    public SearchToolbar getSearchView(){
        return mSearchBar;
    }
    public CustomToolbar getToolbar(){
        return mToolBar;
    }
    private void gotoScan() {
        if (TextUtils.isEmpty(nowRoleCode)){
            return;
        }

        ScanManagerActivity.startActivity(getContext(),"MainActivity");
//        switch (nowRoleCode){
//            case "复重":
//                Intent intent = new Intent(mContext, ChooseWeighScanActivity.class);
//                mContext.startActivity(intent);
//                break;
////            case "":
////                break;
//            default:
////                ToastUtil.showToast(getContext(), "扫码");
//                ScanManagerActivity.startActivity(getContext(),"MainActivity");
//        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    /**
     * 设置中间文字显示
     *
     * @param size 待办数
     */
    public void setTitleText(int size) {
        mToolBar.setMainTitle(Color.WHITE, "我的已办（" + size + "）");
    }

    public void goneTitle() {
        mToolBar.setVisibility(View.GONE);
    }

    private void initView() {
        fragmentList = new ArrayList<>();
        list_Title = new ArrayList<>();
        showFragmentForRole();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                nowRoleCode = list_Title.get(i);
                if (View.VISIBLE ==mSearchBar.getVisibility()){
                    mSearchBar.getSearchView().setText("");
                    mToolBar.setVisibility(View.VISIBLE);
                    mSearchBar.setVisibility(View.GONE);
                    // 向左边移入
                    mToolBar.setAnimation(AnimationUtils.makeInAnimation(getContext(), false));
                    // 向右边移出
                    mSearchBar.setAnimation(AnimationUtils.makeOutAnimation(getContext(), false));
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mSearchBar.getWindowToken(), 0);
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void showFragmentForRole() {
        if (UserInfoSingle.getInstance().getRoleRS() == null) {
            ToastUtil.showToast(mContext, "该用户未被分配任何角色");
            return;
        }
        for (int i = 0; i < UserInfoSingle.getInstance().getRoleRS().size(); i++) {
            if (Constants.RECEIVE.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                fragmentList.add(new TaskCollectVerifyFragment());
                list_Title.add("收验");
            } else if (Constants.COLLECTION.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                fragmentList.add(new CollectorFragment());
                list_Title.add("收运");
            } else if (Constants.PREPLANER.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                fragmentList.add(new TaskStowageFragment());
                list_Title.add("组板");

            } else if (Constants.WEIGHTER.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                fragmentList.add(new AllocateVehiclesFragment());
                list_Title.add("复重");
            }
//            else if (Constants.DRIVERIN.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
//                //登录不成功的时候 手动添加 user
////        LoginResponseBean loginResponseBean = new LoginResponseBean();
////        loginResponseBean.setUserId("ud8eecd98a3ea4e7aaa2f24ab2808680e");
////        UserInfoSingle.setUser(loginResponseBean);
//                fragmentList.add(new DriverInFragment());
//                list_Title.add("内场司机");
//                goneTitle();
//            }
            else if (Constants.DRIVEROUT.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                fragmentList.add(new TaskDriverOutDoneFragment());
                list_Title.add("外场运输");
            } else if (Constants.INSTALL_UNLOAD_EQUIP.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                fragmentList.add(new InstallEquipDoneFragment());
                list_Title.add("装卸机");
            }else if (Constants.INSTALL_EQUIP_LEADER.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                fragmentList.add(new InstallEquipLeaderDoneFragment());// 装卸员小组长任务列表Fragment
                list_Title.add("装卸机");
            } else if (Constants.INPORTDELIVERY.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                fragmentList.add(new InPortDeliveryFragment());
                list_Title.add("进港提货");
            } else if (Constants.INPORTTALLY.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                fragmentList.add(new InPortTallyFragment());
                list_Title.add("进港分拣");
            } else if (Constants.PORTER.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                fragmentList.add(new FlightListBaggerDoneFragment());
                list_Title.add("行李");
            } else if(Constants.JUNCTION_LOAD.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())){
                fragmentList.add(new JunctionLoadDoneFragment());
                list_Title.add("结载");
            }else if(Constants.INTERNATIONAL_GOODS.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())){
                fragmentList.add(new CargoDoneFragment());
                list_Title.add("货物");
            }

        }

        if(list_Title.size() > 0){
            nowRoleCode = list_Title.get(0);
            //如果第一个是外场运输就把搜索框隐藏
            if (list_Title.get(0).equals("外场运输")){
                mToolBar.setRightIconViewVisiable(false);
            }
        }
        else
            ToastUtil.showToast("该用户没有被分配角色");


        if (fragmentList.size() == 1)
            mTabLayout.setVisibility(View.GONE);
        else
            mTabLayout.setVisibility(View.VISIBLE);
        mViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), getContext(), fragmentList, list_Title));
        mTabLayout.setupWithViewPager(mViewPager);//此方法就是让tablayout和ViewPager联动

    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;
        private List<String> list_Title;

        public MyPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList, List<String> list_Title) {
            super(fm);
            this.fragmentList = fragmentList;
            this.list_Title = list_Title;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return list_Title.size();
        }

        /**
         * //此方法用来显示tab上的名字
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return list_Title.get(position);
        }
    }
}
