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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.FlightPhotoRecordActivity;
import qx.app.freight.qxappfreight.activity.LoadPlaneActivity;
import qx.app.freight.qxappfreight.activity.UnloadPlaneActivity;
import qx.app.freight.qxappfreight.adapter.NewInstallEquipAdapter;
import qx.app.freight.qxappfreight.adapter.NewInstallEquipStepAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.request.TaskClearEntity;
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
import qx.app.freight.qxappfreight.dialog.InstallSuggestPushDialog;
import qx.app.freight.qxappfreight.dialog.PushLoadUnloadDialog;
import qx.app.freight.qxappfreight.dialog.UnloadBillInfoDialog;
import qx.app.freight.qxappfreight.presenter.LoadAndUnloadTodoPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.IMUtils;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 装机fragment
 */
public class InstallEquipFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, LoadAndUnloadTodoContract.loadAndUnloadTodoView, GetUnLoadListBillContract.IView, GetFlightCargoResContract.getFlightCargoResView, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private List <LoadAndUnloadTodoBean> mList = new ArrayList <>();
    private List <LoadAndUnloadTodoBean> mCacheList = new ArrayList <>();
    private int mCurrentPage = 1;
    private int mCurrentSize = 10;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
    private NewInstallEquipStepAdapter mSlideAdapter;
    private int mOperatePos;
    private List <LoadAndUnloadTodoBean> mListCache = new ArrayList <>();
    private NewInstallEquipAdapter mAdapter;

    private String searchString = "";//条件搜索关键字
    private TaskFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow = false;

    private PushLoadUnloadDialog mDialog = null;
    private List <String> mTaskIdList = new ArrayList <>();
    private String mSpecialTaskId = null;//专门记录由点击了结束装机或卸机返回刷新数据的taskId，匹配到该taskId则item应该展开

    private int currentPosition = 0;

    private int loadInstall = 0; //请求装机单标记 1 ：装机单

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_install_equip, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
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
//                    Observable.timer(300, TimeUnit.MILLISECONDS)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread()) //等待300毫秒后调取代办接口，避免数据库数据错误
//                            .subscribe(aLong -> {
//                                loadData();
//                            });
                    mCurrentPage = 1;
                    loadData();
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
                showTaskDialog();
            }
        }
    }

    private void showTaskDialog() {
        if ((mDialog != null && mDialog.isShowing()) || mListCache.size() == 0)
            return;
        if (mDialog == null) {
            mDialog = new PushLoadUnloadDialog(getContext(), R.style.custom_dialog, mListCache, success -> {
                if (success) {//成功领受后吐司提示，
                    ToastUtil.showToast("领受装卸机新任务成功");
                } else {//领受失败后，清空未领受列表缓存
                    Log.e("tagPush", "领受失败");
                }
                mDialog.dismiss();
                mCurrentPage = 1;
                loadData();
                mListCache.clear();
                mDialog = null;
            });
        }
//        mDialog.setData(getContext(), mListCache, success -> {
////
////            Tools.closeVibrator(getActivity().getApplicationContext());
////        });
        if (!mDialog.isShowing()) {//新任务弹出框未显示在屏幕中
            if (mTaskIdList.contains(mListCache.get(0).getTaskId())) {//代办列表中有当前推送过来的任务，则不弹窗提示，只是刷新页面
                loadData();
                mListCache.clear();
            } else {
//                Tools.startVibrator(getActivity().getApplicationContext(), true, R.raw.ring);
                mDialog.show();//显示新任务弹窗
            }
        } else {//刷新任务弹出框中的数据显示
            mDialog.refreshData();
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
        mAdapter = new NewInstallEquipAdapter(mList, false, true, true,true);
        mMfrvData.setAdapter(mAdapter);
        mAdapter.setOnFlightSafeguardListenner(new NewInstallEquipAdapter.OnFlightSafeguardListenner() {
            @Override
            public void onFlightSafeguardClick(int position) {
                IMUtils.chatToGroup(mContext, Tools.groupImlibUid(mList.get(position)) + "");
            }

            @Override
            public void onClearClick(int position) {
                startClearTask(position);
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
        loadData();
        setUserVisibleHint(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUserVisibleHint(true);
    }

    /**
     * 查看卸机单
     *
     * @param position
     */
    private void lookUnloadInstall(int position) {

        Intent intent = new Intent(mContext, UnloadPlaneActivity.class);
        intent.putExtra("flight_type", mList.get(position).getFlightType());
        intent.putExtra("plane_info", mList.get(position));
        mContext.startActivity(intent);
//        loadInstall = 1;
//        mPresenter = new GetUnLoadListBillPresenter(this);
//        UnLoadRequestEntity entity = new UnLoadRequestEntity();
//        entity.setUnloadingUser(UserInfoSingle.getInstance().getUserId());
//        entity.setFlightId(mList.get(position).getFlightId());
//        String userName = UserInfoSingle.getInstance().getUsername();
//        entity.setOperationUserName((userName.contains("-")) ? userName.substring(0, userName.indexOf("-")) : userName);
//        ((GetUnLoadListBillPresenter) mPresenter).getUnLoadingList(entity);
    }

    /**
     * 查看装机单
     */
    private void lookLoadInstall(int position) {

        Intent intent = new Intent(mContext, LoadPlaneActivity.class);
        intent.putExtra("plane_info", mList.get(position));
        if ( mList.get(position).getTaskType() == 5)
            intent.putExtra("position", 5);
        else {
            intent.putExtra("position", 3);
        }
        mContext.startActivity(intent);
//        mPresenter = new GetFlightCargoResPresenter(this);
//        LoadingListRequestEntity entity = new LoadingListRequestEntity();
//        entity.setDocumentType(2);
//        if (mList.get(position).getMovement() == 4 && mList.get(position).getRelateInfoObj() != null) {
//            entity.setFlightId(mList.get(position).getRelateInfoObj().getFlightId());
//        }
//        else {
//            entity.setFlightId(mList.get(position).getFlightId());
//        }
//        ((GetFlightCargoResPresenter) mPresenter).getLoadingList(entity);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result.contains("InstallEquipFragment_refresh") || "refresh_data_update".equals(result)) {
            if (result.contains("@"))
                mSpecialTaskId = result.split("@")[1];
            mCurrentPage = 1;
            loadData();
        }
    }

    private void loadData() {
        loadInstall = 0;
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setWorkerId(UserInfoSingle.getInstance().getUserId());
        entity.setCurrent(mCurrentPage);
        entity.setSize(mCurrentSize);
        mPresenter = new LoadAndUnloadTodoPresenter(this);
        ((LoadAndUnloadTodoPresenter) mPresenter).LoadAndUnloadTodo(entity);
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
    public void loadAndUnloadTodoResult(List <LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {
        mTaskIdList.clear();
        List <Boolean> checkedList = new ArrayList <>();
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
                if (!StringUtil.isEmpty(bean.getRelateInfoObj().getLoadingAndUnloadExtJson())) {
                    bean.getRelateInfoObj().setLoadingAndUnloadBean(JSON.parseObject(bean.getRelateInfoObj().getLoadingAndUnloadExtJson(), LoadingAndUnloadBean.class));
                }
            }
            if (!StringUtil.isEmpty(bean.getLoadingAndUnloadExtJson())) {
                bean.setLoadingAndUnloadBean(JSON.parseObject(bean.getLoadingAndUnloadExtJson(), LoadingAndUnloadBean.class));
            }
            //将服务器返回的领受时间、到位时间、开舱门时间、开始装卸机-结束装卸机时间、关闭舱门时间用数组存储，遍历时发现“0”或包含“：0”出现，则对应的步骤数为当前下标
            List <String> times = new ArrayList <>();
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
            int posNow = 0;//判断当前代办任务应该进行哪一步的int值
            boolean hasChecked = false;
            for (int i = 0; i < times.size(); i++) {
                String timeNow = times.get(i);
                if ("0".equals(timeNow)) {
                    posNow = i;
                    break;
                } else if (timeNow.contains(":0")) {//至少跳转到装机或卸机页面去过，不过没有点击结束装机或卸机
                    if (!timeNow.equals("0:0")) {//timeNow的格式为“1990000:0”，说明进过装机或卸机页面，但是按返回按钮等退出页面了
                        hasChecked = true;
                    }
                    posNow = i;
                    break;
                }
            }
            if (posNow > 0) {
                bean.setAcceptTask(true);//已经领受过任务
            } else {
                bean.setAcceptTask(false);//没有领受过任务
            }
            checkedList.add(hasChecked);//总共有10条数据，则生产10条布尔值的list，出现了进过装机或卸机页面的话值就是true，监听中就去判断true作不再调步骤接口的操作
            for (int i = 0; i < bean.getOperationStepObj().size(); i++) {

                bean.getOperationStepObj().get(i).setPlanTime(bean.getOperationStepObj().get(i).getPlanTime()==null||"0".equals(bean.getOperationStepObj().get(i).getPlanTime()) ? "" : sdf.format(new Date(Long.valueOf(bean.getOperationStepObj().get(i).getPlanTime()))));
                if (i == 5) continue;//下标为5时，需要跳过，进入下一轮循环，对应的操作code为FreightUnloadFinish
                if (i == 7) {//下标为7时，特殊处理
                    LoadAndUnloadTodoBean.OperationStepObjBean entity1 = bean.getOperationStepObj().get(i);
                    entity1.setFlightType(bean.getFlightType());
                    if (posNow == 5) {
                        entity1.setItemType(Constants.TYPE_STEP_NOW);
                    }
                } else {
                    int index = i;
                    if (bean.getOperationStepObj().get(i).getOperationCode().equals("FreightUnloadFinish") || bean.getOperationStepObj().get(i).getOperationCode().equals("FreightLoadFinish")) {
                        index++;//筛选卸机结束和装机结束的步骤项
                    }
                    LoadAndUnloadTodoBean.OperationStepObjBean entity1 = bean.getOperationStepObj().get(index);
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
                    if (i == 3 || (bean.getTaskType() == 5 && i == 4)) {//只要位置是第四步，或者代办类型是装卸机一体并且位置是第五步，则需要根据服务器传回的时间显示成指定的格式
                        String[] timeArray = times.get(i).split(":");
                        String start = ("0".equals(timeArray[0])) ? "" : sdf.format(new Date(Long.valueOf(timeArray[0])));
                        String end = ("0".equals(timeArray[1])) ? "" : sdf.format(new Date(Long.valueOf(timeArray[1])));
                        entity1.setStepDoneDate(start + "-" + end);
                    } else {
                        int listIndex;
                        if (i == 6) {//下标为6时，时间显示必须设置为第6个时间
                            listIndex = 5;
                        } else {
                            listIndex = i;
                        }
                        entity1.setStepDoneDate("0".equals(times.get(listIndex)) ? "" : sdf.format(new Date(Long.valueOf(times.get(listIndex)))));
                    }
                }

            }
            List <String> codeList = new ArrayList <>();
            for (int i = 0; i < bean.getOperationStepObj().size(); i++) {//不管哪种类型的代办，都需要将对应的操作步骤code记录成一个列表存在对应的item中
                String code = bean.getOperationStepObj().get(i).getOperationCode();
                if (!code.equals("FreightUnloadFinish") && !code.equals("FreightLoadFinish")) {//排除了装机结束和卸机结束的code，宽体机滑动开始卸机时会自动调取步骤接口生成卸机开始和卸机结束时间
                    codeList.add(code);
                }
            }
            Iterator iterator = bean.getOperationStepObj().iterator();//使用迭代器删除不需要的数据
            while (iterator.hasNext()) {
                LoadAndUnloadTodoBean.OperationStepObjBean codeBean = (LoadAndUnloadTodoBean.OperationStepObjBean) iterator.next();
                if (!codeList.contains(codeBean.getOperationCode())) {
                    iterator.remove();
                }
            }
            bean.setStepCodeList(codeList);
            mCacheList.add(bean);
        }
        seachByText();
        setSlideListener(checkedList);
        if (mTaskFragment != null) {
            if (isShow)
                mTaskFragment.setTitleText(mCacheList.size());
        }
    }

    /**
     * 设置滑动监听
     *
     * @param checkedList 是否滑动过当前步骤  列表
     */
    private void setSlideListener(List <Boolean> checkedList) {
        mAdapter.setOnSlideStepListener((bigPos, adapter, smallPos) -> {
            //滑动步骤去调接口，以及跳转页面
//            if ((smallPos == 3 || smallPos == 4) && checkedList.get(bigPos)) {//如果已经调过滑动开始装机或开始卸机步骤接口，再次滑动不去调接口
//                Log.e("tagTest", "已经开始装卸机，但是返回退出了页面！");
//            } else {
//                mOperatePos = smallPos;
//                mSlideAdapter = adapter;
//                if (smallPos == 3 && mList.get(bigPos).getWidthAirFlag() == 0) {//滑动卸机步骤时如果判断到是宽体机直接调用开始卸机和结束卸机，进行下一步操作
//                    String[] codes = {mList.get(bigPos).getStepCodeList().get(smallPos), "FreightUnloadFinish"};
//                    for (String code : codes) {
//                        go2SlideStep(bigPos, code);
//                    }
//                } else {
//                    go2SlideStep(bigPos, mList.get(bigPos).getStepCodeList().get(smallPos));
//                }
//            }
            mOperatePos = smallPos;
            mSlideAdapter = adapter;
            if (smallPos == 3) {//滑动卸机步骤时如果判断到是宽体机直接调用开始卸机和结束卸机，进行下一步操作
                String[] codes = {mList.get(bigPos).getStepCodeList().get(smallPos), "FreightUnloadFinish"};
                for (String code : codes) {
                    go2SlideStep(bigPos, code);
                }
            } else if (smallPos == 4) {
                String[] codes = {mList.get(bigPos).getStepCodeList().get(smallPos), "FreightLoadFinish"};
                for (String code : codes) {
                    go2SlideStep(bigPos, code);
                }
            } else {
                go2SlideStep(bigPos, mList.get(bigPos).getStepCodeList().get(smallPos));
            }
        });
    }


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
        mPresenter = new LoadAndUnloadTodoPresenter(this);
        ((LoadAndUnloadTodoPresenter) mPresenter).slideTask(entity);
    }

    @Override
    public void slideTaskResult(String result) {
        if ("正确".equals(result)) {
            mSlideAdapter.notifyDataSetChanged();
            //如果是滑动的第一步，则代表任务由未领受变成了领受，则需要刷新整个页面，将该item的背景由黄色改为白色
            //单独装机或卸机任务滑动的是第4步，需要刷新数据关闭舱门；装卸机连班航班任务滑动第5步，同理
            if (mOperatePos == 0 || mOperatePos == 4 || mOperatePos == 5) {
                mCurrentPage = 1;
                loadData();
                mOperatePos = 0;
            }
        }
    }

    /**
     * 清场任务 发起返回
     *
     * @param result
     */
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
}
