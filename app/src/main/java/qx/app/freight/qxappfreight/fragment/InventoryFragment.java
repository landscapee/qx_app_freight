package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.InventoryAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.GetIOManifestEntity;
import qx.app.freight.qxappfreight.bean.response.SmInventoryEntryandexit;
import qx.app.freight.qxappfreight.bean.response.SmInventorySummary;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetInventoryContract;
import qx.app.freight.qxappfreight.presenter.GetInventoryPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 库存 列表
 */
public class InventoryFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, GetInventoryContract.getInventoryView {

    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    @BindView(R.id.et_search)
    EditText etSearch;
    private String strSearch = "";
    private List <SmInventorySummary> mListSearch = new ArrayList <>();
    private List <SmInventorySummary> mList = new ArrayList <>();
    private InventoryAdapter ioManifestAdapter;

    private int pageCurrent = 1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_warehouse, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        ioManifestAdapter = new InventoryAdapter(mListSearch);
        mMfrvData.setAdapter(ioManifestAdapter);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                strSearch = etSearch.getText().toString();
                bySearchList(mList);
            }
        });
        loadData(pageCurrent);
    }

    public void loadData(int pageCurrent1){
        mPresenter = new GetInventoryPresenter(this);
        BaseFilterEntity<GetIOManifestEntity> entityBaseFilterEntity = new BaseFilterEntity <>();
        GetIOManifestEntity getIOManifestEntity = new GetIOManifestEntity();
        entityBaseFilterEntity.setCurrent(pageCurrent1);
        pageCurrent = pageCurrent1;
        entityBaseFilterEntity.setSize(Constants.PAGE_SIZE);

        if (IOManifestFragment.iOqrcodeEntity !=null && !StringUtil.isEmpty(IOManifestFragment.iOqrcodeEntity.getDepotID())) {
            getIOManifestEntity.setAreaId(IOManifestFragment.iOqrcodeEntity.getDepotID());
        } else{
            mMfrvData.finishRefresh();
            mMfrvData.finishLoadMore();
            return;
        }
        entityBaseFilterEntity.setFilter(getIOManifestEntity);
        ((GetInventoryPresenter)mPresenter).getInventoryList(entityBaseFilterEntity);

    }

    @Override
    public void onRetry() {
        pageCurrent=1;
        loadData(pageCurrent);
    }
    @Override
    public void onRefresh() {
        pageCurrent=1;
        loadData(pageCurrent);
    }
    @Override
    public void onLoadMore() {
        loadData(pageCurrent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String msg) {
        if (msg!=null && ("inventory_refresh_out".equals(msg) || "inventory_refresh_in".equals(msg))){
            loadData(1);
        }
    }


    @Override
    public void toastView(String error) {
        if (mMfrvData != null) {
            mMfrvData.finishLoadMore();
        }
        if (mMfrvData != null) {
            mMfrvData.finishRefresh();
        }
        if (error!=null) {
            ToastUtil.showToast(error);
        }
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }
    private void bySearchList(List <SmInventorySummary> mList){
        mListSearch.clear();
        for (SmInventorySummary smInventoryEntryandexit:mList){
            if (smInventoryEntryandexit.getWaybillCode().contains(strSearch)){
                mListSearch.add(smInventoryEntryandexit);
            }
        }
        mMfrvData.notifyForAdapter(ioManifestAdapter);

    }
    @Override
    public void setInventoryResult(List <SmInventorySummary> result) {
        if (pageCurrent == 1) {
            mList.clear();
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
        if (result != null && result.size()> 0){
            pageCurrent++;
            mList.addAll(result);
        }
        bySearchList(mList);
    }
}
