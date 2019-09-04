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
import qx.app.freight.qxappfreight.adapter.NewInstallEquipAdapter;
import qx.app.freight.qxappfreight.adapter.NewInstallEquipStepAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.EndInstallToDoContract;
import qx.app.freight.qxappfreight.dialog.PushLoadUnloadDialog;
import qx.app.freight.qxappfreight.presenter.EndInstallTodoPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.IMUtils;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;


/****
 * 结载
 */
public class JunctionLoadFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, EndInstallToDoContract.IView, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private List <LoadAndUnloadTodoBean> mList = new ArrayList <>();
    private List <LoadAndUnloadTodoBean> mCacheList = new ArrayList <>();
    private int mCurrentPage = 1;
    private int mCurrentSize = 10;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
    private NewInstallEquipStepAdapter mSlideadapter;
    private List <LoadAndUnloadTodoBean> mListCache = new ArrayList <>();
    private String mSearchText;
    private NewInstallEquipAdapter mAdapter;
    private TaskFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow = false;

    private PushLoadUnloadDialog mDialog = null;
    private List <String> mTaskIdList = new ArrayList <>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_install_equip, container, false);
        unbinder = ButterKnife.bind(this, view);
        setUserVisibleHint(true);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        if (result != null) {
            if (result.isChangeWorkerUser()) {//换人直接刷新代办列表
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
                //新任务列表 同 旧任务列表比对
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
                if (mDialog == null) {
                    mDialog = new PushLoadUnloadDialog();
                }
                mDialog.setData(getContext(), mListCache, success -> {
                    if (success) {//成功领受后吐司提示，并延时300毫秒刷新代办列表
                        ToastUtil.showToast("领受结载新任务成功");
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
                    Tools.closeVibrator(getActivity().getApplicationContext());
                });
                if (!mDialog.isAdded()) {//新任务弹出框未显示在屏幕中
                    if (mTaskIdList.contains(list.get(0).getTaskId())) {//代办列表中有当前推送过来的任务，则不弹窗提示，只是刷新页面
                        loadData();
                        mListCache.clear();
                    } else {
                        Tools.startVibrator(getActivity().getApplicationContext(), true, R.raw.ring);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if ("refresh_data_update".equals(result)) {
            mCurrentPage = 1;
            loadData();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUserVisibleHint(true);
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
        mPresenter = new EndInstallTodoPresenter(this);
        mAdapter = new NewInstallEquipAdapter(mList);
        mMfrvData.setAdapter(mAdapter);
        SearchToolbar searchToolbar = ((TaskFragment) getParentFragment()).getSearchView();
        searchToolbar.setHintAndListener("请输入航班号", text -> {
            mSearchText = text;
            seachByText();
        });
        mAdapter.setOnFlightSafeguardListenner(new NewInstallEquipAdapter.OnFlightSafeguardListenner() {
            @Override
            public void onFlightSafeguardClick(int position) {
                IMUtils.chatToGroup(mContext, mList.get(position).getFlightId());
            }

            @Override
            public void onClearClick(int position) {
                ToastUtil.showToast("结载不能发起清场任务");
            }
        });
        loadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        if (isVisibleToUser) {
            Log.e("111111", "setUserVisibleHint: " + "展示");
            if (mTaskFragment != null)
                mTaskFragment.setTitleText(mCacheList.size());
            if (searchToolbar != null) {
                searchToolbar.setHintAndListener("请输入航班号", text -> {
                    mSearchText = text;
                    seachByText();
                });
            }

        }
    }

    private void seachByText() {
        mList.clear();
        if (TextUtils.isEmpty(mSearchText)) {
            mList.addAll(mCacheList);
        } else {
            for (LoadAndUnloadTodoBean item : mCacheList) {
                if (item.getFlightNo().toLowerCase().contains(mSearchText.toLowerCase())) {
                    mList.add(item);
                }
            }
        }
        if (mMfrvData != null) {
            mMfrvData.notifyForAdapter(mAdapter);
        }
    }

    private void loadData() {
        mPresenter = new EndInstallTodoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setWorkerId(UserInfoSingle.getInstance().getUserId());
        entity.setCurrent(mCurrentPage);
        entity.setSize(mCurrentSize);
        ((EndInstallTodoPresenter) mPresenter).getEndInstallTodo(entity);
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
    public void getEndInstallTodoResult(List <LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {
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
            StringUtil.setTimeAndType(bean);//设置对应的时间和时间图标显示
            List <String> times = new ArrayList <>();
            times.add(String.valueOf(bean.getAcceptTime()));
            times.add("0");
            StringUtil.setFlightRoute(bean.getRoute(), bean);//设置航班航线信息
            int posNow = ("0".equals(String.valueOf(bean.getAcceptTime()))) ? 0 : 1;//如果领受时间为0或者null，则表示从未领受过任务，即推送任务时未登陆，或登陆时收到新任务推送按了回退键
            if (posNow > 0) {
                bean.setAcceptTask(true);//已经领受过了设置acceptTask未true，设置该条item的背景为白色，否则为黄色警告颜色背景
            }
            for (int i = 0; i < bean.getOperationStepObj().size(); i++) {
                LoadAndUnloadTodoBean.OperationStepObjBean entity1 = bean.getOperationStepObj().get(i);
                entity1.setFlightType(bean.getFlightType());
                int type;
                if (i < posNow) {//在应该执行的步骤前，类型为已执行
                    type = Constants.TYPE_STEP_OVER;
                } else if (i == posNow) {    //当前任务步骤
                    type = Constants.TYPE_STEP_NOW;
                } else {//否则是未执行
                    type = Constants.TYPE_STEP_NEXT;
                }
                entity1.setItemType(type);
                entity1.setStepDoneDate("0".equals(times.get(i)) ? "" : sdf.format(new Date(Long.valueOf(times.get(i)))));
            }
            mCacheList.add(bean);
        }
        seachByText();
        setSlideListener();
        if (mTaskFragment != null) {
            if (isShow)
                mTaskFragment.setTitleText(mCacheList.size());
        }
    }

    /**
     * 设置滑动监听
     */
    private void setSlideListener() {
        mAdapter.setOnSlideStepListener((bigPos, adapter, smallPos) -> {
            //滑动步骤去调接口，以及跳转页面
            mSlideadapter = adapter;
            go2SlideStep(bigPos, mList.get(bigPos).getOperationStepObj().get(smallPos).getOperationCode());
        });
    }


    private void go2SlideStep(int bigPos, String code) {
        PerformTaskStepsEntity entity = new PerformTaskStepsEntity();
        entity.setType(1);
        entity.setLoadUnloadDataId(mList.get(bigPos).getId());
        entity.setFlightId(Long.valueOf(mList.get(bigPos).getFlightId()));
        entity.setFlightTaskId(mList.get(bigPos).getTaskId());
        entity.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude());
        entity.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude());
        entity.setOperationCode(code);
        entity.setTerminalId(DeviceInfoUtil.getDeviceInfo(getContext()).get("deviceId"));
        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        entity.setUserName(mList.get(bigPos).getWorkerName());
        entity.setCreateTime(System.currentTimeMillis());
        ((EndInstallTodoPresenter) mPresenter).slideTask(entity);
    }


    @Override
    public void slideTaskResult(String result) {
        if ("正确".equals(result)) {
            mSlideadapter.notifyDataSetChanged();
            mCurrentPage = 1;
            loadData();
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

    }

    @Override
    public void dissMiss() {

    }
}
