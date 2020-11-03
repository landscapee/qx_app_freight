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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.AllocaaateHisDetailsActivity;
import qx.app.freight.qxappfreight.activity.AllocaaateScanActivity;
import qx.app.freight.qxappfreight.activity.AllocateScooterActivity;
import qx.app.freight.qxappfreight.adapter.RepeatWeightScooterAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.TodoScootersEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.FilterTransportDateBase;
import qx.app.freight.qxappfreight.bean.response.FlightInfoAndScootersBean;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.contract.GroupBoardToDoContract;
import qx.app.freight.qxappfreight.contract.TodoScootersContract;
import qx.app.freight.qxappfreight.model.ManifestBillModel;
import qx.app.freight.qxappfreight.presenter.GroupBoardToDoPresenter;
import qx.app.freight.qxappfreight.presenter.TodoScootersPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;

/**
 * 复重-板车列表
 * created by swd
 * 2019/7/2 11:25
 */
public class RepeatWeightScooterFragment extends BaseFragment implements TodoScootersContract.todoScootersView, GroupBoardToDoContract.GroupBoardToDoView {
    @BindView(R.id.rl_view)
    RecyclerView rlView;

    private RepeatWeightScooterAdapter adapter;
    private List <GetInfosByFlightIdBean> list;
    private String flightId;
    private String taskId;
    private double weight = 0;

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
        initDialog();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        flightId = getArguments().getString("flightInfoId");
        taskId = getArguments().getString("taskId");
        initView();
//        initData();
    }

    private void initData() {
        mPresenter = new TodoScootersPresenter(this);
        TodoScootersEntity entity = new TodoScootersEntity();
        entity.setFlightInfoId(flightId);
        entity.setTaskId(taskId);
        entity.setFacility("1");
        ((TodoScootersPresenter) mPresenter).todoScooters(entity);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (((AllocateScooterActivity) getActivity()) != null)
                ((AllocateScooterActivity) getActivity()).setTotalWeight(weight, 0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if ("RepeatWeightScooterFragment_refresh".equals(result)) {
            initData();
        }
    }

    private void initView() {
        list = new ArrayList <>();
        adapter = new RepeatWeightScooterAdapter(list);
        rlView.setLayoutManager(new LinearLayoutManager(getContext()));
        rlView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (!Tools.isFastClick())
                return;
            if (list.get(position).getReWeightFinish() == 1)
                startActivity(new Intent(getActivity(), AllocaaateHisDetailsActivity.class).putExtra("dataBean", list.get(position)));
            else
                getScooterByScooterCode(list.get(position).getScooterCode(),list.get(position).getGroundAgentCode());
//            startActivity(new Intent(getActivity(), AllocaaateScanActivity.class).putExtra("dataBean",list.get(position)));
        });
        adapter.setOnReturnClickListener(new RepeatWeightScooterAdapter.OnReturnClickListener() {
            @Override
            public void onReturnClick(GetInfosByFlightIdBean infosByFlightIdBean) {
                returnGroupScooterTask(infosByFlightIdBean);
            }
        });

    }

    private void returnGroupScooterTask(GetInfosByFlightIdBean infosByFlightIdBean) {
        mPresenter = new TodoScootersPresenter(this);
        ((TodoScootersPresenter) mPresenter).returnGroupScooterTask(infosByFlightIdBean);
    }

    /**
     * 根据板车号获取板车信息
     */
    public void getScooterByScooterCode(String scooterCode,String groundAgentCode) {
        mPresenter = new GroupBoardToDoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setScooterCode(scooterCode);
        entity.setGroundAgentCode(groundAgentCode);
        entity.setFlightInfoId(flightId);
        ((GroupBoardToDoPresenter) mPresenter).getScooterByScooterCode(entity);
    }

    @Override
    public void todoScootersResult(FlightInfoAndScootersBean result) {
        weight = 0;
        if (result != null && result.getScooters() != null) {
//            if (result.size() == 0){
//                getActivity().finish();
//                ToastUtil.showToast("该航班负重任务已完成");
//                return;
//            }
            list.clear();
            list.addAll(result.getScooters());
            adapter.isShowReturn(result.isHasGroupScooterTask());
            adapter.notifyDataSetChanged();

            for (GetInfosByFlightIdBean getInfosByFlightIdBean : list) {
                weight += getInfosByFlightIdBean.getWeight();
            }
            ((AllocateScooterActivity) getActivity()).setTotalWeight(weight, 0);
        }

    }

    @Override
    public void getManifestResult(List <ManifestBillModel> result) {

    }

    @Override
    public void returnGroupScooterTaskResult(BaseEntity <Object> scooter) {
        if ("318".equals(scooter.getStatus())) {
            ToastUtil.showToast("该航班已经没有组板任务，请稍后重试");
        }
        initData();
    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {
        showProgessDialog("数据加载中……");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void getGroupBoardToDoResult(FilterTransportDateBase transportListBeans) {

    }

    @Override
    public void getOverWeightToDoResult(FilterTransportDateBase transportListBeans) {

    }

    @Override
    public void getScooterByScooterCodeResult(List <GetInfosByFlightIdBean> getInfosByFlightIdBeans) {
        if (getInfosByFlightIdBeans != null && getInfosByFlightIdBeans.size() > 0) {
//            if (getInfosByFlightIdBeans.size() == 1){
//                startActivity(new Intent(getActivity(), AllocaaateScanActivity.class).putExtra("dataBean", getInfosByFlightIdBeans.get(0)));
//            }
//            else {
            for (GetInfosByFlightIdBean getInfosByFlightIdBean : getInfosByFlightIdBeans) {
                if (flightId.equals(getInfosByFlightIdBean.getFlightInfoId())) {
                    startActivity(new Intent(getActivity(), AllocaaateScanActivity.class).putExtra("dataBean", getInfosByFlightIdBean));
                    return;
                }
            }

        } else {
            ToastUtil.showToast("没有查询到相应的板车");
        }
    }

    @Override
    public void searchWaybillByWaybillCodeResult(List <WaybillsBean> waybillsBeans) {

    }
}
