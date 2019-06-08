package qx.app.freight.qxappfreight.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.AllocateVehiclesAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetInfosByFlightIdContract;
import qx.app.freight.qxappfreight.fragment.TaskFragment;
import qx.app.freight.qxappfreight.presenter.GetInfosByFlightIdPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 出港-配载-复重页面
 * Created by pr
 */
public class AllocateVehiclesFragment extends BaseFragment implements GetInfosByFlightIdContract.getInfosByFlightIdView, EmptyLayout.OnRetryLisenter, MultiFunctionRecylerView.OnRefreshListener {
    @BindView(R.id.mfrv_allocate_list)
    MultiFunctionRecylerView mMfrvAllocateList;

    private AllocateVehiclesAdapter adapter;

    private List<GetInfosByFlightIdBean> list; //条件list
    private List<GetInfosByFlightIdBean> list1; //原始list

    private int pageCurrent = 1;
    private String searchString = "";//条件搜索关键字
    private TaskFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow =false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_allocate_vehicles, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMfrvAllocateList.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvAllocateList.setOnRetryLisenter(this);
        mTaskFragment = (TaskFragment) getParentFragment();
        searchToolbar = mTaskFragment.getSearchView();
        initData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        if (isVisibleToUser){
            Log.e("111111", "setUserVisibleHint: "+ "展示");
            if (mTaskFragment != null)
                mTaskFragment.setTitleText(list1.size());
            if (searchToolbar!=null){
                searchToolbar.setHintAndListener("请输入板车号", text -> {
                    searchString = text;
                    seachWithNum();
                });
            }

        }
    }

    //根据条件筛选数据
    private void seachWithNum() {
        list.clear();
        if (TextUtils.isEmpty(searchString)) {
            list.addAll(list1);
        } else {
            for (GetInfosByFlightIdBean item : list1) {
                if (item.getScooterCode().toLowerCase().contains(searchString.toLowerCase())) {
                    list.add(item);
                }
            }
        }
        if (mMfrvAllocateList!=null){
            mMfrvAllocateList.notifyForAdapter(adapter);
        }

    }

    private void initData() {
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        adapter = new AllocateVehiclesAdapter(list);
        mMfrvAllocateList.setRefreshListener(this);
        mMfrvAllocateList.setOnRetryLisenter(this);
        mMfrvAllocateList.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
//            ToastUtil.showToast(getContext(), list.get(position));
//            CargoHandlingActivity.startActivity(mContext,list.get(position).getTaskId(),list.get(position).getFlightId());
        });
        mPresenter = new GetInfosByFlightIdPresenter(this);
//        getData();
    }




    public void getData() {
        BaseFilterEntity<GetInfosByFlightIdBean> entity = new BaseFilterEntity();
        entity.setUserId("weighter");
        entity.setRoleCode(Constants.WEIGHTER);
        ((GetInfosByFlightIdPresenter) mPresenter).getInfosByFlightId(entity);
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(getActivity(), error);
        if (pageCurrent == 1) {
            mMfrvAllocateList.finishRefresh();
        } else {
            mMfrvAllocateList.finishLoadMore();
        }
    }

    @Override
    public void showNetDialog() {
    }

    @Override
    public void dissMiss() {
    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据……");
        new Handler().postDelayed(() -> {
            getData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void getInfosByFlightIdResult(List<GetInfosByFlightIdBean> getInfosByFlightIdBeans) {
        //因为没有分页，不做分页判断
        list1.clear();

        if (pageCurrent == 1) {
            mMfrvAllocateList.finishRefresh();
        } else {
            mMfrvAllocateList.finishLoadMore();
        }
        list1.addAll(getInfosByFlightIdBeans);
        if (mTaskFragment != null) {
            if (isShow)
                mTaskFragment.setTitleText(list1.size());
        }
        seachWithNum();
    }

    @Override
    public void onRefresh() {
        pageCurrent = 1;
        getData();
    }

    @Override
    public void onLoadMore() {
        pageCurrent++;
        getData();
    }
}
