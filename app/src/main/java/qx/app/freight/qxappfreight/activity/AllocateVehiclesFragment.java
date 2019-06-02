package qx.app.freight.qxappfreight.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouyben.empty.EmptyLayout;

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
import qx.app.freight.qxappfreight.contract.GetInfosByFlightIdContract;
import qx.app.freight.qxappfreight.fragment.TaskFragment;
import qx.app.freight.qxappfreight.presenter.GetInfosByFlightIdPresenter;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 出港-配载-复重页面
 * Created by pr
 */
public class AllocateVehiclesFragment extends BaseFragment implements GetInfosByFlightIdContract.getInfosByFlightIdView, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_allocate_list)
    MultiFunctionRecylerView mMfrvAllocateList;

    private AllocateVehiclesAdapter adapter;

    private List<GetInfosByFlightIdBean> list; //条件list
    private List<GetInfosByFlightIdBean> list1; //原始list

    private String searchString = "";

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
        SearchToolbar searchToolbar = ((TaskFragment) getParentFragment()).getSearchView();
        searchToolbar.setHintAndListener("请输入板车号", text -> {
            searchString = text;
            seachWithNum();
        });
        initData();
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
        mMfrvAllocateList.notifyForAdapter(adapter);
    }

    private void initData() {
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        adapter = new AllocateVehiclesAdapter(list);
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
        entity.setRoleCode(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
        ((GetInfosByFlightIdPresenter) mPresenter).getInfosByFlightId(entity);
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
    public void onRetry() {
        showProgessDialog("正在加载数据……");
        new Handler().postDelayed(() -> {
            getData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void getInfosByFlightIdResult(List<GetInfosByFlightIdBean> getInfosByFlightIdBeans) {
        list1.clear();
        list1.addAll(getInfosByFlightIdBeans);
        seachWithNum();
    }
}
