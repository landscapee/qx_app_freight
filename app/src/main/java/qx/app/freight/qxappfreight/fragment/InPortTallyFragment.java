package qx.app.freight.qxappfreight.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.FFMActivity;
import qx.app.freight.qxappfreight.activity.SortingActivity;
import qx.app.freight.qxappfreight.adapter.InportTallyAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.GroupBoardRequestEntity;
import qx.app.freight.qxappfreight.bean.request.TaskLockEntity;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GroupBoardToDoContract;
import qx.app.freight.qxappfreight.contract.TaskLockContract;
import qx.app.freight.qxappfreight.listener.InportTallyInterface;
import qx.app.freight.qxappfreight.presenter.GroupBoardToDoPresenter;
import qx.app.freight.qxappfreight.presenter.TaskLockPresenter;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 进港理货fragment
 */
public class InPortTallyFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, TaskLockContract.taskLockView, GroupBoardToDoContract.GroupBoardToDoView, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private int mCurrentPage = 1;
    private List<TransportDataBase> mList = new ArrayList<>();  //筛选过后的数据
    private List<TransportDataBase> mListTemp = new ArrayList<>(); // 原始数据
    private InportTallyAdapter mAdapter;

    private String searchString = "";//条件搜索关键字
    private TaskFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow =false;

    /**
     * 待办锁定 当前的任务bean
     */
    private TransportDataBase CURRENT_TASK_BEAN = null;

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
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mTaskFragment = (TaskFragment) getParentFragment();
        searchToolbar = mTaskFragment.getSearchView();
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        mAdapter = new InportTallyAdapter(mList);
        mAdapter.setInportTallyListener(new InportTallyInterface() {
            @Override
            public void toDetail(TransportDataBase item) {
                CURRENT_TASK_BEAN = item;

                mPresenter = new TaskLockPresenter(InPortTallyFragment.this);
                TaskLockEntity entity = new TaskLockEntity();
                List<String> taskIdList = new ArrayList<>();
                taskIdList.add(item.getTaskId());
                entity.setTaskId(taskIdList);
                entity.setUserId(UserInfoSingle.getInstance().getUserId());
                entity.setRoleCode(Constants.INPORTTALLY);

                ((TaskLockPresenter) mPresenter).taskLock(entity);

            }

            @Override
            public void toFFM(TransportDataBase item) {
                startActivity(new Intent(mContext, FFMActivity.class));
            }
        });
        mMfrvData.setAdapter(mAdapter);

//        SearchToolbar searchToolbar = ((TaskFragment)getParentFragment()).getSearchView();
//        searchToolbar.setHintAndListener("请输入航班号", new SearchToolbar.OnTextSearchedListener() {
//            @Override
//            public void onSearched(String text) {
//               searchString = text;
//               seachWithNum();
//            }
//        });
        initData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        if (isVisibleToUser){
            Log.e("111111", "setUserVisibleHint: "+ "展示");
            if (mTaskFragment != null) {
                mTaskFragment.setTitleText(mListTemp.size());
            }
            if (searchToolbar!=null){
                searchToolbar.setHintAndListener("请输入航班号", text -> {
                    searchString = text;
                    seachWithNum();
                });
            }

        }
    }

    private void seachWithNum() {
        mList.clear();
        //搜索关键字为空，则不显示全部数据
        if(TextUtils.isEmpty(searchString)){
            mList.addAll(mListTemp);
        }else{
            for(TransportDataBase itemData: mListTemp){
                if(itemData.getFlightNo()!=null&&itemData.getFlightNo().toLowerCase().contains(searchString.toLowerCase())){
                    mList.add(itemData);
                }
            }
        }
        if (mMfrvData!=null){
            mMfrvData.notifyForAdapter(mAdapter);
        }
    }

    private void initData() {
        /*BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setCurrent(mCurrentPage);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
        entity.setUndoType("2");
        entity.setRoleCode(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());*/
        GroupBoardRequestEntity entity=new GroupBoardRequestEntity();
        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
//        {"stepOwner":"u27f95c83a0d24f19a592d16ebdf28fe3","undoType":2,"roleCode":"preplaner","ascs":["ETD"]}
        entity.setRoleCode("beforehand_in");
        entity.setUndoType(2);
        List<String> ascs=new ArrayList<>();
        ascs.add("ATA");
        ascs.add("STA");
        entity.setAscs(ascs);
        mPresenter = new GroupBoardToDoPresenter(this);
        ((GroupBoardToDoPresenter) mPresenter).getGroupBoardToDo(entity);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUserVisibleHint(true);
    }
    /**
     * 跳转到代办详情
     *
     * @param bean
     */
    private void turnToDetailActivity(TransportDataBase bean) {
        Intent intent = new Intent(mContext, SortingActivity.class);
        intent.putExtra("TASK_INFO", bean);
        mContext.startActivity(intent);
    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        String daibanCode = result.getData();
        Log.e("22222", "daibanCode" + daibanCode);
        if (!TextUtils.isEmpty(result.getData())&&result.getFunctionFlag().equals("MainActivity")) {
            chooseCode(daibanCode);
        }
    }

    /**
     * 通过获取的code，筛选代办，直接进入处理代办
     *
     * @param daibanCode 代办号
     */
    private void chooseCode(String daibanCode) {
        for (TransportDataBase item : mList) {
            if (daibanCode.equals(item.getId())) {

                CURRENT_TASK_BEAN = item;

                mPresenter = new TaskLockPresenter(InPortTallyFragment.this);
                TaskLockEntity entity = new TaskLockEntity();
                List<String> taskIdList = new ArrayList<>();
                taskIdList.add(item.getTaskId());
                entity.setTaskId(taskIdList);
                entity.setUserId(UserInfoSingle.getInstance().getUserId());
                entity.setRoleCode(Constants.INPORTTALLY);

                ((TaskLockPresenter) mPresenter).taskLock(entity);
                return;
            }
        }
    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据……");
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
//        mCurrentPage++;
        mMfrvData.finishLoadMore();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result.equals("InPortTallyFragment_refresh")) {
            initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebSocketResultBean mWebSocketResultBean) {
        if ("N".equals(mWebSocketResultBean.getFlag())) {
            if ("DA_tallyAndInStorage".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())) {
                mListTemp.addAll(mWebSocketResultBean.getChgData());
                mTaskFragment.setTitleText(mListTemp.size());
            }

        } else if ("D".equals(mWebSocketResultBean.getFlag())) {
            for (TransportDataBase mTransportListBean : mList) {
                if (mWebSocketResultBean.getChgData().get(0).getId() != null) {
                    if (mWebSocketResultBean.getChgData().get(0).getId().equals(mTransportListBean.getId())) {
                        mListTemp.remove(mTransportListBean);
                    }
                }
            }
        }
        seachWithNum();
    }

    @Override
    public void toastView(String error) {
        if (mCurrentPage == 1) {
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("请求中......");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void getGroupBoardToDoResult(List<TransportDataBase> transportListBeans) {
        if (mCurrentPage == 1) {
            mMfrvData.finishRefresh();
//            mListTemp.clear();
        } else {
            mMfrvData.finishLoadMore();
        }
        //看代码是没有做分页的，所以clear放这里
        mListTemp.clear();
        for (TransportDataBase item:transportListBeans){
            if (item.getTaskTypeCode().equals("DA_tallyAndInStorage")){
                mListTemp.add(item);
            }
        }
        seachWithNum();
        if (mTaskFragment != null) {
            if (isShow) {
                mTaskFragment.setTitleText(mListTemp.size());
            }
        }
    }

    @Override
    public void getScooterByScooterCodeResult(GetInfosByFlightIdBean getInfosByFlightIdBean) {

    }

    @Override
    public void searchWaybillByWaybillCodeResult(List <WaybillsBean> waybillsBeans) {

    }

    /**
     * 待办锁定
     * @param result
     */
    @Override
    public void taskLockResult(String result) {
        if(CURRENT_TASK_BEAN != null) {
            turnToDetailActivity(CURRENT_TASK_BEAN);
        }
    }
}
