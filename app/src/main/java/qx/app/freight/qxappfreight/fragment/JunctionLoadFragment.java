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
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.EndInstallToDoContract;
import qx.app.freight.qxappfreight.dialog.PushLoadUnloadDialog;
import qx.app.freight.qxappfreight.presenter.EndInstallTodoPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
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
    private List<InstallEquipEntity> mList = new ArrayList<>();
    private List<InstallEquipEntity> mCacheList = new ArrayList<>();
    private int mCurrentPage = 1;
    private int mCurrentSize = 10;
    private static final String[] mStepNames = {"领受", "舱单送达"};
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
    private InstallEquipStepAdapter mSlideadapter;
    private int mOperatePos;
    private List<LoadAndUnloadTodoBean> mListCache = new ArrayList<>();
    private String mSearchText;
    private InstallEquipAdapter mAdapter;
    private TaskFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow = false;

    private boolean mShouldNewDialog = true;
    private PushLoadUnloadDialog mDialog = null;

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
            if (result.isCancelFlag() || result.isChangeWorkerUser() || result.isSplitTask()) {
                loadData();
            } else {
                List<LoadAndUnloadTodoBean> list = result.getTaskData();
                if (list != null) {
                    mListCache.addAll(list);
                }
                if (mDialog == null) {
                    mDialog = new PushLoadUnloadDialog();
                }
                if (mShouldNewDialog) {
                    mDialog.setData(getContext(), mListCache, success -> {
                        if (success) {
                            ToastUtil.showToast("领受结载新任务成功");
                            loadData();
                            mListCache.clear();
                        } else {
                            Log.e("tagPush", "推送出错了");
                            mListCache.clear();
                        }
                        mShouldNewDialog = true;
                    });
                    if (!mDialog.isAdded()) {
                        Log.e("tagPuth", "显示推送任务=========");
                        mDialog.show(getFragmentManager(), "11");
                        mShouldNewDialog = false;
                    }
                } else {
                    Observable.timer(300, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()) // timer 默认在新线程，所以需要切换回主线程
                            .subscribe(aLong -> {
                                Log.e("tagPuth", "添加过了=========");
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
        mPresenter = new EndInstallTodoPresenter(this);
        mAdapter = new InstallEquipAdapter(mList);
        mMfrvData.setAdapter(mAdapter);
        SearchToolbar searchToolbar = ((TaskFragment) getParentFragment()).getSearchView();
        searchToolbar.setHintAndListener("请输入航班号", text -> {
            mSearchText = text;
            seachByText();
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
                searchToolbar.setHintAndListener("请输入板车号", text -> {
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
            for (InstallEquipEntity item : mCacheList) {
                if (item.getFlightInfo().toLowerCase().contains(mSearchText.toLowerCase())) {
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
        ((EndInstallTodoPresenter) mPresenter).getEndInstallTodo(entity);
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
    public void getEndInstallTodoResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {
        if (loadAndUnloadTodoBean.size() == 0) {
            if (mCurrentPage == 1) {
                mMfrvData.finishRefresh();
            } else {
                mMfrvData.finishLoadMore();
            }
            mCacheList.clear();
            mAdapter.notifyDataSetChanged();
        }
        ;
        mCacheList.clear();
        if (mCurrentPage == 1) {
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
        mCurrentPage++;
        for (LoadAndUnloadTodoBean bean : loadAndUnloadTodoBean) {
            //原始装卸机数据封装成InstallEquipEntity
            InstallEquipEntity entity = new InstallEquipEntity();
            entity.setShowDetail(false);
            entity.setAirCraftNo(bean.getAircraftno());
            entity.setFlightInfo(bean.getFlightNo());
            entity.setSeat(bean.getSeat());
            entity.setTaskType(bean.getTaskType());//1，装机；2，卸机；5，装卸机
            entity.setFlightType("M");
            entity.setId(bean.getId());
            entity.setFlightId(Long.valueOf(bean.getFlightId()));
            entity.setTaskId(bean.getTaskId());
            entity.setWorkerName(bean.getWorkerName());
            StringUtil.setTimeAndType(bean, entity);//设置对应的时间和时间图标显示
            List<String> times = new ArrayList<>();
            times.add(String.valueOf(bean.getAcceptTime()));
            times.add("0");
            StringUtil.setFlightRoute(bean.getRoute(), entity);//设置航班航线信息
            entity.setLoadUnloadType(bean.getTaskType());
            List<MultiStepEntity> data = new ArrayList<>();
            int posNow = ("0".equals(String.valueOf(bean.getAcceptTime()))) ? 0 : 1;
            for (int i = 0; i < 2; i++) {
                MultiStepEntity entity1 = new MultiStepEntity();
                entity1.setFlightType(entity.getFlightType());
                entity1.setLoadUnloadType(bean.getTaskType());
                entity1.setStepName(mStepNames[i]);
                int type;
                if (i < posNow) {
                    type = 0;
                } else {
                    type = 1;
                }
                entity1.setItemType(type);
                entity1.setData(bean);
                entity1.setStepDoneDate("0".equals(times.get(i)) ? "" : sdf.format(new Date(Long.valueOf(times.get(i)))));
                data.add(entity1);
            }
            List<String> codeList = new ArrayList<>();
            for (int i = 0; i < bean.getOperationStepObj().size(); i++) {
                codeList.add(bean.getOperationStepObj().get(i).getOperationCode());
            }
            entity.setStepCodeList(codeList);
            entity.setList(data);
            mCacheList.add(entity);
        }
        seachByText();
        setSlideListener();
        if (mTaskFragment != null) {
            if (isShow)
                mTaskFragment.setTitleText(mCacheList.size());
        }
    }

    private boolean mShouldRefreshData = false;

    /**
     * 设置滑动监听
     */
    private void setSlideListener() {
        mAdapter.setOnSlideStepListener((bigPos, adapter, smallPos) -> {
            //滑动步骤去调接口，以及跳转页面
            mOperatePos = smallPos;
            mSlideadapter = adapter;
            mShouldRefreshData = smallPos == 1;
            go2SlideStep(bigPos, mList.get(bigPos).getStepCodeList().get(smallPos));
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
        ((EndInstallTodoPresenter) mPresenter).slideTask(entity);
    }


    @Override
    public void slideTaskResult(String result) {
        if ("正确".equals(result)) {
            mSlideadapter.notifyDataSetChanged();
            if (mOperatePos == 4) {
                mCurrentPage = 1;
                loadData();
                mOperatePos = 0;
            } else if (mShouldRefreshData) {
                mCurrentPage = 1;
                loadData();
                mOperatePos = 0;
                mShouldRefreshData = false;
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

    }

    @Override
    public void dissMiss() {

    }
}
