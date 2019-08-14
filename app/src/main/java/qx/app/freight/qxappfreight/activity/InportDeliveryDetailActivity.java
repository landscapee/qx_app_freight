package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.DeliveryDetailAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;
import qx.app.freight.qxappfreight.bean.ReservoirArea;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ArrivalDeliveryInfoBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.contract.ArrivalDeliveryInfoContract;
import qx.app.freight.qxappfreight.contract.ListReservoirInfoContract;
import qx.app.freight.qxappfreight.dialog.BaggerInputDialog;
import qx.app.freight.qxappfreight.dialog.PutCargoInputDialog;
import qx.app.freight.qxappfreight.dialog.SortingReturnGoodsDialog;
import qx.app.freight.qxappfreight.dialog.SortingReturnGoodsDialogForNet;
import qx.app.freight.qxappfreight.presenter.ArrivalDeliveryInfoPresenter;
import qx.app.freight.qxappfreight.presenter.ListReservoirInfoPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 提货详情
 */
public class InportDeliveryDetailActivity extends BaseActivity implements ArrivalDeliveryInfoContract.arrivalDeliveryInfoView , ListReservoirInfoContract.listReservoirInfoView{

    @BindView(R.id.r_view)
    RecyclerView rView;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.consignee)
    TextView tvPickUpName;
    @BindView(R.id.consignee_phone)
    TextView tvPickUpPhone;
    @BindView(R.id.consignee_card)
    TextView tvPickUpCard;
    @BindView(R.id.serial_number)
    TextView serialNumber;


    private DeliveryDetailAdapter mAdapter;
    private CustomToolbar toolbar;
//    private String billId; //流水号
//    private String taskId;
    private int nowPosition;

//    private int num1;
//    private int num2;

    TransportDataBase bean = null;
    private  boolean isAllOut = true;
    private List<WaybillsBean> mList ;

    private HashMap<String,String> areas = new HashMap <>();

    List<RcInfoOverweight> rcInfoOverweight; // 超重记录列表
    List<RcInfoOverweight> rcTempInfoOverweight; // 超重记录列表备份（用于dialog）

    @Override
    public int getLayoutId() {
        return R.layout.activity_inport_delivery_detail;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        initView();
        getAreaType();
    }

    private void getAreaType() {
        mPresenter = new ListReservoirInfoPresenter(this);
        ((ListReservoirInfoPresenter) mPresenter).listReservoirInfoByCode(UserInfoSingle.getInstance().getDeptCode());
    }

    private void initView() {
//        billId = getIntent().getStringExtra("billId");
//        taskId = getIntent().getStringExtra("taskId");
//        num1 = getIntent().getIntExtra("num1",0);
//        num2 = getIntent().getIntExtra("num2",0);

        bean = (TransportDataBase) getIntent().getSerializableExtra("DATA");

        if (bean == null){

            finish();
        }
        serialNumber.setText(bean.getSerialNumber());
        tvPickUpName.setText(bean.getConsignee());
        tvPickUpPhone.setText(bean.getConsigneePhone());
        tvPickUpCard.setText(bean.getConsigneeIdentityCard());

//        toolbar.setMainTitle(Color.WHITE,"提货("+num1+"/"+num2+")");
        toolbar.setMainTitle(Color.WHITE,"提货");

        rView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mAdapter = new DeliveryDetailAdapter(mList);
        mAdapter.setDeliveryDetailInterface(new DeliveryDetailAdapter.DeliveryDetailInterface() {
            @Override
            public void outStorage(int position, String id, String outStorageUser) {
                deliveryInWaybill(
                        position,
                        id,
                        outStorageUser,
                        mList.get(position).getTallyingTotal()-mList.get(position).getOutboundNumber(),
                        mList.get(position).getOverWieght(),
                        mList.get(position).getOverWieghtCount());
            }

            @Override
            public void inputOverWeight(int position) {
                try {
                    SortingReturnGoodsDialogForNet dialog = new SortingReturnGoodsDialogForNet(InportDeliveryDetailActivity.this,mList.get(position).getId());
                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        rView.setAdapter(mAdapter);

        btnConfirm.setOnClickListener(v -> {
            //未完全出库 不能完成 提货任务
            if (isAllOut)
                deliveryComplet();
        });
    }
    //根据流水单号获取列表
    private void getData() {
        mPresenter = new ArrivalDeliveryInfoPresenter(this);
        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setBillId(bean.getSerialNumber());
        ((ArrivalDeliveryInfoPresenter)mPresenter).arrivalDataSave(entity);
    }
    //出库
    private void deliveryInWaybill(int position,String id, String outStorageUser,int waitPutCargo,double overWeight,int overWeightCount){

        PutCargoInputDialog dialog = new PutCargoInputDialog();
        dialog.setData(this,waitPutCargo,overWeight,overWeightCount);
        dialog.setPutCargoInputListener(new PutCargoInputDialog.PutCargoInputListener() {
            @Override
            public void onConfirm(int data,int carNum) {
                nowPosition = position;
                BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
                entity.setCreateUser(outStorageUser);
                entity.setWaybillId(id);
                entity.setOutboundNumber(data);
                entity.setForkliftTruckNumber(carNum);
//                entity.setOutStorageUser(outStorageUser);
                ((ArrivalDeliveryInfoPresenter)mPresenter).deliveryInWaybill(entity);
            }
        });
        dialog.show(getSupportFragmentManager(),"321");

        Log.e("22222","1111---->"+position);


    }
    //完成
    private void deliveryComplet(){
        mPresenter = new ArrivalDeliveryInfoPresenter(this);
        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setBillId(bean.getSerialNumber());
        entity.setTaskId(bean.getTaskId());
        entity.setCompleteUser(UserInfoSingle.getInstance().getUserId());
        ((ArrivalDeliveryInfoPresenter)mPresenter).completDelivery(entity);
    }

    @Override
    public void arrivalDeliveryInfoResult(ArrivalDeliveryInfoBean result) {

        mList.clear();
        mList.addAll(result.getWaybills());
        for (WaybillsBean mWaybillsBean:mList){
            mWaybillsBean.setRqName(areas.get(mWaybillsBean.getWarehouseArea()));
        }
        rcInfoOverweight = new ArrayList <>();
        mAdapter.notifyDataSetChanged();
        int already = 0;
        for (WaybillsBean mWaybillsBean:mList){
            if (mWaybillsBean.getWaybillStatus() == 6)
                already++;
        }
        isAllOut = true;
        for (WaybillsBean mWaybillsBean : mList){
            if (mWaybillsBean.getWaybillStatus() != 6){
                isAllOut = false;
                break;
            }
        }
        setBtnSuerState(isAllOut);

        toolbar.setMainTitle(Color.WHITE,"提货("+already+"/"+mList.size()+")");

    }

    private void setBtnSuerState(boolean isAllOut) {
        if (isAllOut){
            btnConfirm.setEnabled(true);
        }
        else {
            btnConfirm.setEnabled(false);
        }

    }

    @Override
    public void deliveryInWaybillResult(String result) {
//        if (result.getTotalNumberPackages()-result.getOutboundNumber() == 0){
//            mList.get(nowPosition).setOutStorageTime(result.getOutStorageTime());
//            mList.get(nowPosition).setWaybillStatus(result.getWaybillStatus());
//
//            num1 = num1+1;
//            toolbar.setMainTitle(Color.WHITE,"提货("+num1+"/"+num2+")");
//        }
//        else{
//            int j = 0;
//            for (int i = 0;i<mList.size();i++){
//                if (mList.get(i).getId().equals(result.getId())){
//                    j=i;
//                }
//            }
//            mList.set(j,result);
//        }
//        mAdapter.notifyDataSetChanged();
        getData();
    }

    @Override
    public void completDeliveryResult(String result) {
        finish();
    }

    @Override
    public void toastView(String error) {

        ToastUtil.showToast(error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("请求中……");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void listReservoirInfoResult(List <ReservoirArea> mReservoirAreas) {
        areas.clear();
        for (ReservoirArea mReservoirArea:mReservoirAreas){
            areas.put(mReservoirArea.getId(),mReservoirArea.getReservoirName());
        }
        getData();
    }
}
