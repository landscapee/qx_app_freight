package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.DeliveryDetailAdapter;
import qx.app.freight.qxappfreight.adapter.InPortDeliveryAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ArrivalDeliveryInfoBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.contract.ArrivalDeliveryInfoContract;
import qx.app.freight.qxappfreight.presenter.ArrivalDeliveryInfoPresenter;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

public class InportDeliveryDetailActivity extends BaseActivity implements ArrivalDeliveryInfoContract.arrivalDeliveryInfoView {

    @BindView(R.id.r_view)
    RecyclerView rView;

    private DeliveryDetailAdapter mAdapter;
    private CustomToolbar toolbar;
    private String billId; //流水号
    private String taskId;

    private List<ArrivalDeliveryInfoBean.WaybillsBean> mList ;

    @Override
    public int getLayoutId() {
        return R.layout.activity_inport_delivery_detail;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        initView();
        getData();
    }

    private void initView() {
        billId = getIntent().getStringExtra("billId");
        taskId = getIntent().getStringExtra("taskId");
        mPresenter = new ArrivalDeliveryInfoPresenter(this);
        rView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mAdapter = new DeliveryDetailAdapter(mList);
        mAdapter.setDeliveryDetailInterface(new DeliveryDetailAdapter.DeliveryDetailInterface() {
            @Override
            public void outStorage(String id, String outStorageUser) {
                deliveryInWaybill(id,outStorageUser);
            }
        });
        rView.setAdapter(mAdapter);
    }
    //根据流水单号获取列表
    private void getData() {

        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setBillId(billId);
        ((ArrivalDeliveryInfoPresenter)mPresenter).arrivalDataSave(entity);
    }
    //出库
    private void deliveryInWaybill(String id, String outStorageUser){
        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setId(id);
        entity.setOutStorageUser(outStorageUser);
        ((ArrivalDeliveryInfoPresenter)mPresenter).arrivalDataSave(entity);
    }
    //完成
    private void deliveryComplet(){
        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setCounterbillId("111111");
        entity.setTaskId(taskId);
        entity.setCompleteUser("1111111");
        ((ArrivalDeliveryInfoPresenter)mPresenter).arrivalDataSave(entity);
    }

    @Override
    public void arrivalDeliveryInfoResult(ArrivalDeliveryInfoBean result) {
        mList.clear();
        mList.addAll(result.getWaybills());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deliveryInWaybillResult(String result) {

    }

    @Override
    public void completDeliveryResult(String result) {

    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }
}
