package qx.app.freight.qxappfreight.fragment;

import android.app.Dialog;
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

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.FlightPhotoRecordActivity;
import qx.app.freight.qxappfreight.adapter.InstallEquipLeaderAdapter;
import qx.app.freight.qxappfreight.adapter.LeaderInstallEquipStepAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.LoadUnLoadTaskPushBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListRequestEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.request.TaskClearEntity;
import qx.app.freight.qxappfreight.bean.request.UnLoadRequestEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.CargoCabinData;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadingAndUnloadBean;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.bean.response.UnLoadListBillBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetFlightCargoResContract;
import qx.app.freight.qxappfreight.contract.GetUnLoadListBillContract;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.contract.LoadUnloadLeaderToDoContract;
import qx.app.freight.qxappfreight.dialog.InstallSuggestPushDialog;
import qx.app.freight.qxappfreight.dialog.PushLoadUnloadLeaderDialog;
import qx.app.freight.qxappfreight.dialog.UnloadBillInfoDialog;
import qx.app.freight.qxappfreight.presenter.GetFlightCargoResPresenter;
import qx.app.freight.qxappfreight.presenter.GetUnLoadListBillPresenter;
import qx.app.freight.qxappfreight.presenter.LoadAndUnloadTodoPresenter;
import qx.app.freight.qxappfreight.presenter.LoadUnloadToDoLeaderPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.IMUtils;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 装卸机小组长代办fragment
 */
public class InstallEquipLeaderFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, LoadUnloadLeaderToDoContract.LoadUnloadLeaderToDoView, EmptyLayout.OnRetryLisenter, LoadAndUnloadTodoContract.loadAndUnloadTodoView, GetUnLoadListBillContract.IView, GetFlightCargoResContract.getFlightCargoResView {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private List <LoadAndUnloadTodoBean> mList = new ArrayList <>();
    private List <LoadAndUnloadTodoBean> mCacheList = new ArrayList <>();
    private int mCurrentPage = 1;
    private int mCurrentSize = 10;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
    private LeaderInstallEquipStepAdapter mSlideAdapter;
    private int mOperatePos;
    private List <LoadAndUnloadTodoBean> mListCache = new ArrayList <>();//推送的缓存任务
    private List <LoadAndUnloadTodoBean> mListCacheUse = new ArrayList <>(); //领受拒绝任务显示使用
    private InstallEquipLeaderAdapter mAdapter;

    private String searchString = "";//条件搜索关键字
    private TaskFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow = false;

    private PushLoadUnloadLeaderDialog mDialog = null;
    private List <String> mTaskIdList = new ArrayList <>();
    private String mSpecialTaskId = null;//专门记录由点击了结束装机或卸机返回刷新数据的taskId，匹配到该taskId则item应该展开
    private int loadInstall = 0; //请求装机单标记 1 ：装机单

    private int currentPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_install_equip, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoadUnLoadTaskPushBean result) {
        if (result != null) {
            List <LoadUnLoadTaskPushBean.TaskDataBean.StaffListBean> list = result.getTaskData().getStaffList();
            StringBuilder addMembers = new StringBuilder();
            StringBuilder removeMembers = new StringBuilder();
            int addNumber = 0, removeNumber = 0;
            if (list != null) {
                for (LoadUnLoadTaskPushBean.TaskDataBean.StaffListBean data : list) {
                    if (data.getOperationType() == 1) {
                        addMembers.append(data.getStaffName());
                        addMembers.append("、");
                        addNumber += 1;
                    }
                    if (data.getOperationType() == 2) {
                        removeMembers.append(data.getStaffName());
                        removeMembers.append("、");
                        removeNumber += 1;
                    }
                }
            }
            String toast = "";
            if (addNumber != 0 && removeNumber != 0) {
                toast = result.getTaskData().getSeat() + "机位" + result.getTaskData().getFlightNo() + "航班装卸任务新增了" + addNumber +
                        "个成员(" + addMembers.toString().substring(0, addMembers.toString().length() - 1) + ")," + "移除了" + removeNumber + "个成员(" +
                        removeMembers.toString().substring(0, removeMembers.toString().length() - 1) + ")";
            }
            if (addNumber == 0 && removeNumber != 0) {
                toast = result.getTaskData().getSeat() + "机位" + result.getTaskData().getFlightNo() + "航班装卸任务移除了" + removeNumber + "个成员(" +
                        removeMembers.toString().substring(0, removeMembers.toString().length() - 1) + ")";
            }
            if (addNumber != 0 && removeNumber == 0) {
                toast = result.getTaskData().getSeat() + "机位" + result.getTaskData().getFlightNo() + "航班装卸任务新增了" + addNumber +
                        "个成员(" + addMembers.toString().substring(0, addMembers.toString().length() - 1) + ")";
            }
            Log.e("tagTest", "toast===" + toast);
            ToastUtil.showToast(toast);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        if (result != null) {
            if (result.isChangeWorkerUser() || result.isSplitTask()) {//换人或拆分任务直接刷新代办列表
                loadData();
            } else if (result.isCancelFlag()) {
                if (!result.isConfirmTask()) {//不再保障任务，吐司提示航班任务取消保障
                    List <LoadAndUnloadTodoBean> list = result.getTaskData();
                    String flightName = list.get(0).getFlightNo();
                    ToastUtil.showToast("航班" + flightName + "任务已取消保障，数据即将重新刷新");
                    Observable.timer(300, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()) //等待300毫秒后调取代办接口，避免数据库数据错误
                            .subscribe(aLong -> {
                                loadData();
                            });
                } else {//取消任务，刷新代办列表
                    loadData();
                }
            } else {//新任务推送，筛选最新数据再添加进行展示
                List <LoadAndUnloadTodoBean> list = result.getTaskData();
                for (LoadAndUnloadTodoBean bean : list) {
                    for (LoadAndUnloadTodoBean bean1 : mListCache) {
                        if (bean.getTaskId().equals(bean1.getTaskId())) {//如果新任务id==旧任务id就删除
                            mListCache.remove(bean1);
                        }
                    }
                    mListCache.add(bean); //添加新任务到旧任务列表
                }
                //任务列表同 代办列表比对，如果待办列表含有同样的数据，则删除任务列表中对应数据
                for (LoadAndUnloadTodoBean bean : mListCache) {
                    if (mTaskIdList.contains(bean.getTaskId())) {//删除代办列表中已经展示的数据，目的在于推送过来新任务弹窗提示时如果收到任务动态信息，需要将修改后的任务信息展示出来
                        mListCache.remove(bean);
                    }
                }
                showDialogTask();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if ("refresh_data_update".equals(result)||result.contains("InstallEquipLeaderFragment_refresh")) {
            mCurrentPage = 1;
            loadData();
        }
    }


    /**
     * mListCache 为0 就不展示
     */
    private void showDialogTask() {
        if ((mDialog != null && mDialog.isShowing()) || mListCache.size() == 0)
            return;
        mListCacheUse.clear();
        mListCacheUse.add(mListCache.get(0));
        mListCache.remove(0);
        mDialog = new PushLoadUnloadLeaderDialog(getContext(), R.style.custom_dialog, mListCacheUse, status -> {
            switch (status) {
                case 0://成功领受后吐司提示
                    mDialog.dismiss();
                    ToastUtil.showToast("领受新装卸任务成功");
                    mListCacheUse.clear();
//                    mDialog = null;
                    loadData();
                    showDialogTask();
                    break;
                case 1://拒绝任务后清除taskId记录
                    mDialog.dismiss();
                    mTaskIdList.remove(mListCacheUse.get(0).getTaskId());
                    mListCacheUse.clear();
                    if (isShow) {
                        mTaskFragment.setTitleText(mTaskIdList.size());
                        mMfrvData.notifyForAdapter(mAdapter);
                    }
//                    mDialog = null;
                    showDialogTask();
                    break;
                case -1://领受失败后，清空未领受列表缓存
                    Log.e("tagPush", "推送出错了");
                    mDialog.dismiss();
                    mListCacheUse.clear();
                    break;
            }
        });
        if (!mDialog.isShowing()) {//新任务弹出框未显示在屏幕中
            mDialog.show();//显示新任务弹窗
        }
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
        mAdapter = new InstallEquipLeaderAdapter(mList,false,false,true,true);
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
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setWorkerId(UserInfoSingle.getInstance().getUserId());
        entity.setCurrent(mCurrentPage);
        entity.setSize(mCurrentSize);
        mPresenter = new LoadUnloadToDoLeaderPresenter(this);
        ((LoadUnloadToDoLeaderPresenter) mPresenter).getLoadUnloadLeaderToDo(entity);
    }
    /**
     * 查看卸机单
     *
     * @param position
     */
    private void lookUnloadInstall(int position) {

//        Intent intent = new Intent(mContext, UnloadPlaneActivity.class);
//        intent.putExtra("flight_type", mList.get(position).getFlightType());
//        intent.putExtra("plane_info", mList.get(position));
//        mContext.startActivity(intent);
        loadInstall = 1;
        mPresenter = new GetUnLoadListBillPresenter(this);
        UnLoadRequestEntity entity = new UnLoadRequestEntity();
        entity.setUnloadingUser(UserInfoSingle.getInstance().getUserId());
        entity.setFlightId(mList.get(position).getFlightId());
        String userName = UserInfoSingle.getInstance().getUsername();
        entity.setOperationUserName((userName.contains("-")) ? userName.substring(0, userName.indexOf("-")) : userName);
        ((GetUnLoadListBillPresenter) mPresenter).getUnLoadingList(entity);
    }
    /**
     * 查看装机单
     */
    private void lookLoadInstall(int position) {

//        Intent intent = new Intent(mContext, LoadPlaneActivity.class);
//        intent.putExtra("plane_info", mList.get(position));
//        if ( mList.get(position).getTaskType() == 5)
//            intent.putExtra("position", 5);
//        else {
//            intent.putExtra("position", 3);
//        }
//        mContext.startActivity(intent);
        mPresenter = new GetFlightCargoResPresenter(this);
        LoadingListRequestEntity entity = new LoadingListRequestEntity();
        entity.setDocumentType(2);
        if (mList.get(position).getMovement() == 4 && mList.get(position).getRelateInfoObj() != null) {
            entity.setFlightId(mList.get(position).getRelateInfoObj().getFlightId());
        }
        else {
            entity.setFlightId(mList.get(position).getFlightId());
        }
        ((GetFlightCargoResPresenter) mPresenter).getLoadingList(entity);
    }
    /**
     * 进入航班拍照界面
     *
     * @param position
     */
    private void intoPhotoAct(int position) {
        Intent intent = new Intent(getActivity(), FlightPhotoRecordActivity.class);
        intent.putExtra("flight_number", mList.get(position).getFlightNo());
        intent.putExtra("flight_id", mList.get(position).getFlightId());
        intent.putExtra("task_id", mList.get(position).getId());
        intent.putExtra("task_pic", mList.get(position).getTaskPic());
        intent.putExtra("task_task_id", mList.get(position).getTaskId());

        getActivity().startActivity(intent);
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
    public void getLoadUnloadLeaderToDoResult(List <LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {
        mTaskIdList.clear();
        mCacheList.clear();
        if (mCurrentPage == 1) {
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
        mCurrentPage++;
        for (LoadAndUnloadTodoBean bean : loadAndUnloadTodoBean) {
            mTaskIdList.add(bean.getTaskId());
            //原始装卸机数据封装成InstallEquipEntity
            if (mSpecialTaskId != null && mSpecialTaskId.equals(bean.getTaskId())) {//mSpecialTaskId不为空，则说明进去过装机卸机页面点击过结束装机或卸机，回到代办列表页面，该值对应的数据应该默认展开
                bean.setShowDetail(true);
                mSpecialTaskId = null;
            }
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
            List <String> times = new ArrayList <>();
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
            int posNow = 0;//判断当前代办任务应该进行哪一步的int值
            for (int i = 0; i < times.size(); i++) {
                String timeNow = times.get(i);
                if ("0".equals(timeNow)) {
                    posNow = i;
                    break;
                } else {
                    if (i == 3) {
                        posNow = 4;
                    }
                }
            }
            if (posNow > 0) {
                bean.setAcceptTask(true);//已经领受过任务
            } else {
                bean.setAcceptTask(false);//没有领受过任务
            }
            for (int i = 0; i < bean.getOperationStepObj().size(); i++) {
                LoadAndUnloadTodoBean.OperationStepObjBean entity1 = bean.getOperationStepObj().get(i);
                entity1.setTaskId(bean.getTaskId());
                entity1.setFlightType(bean.getFlightType());
                int type;
                if (i < posNow) {//在应该执行的步骤前，类型为已执行
                    type = Constants.TYPE_STEP_OVER;
                } else if (i == posNow) {//是应该执行的步骤，类型为当前执行
                    type = Constants.TYPE_STEP_NOW;
                } else {//否则是未执行
                    type = Constants.TYPE_STEP_NEXT;
                }
                entity1.setItemType(type);
                entity1.setStepDoneDate("0".equals(times.get(i)) ? "" : sdf.format(new Date(Long.valueOf(times.get(i)))));
                entity1.setPlanTime(bean.getOperationStepObj().get(i).getPlanTime()==null||"0".equals(bean.getOperationStepObj().get(i).getPlanTime()) ? "" : sdf.format(new Date(Long.valueOf(bean.getOperationStepObj().get(i).getPlanTime()))));
            }

            mCacheList.add(bean);
        }
        mListCache.clear();
        //过滤掉已经重复展示的 dialog
        filtDialog();

        if (mListCache.size() != 0) {
//            mCacheList.removeAll(mListCache);
            showDialogTask();
        } else {
            seachByText();
            setMoreListener();
        }
        if (mTaskFragment != null) {
            if (isShow)
                mTaskFragment.setTitleText(mCacheList.size());
        }
    }

    /**
     * //过滤掉已经重复展示的 dialog
     */
    private synchronized void filtDialog() {
        for (LoadAndUnloadTodoBean bean : mCacheList) {
            if (!bean.isAcceptTask()) {
                boolean isInclude = false;
                for (LoadAndUnloadTodoBean loadAndUnloadTodoBean1 : mListCache) {
                    if (loadAndUnloadTodoBean1.getTaskId().equals(bean.getTaskId())) {
                        isInclude = true;
                    }
                }
                if (!isInclude) {
                    mListCache.add(bean);
                }
            }
        }
    }

    /**
     * 设置滑动监听
     */
    private void setMoreListener() {
        mAdapter.setOnSlideStepListener((bigPos, adapter, smallPos) -> {
            mOperatePos = smallPos;
            mSlideAdapter = adapter;
            go2SlideStep(bigPos, mList.get(bigPos).getOperationStepObj().get(smallPos).getOperationCode());
        });
//        mAdapter.setOnClearSeatListener(position -> startClearTask(position));
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
                intoPhotoAct(position);
            }

            @Override
            public void onLookUnloadInstall(int position) {
                lookUnloadInstall(position);
            }

            @Override
            public void onLookLoadInstall(int position) {
                currentPosition = position;
                lookLoadInstall(position);
            }
        });
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
    /**
     * 去滑动操作
     *
     * @param bigPos 代办列表中的位置下标
     * @param code   操作码
     */
    private void go2SlideStep(int bigPos, String code) {
        PerformTaskStepsEntity entity = new PerformTaskStepsEntity();
        entity.setType(1);
        entity.setLoadUnloadDataId(mList.get(bigPos).getId());
        entity.setFlightId(Long.valueOf(mList.get(bigPos).getFlightId()));
        entity.setFlightTaskId(mList.get(bigPos).getTaskId());
        entity.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude()+"");
        entity.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude()+"");
        entity.setOperationCode(code);
        entity.setTerminalId(DeviceInfoUtil.getDeviceInfo(getContext()).get("deviceId"));
        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        entity.setUserName(UserInfoSingle.getInstance().getUsername());
        entity.setCreateTime(System.currentTimeMillis());
        mPresenter = new LoadUnloadToDoLeaderPresenter(this);
        ((LoadUnloadToDoLeaderPresenter) mPresenter).slideTask(entity);
    }

    @Override
    public void loadAndUnloadTodoResult(List <LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {

    }

    @Override
    public void slideTaskResult(String result) {
        if ("正确".equals(result)) {
            mSlideAdapter.notifyDataSetChanged();
            if (mOperatePos == 3) {
                mCurrentPage = 1;
                loadData();
                mOperatePos = 0;
            }
        }
    }

    @Override
    public void startClearTaskResult(String result) {
        ToastUtil.showToast("操作成功");
    }

    @Override
    public void toastView(String error) {
        if (loadInstall == 1)
            ToastUtil.showToast("暂无装机单");

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
    public void getLoadingListResult(LoadingListBean result) {
        if ("318".equals(result.getStatus())) {
            ToastUtil.showToast("装机单未就绪");
        } else {
            if (result.getData() != null || result.getData().size() != 0) {

                if (!TextUtils.isEmpty(result.getData().get(0).getContent())) {
                    Gson mGson = new Gson();
                    LoadingListBean.DataBean.ContentObjectBean[] datas = mGson.fromJson(result.getData().get(0).getContent(), LoadingListBean.DataBean.ContentObjectBean[].class);
                    //舱位集合
                    List <LoadingListBean.DataBean.ContentObjectBean> mBaseContent = new ArrayList <>(Arrays.asList(datas));

                    if (mBaseContent != null && mBaseContent.size() > 0) {
                        List <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> scooters = new ArrayList <>();
                        for (LoadingListBean.DataBean.ContentObjectBean mContentObjectBean : mBaseContent) {
                            scooters.addAll(mContentObjectBean.getScooters());
                        }
                        InstallSuggestPushDialog updatePushDialog = new InstallSuggestPushDialog(getActivity(), R.style.custom_dialog, scooters, mList.get(currentPosition).getRelateInfoObj() != null ? mList.get(currentPosition).getRelateInfoObj().getFlightNo() : mList.get(currentPosition).getFlightNo(), false, () -> {
                        });
                        updatePushDialog.show();
                    }
                }
            } else {
                ToastUtil.showToast("装机单未就绪");
            }
        }
    }

    @Override
    public void setFlightSpace(CargoCabinData result) {

    }

    @Override
    public void flightDoneInstallResult(String result) {

    }

    @Override
    public void overLoadResult(String result) {

    }

    @Override
    public void confirmLoadPlanResult(String result) {

    }

    @Override
    public void getPullStatusResult(BaseEntity <String> result) {

    }

    @Override
    public void getUnLoadingListResult(UnLoadListBillBean result) {
        if (result != null) {
            if (result.getData() != null) {
                List <UnLoadListBillBean.DataBean.ContentObjectBean> list = result.getData().getContentObject();
                UnloadBillInfoDialog unloadBillInfoDialog = new UnloadBillInfoDialog();
                unloadBillInfoDialog.setData(list, getActivity());
                unloadBillInfoDialog.show(getActivity().getSupportFragmentManager(), "unload_bill");
            } else {
                ToastUtil.showToast(result.getMessage());
            }
        }
    }

    @Override
    public void getUnLoadDoneScooterResult(List <TransportTodoListBean> result) {

    }
}
