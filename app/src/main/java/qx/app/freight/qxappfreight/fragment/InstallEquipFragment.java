package qx.app.freight.qxappfreight.fragment;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import qx.app.freight.qxappfreight.adapter.InstallEquipAdapter;
import qx.app.freight.qxappfreight.adapter.InstallEquipStepAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.InstallEquipEntity;
import qx.app.freight.qxappfreight.bean.MultiStepEntity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.dialog.PushLoadUnloadDialog;
import qx.app.freight.qxappfreight.presenter.LoadAndUnloadTodoPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 装机fragment
 */
public class InstallEquipFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, LoadAndUnloadTodoContract.loadAndUnloadTodoView, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private List<InstallEquipEntity> mList = new ArrayList<>();
    private List<InstallEquipEntity> mCacheList = new ArrayList<>();
    private int mCurrentPage = 1;
    private int mCurrentSize = 10;
    private static final String[] mStepNamesInstall = {"领受", "到位", "开启舱门", "装机", "关闭舱门"};
    private static final String[] mStepNamesUninstall = {"领受", "到位", "开启舱门", "卸机", "关闭舱门"};
    private static final String[] mStepNamesInstallUninstall = {"领受", "到位", "开启舱门", "卸机", "装机", "关闭舱门"};
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
    private InstallEquipStepAdapter mSlideadapter;
    private int mOperatePos;
    private List<LoadAndUnloadTodoBean> mListCache = new ArrayList<>();
    private InstallEquipAdapter mAdapter;

    private String searchString = "";//条件搜索关键字
    private TaskFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow = false;

    private PushLoadUnloadDialog mDialog = null;
    private List<String> mTaskIdList = new ArrayList<>();
    private String mSpecialTaskId = null;//专门记录由点击了结束装机或卸机返回刷新数据的taskId，匹配到该taskId则item应该展开

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
                    List<LoadAndUnloadTodoBean> list = result.getTaskData();
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
                List<LoadAndUnloadTodoBean> list = result.getTaskData();
                List<String> pushTaskIds = new ArrayList<>();//将推送任务列表中所有的taskId保存起来存入pushTaskIds中
                for (LoadAndUnloadTodoBean bean : mListCache) {
                    pushTaskIds.add(bean.getTaskId());
                }
                List<String> removeTaskIds = new ArrayList<>();//将最新推送过来的数据的taskId保存起来
                for (LoadAndUnloadTodoBean bean : list) {
                    if (pushTaskIds.contains(bean.getTaskId())) {//如果已经存储过该taskId，则将对应的taskId记录下来以便删除重复数据
                        removeTaskIds.add(bean.getTaskId());
                    }
                }
                for (LoadAndUnloadTodoBean bean : mListCache) {
                    if (removeTaskIds.contains(bean.getTaskId())) {//删除重复的旧数据，更新新数据
                        mListCache.remove(bean);
                    }
                }
                mListCache.addAll(list);
                for (LoadAndUnloadTodoBean bean : mListCache) {
                    if (mTaskIdList.contains(bean.getTaskId())) {//删除代办列表中已经展示的数据，目的在于推送过来新任务弹窗提示时如果收到任务动态信息，需要将修改后的任务信息展示出来
                        mListCache.remove(bean);
                    }
                }
                if (mDialog == null) {
                    mDialog = new PushLoadUnloadDialog();
                }
                mDialog.setData(getContext(), mListCache, success -> {
                    if (success) {//成功领受后吐司提示，并延时300毫秒刷新代办列表
                        ToastUtil.showToast("领受装卸机新任务成功");
                        Observable.timer(300, TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> {
                                    loadData();
                                });
                        mListCache.clear();
                    } else {//领受失败后，清空未领受列表缓存
                        Log.e("tagPush", "推送出错了");
                        mListCache.clear();
                    }
                });
                if (!mDialog.isAdded()) {//新任务弹出框未显示在屏幕中
                    if (mTaskIdList.contains(list.get(0).getTaskId())) {//代办列表中有当前推送过来的任务，则不弹窗提示，只是刷新页面
                        loadData();
                        mListCache.clear();
                    } else {
                        mDialog.show(getFragmentManager(), "11");//显示新任务弹窗
                    }
                } else {//刷新任务弹出框中的数据显示
                    Observable.timer(300, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                mDialog.refreshData();
                            });
                }
            }
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
        mPresenter = new LoadAndUnloadTodoPresenter(this);
        mAdapter = new InstallEquipAdapter(mList);
        mMfrvData.setAdapter(mAdapter);
        loadData();
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

    /**
     * 根据搜索框输入检索对应的结果项
     */
    private void seachByText() {
        mList.clear();
        if (TextUtils.isEmpty(searchString)) {
            mList.addAll(mCacheList);
        } else {
            for (InstallEquipEntity item : mCacheList) {
                if (item.getFlightInfo().toLowerCase().contains(searchString.toLowerCase())) {
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
        if (result.contains("InstallEquipFragment_refresh")) {
            mSpecialTaskId = result.split("@")[1];
            loadData();
        }
    }

    private void loadData() {
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setWorkerId(UserInfoSingle.getInstance().getUserId());
        entity.setCurrent(mCurrentPage);
        entity.setSize(mCurrentSize);
        ((LoadAndUnloadTodoPresenter) mPresenter).LoadAndUnloadTodo(entity);
    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据……");
        new Handler().postDelayed(() -> {
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
    public void loadAndUnloadTodoResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {
        mTaskIdList.clear();
        List<Boolean> checkedList = new ArrayList<>();
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
            InstallEquipEntity entity = new InstallEquipEntity();
            entity.setWidePlane(bean.getWidthAirFlag() == 0);
            if (mSpecialTaskId != null && mSpecialTaskId.equals(bean.getTaskId())) {//mSpecialTaskId不为空，则说明进去过装机卸机页面点击过结束装机或卸机，回到代办列表页面，该值对应的数据应该默认展开
                entity.setShowDetail(true);
                mSpecialTaskId = null;
            }
            entity.setAirCraftNo(bean.getAircraftno());
            entity.setFlightInfo(bean.getFlightNo());
            entity.setSeat(bean.getSeat());
            entity.setTaskType(bean.getTaskType());//1，装机；2，卸机；5，装卸机
            entity.setFlightType(bean.getFlightType());
            entity.setId(bean.getId());
            entity.setFlightId(Long.valueOf(bean.getFlightId()));
            entity.setTaskId(bean.getTaskId());
            entity.setWorkerName(bean.getWorkerName());
            StringUtil.setTimeAndType(bean, entity);//设置对应的时间和时间图标显示
            StringUtil.setFlightRoute(bean.getRoute(), entity);//设置航班航线信息
            entity.setLoadUnloadType(bean.getTaskType());
            List<MultiStepEntity> data = new ArrayList<>();
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
                entity.setAcceptTask(true);//已经领受过任务
            } else {
                entity.setAcceptTask(false);//没有领受过任务
            }
            checkedList.add(hasChecked);//总共有10条数据，则生产10条布尔值的list，出现了进过装机或卸机页面的话值就是true，监听中就去判断true作不再调步骤接口的操作
            int size = (bean.getTaskType() == 1 || bean.getTaskType() == 2) ? 5 : 6;//如果是装机或卸机的话则总共步骤数为5，否则为6
            for (int i = 0; i < size; i++) {
                MultiStepEntity entity1 = new MultiStepEntity();
                entity1.setFlightType(entity.getFlightType());
                entity1.setLoadUnloadType(bean.getTaskType());
                if (bean.getTaskType() == 1) {//装机
                    entity1.setStepName(mStepNamesInstall[i]);
                } else if (bean.getTaskType() == 2) {//卸机
                    entity1.setStepName(mStepNamesUninstall[i]);
                } else {
                    entity1.setStepName(mStepNamesInstallUninstall[i]);
                }
                int type;
                if (i < posNow) {//在应该执行的步骤前，类型为已执行
                    type = 0;
                } else if (i == posNow) {//是应该执行的步骤，类型为当前执行
                    type = 1;
                } else {//否则是未执行
                    type = 2;
                }
                entity1.setItemType(type);
                entity1.setData(bean);
                if (i == 3 || (bean.getTaskType() == 5 && i == 4)) {//只要位置是第四步，或者代办类型是装卸机一体并且位置是第五步，则需要根据服务器传回的时间显示成指定的格式
                    String[] timeArray = times.get(i).split(":");
                    String start = ("0".equals(timeArray[0])) ? "" : sdf.format(new Date(Long.valueOf(timeArray[0])));
                    String end = ("0".equals(timeArray[1])) ? "" : sdf.format(new Date(Long.valueOf(timeArray[1])));
                    entity1.setStepDoneDate(start + "-" + end);
                } else {
                    entity1.setStepDoneDate("0".equals(times.get(i)) ? "" : sdf.format(new Date(Long.valueOf(times.get(i)))));
                }
                data.add(entity1);
            }
            List<String> codeList = new ArrayList<>();
            for (int i = 0; i < bean.getOperationStepObj().size(); i++) {//不管哪种类型的代办，都需要将对应的操作步骤code记录成一个列表存在对应的item中
                String code = bean.getOperationStepObj().get(i).getOperationCode();
                if (!code.equals("FreightUnloadFinish") && !code.equals("FreightLoadFinish")) {//排除了装机结束和卸机结束的code，宽体机滑动开始卸机时会自动调取步骤接口生成卸机开始和卸机结束时间
                    codeList.add(bean.getOperationStepObj().get(i).getOperationCode());
                }
            }
            entity.setStepCodeList(codeList);
            entity.setList(data);
            mCacheList.add(entity);
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
    private void setSlideListener(List<Boolean> checkedList) {
        mAdapter.setOnSlideStepListener((bigPos, adapter, smallPos) -> {
            //滑动步骤去调接口，以及跳转页面
            if ((smallPos == 3 || smallPos == 4) && checkedList.get(bigPos)) {//如果已经调过滑动开始装机或开始卸机步骤接口，再次滑动不去调接口
                Log.e("tagTest", "已经开始装卸机，但是返回退出了页面！");
            } else {
                mOperatePos = smallPos;
                mSlideadapter = adapter;
                if (smallPos == 3 && mList.get(bigPos).getList().get(smallPos).getData().getWidthAirFlag() == 0) {//滑动卸机步骤时如果判断到是宽体机直接调用开始卸机和结束卸机，进行下一步操作
                    String[] codes = {mList.get(bigPos).getStepCodeList().get(smallPos), "FreightUnloadFinish"};
                    for (String code : codes) {
                        go2SlideStep(bigPos, code);
                    }
                } else {
                    go2SlideStep(bigPos, mList.get(bigPos).getStepCodeList().get(smallPos));
                    if (smallPos == 0) {//如果是滑动的第一步，则代表任务由未领受变成了领受，则需要刷新整个页面，将该item的背景由黄色改为白色
                        Observable.timer(1, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()) // 延迟1秒去调接口获取代办数据，否则数据仍然为未领受
                                .subscribe(aLong -> loadData());
                    }
                }
            }
        });
    }


    private void go2SlideStep(int bigPos, String code) {
        PerformTaskStepsEntity entity = new PerformTaskStepsEntity();
        entity.setType(1);
        entity.setLoadUnloadDataId(mList.get(bigPos).getId());
        entity.setFlightId(mList.get(bigPos).getFlightId());
        entity.setFlightTaskId(mList.get(bigPos).getTaskId());
        entity.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude());
        entity.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude());
        entity.setOperationCode(code);
        entity.setTerminalId(DeviceInfoUtil.getDeviceInfo(getContext()).get("deviceId"));
        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        entity.setUserName(mList.get(bigPos).getWorkerName());
        entity.setCreateTime(System.currentTimeMillis());
        ((LoadAndUnloadTodoPresenter) mPresenter).slideTask(entity);
    }

    @Override
    public void slideTaskResult(String result) {
        if ("正确".equals(result)) {
            mSlideadapter.notifyDataSetChanged();
            if (mOperatePos == 4 || mOperatePos == 5) {
                mCurrentPage = 1;
                loadData();
                mOperatePos = 0;
            }
        }
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
}
