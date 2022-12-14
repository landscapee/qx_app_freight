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
 * ????????????????????????fragment
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
    private List <LoadAndUnloadTodoBean> mListCache = new ArrayList <>();//?????????????????????
    private List <LoadAndUnloadTodoBean> mListCacheUse = new ArrayList <>(); //??????????????????????????????
    private InstallEquipLeaderAdapter mAdapter;

    private String searchString = "";//?????????????????????
    private TaskFragment mTaskFragment; //?????????fragment
    private SearchToolbar searchToolbar;//?????????????????????
    private boolean isShow = false;

    private PushLoadUnloadLeaderDialog mDialog = null;
    private List <String> mTaskIdList = new ArrayList <>();
    private String mSpecialTaskId = null;//??????????????????????????????????????????????????????????????????taskId???????????????taskId???item????????????
    private int loadInstall = 0; //????????????????????? 1 ????????????

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
                        addMembers.append("???");
                        addNumber += 1;
                    }
                    if (data.getOperationType() == 2) {
                        removeMembers.append(data.getStaffName());
                        removeMembers.append("???");
                        removeNumber += 1;
                    }
                }
            }
            String toast = "";
            if (addNumber != 0 && removeNumber != 0) {
                toast = result.getTaskData().getSeat() + "??????" + result.getTaskData().getFlightNo() + "???????????????????????????" + addNumber +
                        "?????????(" + addMembers.toString().substring(0, addMembers.toString().length() - 1) + ")," + "?????????" + removeNumber + "?????????(" +
                        removeMembers.toString().substring(0, removeMembers.toString().length() - 1) + ")";
            }
            if (addNumber == 0 && removeNumber != 0) {
                toast = result.getTaskData().getSeat() + "??????" + result.getTaskData().getFlightNo() + "???????????????????????????" + removeNumber + "?????????(" +
                        removeMembers.toString().substring(0, removeMembers.toString().length() - 1) + ")";
            }
            if (addNumber != 0 && removeNumber == 0) {
                toast = result.getTaskData().getSeat() + "??????" + result.getTaskData().getFlightNo() + "???????????????????????????" + addNumber +
                        "?????????(" + addMembers.toString().substring(0, addMembers.toString().length() - 1) + ")";
            }
            Log.e("tagTest", "toast===" + toast);
            ToastUtil.showToast(toast);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        if (result != null) {
            if (result.isChangeWorkerUser() || result.isSplitTask()) {//?????????????????????????????????????????????
                loadData();
            } else if (result.isCancelFlag()) {
                if (!result.isConfirmTask()) {//?????????????????????????????????????????????????????????
                    List <LoadAndUnloadTodoBean> list = result.getTaskData();
                    String flightName = list.get(0).getFlightNo();
                    ToastUtil.showToast("??????" + flightName + "????????????????????????????????????????????????");
                    Observable.timer(300, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()) //??????300?????????????????????????????????????????????????????????
                            .subscribe(aLong -> {
                                loadData();
                            });
                } else {//?????????????????????????????????
                    loadData();
                }
            } else {//?????????????????????????????????????????????????????????
                List <LoadAndUnloadTodoBean> list = result.getTaskData();
                for (LoadAndUnloadTodoBean bean : list) {
                    for (LoadAndUnloadTodoBean bean1 : mListCache) {
                        if (bean.getTaskId().equals(bean1.getTaskId())) {//???????????????id==?????????id?????????
                            mListCache.remove(bean1);
                        }
                    }
                    mListCache.add(bean); //?????????????????????????????????
                }
                //??????????????? ???????????????????????????????????????????????????????????????????????????????????????????????????
                for (LoadAndUnloadTodoBean bean : mListCache) {
                    if (mTaskIdList.contains(bean.getTaskId())) {//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
     * mListCache ???0 ????????????
     */
    private void showDialogTask() {
        if ((mDialog != null && mDialog.isShowing()) || mListCache.size() == 0)
            return;
        mListCacheUse.clear();
        mListCacheUse.add(mListCache.get(0));
        mListCache.remove(0);
        mDialog = new PushLoadUnloadLeaderDialog(getContext(), R.style.custom_dialog, mListCacheUse, status -> {
            switch (status) {
                case 0://???????????????????????????
                    mDialog.dismiss();
                    ToastUtil.showToast("???????????????????????????");
                    mListCacheUse.clear();
//                    mDialog = null;
                    loadData();
                    showDialogTask();
                    break;
                case 1://?????????????????????taskId??????
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
                case -1://?????????????????????????????????????????????
                    Log.e("tagPush", "???????????????");
                    mDialog.dismiss();
                    mListCacheUse.clear();
                    break;
            }
        });
        if (!mDialog.isShowing()) {//???????????????????????????????????????
            mDialog.show();//?????????????????????
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
                searchToolbar.setHintAndListener("??????????????????", text -> {
                    searchString = text;
                    seachByText();
                });
            }
        }
    }

    /**
     * ?????????????????????????????????????????????
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
     * ???????????????
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
     * ???????????????
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
     * ????????????????????????
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
        showProgessDialog("????????????????????????");
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
            //??????????????????????????????InstallEquipEntity
            if (mSpecialTaskId != null && mSpecialTaskId.equals(bean.getTaskId())) {//mSpecialTaskId???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                bean.setShowDetail(true);
                mSpecialTaskId = null;
            }
            StringUtil.setTimeAndType(bean);//??????????????????????????????????????????
            StringUtil.setFlightRoute(bean.getRoute(), bean);//????????????????????????
            if (bean.getRelateInfoObj() != null) {
                StringUtil.setTimeAndType(bean.getRelateInfoObj());//??????????????????????????????????????????
                StringUtil.setFlightRoute(bean.getRelateInfoObj().getRoute(), bean.getRelateInfoObj());//????????????????????????
                if (!StringUtil.isEmpty(bean.getRelateInfoObj().getLoadingAndUnloadExtJson())){
                    bean.getRelateInfoObj().setLoadingAndUnloadBean(JSON.parseObject(bean.getRelateInfoObj().getLoadingAndUnloadExtJson(), LoadingAndUnloadBean.class));
                }
            }
            if (!StringUtil.isEmpty(bean.getLoadingAndUnloadExtJson())){
                bean.setLoadingAndUnloadBean(JSON.parseObject(bean.getLoadingAndUnloadExtJson(), LoadingAndUnloadBean.class));
            }
            //????????????????????????????????????????????????????????????????????????????????????-??????????????????????????????????????????????????????????????????????????????0??????????????????0????????????????????????????????????????????????
            List <String> times = new ArrayList <>();
            times.add(String.valueOf(bean.getAcceptTime()));
            times.add(String.valueOf(bean.getArrivalTime()));
            if (bean.getTaskType() == 6) {//??????
                times.add(String.valueOf(bean.getStartLoadTime()));
                times.add(String.valueOf(bean.getEndLoadTime()));
            } else if (bean.getTaskType() == 7) {//??????
                times.add(String.valueOf(bean.getStartUnloadTime()));
                times.add(String.valueOf(bean.getEndUnloadTime()));
            } else {//?????????
                times.add(String.valueOf(bean.getStartLoadTime()));
                times.add(String.valueOf(bean.getEndLoadTime()));
            }
            int posNow = 0;//????????????????????????????????????????????????int???
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
                bean.setAcceptTask(true);//?????????????????????
            } else {
                bean.setAcceptTask(false);//?????????????????????
            }
            for (int i = 0; i < bean.getOperationStepObj().size(); i++) {
                LoadAndUnloadTodoBean.OperationStepObjBean entity1 = bean.getOperationStepObj().get(i);
                entity1.setTaskId(bean.getTaskId());
                entity1.setFlightType(bean.getFlightType());
                int type;
                if (i < posNow) {//????????????????????????????????????????????????
                    type = Constants.TYPE_STEP_OVER;
                } else if (i == posNow) {//????????????????????????????????????????????????
                    type = Constants.TYPE_STEP_NOW;
                } else {//??????????????????
                    type = Constants.TYPE_STEP_NEXT;
                }
                entity1.setItemType(type);
                entity1.setStepDoneDate("0".equals(times.get(i)) ? "" : sdf.format(new Date(Long.valueOf(times.get(i)))));
                entity1.setPlanTime(bean.getOperationStepObj().get(i).getPlanTime()==null||"0".equals(bean.getOperationStepObj().get(i).getPlanTime()) ? "" : sdf.format(new Date(Long.valueOf(bean.getOperationStepObj().get(i).getPlanTime()))));
            }

            mCacheList.add(bean);
        }
        mListCache.clear();
        //?????????????????????????????? dialog
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
     * //?????????????????????????????? dialog
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
     * ??????????????????
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
                showYesOrNoDialog("","?????????????????????????",position);

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
     * ??????????????????
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
     * ?????????????????????
     *
     * @param title
     * @param msg
     * @param flag
     */
    private void showYesOrNoDialog(String title, String msg, int flag) {
        CommonDialog dialog = new CommonDialog(getActivity());
        dialog.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("??????")
                .setNegativeButton("??????")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            startClearTask(flag);
                        } else {
//                            ToastUtil.showToast("????????????????????????");
                        }
                    }
                })
                .show();

    }
    /**
     * ???????????????
     *
     * @param bigPos ??????????????????????????????
     * @param code   ?????????
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
        if ("??????".equals(result)) {
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
        ToastUtil.showToast("????????????");
    }

    @Override
    public void toastView(String error) {
        if (loadInstall == 1)
            ToastUtil.showToast("???????????????");

        if (mMfrvData != null)
            mMfrvData.finishLoadMore();
        if (mMfrvData != null)
            mMfrvData.finishRefresh();
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("?????????......");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void getLoadingListResult(LoadingListBean result) {
        if ("318".equals(result.getStatus())) {
            ToastUtil.showToast("??????????????????");
        } else {
            if (result.getData() != null || result.getData().size() != 0) {

                if (!TextUtils.isEmpty(result.getData().get(0).getContent())) {
                    Gson mGson = new Gson();
                    LoadingListBean.DataBean.ContentObjectBean[] datas = mGson.fromJson(result.getData().get(0).getContent(), LoadingListBean.DataBean.ContentObjectBean[].class);
                    //????????????
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
                ToastUtil.showToast("??????????????????");
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
