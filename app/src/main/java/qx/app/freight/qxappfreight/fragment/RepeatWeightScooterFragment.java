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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.AllocaaateScanActivity;
import qx.app.freight.qxappfreight.adapter.RepeatWeightScooterAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.TodoScootersEntity;
import qx.app.freight.qxappfreight.bean.response.FilterTransportDateBase;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.contract.GroupBoardToDoContract;
import qx.app.freight.qxappfreight.contract.TodoScootersContract;
import qx.app.freight.qxappfreight.dialog.ChooseFlightDialog;
import qx.app.freight.qxappfreight.model.ManifestBillModel;
import qx.app.freight.qxappfreight.presenter.GroupBoardToDoPresenter;
import qx.app.freight.qxappfreight.presenter.TodoScootersPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**复重-板车列表
 * created by swd
 * 2019/7/2 11:25
 */
public class RepeatWeightScooterFragment extends BaseFragment implements TodoScootersContract.todoScootersView, GroupBoardToDoContract.GroupBoardToDoView {
    @BindView(R.id.rl_view)
    RecyclerView rlView;

    private RepeatWeightScooterAdapter adapter;
    private List<GetInfosByFlightIdBean> list;

    private String flightId;
    private String taskId;

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

    private void initView() {
        list = new ArrayList<>();
        adapter = new RepeatWeightScooterAdapter(list);
        rlView.setLayoutManager(new LinearLayoutManager(getContext()));
        rlView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            getScooterByScooterCode(list.get(position).getScooterCode());
//            startActivity(new Intent(getActivity(), AllocaaateScanActivity.class).putExtra("dataBean",list.get(position)));
        });

    }
    /**
     * 根据板车号获取板车信息
     */
    public void getScooterByScooterCode(String scooterCode) {
        mPresenter = new GroupBoardToDoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setScooterCode(scooterCode);
        ((GroupBoardToDoPresenter) mPresenter).getScooterByScooterCode(entity);
    }

    @Override
    public void todoScootersResult(List<GetInfosByFlightIdBean> result) {
        if (result!= null){
//            if (result.size() == 0){
//                getActivity().finish();
//                ToastUtil.showToast("该航班负重任务已完成");
//                return;
//            }
            list.clear();
            list.addAll(result);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void getManifestResult(List<ManifestBillModel> result) {

    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {
//        showProgessDialog("数据加载中……");
    }

    @Override
    public void dissMiss() {
//        dismissProgessDialog();
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
    public void getScooterByScooterCodeResult(List <GetInfosByFlightIdBean> getInfosByFlightIdBeans) {
        if (getInfosByFlightIdBeans!=null&&getInfosByFlightIdBeans.size()>0){
//            if (getInfosByFlightIdBeans.size() == 1){
//                startActivity(new Intent(getActivity(), AllocaaateScanActivity.class).putExtra("dataBean", getInfosByFlightIdBeans.get(0)));
//            }
//            else {
                for (GetInfosByFlightIdBean getInfosByFlightIdBean:getInfosByFlightIdBeans){
                    if (flightId.equals(getInfosByFlightIdBean.getFlightInfoId())){
                        startActivity(new Intent(getActivity(), AllocaaateScanActivity.class).putExtra("dataBean", getInfosByFlightIdBean));
                        return;
                    }
            }

        }
        else {
            ToastUtil.showToast("没有查询到相应的板车");
        }
    }

    @Override
    public void searchWaybillByWaybillCodeResult(List <WaybillsBean> waybillsBeans) {

    }
}
