package qx.app.freight.qxappfreight.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.CargoManifestInfoActivity;
import qx.app.freight.qxappfreight.activity.ScanManagerActivity;
import qx.app.freight.qxappfreight.adapter.CargoManifestAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.GroupBoardRequestEntity;
import qx.app.freight.qxappfreight.bean.request.TaskLockEntity;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GroupBoardToDoContract;
import qx.app.freight.qxappfreight.dialog.UpdatePushDialog;
import qx.app.freight.qxappfreight.presenter.GroupBoardToDoPresenter;
import qx.app.freight.qxappfreight.presenter.TaskLockPresenter;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/*****
 * 货邮舱单页面
 */

public class CargoManifestFragment extends BaseFragment implements GroupBoardToDoContract.GroupBoardToDoView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    @BindView(R.id.toolbar)
    CustomToolbar mToolBar;
    @BindView(R.id.search_toolbar)
    SearchToolbar mSearchBar;
    private CargoManifestAdapter adapter;
    private List<TransportDataBase> list1 = new ArrayList<>();
    private List<TransportDataBase> list = new ArrayList<>();
    private int pageCurrent = 1;//页数
    private String seachString = "";

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cargomanifest, container, false);
        unbinder = ButterKnife.bind(this, view);
        mToolBar.setLeftIconView(View.VISIBLE, R.mipmap.richscan, v -> gotoScan());
        mToolBar.setRightIconView(View.VISIBLE, R.mipmap.search, v -> {
            mToolBar.setVisibility(View.GONE);
            mSearchBar.setVisibility(View.VISIBLE);
            // 向左边移入
            mSearchBar.setAnimation(AnimationUtils.makeInAnimation(getContext(), true));
            // 向右边移出
            mToolBar.setAnimation(AnimationUtils.makeOutAnimation(getContext(), true));
        });
        mSearchBar.setVisibility(View.GONE);
        mSearchBar.getCloseView().setOnClickListener(v -> {
            mSearchBar.getSearchView().setText("");
            mToolBar.setVisibility(View.VISIBLE);
            mSearchBar.setVisibility(View.GONE);
            // 向左边移入
            mToolBar.setAnimation(AnimationUtils.makeInAnimation(getContext(), false));
            // 向右边移出
            mSearchBar.setAnimation(AnimationUtils.makeOutAnimation(getContext(), false));
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        mMfrvData.setRefreshStyle(false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        setUserVisibleHint(true);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mSearchBar.setHintAndListener("请输入运单号", text -> {
                seachString = text;
                seachWith();
            });
        }
    }
    private void gotoScan() {
        ScanManagerActivity.startActivity(getContext(), "MainActivity");
    }

    private void initData() {
        adapter = new CargoManifestAdapter(list);
        mMfrvData.setAdapter(adapter);
        //跳转到详情页面
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getContext(), CargoManifestInfoActivity.class);
            intent.putExtra("data", list.get(position));
            getContext().startActivity(intent);
        });
        getData();
    }

    private void seachWith() {
        list.clear();
        if (TextUtils.isEmpty(seachString)) {
            list.addAll(list1);
        } else {
            for (TransportDataBase team : list1) {
                if (team.getFlightNo().toLowerCase().contains(seachString.toLowerCase())) {
                    list.add(team);
                }
            }
        }
        if (mMfrvData != null) {
            mMfrvData.notifyForAdapter(adapter);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result.equals("CargoHandlingActivity_refresh")) {
            pageCurrent = 1;
            getData();
        }
    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        String daibanCode = result.getData();
        Log.e("22222", "daibanCode" + daibanCode);
        if (!TextUtils.isEmpty(result.getData()) && result.getFunctionFlag().equals("MainActivity")) {
            chooseCode(daibanCode);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebSocketResultBean mWebSocketResultBean) {
        if ("N".equals(mWebSocketResultBean.getFlag())) {
            List<TransportDataBase> datas = mWebSocketResultBean.getChgData();
            list.addAll(datas);
            UpdatePushDialog pushDialog = new UpdatePushDialog(getContext(), R.style.custom_dialog, datas.get(0).getAircraftNo() + "收到新的货邮舱单，请查看！", () -> {
                Intent intent = new Intent(getContext(), CargoManifestInfoActivity.class);
                intent.putExtra("data", datas.get(0));
                getContext().startActivity(intent);
            });
            pushDialog.show();
        } else if ("D".equals(mWebSocketResultBean.getFlag())) {
            for (TransportDataBase mTransportListBean : list) {
                if (mWebSocketResultBean.getChgData().get(0).getTaskId().equals(mTransportListBean.getTaskId())) {
                    list.remove(mTransportListBean);
                }
            }
        }
        seachWith();
    }

    /**
     * 通过获取的code，筛选代办，直接进入处理代办
     *
     * @param daibanCode 代办号
     */
    private void chooseCode(String daibanCode) {
        for (TransportDataBase item : list) {
            if (daibanCode.equals(item.getId())) {
                /**
                 * 待办锁定 当前的任务bean
                 */
                //                mPresenter = new TaskLockPresenter(this);
                TaskLockEntity entity = new TaskLockEntity();
                List<String> taskIdList = new ArrayList<>();
                taskIdList.add(item.getTaskId());
                entity.setTaskId(taskIdList);
                entity.setUserId(UserInfoSingle.getInstance().getUserId());
                entity.setRoleCode(Constants.BEFOREHAND);
                ((TaskLockPresenter) mPresenter).taskLock(entity);
                return;
            }
        }
    }

    private void getData() {
        mPresenter = new GroupBoardToDoPresenter(this);
        GroupBoardRequestEntity entity = new GroupBoardRequestEntity();
        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
        entity.setRoleCode(Constants.JUNCTION_LOAD);
        //舱单传
        entity.setUndoType(2);
        List<String> ascs = new ArrayList<>();
        ascs.add("ETD");
        entity.setAscs(ascs);
        ((GroupBoardToDoPresenter) mPresenter).getGroupBoardToDo(entity);
    }

    @Override
    public void getGroupBoardToDoResult(List<TransportDataBase> transportListBeans) {
        mToolBar.setMainTitle(Color.WHITE, "我的待办（" + transportListBeans.size() + "）");
        if (pageCurrent == 1) {
            list1.clear();
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
        list1.addAll(transportListBeans);
        seachWith();
    }

    /**
     * 待办锁定 - 回调
     * //     * @param result
     */
//    @Override
//    public void taskLockResult(String result) {
//        if (CURRENT_TASK_BEAN != null) {
////            turnToDetailActivity(CURRENT_TASK_BEAN);
//        }
//    }
    @Override
    public void getScooterByScooterCodeResult(GetInfosByFlightIdBean getInfosByFlightIdBean) {
    }

    @Override
    public void searchWaybillByWaybillCodeResult(List <WaybillsBean> waybillsBeans) {

    }

    @Override
    public void toastView(String error) {
        if (pageCurrent == 1) {
            list.clear();
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

    @Override
    public void onRetry() {
        pageCurrent = 1;
        getData();
    }

    @Override
    public void onRefresh() {
        pageCurrent = 1;
        getData();
    }

    @Override
    public void onLoadMore() {
//        pageCurrent++;
//        getData();
    }
}