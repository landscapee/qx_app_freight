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

import com.alibaba.fastjson.JSON;
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
import qx.app.freight.qxappfreight.adapter.GoodsManifestAdapter;
import qx.app.freight.qxappfreight.adapter.JZLoadAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ManifestMainBean;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.GroupBoardRequestEntity;
import qx.app.freight.qxappfreight.bean.request.TaskLockEntity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadingAndUnloadBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.EndInstallToDoContract;
import qx.app.freight.qxappfreight.contract.GroupBoardToDoContract;
import qx.app.freight.qxappfreight.dialog.UpdatePushDialog;
import qx.app.freight.qxappfreight.presenter.EndInstallTodoPresenter;
import qx.app.freight.qxappfreight.presenter.GroupBoardToDoPresenter;
import qx.app.freight.qxappfreight.presenter.TaskLockPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.IMUtils;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/*****
 * 货邮舱单页面
 */

public class CargoManifestFragment extends BaseFragment implements EndInstallToDoContract.IView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    @BindView(R.id.toolbar)
    CustomToolbar mToolBar;
    @BindView(R.id.search_toolbar)
    SearchToolbar mSearchBar;
    private JZLoadAdapter adapter;
    private List<LoadAndUnloadTodoBean> list1 = new ArrayList<>();
    private List<LoadAndUnloadTodoBean> list = new ArrayList<>();
    private int pageCurrent = 1;//页数
    private String seachString = "";

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cargomanifest, container, false);
        unbinder = ButterKnife.bind(this, view);
//        mToolBar.setLeftIconView(View.VISIBLE, R.mipmap.richscan, v -> gotoScan());
        mToolBar.setLeftIconView(View.GONE);
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
            mSearchBar.setHintAndListener("请输入航班号", text -> {
                seachString = text;
                seachWith();
            });
        }
    }
    private void gotoScan() {
        ScanManagerActivity.startActivity(getContext(), "MainActivity");
    }

    private void initData() {
        adapter = new JZLoadAdapter(list);
        adapter.setOnFlightSafeguardListenner(new JZLoadAdapter.OnFlightSafeguardListenner() {
            @Override
            public void onFlightSafeguardClick(int position) {
                IMUtils.chatToGroup(mContext, list.get(position).getFlightId());
            }

            @Override
            public void onClearClick(int position) {
                ToastUtil.showToast("结载不能发起清场任务");
            }
        });
        mMfrvData.setAdapter(adapter);
        //跳转到详情页面
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (list !=null&&list.size() > 0){

                CargoManifestInfoActivity.startActivity(getContext(),list.get(position),0);
//                Intent intent = new Intent(getContext(), CargoManifestInfoActivity.class);
//                intent.putExtra("data", list.get(position));
//                getContext().startActivity(intent);
            }

        });
        getData();
    }

    private void seachWith() {
        list.clear();
        if (TextUtils.isEmpty(seachString)) {
            list.addAll(list1);
        } else {
            for (LoadAndUnloadTodoBean team : list1) {
                if (team.getFlightNo().toLowerCase().contains(seachString.toLowerCase())) {
                    list.add(team);
                }
            }
        }
        mToolBar.setMainTitle(Color.WHITE, "货邮舱单（" + list.size() + "）");
        if (mMfrvData != null) {
            mMfrvData.notifyForAdapter(adapter);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result.equals("CargoManifestFragment_refresh")) {
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
//            chooseCode(daibanCode);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        if (result != null&& result.isNewStowage()) {

            List <LoadAndUnloadTodoBean> loadAndUnloadTodoBeans  = result.getTaskData();
            if (loadAndUnloadTodoBeans !=null && loadAndUnloadTodoBeans.size()> 0){
                UpdatePushDialog pushDialog = new UpdatePushDialog(getContext(), R.style.custom_dialog, loadAndUnloadTodoBeans.get(0).getFlightNo() + "收到新的货邮舱单，请查看！", () -> {

                    CargoManifestInfoActivity.startActivity(getContext(),loadAndUnloadTodoBeans.get(0),1);
//                    Intent intent = new Intent(getContext(), CargoManifestInfoActivity.class);
//                    intent.putExtra("data", loadAndUnloadTodoBeans.get(0));
//                    getContext().startActivity(intent);
                });
                pushDialog.show();
                getData();
            }

        }
    }


    private void getData() {
        mPresenter = new EndInstallTodoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setWorkerId(UserInfoSingle.getInstance().getUserId());
        entity.setCurrent(pageCurrent);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setFilterAtd(true);
        entity.setFilterHycd(true);

        ((EndInstallTodoPresenter) mPresenter).getEndInstallTodo(entity);
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
    public void toastView(String error) {
        if (pageCurrent == 1) {
            list.clear();
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
        if (error!=null)
            ToastUtil.showToast(error);
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
        getData();
    }

    @Override
    public void getEndInstallTodoResult(List <LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {
        if (pageCurrent == 1) {
            list1.clear();
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
        for (LoadAndUnloadTodoBean bean : loadAndUnloadTodoBean) {
            StringUtil.setFlightRoute(bean.getRoute(), bean);//设置航班航线信息
            //结载单独使用数据 json 解析
            if (!StringUtil.isEmpty(bean.getLoadingAndUnloadExtJson())){
                bean.setLoadingAndUnloadBean(JSON.parseObject(bean.getLoadingAndUnloadExtJson(), LoadingAndUnloadBean.class));
            }
        }
        list1.addAll(loadAndUnloadTodoBean);

        seachWith();
    }

    @Override
    public void slideTaskResult(String result) {

    }
}