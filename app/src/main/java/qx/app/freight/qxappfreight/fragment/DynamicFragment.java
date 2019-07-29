package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.response.FlightEventBusBean;

/**
 * 航班动态
 */
public class DynamicFragment extends BaseFragment {
    @BindView(R.id.tb_title)
    RadioGroup mRgTitle;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R.id.rb_yesterday)
    RadioButton mRbyesterday;
    @BindView(R.id.rb_today)
    RadioButton mToday;
    @BindView(R.id.rb_tomrro)
    RadioButton mTomrro;


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
        mRgTitle.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_yesterday:
                    nowFragment = mYFragment; //昨天
                    setEvnt("yesterday");
                    break;
                case R.id.rb_today:
                    nowFragment = mTFragment; //今天
                    setEvnt("today");
                    break;
                case R.id.rb_tomrro:
                    nowFragment = mToFragment; //明天
                    setEvnt("tomorrow");
                    break;
            }
            showFragment(nowFragment);
        });
    }

    public void setEvnt(String str) {
        EventBus.getDefault().post(new FlightEventBusBean(str));
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
