package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.TaskManifestAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 出港-配载-组板
 */
public class TaskManifestFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;

    private List<String> list;
    private TaskManifestAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_stowage, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        initData();
    }

    private void initData() {
        list = new ArrayList<>();
        list.add("申报单");
        list.add("运单");
        list.add("养殖证明");
        list.add("危险品鉴定报告");
        list.add("消毒证明");
        list.add("植物检查检疫证明");
        list.add("动物检查检疫证明");
        list.add("毒品随便运证明");
        list.add("2");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("3");
        adapter = new TaskManifestAdapter(list,TaskManifestAdapter.TYPE_PEI_ZAI);
        mMfrvData.setAdapter(adapter);
    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据。。。。。。");
        new Handler().postDelayed(() -> {
            initData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {
        list.add("动物检查检疫证明");
        list.add("毒品随便运证明");
        list.add("危险品鉴定报告");
        list.add("消毒证明");
        list.add("植物检查检疫证明");
        list.add("动物检查检疫证明");
        list.add("毒品随便运证明");
        adapter.notifyDataSetChanged();
    }
}
