package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoMyBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskMyBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.AcceptTerminalTodoContract;
import qx.app.freight.qxappfreight.presenter.AcceptTerminalTodoPresenter;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 外场运输
 */
public class TaskDriverOutFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, AcceptTerminalTodoContract.acceptTerminalTodoView {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;

    private List<AcceptTerminalTodoBean> list;
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
        list = new ArrayList<>();
        adapter = new DriverOutTaskAdapter(list);
        mMfrvData.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {

        });
        getData();
    }

    private void getData(){
        mPresenter = new AcceptTerminalTodoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setCurrent(currentPage);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setUserId("u9bca020ce9204662b9902386fd648e86");
//        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        ((AcceptTerminalTodoPresenter) mPresenter).acceptTerminalTodo(entity);
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

            //根据BeginAreaId分类
            acceptTerminalTodoBeanList.forEach(acceptTerminalTodoBean -> {
                acceptTerminalTodoBean.setUseTasks(new ArrayList <List <OutFieldTaskBean>>(acceptTerminalTodoBean.getTasks().stream().collect(Collectors.groupingBy(OutFieldTaskBean::getBeginAreaId)).values()));
//                acceptTerminalTodoBean.setCollect(acceptTerminalTodoBean.getTasks().stream().collect(Collectors.groupingBy(OutFieldTaskBean::getBeginAreaId)));
            });
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
}
