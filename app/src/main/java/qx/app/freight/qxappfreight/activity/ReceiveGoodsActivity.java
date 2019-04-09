package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.drakeet.materialdialog.MaterialDialog;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ReceiveGoodsAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.TransportListCommitEntity;
import qx.app.freight.qxappfreight.bean.response.AgentBean;
import qx.app.freight.qxappfreight.bean.response.AutoReservoirBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.AgentTransportationListContract;
import qx.app.freight.qxappfreight.contract.TransportListCommitContract;
import qx.app.freight.qxappfreight.presenter.AgentTransportationListPresent;
import qx.app.freight.qxappfreight.presenter.TransportListCommitPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionSlideRecylerView;

/**
 * TODO :出港收货页面
 * Created by pr
 */
public class ReceiveGoodsActivity extends BaseActivity implements AgentTransportationListContract.agentTransportationListView, TransportListCommitContract.transportListCommitView, MultiFunctionSlideRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_receive_good)
    MultiFunctionSlideRecylerView mMfrvData;
    @BindView(R.id.btn_receive_good)
    Button mBtnReceiveGood;         //提交
    @BindView(R.id.btn_printing)
    TextView mBtnPrinting;            //打印
    @BindView(R.id.tv_total_number)
    TextView mTvTotalNumber;        //总件数
    @BindView(R.id.tv_total_volume)
    TextView mTvTotalVolume;        //总体积
    @BindView(R.id.tv_total_weight)
    TextView mTvTotalWeight;        //总重量

    private ReceiveGoodsAdapter mAdapter;
    private List<MyAgentListBean> list;
    private String waybillId, taskId, reservoirType, waybillCode;
    private CustomToolbar toolbar;
    private List<TransportListBean.DeclareItemBean> mDeclareItemBeans;
    private TransportListCommitEntity transportListCommitEntity;
    private String mScooterCode;

    private int pageCurrent = 1;

    //库区
    private String reservoirName;

    public static void startActivity(Activity context, String waybillId, String taskId, String waybillCode, List<TransportListBean.DeclareItemBean> declareItemBean, String reservoirType) {
        Intent starter = new Intent(context, ReceiveGoodsActivity.class);
        starter.putExtra("waybillId", waybillId);
        starter.putExtra("waybillCode", waybillCode);
        starter.putExtra("taskId", taskId);
        starter.putExtra("reservoirType", reservoirType);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("transportListBeans", (Serializable) declareItemBean);
        starter.putExtras(mBundle);
        context.startActivityForResult(starter, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_receive_goods;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        EventBus.getDefault().register(this);
        toolbar = getToolbar();
        waybillId = getIntent().getStringExtra("waybillId");
        taskId = getIntent().getStringExtra("taskId");
        reservoirType = getIntent().getStringExtra("reservoirType");
        waybillCode = getIntent().getStringExtra("waybillCode");
        mDeclareItemBeans = (List<TransportListBean.DeclareItemBean>) getIntent().getSerializableExtra("transportListBeans");
        toolbar.setRightTextViewImage(this, View.VISIBLE, R.color.flight_a, "新增", R.mipmap.new_2, v -> {
            //扫一扫
            ScanManagerActivity.startActivity(this);
        });
        //提交
        mBtnReceiveGood.setOnClickListener(v -> commit());
        getAutoReservoir();
        initView();
    }

    public void getAutoReservoir() {
        mPresenter = new AgentTransportationListPresent(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setDeptCode(UserInfoSingle.getInstance().getDeptCode());
        entity.setReservoirType(Integer.valueOf(reservoirType));
        ((AgentTransportationListPresent) mPresenter).autoReservoirv(entity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (result.getFunctionFlag().equals("ReceiveGoodsActivity")) {
            //板车号
            mScooterCode = result.getData();
            if (!"".equals(mScooterCode)) {
                startAct(mScooterCode);
            } else {
                ToastUtil.showToast(ReceiveGoodsActivity.this, "扫码数据为空请重新扫码");
            }
        } else {
            Log.e("resultCode", "收货页面不是200");
        }
    }

    public void startAct(String mScooterCode) {
        AddReceiveGoodActivity.startActivity(ReceiveGoodsActivity.this, waybillId, mScooterCode, waybillCode, mDeclareItemBeans);
    }


    //提交
    public void commit() {
        mPresenter = new TransportListCommitPresenter(this);
        if (list.size() != 0) {
            transportListCommitEntity = new TransportListCommitEntity();
            transportListCommitEntity.setType("1");//1提交
            transportListCommitEntity.setTaskId(taskId);//当前任务id
            transportListCommitEntity.setUserId(UserInfoSingle.getInstance().getUserId());//当前操作人
            transportListCommitEntity.setWaybillId(waybillId);
            List<TransportListCommitEntity.RcInfosEntity> mListRcInfosEntity = new ArrayList<>();
            for (MyAgentListBean mMyAgentListBean : list) {
                TransportListCommitEntity.RcInfosEntity rcInfosEntity = new TransportListCommitEntity.RcInfosEntity();
                rcInfosEntity.setId(mMyAgentListBean.getId());
                rcInfosEntity.setReservoirName(mMyAgentListBean.getReservoirName());
                rcInfosEntity.setReservoirType(reservoirType);
                rcInfosEntity.setWaybillId(mMyAgentListBean.getWaybillId());
                rcInfosEntity.setWaybillCode(mMyAgentListBean.getWaybillCode());
                rcInfosEntity.setCargoId(mMyAgentListBean.getCargoId());
                rcInfosEntity.setNumber(mMyAgentListBean.getNumber());
                rcInfosEntity.setWeight(mMyAgentListBean.getWeight());
                rcInfosEntity.setVolume(mMyAgentListBean.getVolume());
                rcInfosEntity.setPackagingType(mMyAgentListBean.getPackagingType());
                rcInfosEntity.setScooterId(mMyAgentListBean.getScooterId());
                rcInfosEntity.setUldId(mMyAgentListBean.getUldId());
                rcInfosEntity.setOverWeight(mMyAgentListBean.getOverWeight());
                rcInfosEntity.setRepType(mMyAgentListBean.getRepType());
                rcInfosEntity.setRepPlaceId(mMyAgentListBean.getRepPlaceId());
                mListRcInfosEntity.add(rcInfosEntity);
            }
            transportListCommitEntity.setRcInfos(mListRcInfosEntity);
            ((TransportListCommitPresenter) mPresenter).transportListCommit(transportListCommitEntity);
        } else {
            ToastUtil.showToast(this, "请先添加数据");
        }
    }

    public void upData() {
        mPresenter = new AgentTransportationListPresent(this);
        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
        MyAgentListBean myAgentListBean = new MyAgentListBean();
        myAgentListBean.setWaybillId(waybillId);
        baseFilterEntity.setSize(10);
        baseFilterEntity.setCurrent(pageCurrent);
        baseFilterEntity.setFilter(myAgentListBean);
        ((AgentTransportationListPresent) mPresenter).agentTransportationList(baseFilterEntity);
    }

    public void initView() {
        mTvTotalNumber.setText("总件数:" + 0 + "");
        mTvTotalVolume.setText("总体积:" + 0 + "m³");
        mTvTotalWeight.setText("总重量" + 0 + "kg");
        mMfrvData.setLayoutManager(new LinearLayoutManager(this));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        list = new ArrayList<>();
        mAdapter = new ReceiveGoodsAdapter(list);
        mMfrvData.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
        mAdapter.setOnDeleteClickListener((view, position) -> {
            if (list.size() != 0) {
                mPresenter = new TransportListCommitPresenter(this);
                mMfrvData.closeMenu();
                ((TransportListCommitPresenter) mPresenter).deleteCollectionInfo(list.get(position).getId());
                Toast.makeText(ReceiveGoodsActivity.this, "当前删除：" + position, Toast.LENGTH_SHORT).show();
            }
        });
        upData();
    }

    @Override
    public void onRefresh() {
        pageCurrent = 1;
        upData();
    }

    @Override
    public void onLoadMore() {
        upData();
    }

    @Override
    public void agentTransportationListResult(AgentBean myAgentListBean) {
        toolbar.setMainTitle(Color.WHITE, "出港收货" + "(" + myAgentListBean.getRecords().size() + ")");
        if (myAgentListBean != null) {
            if (pageCurrent == 1) {
                list.clear();
                mMfrvData.finishRefresh();
            } else {
                pageCurrent++;
                mMfrvData.finishLoadMore();
            }
            list.addAll(myAgentListBean.getRecords());
            int number = 0;
            int weight = 0;
            int volume = 0;
            for (MyAgentListBean myAgentListBean1 : list) {
                number += myAgentListBean1.getNumber();
                weight += myAgentListBean1.getWeight();
                volume += myAgentListBean1.getVolume();
                if (!"".equals(reservoirName))
                    myAgentListBean1.setReservoirName(reservoirName);
            }
            mTvTotalNumber.setText("总件数:" + number + "");
            mTvTotalVolume.setText("总体积:" + volume + "m³");
            mTvTotalWeight.setText("总重量" + weight + "kg");
            mMfrvData.notifyForAdapter(mAdapter);
        } else {
            ToastUtil.showToast(this, "数据错误");
        }
    }

    @Override
    public void autoReservoirvResult(AutoReservoirBean myAgentListBean) {
        if (null != myAgentListBean) {
            reservoirName = myAgentListBean.getReservoirName();
        } else
            ToastUtil.showToast("数据为空");
    }

    @Override
    public void transportListCommitResult(String result) {
        if (!"".equals(result)) {
            MaterialDialog normalDialog = new MaterialDialog(this);
            View mView = LayoutInflater.from(this).inflate(R.layout.dialog_progressbar, null);
            normalDialog.setMessage("提交中");
            normalDialog.setContentView(mView);
            normalDialog.show();
            new Handler().postDelayed(() -> {
                EventBus.getDefault().post("collector_refresh");
                finish();
            }, 3000);
        }
    }


    //删除板车
    @Override
    public void deleteCollectionInfoResult(String result) {
        upData();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void toastView(String error) {
        Log.e("refresh", error);
        if (pageCurrent == 1) {
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
    }

    @Override
    public void showNetDialog() {
//        showProgessDialog("提交中");
    }

    @Override
    public void dissMiss() {
//        dismissProgessDialog();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            MyAgentListBean mMyAgentListBean = (MyAgentListBean) data.getSerializableExtra("mMyAgentListBean");
            //总重量，总体积，总件数 计算
            if (mMyAgentListBean != null) {
                int number = 0;
                int weight = 0;
                int volume = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getCargoCn().equals(mMyAgentListBean.getCargoCn())&&list.get(i).getScooterCode().equals(mMyAgentListBean.getScooterCode())){
                        ToastUtil.showToast("当前板车号已在列表中请勿重复添加");
                        return;
                    }
                    number += list.get(i).getNumber();
                    weight += list.get(i).getWeight();
                    volume += list.get(i).getVolume();
                    if (!"".equals(reservoirName))
                        mMyAgentListBean.setReservoirName(reservoirName);
                }
                list.add(mMyAgentListBean);
                mTvTotalNumber.setText("总件数:" + number + "");
                mTvTotalVolume.setText("总体积:" + volume + "m³");
                mTvTotalWeight.setText("总重量" + weight + "kg");
                mMfrvData.notifyForAdapter(mAdapter);
            }
        } else if (Constants.SCAN_RESULT == resultCode) {
            mScooterCode = data.getStringExtra(Constants.SACN_DATA);
            if (!"".equals(mScooterCode)) {
                startAct(mScooterCode);
            } else {
                ToastUtil.showToast(ReceiveGoodsActivity.this, "扫码数据为空请重新扫码");
            }
        } else {
            Log.e("resultCode", "收货页面不是200");
        }
    }

    @Override
    public void onRetry() {
        showProgessDialog("加载数据中……");
        new Handler().postDelayed(() -> {
            upData();
            dismissProgessDialog();
        }, 2000);
    }
}
