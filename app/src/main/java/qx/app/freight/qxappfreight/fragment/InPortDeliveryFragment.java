package qx.app.freight.qxappfreight.fragment;

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
import qx.app.freight.qxappfreight.adapter.InPortDeliveryAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;

/**
 * 进港-交货页面
 * Created by swd
 */
public class InPortDeliveryFragment extends BaseFragment {
    @BindView(R.id.mfrv_allocate_list)
    RecyclerView mRcView;

    InPortDeliveryAdapter mAdapter;
    List<String> mList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_allocate_vehicles, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        getData();
    }

    private void initView() {
        mRcView.setLayoutManager(new LinearLayoutManager(getContext()));
        mList = new ArrayList<>();
        mAdapter = new InPortDeliveryAdapter(mList);
        mRcView.setAdapter(mAdapter);
    }

    private void getData() {

    }
}
