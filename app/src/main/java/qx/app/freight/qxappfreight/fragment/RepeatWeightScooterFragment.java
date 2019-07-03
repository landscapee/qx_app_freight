package qx.app.freight.qxappfreight.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.AllocaaateScanActivity;
import qx.app.freight.qxappfreight.adapter.RepeatWeightScooterAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.request.TodoScootersEntity;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.contract.TodoScootersContract;
import qx.app.freight.qxappfreight.presenter.TodoScootersPresenter;

/**复重-板车列表
 * created by swd
 * 2019/7/2 11:25
 */
public class RepeatWeightScooterFragment extends BaseFragment implements TodoScootersContract.todoScootersView {
    @BindView(R.id.rl_view)
    RecyclerView rlView;

    private RepeatWeightScooterAdapter adapter;
    private List<GetInfosByFlightIdBean> list;

    private String flightId;
    private String taskId;

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
        flightId = getArguments().getString("flightInfoId");
        taskId = getArguments().getString("taskId");
        initView();
        initData();
    }

    private void initData() {
        mPresenter = new TodoScootersPresenter(this);
        TodoScootersEntity entity = new TodoScootersEntity();
        entity.setFlightInfoId(flightId);
        entity.setTaskId(taskId);
        ((TodoScootersPresenter) mPresenter).todoScooters(entity);
    }

    private void initView() {
        list = new ArrayList<>();
        adapter = new RepeatWeightScooterAdapter(list);
        rlView.setLayoutManager(new LinearLayoutManager(getContext()));
        rlView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            startActivity(new Intent(getActivity(), AllocaaateScanActivity.class).putExtra("dataBean",list.get(position)));
        });

    }

    @Override
    public void todoScootersResult(List<GetInfosByFlightIdBean> result) {
        list.clear();
        list.addAll(result);
        adapter.notifyDataSetChanged();
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
}
