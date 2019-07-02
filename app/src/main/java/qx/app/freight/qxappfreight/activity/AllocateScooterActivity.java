package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.fragment.RepeatWeightManifestFragment;
import qx.app.freight.qxappfreight.fragment.RepeatWeightScooterFragment;

/**复重环节，查看舱单列表
 *
 */
public class AllocateScooterActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    private List<Fragment> fragmentList;
    private List<String> list_Title;

    private String flightId;
    private String taskId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_allocate_scooter;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        flightId = getIntent().getStringExtra("flightId");
        taskId = getIntent().getStringExtra("taskId");

        setAdapter();
    }

    private void setAdapter() {
        fragmentList = new ArrayList<>();
        list_Title = new ArrayList<>();
        list_Title.add("舱单");
        list_Title.add("板车");

        fragmentList.add(RepeatWeightManifestFragment.getInstance(flightId));
        fragmentList.add(RepeatWeightScooterFragment.getInstance(flightId,taskId));


        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), this, fragmentList, list_Title));
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
