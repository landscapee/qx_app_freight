package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseFragment;

public class DynamicFragment extends BaseFragment {
    @BindView(R.id.tb_title)
    TabLayout mTbTitle;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;

    private YesterdayFragment mYFragment;
    private TodayFragment mTFragment;
    private TomorrowFragment mToFragment;
    private Fragment nowFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initFragment();
    }

    public void initView() {
        mTbTitle.addTab(mTbTitle.newTab().setText("昨天"));
        mTbTitle.addTab(mTbTitle.newTab().setText("今天"),true);
        mTbTitle.addTab(mTbTitle.newTab().setText("明天"));


        mTbTitle.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
                switch (tab.getPosition()) {
                    case 0:
                        nowFragment = mYFragment; //昨天
                        break;
                    case 1:
                        nowFragment = mTFragment; //今天
                        break;
                    case 2:
                        nowFragment = mToFragment; //明天
                        break;
                }
                showFragment(nowFragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction()
                .hide(mYFragment)
                .hide(mTFragment)
                .hide(mToFragment);
        transaction.show(fragment).commit();
    }

    private void initFragment() {
        mYFragment = new YesterdayFragment();
        mTFragment = new TodayFragment();
        mToFragment = new TomorrowFragment();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.fl_content, mYFragment)
                .add(R.id.fl_content, mTFragment)
                .add(R.id.fl_content, mToFragment)
                .commit();
        showFragment(mTFragment);
    }
}
