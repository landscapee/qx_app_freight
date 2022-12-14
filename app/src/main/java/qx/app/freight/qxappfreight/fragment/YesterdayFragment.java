package qx.app.freight.qxappfreight.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightBean;
import qx.app.freight.qxappfreight.contract.FlightdynamicContract;
import qx.app.freight.qxappfreight.presenter.FlightdynamicPresenter;

public class YesterdayFragment extends BaseFragment implements FlightdynamicContract.flightdynamicView {
    @BindView(R.id.magic_indicator_yest)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager_yest)
    ViewPager viewPager;


    private List<BaseFragment> mListFragment;
    private MyTabAdapter tabAdapter;
    private MyPageAdapter pageAdapter;
    private List<String> mData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yesterday, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mData = new ArrayList<>();
        mListFragment = new ArrayList<>();
        //??????????????????
        mPresenter = new FlightdynamicPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setDay("yesterday");
        ((FlightdynamicPresenter) mPresenter).flightdynamic(entity);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void flightdynamicResult(FlightBean result) {
        mData.add("??????(" + result.getTotal() + ")");
        mData.add("??????(" + result.getArrive() + ")");
        mData.add("??????(" + result.getDeparture() + ")");
        mData.add("??????(" + result.getReversal() + ")");
        mData.add("??????(" + result.getPreparation() + ")");

        tabAdapter = new MyTabAdapter(mData);
        CommonNavigator navigator = new CommonNavigator(getContext());
        // ??????????????????????????????????????????????????????title
        navigator.setAdjustMode(true);
        navigator.setAdapter(tabAdapter);
        navigator.setSkimOver(true);
        navigator.setEnablePivotScroll(true);
        navigator.setScrollPivotX(0.35f);
        magicIndicator.setNavigator(navigator);
        //??????fragment
        pageAdapter = new MyPageAdapter(getFragmentManager());
        mListFragment.add(DynamicInfoFragment.getInstance("", "","yesterday"));
        mListFragment.add(DynamicInfoFragment.getInstance("", "A","yesterday"));
        mListFragment.add(DynamicInfoFragment.getInstance("", "D","yesterday"));
        mListFragment.add(DynamicInfoFragment.getInstance("2", "","yesterday"));
        mListFragment.add(DynamicInfoFragment.getInstance("1", "","yesterday"));
        //viewpager????????????????????????tab
        viewPager.setOffscreenPageLimit(mData.size());
        viewPager.setAdapter(pageAdapter);
        ViewPagerHelper.bind(magicIndicator, viewPager);
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

    public class MyTabAdapter extends CommonNavigatorAdapter {
        private List<String> mList;


        public MyTabAdapter(List<String> noticeBeanList) {
            mList = noticeBeanList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, int index) {
            SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
            //????????????
            simplePagerTitleView.setNormalColor(Color.parseColor("#888888"));
            //????????????
            simplePagerTitleView.setSelectedColor(Color.parseColor("#2E81FD"));
            //??????
            simplePagerTitleView.setText(mList.get(index));
            //??????
            simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
            simplePagerTitleView.setOnClickListener(v -> {
                viewPager.setCurrentItem(index);
            });

            return simplePagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
            linePagerIndicator.setColors(Color.parseColor("#2E81FD"));
            linePagerIndicator.setLineHeight(6f);
            linePagerIndicator.setRoundRadius(4f);
            //???????????????
            linePagerIndicator.setLineHeight(UIUtil.dip2px(context, 2));
            //???????????????
            linePagerIndicator.setLineWidth(UIUtil.dip2px(context, 20));
            linePagerIndicator.setRoundRadius(UIUtil.dip2px(context, 3));
            linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
            return linePagerIndicator;
        }
    }

    public class MyPageAdapter extends FragmentPagerAdapter {


        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mListFragment.get(i);
        }

        @Override
        public int getCount() {
            return mListFragment.size();
        }
    }
}
