package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.RepeatWeightScooterAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;

/**复重-板车列表
 * created by swd
 * 2019/7/2 11:25
 */
public class RepeatWeightScooterFragment extends BaseFragment {
    @BindView(R.id.rl_view)
    RecyclerView rlView;

    private RepeatWeightScooterAdapter adapter;
    private List<GetInfosByFlightIdBean> list;

    public static RepeatWeightScooterFragment getInstance(String flightInfoId, String taskId) {
        RepeatWeightScooterFragment fragment = new RepeatWeightScooterFragment();
        Bundle bundle = new Bundle();
        bundle.putString("flightInfoId", flightInfoId);
        bundle.putString("taskId", taskId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.only_recyle_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        adapter = new RepeatWeightScooterAdapter(list);
        rlView.setLayoutManager(new LinearLayoutManager(getContext()));
        rlView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {

        });

    }
}
