package qx.app.freight.qxappfreight.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.CollectorDeclareActivity;
import qx.app.freight.qxappfreight.activity.ReturnGoodsActivity;
import qx.app.freight.qxappfreight.activity.ScanManagerActivity;
import qx.app.freight.qxappfreight.activity.StoreTypeChangeActivity;
import qx.app.freight.qxappfreight.adapter.MainListRvAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.TaskLockEntity;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetWayBillInfoByIdContract;
import qx.app.freight.qxappfreight.contract.TaskLockContract;
import qx.app.freight.qxappfreight.contract.TransportListContract;
import qx.app.freight.qxappfreight.presenter.TaskLockPresenter;
import qx.app.freight.qxappfreight.presenter.TransportListPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/****
 * 收运
 */
public class CollectorFragment extends BaseFragment implements TaskLockContract.taskLockView, TransportListContract.transportListContractView, GetWayBillInfoByIdContract.getWayBillInfoByIdView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    RecyclerView mMfrvData;

    @BindView(R.id.iv_scan)
    ImageView ivScan;

    private MainListRvAdapter adapter;
    private List <TransportDataBase> list1 = new ArrayList <>();
    private List <TransportDataBase> list = new ArrayList <>();
    private int pageCurrent = 1;
    private String seachString = "";
    private TaskFragment mTaskFragment;
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow = false;


    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.et_waybill_code)
    EditText etWaybillCode;
    /**
     * 待办锁定 当前的任务bean
     */
    private TransportDataBase CURRENT_TASK_BEAN = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collector, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mTaskFragment = (TaskFragment) getParentFragment();
//        searchToolbar = mTaskFragment.getSearchView();
//        mTaskFragment.getToolbar().setleftIconViewVisiable(false);
//        mTaskFragment.getToolbar().setRightIconViewVisiable(false);
//        mTaskFragment.setTitleText();
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
//        mMfrvData.setRefreshListener(this);
//        mMfrvData.setOnRetryLisenter(this);
        initView();
//        SearchToolbar searchToolbar = ((TaskFragment) getParentFragment()).getSearchView();
//        searchToolbar.setHintAndListener("请输入运单号", text -> {
//            seachString = text;
//            seachWith();
//        });
//        setUserVisibleHint(true);
        setSearchToolbar();
    }

    private void seachWith() {
        list.clear();
        if (TextUtils.isEmpty(seachString)) {
            list.addAll(list1);
        } else {
            for (TransportDataBase team : list1) {
                if (team.getWaybillCode() != null && team.getWaybillCode().toLowerCase().contains(seachString.toLowerCase())) {
                    list.add(team);
                }
            }
        }
        adapter.notifyDataSetChanged();
//        if (mMfrvData != null) {
//            mMfrvData.notifyForAdapter(adapter);
//        }
    }


    private void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        adapter = new MainListRvAdapter(list);
        mMfrvData.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            CURRENT_TASK_BEAN = list.get(position);
            mPresenter = new TaskLockPresenter(this);
            TaskLockEntity entity = new TaskLockEntity();
            List <String> taskIdList = new ArrayList <>();
            taskIdList.add(list.get(position).getTaskId());
            entity.setTaskId(taskIdList);
            entity.setUserId(UserInfoSingle.getInstance().getUserId());
            entity.setRoleCode(Constants.COLLECTION);
            ((TaskLockPresenter) mPresenter).taskLock(entity);

        });
        btnSearch.setOnClickListener(v -> {

            searchWaybill(etWaybillCode.getText().toString());


        });
        ivScan.setOnClickListener(v -> {
            ScanManagerActivity.startActivity(getContext(), "CollectorFragment");
        });
    }

    private void searchWaybill(String toString) {
        if (!StringUtil.isEmpty(toString) && toString.length() >= 4)
            loadData(toString);
        else {
            ToastUtil.showToast("请输入至少4位运单号");
            list.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private void trunToCollectorActivity(TransportDataBase bean) {
        switch (bean.getTaskTypeCode()) {
//            case "changeApply": //换单审核
//                DeliveryVerifyActivity.startActivity(getContext(), bean.getId(), bean.getTaskId());
//                break;
            case "borrowCollection"://借货收运
            case "collection"://出港收货
                Log.e("tagTest", "出港收货===id====" + bean.getId());
                startActivity(new Intent(getContext(), CollectorDeclareActivity.class)
                        .putExtra("wayBillId", bean.getWaybillId())
                        .putExtra("taskId", bean.getTaskId())
                        .putExtra("id", bean.getId())
                        .putExtra("storage", bean.getStorageType())
                        .putExtra("taskTypeCode", bean.getTaskTypeCode()));
                break;
            case "reCollection"://补单收运
                Log.e("tagTest", "补单收运===id====" + bean.getId());
                startActivity(new Intent(getContext(), CollectorDeclareActivity.class)
                        .putExtra("wayBillId", bean.getWaybillId())
                        .putExtra("taskId", bean.getTaskId())
                        .putExtra("id", bean.getId())
                        .putExtra("taskTypeCode", bean.getTaskTypeCode()));
                break;
            case "RR_collectReturn"://出港退货
            case "borrowOut"://借货出库
                ReturnGoodsActivity.startActivity(getActivity(), bean);
                break;

            case "changeCollection": //存储变更
                StoreTypeChangeActivity.startActivity(getActivity(), bean);
                break;
        }
    }

    //获取数据
    private void loadData(String waybillCode) {
        mPresenter = new TransportListPresenter(this);
        BaseFilterEntity <TransportDataBase> entity = new BaseFilterEntity();
        TransportDataBase tempBean = new TransportDataBase();
        tempBean.setWaybillCode(waybillCode);
        tempBean.setTaskStartTime("");
        tempBean.setTaskEndTime("");
        tempBean.setRole(Constants.COLLECTION);
//        for (LoginResponseBean.RoleRSBean mRoleRSBean : UserInfoSingle.getInstance().getRoleRS()) {
//            if (Constants.COLLECTION.equals(mRoleRSBean.getRoleCode())) {
//                tempBean.setRole(mRoleRSBean.getRoleCode());
//            }
//        }
        entity.setFilter(tempBean);
        entity.setCurrentStep("");
        entity.setSize(Constants.PAGE_SIZE);
        entity.setCurrent(pageCurrent);
        ((TransportListPresenter) mPresenter).transportListPresenter(entity);
    }

    @Override
    public void onRetry() {
//        showProgessDialog("加载数据中……");
//        new Handler().postDelayed(() -> {
//            loadData();
//            dismissProgessDialog();
//        }, 2000);
    }

    @Override
    public void onRefresh() {
//        pageCurrent = 1;
//        loadData();
    }

    @Override
    public void onLoadMore() {
//        pageCurrent++;
//        loadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result.equals("collector_refresh")) {
//            pageCurrent = 1;
//            Log.e("refresh", result);
//            loadData();
            if (list!=null){
                list.clear();
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        String daibanCode = result.getData();
        if (!TextUtils.isEmpty(result.getFunctionFlag())) {
            if (!TextUtils.isEmpty(result.getData()) && (result.getFunctionFlag().equals("CollectorFragment") || result.getFunctionFlag().equals("MainActivity")) && isShow) {
                String[] parts = daibanCode.split("\\/");
                List <String> strsToList = Arrays.asList(parts);
                if (strsToList.size() >= 4) {
                    searchWaybill(strsToList.get(3));
//                chooseCode(strsToList.get(3));
                }
            }
        } else {
            if (!TextUtils.isEmpty(result.getData()) && isShow) {
                String[] parts = daibanCode.split("\\/");
                List <String> strsToList = Arrays.asList(parts);
                if (strsToList.size() >= 4) {
                    searchWaybill(strsToList.get(3));
//                chooseCode(strsToList.get(3));
                }
            }
        }

    }

    //收运接受到推送增加或者删除数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebSocketResultBean mWebSocketResultBean) {
//        if ("N".equals(mWebSocketResultBean.getFlag())) {
//            //非换单审核的全部都加
//            if ("changeCollection".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())
//                    || "collection".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())
//                    || "RR_collectReturn".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())
//                    || "borrowCollection".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())) {
//                list1.addAll(mWebSocketResultBean.getChgData());
//                if (isShow) {
//                    mTaskFragment.setTitleText(list1.size());
//                }
//            }
//            seachWith();
//        } else if ("D".equals(mWebSocketResultBean.getFlag())) {
//            if (null != CURRENT_TASK_BEAN) {
//                if (CURRENT_TASK_BEAN.getWaybillId().equals(mWebSocketResultBean.getChgData().get(0).getWaybillId())) {
//                    ActManager.getAppManager().finishReCollection();
//                    ToastUtil.showToast("当前收运任务已完成");
//                }
//            }
//            if ("changeCollection".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())
//                    || "collection".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())
//                    || "RR_collectReturn".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())
//                    || "borrowCollection".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())) {
//                loadData();
//            }
//        }

    }

    /**
     * 通过获取的code，筛选代办，直接进入处理代办
     *
     * @param daibanCode 代办号
     */
    private void chooseCode(String daibanCode) {
        for (TransportDataBase item : list) {
            if (daibanCode.equals(item.getWaybillCode())) {
                CURRENT_TASK_BEAN = item;
                mPresenter = new TaskLockPresenter(this);
                TaskLockEntity entity = new TaskLockEntity();
                List <String> taskIdList = new ArrayList <>();
                taskIdList.add(item.getTaskId());
                entity.setTaskId(taskIdList);
                entity.setUserId(UserInfoSingle.getInstance().getUserId());
                entity.setRoleCode(Constants.COLLECTION);
                ((TaskLockPresenter) mPresenter).taskLock(entity);
                return;
            }
        }
    }

    @Override
    public void transportListContractResult(TransportListBean transportListBeans) {
        if (transportListBeans != null && transportListBeans.getRecords().size() > 0) {

            //出现不相同运单号
            String watbillCode = transportListBeans.getRecords().get(0).getWaybillCode();
            for (TransportDataBase transportDataBase : transportListBeans.getRecords()) {
                if (watbillCode != null && !watbillCode.equals(transportDataBase.getWaybillCode())) {
                    ToastUtil.showToast("请输入更加完整的运单号");
                    return;
                }
            }
            etWaybillCode.setText("");
            if (transportListBeans.getRecords().size() == 1)
                trunToCollectorActivity(transportListBeans.getRecords().get(0));
            else {
                list1.clear();
                for (TransportDataBase record : transportListBeans.getRecords()) {
                    if (!"changeApply".equals(record.getTaskTypeCode())) {
                        list1.add(record);
                    }
                }
                seachWith();
//                if (mTaskFragment != null) {
//                    if (isShow) {
//                        mTaskFragment.setTitleText(list1.size());
//                    }
//                }
            }
        } else {
            ToastUtil.showToast(getActivity(), "数据为空");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        if (isShow) {
            setSearchToolbar();
        }
    }

    public void setSearchToolbar() {
        if (!isShow)
            return;
        if (mTaskFragment == null) {
            mTaskFragment = (TaskFragment) getParentFragment();
        }
        if (mTaskFragment != null) {
            mTaskFragment.setTitleText(list1.size());
            searchToolbar = mTaskFragment.getSearchView();
            mTaskFragment.getToolbar().setleftIconViewVisiable(false);
            mTaskFragment.getToolbar().setRightIconViewVisiable(false);
            mTaskFragment.setTitleText();
        }
        if (searchToolbar != null) {
            searchToolbar.setHintAndListener("请输入运单号", text -> {
                seachString = text;
                seachWith();
            });
        }

    }


    @Override
    public void toastView(String error) {
        if (error != null)
            ToastUtil.showToast(getActivity(), error);

//        if (mMfrvData != null)
//            mMfrvData.finishLoadMore();
//        if (mMfrvData != null)
//            mMfrvData.finishRefresh();
    }

    @Override
    public void showNetDialog() {
    }

    @Override
    public void dissMiss() {
    }

    /**
     * 待办锁定结果返回
     *
     * @param result
     */
    @Override
    public void taskLockResult(String result) {
        if (CURRENT_TASK_BEAN != null) {
            trunToCollectorActivity(CURRENT_TASK_BEAN);
        }
    }

    @Override
    public void getWayBillInfoByIdResult(DeclareWaybillBean result) {

    }

    @Override
    public void sendPrintMessageResult(String result) {

    }

    @Override
    public void getWaybillStatusResult(TransportDataBase result) {
        trunToCollectorActivity(result);
    }
}
