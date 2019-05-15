package qx.app.freight.qxappfreight.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ClearHistoryAdapter;
import qx.app.freight.qxappfreight.adapter.ClearStorageAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.InventoryQueryBean;
import qx.app.freight.qxappfreight.contract.InventoryQueryContract;
import qx.app.freight.qxappfreight.presenter.InventoryQueryPresenter;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

public class ClearStorageFragment extends BaseFragment implements InventoryQueryContract.inventoryQueryView, MultiFunctionRecylerView.OnRefreshListener {
    @BindView(R.id.toolbar)
    CustomToolbar mToolBar;
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    @BindView(R.id.mfrv_history)
    MultiFunctionRecylerView mMfrvDataHistory;

    //新的任务
    private ClearStorageAdapter mCSadapter;
    private List<InventoryQueryBean> mCSlist;

    //历史任务
    private ClearHistoryAdapter mCHadapter;
    private List<InventoryQueryBean> mCHlist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clearstorage, container, false);
        unbinder = ButterKnife.bind(this, view);
        //标题
        mToolBar.setMainTitle(Color.WHITE, "清库");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvDataHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvDataHistory.setRefreshListener(this);
        initView();
    }

    private void initView() {
        mCSlist = new ArrayList<>();
        mCHlist = new ArrayList<>();

        mCSadapter = new ClearStorageAdapter(mCSlist);
        mCHadapter = new ClearHistoryAdapter(mCHlist);

        mMfrvData.setAdapter(mCSadapter);
        mMfrvDataHistory.setAdapter(mCHadapter);

        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setCurrent(1);
        entity.setSize(10);
        mPresenter = new InventoryQueryPresenter(this);
        ((InventoryQueryPresenter) mPresenter).InventoryQuery();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void inventoryQueryResult(List<InventoryQueryBean> inventoryQueryBean) {
        if (inventoryQueryBean != null) {
//            mMfrvData.finishRefresh();
//            mMfrvData.finishLoadMore();
            mCSlist.clear();
            mCHlist.clear();
            for (InventoryQueryBean mList : inventoryQueryBean) {
                if (1 == mList.getStatus()) {
                    mCSlist.add(mList);
                    mCSadapter.notifyDataSetChanged();
                } else if (0 == mList.getStatus()) {
                    mCHlist.add(mList);
                    mCHadapter.notifyDataSetChanged();
                }
            }
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
