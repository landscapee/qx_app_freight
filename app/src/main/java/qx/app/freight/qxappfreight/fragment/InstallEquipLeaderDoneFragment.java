package qx.app.freight.qxappfreight.fragment;

import android.app.Dialog;
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

import com.alibaba.fastjson.JSON;
import com.ouyben.empty.EmptyLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.InstallEquipLeaderAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.DoneTaskEntity;
import qx.app.freight.qxappfreight.bean.request.TaskClearEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadingAndUnloadBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.contract.StevedoresTaskHisContract;
import qx.app.freight.qxappfreight.presenter.LoadAndUnloadTodoPresenter;
import qx.app.freight.qxappfreight.presenter.StevedoresTaskHisPresenter;
import qx.app.freight.qxappfreight.utils.IMUtils;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 装卸机小组长代办fragment 已办
 */
public class InstallEquipLeaderDoneFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, StevedoresTaskHisContract.stevedoresTaskHisView, EmptyLayout.OnRetryLisenter, LoadAndUnloadTodoContract.loadAndUnloadTodoView  {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private List<LoadAndUnloadTodoBean> mList = new ArrayList<>();
    private List<LoadAndUnloadTodoBean> mCacheList = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
    private List<LoadAndUnloadTodoBean> mListCache = new ArrayList<>();//推送的缓存任务
    private InstallEquipLeaderAdapter mAdapter;

    private String searchString = "";//条件搜索关键字
    private TaskDoneFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow = false;
    private int currentPage = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_install_equip, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUserVisibleHint(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTaskFragment = (TaskDoneFragment) getParentFragment();
        searchToolbar = mTaskFragment.getSearchView();
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        mAdapter = new InstallEquipLeaderAdapter(mList);
        mAdapter.setOnClearSeatListener(new InstallEquipLeaderAdapter.OnClearSeatListener() {
            @Override
            public void onClearClicked(int position) {
                showYesOrNoDialog("","确认通知押运清场?",position);
            }

            @Override
            public void onFlightSafeguardClick(int position) {
                IMUtils.chatToGroup(mContext, Tools.groupImlibUid(mList.get(position))+"");
            }

            @Override
            public void onUploadPhoto(int position) {

            }

            @Override
            public void onLookUnloadInstall(int position) {

            }

            @Override
            public void onLookLoadInstall(int position) {

            }
        });
        mMfrvData.setAdapter(mAdapter);
        loadData();
        setUserVisibleHint(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        if (isVisibleToUser) {
            if (mTaskFragment != null)
                mTaskFragment.setTitleText(mCacheList.size());
            if (searchToolbar != null) {
                searchToolbar.setHintAndListener("请输入航班号", text -> {
                    searchString = text;
                    seachByText();
                });
            }
        }
    }

    /**
     * 根据搜索框输入检索对应的结果项
     */
    private void seachByText() {
        mList.clear();
        if (TextUtils.isEmpty(searchString)) {
            mList.addAll(mCacheList);
        } else {
            for (LoadAndUnloadTodoBean item : mCacheList) {
                if (item.getFlightNo()!=null&&item.getFlightNo().toLowerCase().contains(searchString.toLowerCase())) {
                    mList.add(item);
                }
            }
        }
        if (mMfrvData != null) {
            mMfrvData.notifyForAdapter(mAdapter);
        }
    }

    private void loadData() {
        mPresenter = new StevedoresTaskHisPresenter(this);
        BaseFilterEntity<DoneTaskEntity> entity = new BaseFilterEntity();
        DoneTaskEntity doneTaskEntity = new DoneTaskEntity();
        doneTaskEntity.setOperatorId(UserInfoSingle.getInstance().getUserId());
        entity.setCurrent(currentPage);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setFilter(doneTaskEntity);
        ((StevedoresTaskHisPresenter) mPresenter).stevedoresTaskHis(entity);
    }
    /**
     * 发起清场任务
     */
    private void startClearTask(int position) {
        TaskClearEntity entity = new TaskClearEntity();
        entity.setStaffId(UserInfoSingle.getInstance().getUserId());
        entity.setFlightId(Long.valueOf(mList.get(position).getFlightId()));
        entity.setSeat(mList.get(position).getSeat());
        entity.setType("clear");
        mPresenter = new LoadAndUnloadTodoPresenter(this);
        ((LoadAndUnloadTodoPresenter) mPresenter).startClearTask(entity);
    }
    /**
     * 二次确认弹出框
     *
     * @param title
     * @param msg
     * @param flag
     */
    private void showYesOrNoDialog(String title, String msg, int flag) {
        CommonDialog dialog = new CommonDialog(getActivity());
        dialog.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定")
                .setNegativeButton("取消")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            startClearTask(flag);
                        } else {
//                            ToastUtil.showToast("点击了右边的按钮");
                        }
                    }
                })
                .show();

    }
    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据……");
        new Handler().postDelayed(() -> {
            currentPage = 1;
            loadData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        loadData();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    @Override
    public void stevedoresTaskHisResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {

        if (currentPage == 1) {
            mCacheList.clear();
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
        currentPage++;
        for (LoadAndUnloadTodoBean bean : loadAndUnloadTodoBean) {
            StringUtil.setTimeAndType(bean);//设置对应的时间和时间图标显示
            StringUtil.setFlightRoute(bean.getRoute(), bean);//设置航班航线信息
            if (bean.getRelateInfoObj() != null) {
                StringUtil.setTimeAndType(bean.getRelateInfoObj());//设置对应的时间和时间图标显示
                StringUtil.setFlightRoute(bean.getRelateInfoObj().getRoute(), bean.getRelateInfoObj());//设置航班航线信息
                if (!StringUtil.isEmpty(bean.getRelateInfoObj().getLoadingAndUnloadExtJson())){
                    bean.getRelateInfoObj().setLoadingAndUnloadBean(JSON.parseObject(bean.getRelateInfoObj().getLoadingAndUnloadExtJson(), LoadingAndUnloadBean.class));
                }
            }
            if (!StringUtil.isEmpty(bean.getLoadingAndUnloadExtJson())){
                bean.setLoadingAndUnloadBean(JSON.parseObject(bean.getLoadingAndUnloadExtJson(), LoadingAndUnloadBean.class));
            }
            //将服务器返回的领受时间、到位时间、开舱门时间、开始装卸机-结束装卸机时间、关闭舱门时间用数组存储，遍历时发现“0”或包含“：0”出现，则对应的步骤数为当前下标
            List<String> times = new ArrayList<>();
            times.add(String.valueOf(bean.getAcceptTime()));
            times.add(String.valueOf(bean.getArrivalTime()));
            if (bean.getTaskType() == 6) {//装机
                times.add(String.valueOf(bean.getStartLoadTime()));
                times.add(String.valueOf(bean.getEndLoadTime()));
            } else if (bean.getTaskType() == 7) {//卸机
                times.add(String.valueOf(bean.getStartUnloadTime()));
                times.add(String.valueOf(bean.getEndUnloadTime()));
            } else {//装卸机
                times.add(String.valueOf(bean.getStartLoadTime()));
                times.add(String.valueOf(bean.getEndLoadTime()));
            }
            bean.setAcceptTask(true);//已经领受过任务
            for (int i = 0; i < bean.getOperationStepObj().size(); i++) {
                LoadAndUnloadTodoBean.OperationStepObjBean entity1 = bean.getOperationStepObj().get(i);
                entity1.setTaskId(bean.getTaskId());
                entity1.setFlightType(bean.getFlightType());
                entity1.setItemType(Constants.TYPE_STEP_OVER);
                entity1.setStepDoneDate("0".equals(times.get(i)) ? "" : sdf.format(new Date(Long.valueOf(times.get(i)))));
                entity1.setPlanTime(bean.getOperationStepObj().get(i).getPlanTime()==null||"0".equals(bean.getOperationStepObj().get(i).getPlanTime()) ? "" : sdf.format(new Date(Long.valueOf(bean.getOperationStepObj().get(i).getPlanTime()))));
            }
            mCacheList.add(bean);
        }
        mListCache.clear();
        for (LoadAndUnloadTodoBean bean : mCacheList) {
            if (!bean.isAcceptTask()) {
                mListCache.add(bean);
            }
        }
        if (mListCache.size() != 0) {
//            mCacheList.removeAll(mListCache);
            Log.e("tagTest", "弹框。。。。。");
        } else {
            seachByText();
        }
        if (mTaskFragment != null) {
            if (isShow)
                mTaskFragment.setTitleText(mCacheList.size());
        }
    }


    @Override
    public void toastView(String error) {
        if (mMfrvData != null)
            mMfrvData.finishLoadMore();
        if (mMfrvData != null)
            mMfrvData.finishRefresh();
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
    public void loadAndUnloadTodoResult(List <LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {

    }

    @Override
    public void slideTaskResult(String result) {

    }

    @Override
    public void startClearTaskResult(String result) {
        if (result !=null)
            ToastUtil.showToast(result);
    }
}
