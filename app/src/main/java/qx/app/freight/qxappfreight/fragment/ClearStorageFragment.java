package qx.app.freight.qxappfreight.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.AddClearStorageActivity;
import qx.app.freight.qxappfreight.activity.ClearStorageDetailActivity;
import qx.app.freight.qxappfreight.adapter.ClearHistoryAdapter;
import qx.app.freight.qxappfreight.adapter.ClearStorageAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.InventoryQueryBean;
import qx.app.freight.qxappfreight.bean.response.NoticeViewBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.InventoryQueryContract;
import qx.app.freight.qxappfreight.presenter.InventoryQueryPresenter;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/*****
 * 清库功能
 */
public class ClearStorageFragment extends BaseFragment implements InventoryQueryContract.inventoryQueryView {
    @BindView(R.id.toolbar)
    CustomToolbar mToolBar;
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    @BindView(R.id.mfrv_history)
    MultiFunctionRecylerView mMfrvDataHistory;
    @BindView(R.id.ll_clear)
    LinearLayout mLLClear;

    //新的任务
    private ClearStorageAdapter mCSadapter;
    private List<InventoryQueryBean.RecordsBean> mCSlist;

    //历史任务
    private ClearHistoryAdapter mCHadapter;
    private List<InventoryQueryBean.RecordsBean> mCHlist;

    private int mCurrentPage1 = 1;
    private int mCurrentPage2 = 1;

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
        int rw = 0;
        for (int i = 0; i < UserInfoSingle.getInstance().getRoleRS().size(); i++) {
            if (Constants.INPORTTALLY.equals(UserInfoSingle.getInstance().getRoleRS().get(i).getRoleCode())) {
                rw = 1;
                break;
            }
        }
        if (rw == 1) {
            mLLClear.setVisibility(View.VISIBLE);
        } else {
            mLLClear.setVisibility(View.GONE);
        }


        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        //最新
        mMfrvData.setRefreshListener(new MultiFunctionRecylerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage1 = 1;
                initData1();
            }

            @Override
            public void onLoadMore() {
                mCurrentPage1++;
                initData1();
            }
        });
        //历史
        mMfrvDataHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvDataHistory.setRefreshListener(new MultiFunctionRecylerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage2 = 1;
                initData2();
            }

            @Override
            public void onLoadMore() {
                mCurrentPage2++;
                initData2();
            }
        });
        initView();
        //新的任务
        mCSadapter.setOnItemClickListener((adapter, view13, position) -> {
            interNewAct(position);
        });
        //历史任务
        mCHadapter.setOnItemClickListener((adapter, view12, position) -> {
            interHistoryAct(position);
        });
    }

    private void interHistoryAct(int position) {
        String titleName;
        if (mCHlist.get(position).getTaskType() == 1) {
            titleName = "全仓清库";
        } else {
            titleName = "鲜活清库";
        }
        startActivity(new Intent(getContext(), ClearStorageDetailActivity.class)
                .putExtra("taskTitle", titleName)
                .putExtra("taskId", mCHlist.get(position).getId()));
    }

    private void interNewAct(int position) {
        String titleName;
        if (mCSlist.get(position).getTaskType() == 1) {
            titleName = "全仓清库";
        } else {
            titleName = "鲜活清库";
        }
        startActivity(new Intent(getContext(), AddClearStorageActivity.class)
                .putExtra("taskTitle", titleName)
                .putExtra("taskId", mCSlist.get(position).getId()));
    }


    private void initView() {
        mCSlist = new ArrayList<>();
        mCHlist = new ArrayList<>();

        mCSadapter = new ClearStorageAdapter(mCSlist);
        mCHadapter = new ClearHistoryAdapter(mCHlist);

        mMfrvData.setAdapter(mCSadapter);
        mMfrvDataHistory.setAdapter(mCHadapter);
        mPresenter = new InventoryQueryPresenter(this);
        initData1();
        initData2();
    }

    private void initData1() {
        BaseFilterEntity entity = new BaseFilterEntity();
        NoticeViewBean bean = new NoticeViewBean();
        bean.setStatus(1);
        entity.setFilter(bean);
        entity.setCurrent(mCurrentPage1);
        entity.setSize(Constants.PAGE_SIZE);
        ((InventoryQueryPresenter) mPresenter).InventoryQuery(entity);
    }

    private void initData2() {
        BaseFilterEntity entity = new BaseFilterEntity();
        NoticeViewBean bean = new NoticeViewBean();
        bean.setStatus(0);
        entity.setFilter(bean);
        entity.setCurrent(mCurrentPage2);
        entity.setSize(Constants.PAGE_SIZE);
        ((InventoryQueryPresenter) mPresenter).InventoryQueryHistory(entity);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void inventoryQueryResult(InventoryQueryBean inventoryQueryBean) {
        if (null != inventoryQueryBean) {
            if (mCurrentPage1 == 1) {
                mCSlist.clear();
                mMfrvData.finishRefresh();
            } else {
                mMfrvData.finishLoadMore();
            }
            mCSlist.addAll(inventoryQueryBean.getRecords());
            mCSadapter.notifyDataSetChanged();
        }
    }

    @Override
    public void inventoryQueryHistoryResult(InventoryQueryBean inventoryQueryBean) {
        if (null != inventoryQueryBean) {
            if (mCurrentPage2 == 1) {
                mCHlist.clear();
                mMfrvDataHistory.finishRefresh();
            } else {
                mMfrvDataHistory.finishLoadMore();
            }
            mCHlist.addAll(inventoryQueryBean.getRecords());
            mCHadapter.notifyDataSetChanged();
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

}
