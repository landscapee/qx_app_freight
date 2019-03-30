package qx.app.freight.qxappfreight.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.DriverOutTaskAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoMyBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadUnloadTpBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskMyBean;
import qx.app.freight.qxappfreight.bean.response.StepBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.AcceptTerminalTodoContract;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.dialog.TpPushDialog;
import qx.app.freight.qxappfreight.presenter.AcceptTerminalTodoPresenter;
import qx.app.freight.qxappfreight.presenter.LoadAndUnloadTodoPresenter;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.GsonUtil;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 外场运输
 */
public class TaskDriverOutFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, AcceptTerminalTodoContract.acceptTerminalTodoView, LoadAndUnloadTodoContract.loadAndUnloadTodoView {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;

    List<AcceptTerminalTodoBean> listCache;

    private List<AcceptTerminalTodoBean> list;
    private int slidePosition,slidePositionChild,step;
    private DriverOutTaskAdapter adapter;
    private int currentPage = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_driver_out, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
        listCache = new ArrayList <>();
        list = new ArrayList<>();
        adapter = new DriverOutTaskAdapter(list);
        mMfrvData.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {

        });
        adapter.setmOnStepListener((step, parentPosition, position) -> {
            /**
             * 同时开启多个航班
             */
                for (OutFieldTaskBean mOutFieldTaskBean: list.get(parentPosition).getUseTasks().get(position)){
                    slidePosition = parentPosition;
                    slidePositionChild = position;
                    this.step = step;
                    submitStep(mOutFieldTaskBean,step);
                }

        });
        getData();
    }

    private void getData(){
        mPresenter = new AcceptTerminalTodoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setCurrent(currentPage);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        ((AcceptTerminalTodoPresenter) mPresenter).acceptTerminalTodo(entity);
    }

    private void submitStep(OutFieldTaskBean mOutFieldTaskBean,int step){

        mPresenter = new LoadAndUnloadTodoPresenter(this);
        PerformTaskStepsEntity entity=new PerformTaskStepsEntity();
        entity.setType(0);
        entity.setLoadUnloadDataId(mOutFieldTaskBean.getId());
        entity.setFlightId(Long.valueOf(mOutFieldTaskBean.getFlightId()));
        entity.setFlightTaskId(mOutFieldTaskBean.getTaskId());
        entity.setLatitude((Tools.getGPSPosition()==null)?"":Tools.getGPSPosition().getLatitude());
        entity.setLongitude((Tools.getGPSPosition()==null)?"":Tools.getGPSPosition().getLongitude());

        if (step == 0)
            entity.setOperationCode("CargoOutTransportStart");
        else if(step == 1)
            entity.setOperationCode("CargoOutTransportEnd");
        else
            entity.setOperationCode("CargoOutTransportReceived");

        entity.setUserName(UserInfoSingle.getInstance().getUsername());

        entity.setTerminalId(DeviceInfoUtil.getDeviceInfo(getContext()).get("deviceId"));
        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        entity.setCreateTime(System.currentTimeMillis());

        ((LoadAndUnloadTodoPresenter) mPresenter).slideTask(entity);

    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据……");
        new Handler().postDelayed(() -> {
            getData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        getData();
    }

    @Override
    public void onLoadMore() {
        getData();
    }
     @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result.equals("TaskDriverOutFragment_refresh")) {

            getData();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoadUnloadTpBean result) {

        List<AcceptTerminalTodoBean> list = JSON.parseArray(result.getTaskData(),AcceptTerminalTodoBean.class);
        if (list != null)
            listCache.addAll(list);
        showTpNewTaskDialog();


//        if (result.equals("TaskDriverOutFragment_refresh")) {
//
//            getData();
//        }
    }
    //全屏dialog
    private void showTpNewTaskDialog() {

        if (listCache.size() > 0){
            TpPushDialog tpPushDialog = new TpPushDialog(getContext(),R.style.custom_dialog, listCache.get(0), OutFieldTaskBeans -> {
                for (OutFieldTaskBean mOutFieldTaskBean:OutFieldTaskBeans){
                    submitStep(mOutFieldTaskBean,0);
                }
                showTpNewTaskDialog();
            });
            tpPushDialog.show();
            listCache.remove(0);
        }
    }


    @Override
    public void acceptTerminalTodoResult(List<AcceptTerminalTodoBean> acceptTerminalTodoBeanList) {
        if (acceptTerminalTodoBeanList != null) {

           if (currentPage == 1){
               mMfrvData.finishRefresh();
               list.clear();
           }
           else {
               currentPage++;
               mMfrvData.finishLoadMore();
           }
            //把运输的板车类型 赋值大 子任务
            for (AcceptTerminalTodoBean mAcceptTerminalTodoBean:acceptTerminalTodoBeanList){
               for (OutFieldTaskBean mOutFieldTaskBean:mAcceptTerminalTodoBean.getTasks()){
                   mOutFieldTaskBean.setTransfortType(mAcceptTerminalTodoBean.getTransfortType());
               }
            }

            //根据BeginAreaId分类 21 以下 用 else
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){

                acceptTerminalTodoBeanList.forEach(acceptTerminalTodoBean -> {
                    acceptTerminalTodoBean.setUseTasks(new ArrayList <List <OutFieldTaskBean>>(acceptTerminalTodoBean.getTasks().stream().collect(Collectors.groupingBy(OutFieldTaskBean::getBeginAreaCargoType)).values()));
                });
            }
            else {

                for (AcceptTerminalTodoBean mAcceptTerminalTodoBean:acceptTerminalTodoBeanList){

                    mAcceptTerminalTodoBean.setUseTasks(getUseTasks(mAcceptTerminalTodoBean));

                }

            }

            list.addAll(acceptTerminalTodoBeanList);
            TaskFragment fragment = (TaskFragment) getParentFragment();
            if (fragment != null) {
                fragment.setTitleText(list.size());
            }
            mMfrvData.notifyForAdapter(adapter);
        } else {
            Log.e("失败", "外场运输待办");
        }
    }

    /**
     * 根据 开始位置和运输类型
     * @param mAcceptTerminalTodoBean
     * @return
     */
    private List<List<OutFieldTaskBean>> getUseTasks(AcceptTerminalTodoBean mAcceptTerminalTodoBean) {

        Map<String,List<OutFieldTaskBean>> map = new HashMap <>();

        for (OutFieldTaskBean task :mAcceptTerminalTodoBean.getTasks()){
                if (map.get(task.getBeginAreaCargoType()) == null){
                    List<OutFieldTaskBean> list = new ArrayList <>();
                    list.add(task);
                    map.put(task.getBeginAreaCargoType(),list);
                }
                else {
                    map.get(task.getBeginAreaCargoType()).add(task);
                }

        }
        List<List<OutFieldTaskBean>>  OutFieldTaskBeans = new ArrayList <>(map.values());

        return OutFieldTaskBeans;
    }

    @Override
    public void toastView(String error) {
        if (currentPage == 1){
            mMfrvData.finishRefresh();
        }
        else {
            mMfrvData.finishLoadMore();
        }
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }

    @Override
    public void loadAndUnloadTodoResult(List <LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {

    }

    @Override
    public void slideTaskResult(String result) {

        ToastUtil.showToast("任务执行成功");
        getData();
//        StepBean stepBean = JSON.parseObject(result,StepBean.class);
//        upDateStepStatus(stepBean);


    }

    /**
     * 更新步骤状态
     * @param stepBean
     */
    private void upDateStepStatus(StepBean stepBean) {
        for (OutFieldTaskBean taskBeans:list.get(slidePosition).getUseTasks().get(slidePositionChild)){

            if (taskBeans.getFlightId().equals(stepBean.getData().getFlightId()+"")){
                switch (step){
                    case 0:
                        taskBeans.setAcceptTime(stepBean.getData().getCreateTime());
                        break;
                    case 1:
                        taskBeans.setTaskBeginTime(stepBean.getData().getCreateTime());
                        break;
                    case 2:
                        taskBeans.setTaskEndTime(stepBean.getData().getCreateTime());
                        break;
                }
            }

        }
        adapter.notifyDataSetChanged();

    }
}
