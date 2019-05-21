package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ReturnGoodAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.TransportListCommitEntity;
import qx.app.freight.qxappfreight.bean.response.AgentBean;
import qx.app.freight.qxappfreight.bean.response.AutoReservoirBean;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.contract.AgentTransportationListContract;
import qx.app.freight.qxappfreight.contract.ReturnCargoCommitContract;
import qx.app.freight.qxappfreight.presenter.AgentTransportationListPresent;
import qx.app.freight.qxappfreight.presenter.ReturnCargoCommitPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * TODO : 出港退货
 * Created by pr
 */
public class ReturnGoodsActivity extends BaseActivity implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, ReturnCargoCommitContract.returnCargoCommitView, AgentTransportationListContract.agentTransportationListView {
    @BindView(R.id.mfrv_returngood_list)
    MultiFunctionRecylerView mMfrvAllocateList;
    @BindView(R.id.bt_sure)
    Button mBtSure;
    private ReturnGoodAdapter adapter;
    private TransportListBean.TransportDataBean mBean;
    private List<MyAgentListBean> list;
    private CustomToolbar toolbar;


    public static void startActivity(Activity context, TransportDataBase mBean) {
        Intent intent = new Intent(context, ReturnGoodsActivity.class);
        intent.putExtra("TransportListBean", mBean);
        intent.putExtras(intent);
        context.startActivityForResult(intent, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_return_good;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        initView();
        initData();
    }

    private void initView() {
        list = new ArrayList<>();
        mMfrvAllocateList.setLayoutManager(new LinearLayoutManager(this));
        mMfrvAllocateList.setRefreshListener(this);
        mMfrvAllocateList.setOnRetryLisenter(this);
        mBean = (TransportListBean.TransportDataBean) getIntent().getSerializableExtra("TransportListBean");
    }

    private void initData() {
        mPresenter = new AgentTransportationListPresent(this);
        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
        MyAgentListBean myAgentListBean = new MyAgentListBean();
        myAgentListBean.setWaybillId(mBean.getId());
        baseFilterEntity.setSize(10);
        baseFilterEntity.setCurrent(1);
        baseFilterEntity.setFilter(myAgentListBean);
        ((AgentTransportationListPresent) mPresenter).agentTransportationList(baseFilterEntity);
        click();
    }

    private void click() {
        mBtSure.setOnClickListener(v -> {
            if (list.size() <= 0) {
                return;
            }
            mPresenter = new ReturnCargoCommitPresenter(this);
            TransportListCommitEntity entity = new TransportListCommitEntity();
            DeclareWaybillBean mDeclareBean = new DeclareWaybillBean();
            //最外层
            entity.setType("1");
            entity.setTaskId(mBean.getTaskId());
            entity.setUserId(UserInfoSingle.getInstance().getUserId());
            entity.setWaybillId(mBean.getId());
            //第一个实体
            entity.setWaybillInfo(mDeclareBean);
            //第二个实体
            List<TransportListCommitEntity.RcInfosEntity> mListRcInfosEntity = new ArrayList<>();
            if (list.size() > 0) {
                for (MyAgentListBean mMyAgentListBean : list) {
                    TransportListCommitEntity.RcInfosEntity rcInfosEntity = new TransportListCommitEntity.RcInfosEntity();
                    rcInfosEntity.setId(mMyAgentListBean.getId());
                    rcInfosEntity.setCargoId(mMyAgentListBean.getCargoId());
                    rcInfosEntity.setCargoCn(mMyAgentListBean.getCargoCn());
                    rcInfosEntity.setReservoirName(mMyAgentListBean.getReservoirName());
                    rcInfosEntity.setReservoirType(mBean.getColdStorage());
                    rcInfosEntity.setWaybillId(mMyAgentListBean.getWaybillId());
                    rcInfosEntity.setWaybillCode(mMyAgentListBean.getWaybillCode());
                    rcInfosEntity.setCargoId(mMyAgentListBean.getCargoId());
                    rcInfosEntity.setNumber(mMyAgentListBean.getNumber());
                    rcInfosEntity.setWeight((int) mMyAgentListBean.getWeight());
                    rcInfosEntity.setVolume(mMyAgentListBean.getVolume());
                    rcInfosEntity.setPackagingType(mMyAgentListBean.getPackagingType());
                    rcInfosEntity.setScooterId(mMyAgentListBean.getScooterId());
                    rcInfosEntity.setUldId(mMyAgentListBean.getUldId());
                    rcInfosEntity.setOverWeight(mMyAgentListBean.getOverWeight());
                    rcInfosEntity.setRepType(mMyAgentListBean.getRepType());
                    rcInfosEntity.setRepPlaceId(mMyAgentListBean.getRepPlaceId());
                    mListRcInfosEntity.add(rcInfosEntity);
                }
            }
            entity.setRcInfos(mListRcInfosEntity);
            ((ReturnCargoCommitPresenter) mPresenter).returnCargoCommit(entity);
        });


    }

    @Override
    public void onRetry() {

    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {
        initData();
    }

    @Override
    public void returnCargoCommitResult(String result) {
        if (!TextUtils.isEmpty(result)) {
            EventBus.getDefault().post("collector_refresh");
            ToastUtil.showToast(result);
            finish();
        }
    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {
        showProgessDialog("提交中");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void agentTransportationListResult(AgentBean myAgentListBean) {
        mMfrvAllocateList.finishRefresh();
        mMfrvAllocateList.finishLoadMore();
        toolbar.setMainTitle(Color.WHITE, "出港退货" + "(" + myAgentListBean.getRcInfo().size() + ")");
        if (0 != myAgentListBean.getRcInfo().size()) {
            list = myAgentListBean.getRcInfo();
            adapter = new ReturnGoodAdapter(myAgentListBean.getRcInfo());
            mMfrvAllocateList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            ToastUtil.showToast("数据为空");
        }
    }

    @Override
    public void autoReservoirvResult(AutoReservoirBean myAgentListBean) {

    }
}
