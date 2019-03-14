package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.DeliveryDetailAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ArrivalDeliveryInfoBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.contract.ArrivalDeliveryInfoContract;
import qx.app.freight.qxappfreight.dialog.ChooseStoreroomDialog;
import qx.app.freight.qxappfreight.dialog.PopTestDialog;
import qx.app.freight.qxappfreight.model.TestBean;
import qx.app.freight.qxappfreight.presenter.ArrivalDeliveryInfoPresenter;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

public class InportDeliveryDetailActivity extends BaseActivity implements ArrivalDeliveryInfoContract.arrivalDeliveryInfoView {

    @BindView(R.id.r_view)
    RecyclerView rView;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private DeliveryDetailAdapter mAdapter;
    private CustomToolbar toolbar;
    private String billId; //流水号
    private String taskId;
    private int nowPosition;

    private int num1;
    private int num2;

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
        num1 = getIntent().getIntExtra("num1",0);
        num2 = getIntent().getIntExtra("num2",0);
        toolbar.setMainTitle(Color.WHITE,"提货("+num1+"/"+num2+")");
        mPresenter = new ArrivalDeliveryInfoPresenter(this);
        rView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mAdapter = new DeliveryDetailAdapter(mList);
        mAdapter.setDeliveryDetailInterface(new DeliveryDetailAdapter.DeliveryDetailInterface() {
            @Override
            public void outStorage(int position, String id, String outStorageUser) {
                deliveryInWaybill(position,id,outStorageUser);
            }

        });

        rView.setAdapter(mAdapter);

        btnConfirm.setOnClickListener(v -> {
//            deliveryComplet();
            showDialog();
//            showChooseDialog();
        });
    }
    //根据流水单号获取列表
    private void getData() {

        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setBillId(billId);
        ((ArrivalDeliveryInfoPresenter)mPresenter).arrivalDataSave(entity);
    }
    //出库
    private void deliveryInWaybill(int position,String id, String outStorageUser){
        Log.e("22222","1111---->"+position);
        nowPosition = position;
        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setId(id);
        entity.setOutStorageUser(outStorageUser);
        ((ArrivalDeliveryInfoPresenter)mPresenter).deliveryInWaybill(entity);
    }
    //完成
    private void deliveryComplet(){
        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setCounterbillId(billId);
        entity.setTaskId(taskId);
        entity.setCompleteUser(UserInfoSingle.getInstance().getUserId());
        ((ArrivalDeliveryInfoPresenter)mPresenter).completDelivery(entity);
    }

    private void showDialog(){
        List<TestBean> list22 = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            if (i==20){
                list22.add(new TestBean(1,i));
            }else if (i==25){
                list22.add(new TestBean(1,i));
            }else {
                list22.add(new TestBean(0,i));
            }

        }

        PopTestDialog dialog = new PopTestDialog();
        dialog.setData(list22,this);
        dialog.show(getSupportFragmentManager(),"123");
    }

    private void showChooseDialog(){
        List<TestBean> list22 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list22.add(new TestBean("库房"+i,false));
        }

        ChooseStoreroomDialog dialog = new ChooseStoreroomDialog();
        dialog.setData(list22,this);
        dialog.show(getSupportFragmentManager(),"123");
    }

    @Override
    public void arrivalDeliveryInfoResult(ArrivalDeliveryInfoBean result) {
        mList.clear();
        mList.addAll(result.getWaybills());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deliveryInWaybillResult(ArrivalDeliveryInfoBean.WaybillsBean result) {
        mList.get(nowPosition).setOutStorageTime(result.getOutStorageTime());
        mList.get(nowPosition).setWaybillStatus(6);
        mAdapter.notifyDataSetChanged();
        num1 = num1+1;
        toolbar.setMainTitle(Color.WHITE,"提货("+num1+"/"+num2+")");
    }

    @Override
    public void completDeliveryResult(String result) {
        finish();
    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {
        showProgessDialog("");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }
}
