package qx.app.freight.qxappfreight.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ClearStorageAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.response.InventoryQueryBean;
import qx.app.freight.qxappfreight.contract.InventoryQueryContract;
import qx.app.freight.qxappfreight.presenter.InventoryQueryPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

public class ClearStorageFragment extends BaseFragment implements InventoryQueryContract.inventoryQueryView, MultiFunctionRecylerView.OnRefreshListener {
    @BindView(R.id.toolbar)
    CustomToolbar mToolBar;
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    @BindView(R.id.tv_kc_nub)
    TextView tvKcNub;
    @BindView(R.id.tv_qk_nub)
    TextView tvQkNub;
    @BindView(R.id.clear_commit)
    Button clearCommit;
    private ClearStorageAdapter adapter;
    private List<InventoryQueryBean> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clearstorage, container, false);
        unbinder = ButterKnife.bind(this, view);
        //历史记录
        mToolBar.setLeftTextView(View.VISIBLE, Color.WHITE, "历史记录", v -> searchRecord());
        //新增
        mToolBar.setRightTextViewImage(getActivity(), View.VISIBLE, R.color.flight_a, "新增", R.mipmap.new_2, v -> {
            adddata();
        });
        //标题
        mToolBar.setMainTitle(Color.WHITE, "清库");
        return view;
    }

    private void adddata() {
    }

    private void searchRecord() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearCommit.setOnClickListener(v -> {
            ToastUtil.showToast("123");
        });
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        adapter = new ClearStorageAdapter(list);
        mMfrvData.setAdapter(adapter);
        mPresenter = new InventoryQueryPresenter(this);
//        ((InventoryQueryPresenter) mPresenter).InventoryQuery();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void inventoryQueryResult(List<InventoryQueryBean> inventoryQueryBean) {
        if (inventoryQueryBean != null) {
            mMfrvData.finishRefresh();
            mMfrvData.finishLoadMore();
            list.clear();
            //清库件数
            tvQkNub.setText("清库件数： " + inventoryQueryBean.size());
            //库存件数
            tvKcNub.setText("库存件数： " + inventoryQueryBean.size());
            list.addAll(inventoryQueryBean);
            adapter.notifyDataSetChanged();
        }
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

    @Override
    public void onRefresh() {
        initView();
    }

    @Override
    public void onLoadMore() {
        initView();
    }
}
