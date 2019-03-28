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
import qx.app.freight.qxappfreight.utils.ToastUtil;

public class TodayFragment extends BaseFragment {
    @BindView(R.id.magic_indicator_today)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager_today)
    ViewPager viewPager;

    private List<BaseFragment> mListFragment;
    private MyTabAdapter tabAdapter;
    private MyPageAdapter pageAdapter;
    private List<String> mData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
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
        mData.add("全部(99)");
        mData.add("进港(99)");
        mData.add("离岗(99)");
        mData.add("返航(99)");
        mData.add("备降(99)");
        tabAdapter = new MyTabAdapter(mData);

        CommonNavigator navigator = new CommonNavigator(getContext());
        // 自适应模式，适用于数目固定的、少量的title
        navigator.setAdjustMode(true);
        navigator.setAdapter(tabAdapter);
        navigator.setSkimOver(true);
        navigator.setEnablePivotScroll(true);
        navigator.setScrollPivotX(0.35f);
        magicIndicator.setNavigator(navigator);

        //创建fragment
        pageAdapter = new MyPageAdapter(getFragmentManager());
        for (int i = 0; i < 5; i++) {
            mListFragment.add(DynamicInfoFragment.getInstance(mData.get(i)));
        }
        //viewpager根据数据创建多个tab
        viewPager.setOffscreenPageLimit(mData.size());
        viewPager.setAdapter(pageAdapter);

        ViewPagerHelper.bind(magicIndicator, viewPager);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
            //默认颜色
            simplePagerTitleView.setNormalColor(Color.parseColor("#888888"));
            //选中颜色
            simplePagerTitleView.setSelectedColor(Color.parseColor("#2E81FD"));
            //文字
            simplePagerTitleView.setText(mList.get(index));
            //大小
            simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
            simplePagerTitleView.setOnClickListener(v -> {
                viewPager.setCurrentItem(index);
                ToastUtil.showToast("今天" + index + "");
            });

            return simplePagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
            linePagerIndicator.setColors(Color.parseColor("#2E81FD"));
            linePagerIndicator.setLineHeight(6f);
            linePagerIndicator.setRoundRadius(4f);
            //底线的高度
            linePagerIndicator.setLineHeight(UIUtil.dip2px(context, 6));
            //底线的宽度
            linePagerIndicator.setLineWidth(UIUtil.dip2px(context, 50));
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