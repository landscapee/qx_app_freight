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
import qx.app.freight.qxappfreight.adapter.InstallEquipLeaderAdapter;
import qx.app.freight.qxappfreight.adapter.LeaderInstallEquipStepAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.LoadUnLoadTaskPushBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.LoadUnloadLeaderToDoContract;
import qx.app.freight.qxappfreight.contract.StevedoresTaskHisContract;
import qx.app.freight.qxappfreight.dialog.PushLoadUnloadLeaderDialog;
import qx.app.freight.qxappfreight.presenter.LoadUnloadToDoLeaderPresenter;
import qx.app.freight.qxappfreight.presenter.StevedoresTaskHisPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 装卸机小组长代办fragment 已办
 */
public class InstallEquipLeaderDoneFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, StevedoresTaskHisContract.stevedoresTaskHisView, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private List<LoadAndUnloadTodoBean> mList = new ArrayList<>();
    private List<LoadAndUnloadTodoBean> mCacheList = new ArrayList<>();
    private int mCurrentPage = 1;
    private int mCurrentSize = 10;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
    private LeaderInstallEquipStepAdapter mSlideAdapter;
    private int mOperatePos;
    private List<LoadAndUnloadTodoBean> mListCache = new ArrayList<>();//推送的缓存任务
    private List<LoadAndUnloadTodoBean> mListCacheUse = new ArrayList<>(); //领受拒绝任务显示使用
    private InstallEquipLeaderAdapter mAdapter;

    private String searchString = "";//条件搜索关键字
    private TaskDoneFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow = false;

    private PushLoadUnloadLeaderDialog mDialog = null;
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
    public void onEventMainThread(LoadUnLoadTaskPushBean result) {
        //已办不需要推送
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
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
        mTaskFragment = (TaskDoneFragment) getParentFragment();
        searchToolbar = mTaskFragment.getSearchView();
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        mPresenter = new StevedoresTaskHisPresenter(this);
        mAdapter = new InstallEquipLeaderAdapter(mList);
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
//        BaseFilterEntity entity = new BaseFilterEntity();
//        entity.setWorkerId(UserInfoSingle.getInstance().getUserId());
//        entity.setCurrent(mCurrentPage);
//        entity.setSize(mCurrentSize);
        ((StevedoresTaskHisPresenter) mPresenter).stevedoresTaskHis(UserInfoSingle.getInstance().getUserId());
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
    public void stevedoresTaskHisResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {
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
            if (bean.getRelateInfoObj() !=null){
                StringUtil.setTimeAndType(bean.getRelateInfoObj());//设置对应的时间和时间图标显示
                StringUtil.setFlightRoute(bean.getRelateInfoObj().getRoute(), bean.getRelateInfoObj());//设置航班航线信息
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
            int posNow = 10;//判断当前代办任务应该进行哪一步的int值
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
