package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.fragment.RepeatWeightManifestFragment;
import qx.app.freight.qxappfreight.fragment.RepeatWeightScooterFragment;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**复重环节，查看舱单列表 查看板车列表
 *
 */
public class AllocateScooterActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.tv_total_weight)
    TextView tvTotalWeight;

    private List<Fragment> fragmentList;
    private List<String> list_Title;

    private String flightInfoId;
    private String taskId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_allocate_scooter;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        flightInfoId = getIntent().getStringExtra("flightInfoId");
        taskId = getIntent().getStringExtra("taskId");
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "查看详情");
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        setAdapter();
    }

    private void setAdapter() {
        fragmentList = new ArrayList<>();
        list_Title = new ArrayList<>();
        list_Title.add("板车");
        list_Title.add("舱单");

        fragmentList.add(RepeatWeightScooterFragment.getInstance(flightInfoId,taskId));
        fragmentList.add(RepeatWeightManifestFragment.getInstance(flightInfoId));


        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), this, fragmentList, list_Title));
        mTabLayout.setupWithViewPager(mViewPager);//此方法就是让tablayout和ViewPager联动
    }

    public void setTotalWeight(double weight){
        tvTotalWeight.setText("货邮总重量:"+weight+"kg");
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
