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
import qx.app.freight.qxappfreight.bean.response.ReturnBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.AgentTransportationListContract;
import qx.app.freight.qxappfreight.contract.ReturnCargoCommitContract;
import qx.app.freight.qxappfreight.contract.ReturnTransportationListContract;
import qx.app.freight.qxappfreight.presenter.AgentTransportationListPresent;
import qx.app.freight.qxappfreight.presenter.ReturnCargoCommitPresenter;
import qx.app.freight.qxappfreight.presenter.ReturnTransportationListPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * TODO : 出港退货
 * Created by pr
 */
public class ReturnGoodsActivity extends BaseActivity implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, ReturnCargoCommitContract.returnCargoCommitView, ReturnTransportationListContract.returnTransportationListView {
    @BindView(R.id.mfrv_returngood_list)
    MultiFunctionRecylerView mMfrvAllocateList;
    @BindView(R.id.bt_sure)
    Button mBtSure;
    private ReturnGoodAdapter adapter;
    private TransportDataBase mBean;
    private List<ReturnBean> list;
    private CustomToolbar toolbar;
    private int pageCurrent = 1;


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
        mBean = (TransportDataBase) getIntent().getSerializableExtra("TransportListBean");
    }

    private void initData() {
        mPresenter = new ReturnTransportationListPresenter(this);
        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
        MyAgentListBean myAgentListBean = new MyAgentListBean();
        myAgentListBean.setWaybillId(mBean.getId());
        myAgentListBean.setTaskTypeCode(mBean.getTaskTypeCode());
        baseFilterEntity.setSize(Constants.PAGE_SIZE);
        baseFilterEntity.setCurrent(pageCurrent);
        baseFilterEntity.setFilter(myAgentListBean);
        ((ReturnTransportationListPresenter) mPresenter).returnTransportationList(baseFilterEntity);
        click();
    }

    private void click() {
        mBtSure.setOnClickListener(v -> {
//            if (list.size() <= 0) {
//                return;
//            }
            mPresenter = new ReturnCargoCommitPresenter(this);
            TransportListCommitEntity entity = new TransportListCommitEntity();
            entity.setTaskId(mBean.getTaskId());
            entity.setWaybillId(mBean.getWaybillId());
            entity.setUserId(UserInfoSingle.getInstance().getUserId());
            ((ReturnCargoCommitPresenter) mPresenter).returnCargoCommit(entity);
        });


    }

    @Override
    public void onRetry() {

    }

    @Override
    public void onRefresh() {
        pageCurrent = 1;
        initData();
    }

    @Override
    public void onLoadMore() {
        pageCurrent++;
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
        if (pageCurrent == 1) {
            mMfrvAllocateList.finishRefresh();
        } else {
            mMfrvAllocateList.finishLoadMore();
        }
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
    public void returnTransportationListResult(List<ReturnBean> addScooterBean) {
        if (addScooterBean!=null) {
            if (pageCurrent == 1) {
                mMfrvAllocateList.finishRefresh();
            } else {
                mMfrvAllocateList.finishLoadMore();
            }
            toolbar.setMainTitle(Color.WHITE, "出港退货" + "(" + addScooterBean.size() + ")");
            if (0 != addScooterBean.size()) {
                list = addScooterBean;
                adapter = new ReturnGoodAdapter(addScooterBean);
                mMfrvAllocateList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                ToastUtil.showToast("数据为空");
            }
        }
    }
}
