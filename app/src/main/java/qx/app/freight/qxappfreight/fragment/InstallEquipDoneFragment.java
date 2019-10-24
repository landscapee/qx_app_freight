package qx.app.freight.qxappfreight.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouyben.empty.EmptyLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.FlightPhotoRecordActivity;
import qx.app.freight.qxappfreight.adapter.NewInstallEquipAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.DoneTaskEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.LoadUnloadTaskHisContract;
import qx.app.freight.qxappfreight.contract.ReOpenLoadTaskContract;
import qx.app.freight.qxappfreight.dialog.InputDialog;
import qx.app.freight.qxappfreight.presenter.LoadUnloadTaskHisPresenter;
import qx.app.freight.qxappfreight.presenter.ReOpenLoadTaskPresenter;
import qx.app.freight.qxappfreight.utils.IMUtils;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 装机fragment 已办
 */
public class InstallEquipDoneFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, LoadUnloadTaskHisContract.loadUnloadTaskHisView, ReOpenLoadTaskContract.ReOpenLoadTaskView {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private List<LoadAndUnloadTodoBean> mList = new ArrayList<>();
    private List<LoadAndUnloadTodoBean> mCacheList = new ArrayList<>();
    private int mCurrentPage = 1;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
    private NewInstallEquipAdapter mAdapter;
    private String searchString = "";//条件搜索关键字
    private TaskDoneFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_install_equip, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTaskFragment = (TaskDoneFragment) getParentFragment();
        searchToolbar = mTaskFragment.getSearchView();
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        mPresenter = new LoadUnloadTaskHisPresenter(this);
        mAdapter = new NewInstallEquipAdapter(mList, false,true);
        mMfrvData.setAdapter(mAdapter);
        mAdapter.setOnFlightSafeguardListenner(new NewInstallEquipAdapter.OnFlightSafeguardListenner() {
            @Override
            public void onFlightSafeguardClick(int position) {
                IMUtils.chatToGroup(mContext, mList.get(position).getFlightId());
            }

            @Override
            public void onClearClick(int position) {
            }

            @Override
            public void onUploadPhoto(int position) {
                intoPhotoAct(position);
            }

            @Override
            public void onLookUnloadInstall(int position) {

            }

            @Override
            public void onLookLoadInstall(int position) {

            }
        });
        mAdapter.setOnReOpenLoadTaskListener(pos -> {
            showDialog(pos);
        });
        loadData();
    }

    private void reOpenLoadTask(int pos,String remark){
        mPresenter = new ReOpenLoadTaskPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(mList.get(pos).getFlightId());
        entity.setWorkerId(UserInfoSingle.getInstance().getUserId());
        entity.setRemark(remark);
        ((ReOpenLoadTaskPresenter) mPresenter).reOpenLoadTask(entity);
    }

    /**
     * CommonDialog 的用法
     */
    private void showDialog(final int pos) {
        InputDialog dialog1 = new InputDialog(getActivity());
        dialog1.setTitle("开舱门原因")
                .setHint("请输入......")
                .setPositiveButton("取消")
                .setNegativeButton("确定")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new InputDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {

                        } else {
                            reOpenLoadTask(pos,dialog1.getMessage());
                        }
                    }
                })
                .show();
    }
    /**
     * 进入航班拍照界面
     * @param position
     */
    private void intoPhotoAct(int position) {
        Intent intent = new Intent(getActivity(), FlightPhotoRecordActivity.class);
        intent.putExtra("flight_number",mList.get(position).getFlightNo());
        intent.putExtra("flight_id",mList.get(position).getFlightId());
        intent.putExtra("task_id",mList.get(position).getId());
        intent.putExtra("task_pic",mList.get(position).getTaskPic());
        intent.putExtra("task_task_id",mList.get(position).getTaskId());
        getActivity().startActivity(intent);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        if (isVisibleToUser) {
            if (mTaskFragment != null)
                mTaskFragment.setTitleText(mCacheList.size());
            if (searchToolbar != null) {
                searchToolbar.setHintAndListener("请输入板车号", text -> {
                    searchString = text;
                    seachByText();
                });
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUserVisibleHint(true);
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
                if (item.getFlightNo().toLowerCase().contains(searchString.toLowerCase())) {
                    mList.add(item);
                }
            }
        }
        if (mMfrvData != null) {
            mMfrvData.notifyForAdapter(mAdapter);
        }
    }

    private void loadData() {
        mPresenter = new LoadUnloadTaskHisPresenter(this);
        BaseFilterEntity<DoneTaskEntity> entity = new BaseFilterEntity();
        DoneTaskEntity doneTaskEntity = new DoneTaskEntity();
        doneTaskEntity.setOperatorId(UserInfoSingle.getInstance().getUserId());
        entity.setCurrent(mCurrentPage);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setFilter(doneTaskEntity);
        ((LoadUnloadTaskHisPresenter) mPresenter).loadUnloadTaskHis(entity);
    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据……");
        new Handler().postDelayed(() -> {
            mCurrentPage = 1;
            loadData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        loadData();
    }

    @Override
    public void onLoadMore() {
        loadData();
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
    public void loadUnloadTaskHisResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {

        if (mCurrentPage == 1) {
            mCacheList.clear();
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
        mCurrentPage++;
        for (LoadAndUnloadTodoBean bean : loadAndUnloadTodoBean) {
            StringUtil.setTimeAndType(bean);//设置对应的时间和时间图标显示
            StringUtil.setFlightRoute(bean.getRoute(), bean);//设置航班航线信息
            if (bean.getRelateInfoObj() != null) {
                StringUtil.setTimeAndType(bean.getRelateInfoObj());//设置对应的时间和时间图标显示
                StringUtil.setFlightRoute(bean.getRelateInfoObj().getRoute(), bean.getRelateInfoObj());//设置航班航线信息
            }
            //将服务器返回的领受时间、到位时间、开舱门时间、开始装卸机-结束装卸机时间、关闭舱门时间用数组存储，遍历时发现“0”或包含“：0”出现，则对应的步骤数为当前下标
            List<String> times = new ArrayList<>();
            times.add(String.valueOf(bean.getAcceptTime()));
            times.add(String.valueOf(bean.getArrivalTime()));
            times.add(String.valueOf(bean.getOpenDoorTime()));
            if (bean.getTaskType() == 1) {//装机
                times.add(bean.getStartLoadTime() + ":" + bean.getEndLoadTime());
            } else if (bean.getTaskType() == 2) {//卸机
                times.add(bean.getStartUnloadTime() + ":" + bean.getEndUnloadTime());
            } else {//装卸机
                times.add(bean.getStartUnloadTime() + ":" + bean.getEndUnloadTime());
                times.add(bean.getStartLoadTime() + ":" + bean.getEndLoadTime());
            }
            times.add(String.valueOf(bean.getCloseDoorTime()));
            bean.setAcceptTask(true);//已经领受过任务
            for (int i = 0; i < bean.getOperationStepObj().size(); i++) {
                LoadAndUnloadTodoBean.OperationStepObjBean entity1 = bean.getOperationStepObj().get(i);
                entity1.setFlightType(bean.getFlightType());
                entity1.setItemType(Constants.TYPE_STEP_OVER);
                if (bean.getTaskType() == 1 || bean.getTaskType() == 2) {//装机或卸机
                    if (i == 3) {
                        entity1.setOperationName(entity1.getOperationName());
                        String[] timeArray = times.get(i).split(":");
                        String start = ("0".equals(timeArray[0])) ? "" : sdf.format(new Date(Long.valueOf(timeArray[0])));
                        String end = ("0".equals(timeArray[1])) ? "" : sdf.format(new Date(Long.valueOf(timeArray[1])));
                        entity1.setStepDoneDate(start + "-" + end);
                    } else if (i == 4) {
                        continue;
                    } else {
                        entity1.setOperationName(entity1.getOperationName());
                        entity1.setStepDoneDate(sdf.format(new Date(Long.valueOf(times.get((i == 5) ? 4 : i)))));//步骤为6，时间选用第5个
                    }
                } else {//装卸机一体任务
                    if (i == 3) {
                        entity1.setOperationName(entity1.getOperationName());
                        String[] timeArray = times.get(i).split(":");
                        String start = ("0".equals(timeArray[0])) ? "" : sdf.format(new Date(Long.valueOf(timeArray[0])));
                        String end = ("0".equals(timeArray[1])) ? "" : sdf.format(new Date(Long.valueOf(timeArray[1])));
                        entity1.setStepDoneDate(start + "-" + end);
                    } else if (i == 4 || i == 6) {
                        continue;
                    } else if (i == 5) {
                        entity1.setOperationName(entity1.getOperationName());
                        String[] timeArray = times.get(4).split(":");
                        String start = ("0".equals(timeArray[0])) ? "" : sdf.format(new Date(Long.valueOf(timeArray[0])));
                        String end = ("0".equals(timeArray[1])) ? "" : sdf.format(new Date(Long.valueOf(timeArray[1])));
                        entity1.setStepDoneDate(start + "-" + end);
                    } else {
                        entity1.setOperationName(entity1.getOperationName());
                        entity1.setStepDoneDate(sdf.format(new Date(Long.valueOf(times.get((i == 7) ? 5 : i)))));//步骤为7，时间选用第6个
                    }
                }
            }
            if (bean.getTaskType() == 1 || bean.getTaskType() == 2) {
                bean.getOperationStepObj().remove(4);
            } else {
                bean.getOperationStepObj().remove(4);
                bean.getOperationStepObj().remove(5);
            }
            mCacheList.add(bean);
        }
        seachByText();
        if (mTaskFragment != null) {
            if (isShow)
                mTaskFragment.setTitleText(mCacheList.size());
        }
    }

    @Override
    public void reOpenLoadTaskResult(String result) {
        if (result != null){
            ToastUtil.showToast("操作成功");
        }
        mCurrentPage = 1;
        loadData();
    }
}
