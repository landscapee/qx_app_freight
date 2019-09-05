package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouyben.empty.EmptyLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

public class HyFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {

    @BindView(R.id.mfrv_data)
    RecyclerView mRvData;//货邮舱单信息列表

    @BindView(R.id.sr_refush)
    SwipeRefreshLayout mSrRefush;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hy, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRvData.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onRetry() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
