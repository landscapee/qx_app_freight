package qx.app.freight.qxappfreight.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import qx.app.freight.qxappfreight.bean.ReservoirArea;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ArrivalDeliveryInfoBean;
import qx.app.freight.qxappfreight.bean.response.FilterTransportDateBase;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.WaybillArea;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.ArrivalDeliveryInfoContract;
import qx.app.freight.qxappfreight.contract.GroupBoardToDoContract;
import qx.app.freight.qxappfreight.contract.ListReservoirInfoContract;
import qx.app.freight.qxappfreight.dialog.ForkliftCostDialogForNet;
import qx.app.freight.qxappfreight.dialog.OutStoragePopWindow;
import qx.app.freight.qxappfreight.dialog.PickGoodsRecordsDialogForNet;
import qx.app.freight.qxappfreight.dialog.SortingReturnGoodsDialogForNet;
import qx.app.freight.qxappfreight.presenter.ArrivalDeliveryInfoPresenter;
import qx.app.freight.qxappfreight.presenter.GroupBoardToDoPresenter;
import qx.app.freight.qxappfreight.presenter.ListReservoirInfoPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * ????????????
 */
public class InportDeliveryDetailActivity extends BaseActivity implements ArrivalDeliveryInfoContract.arrivalDeliveryInfoView, ListReservoirInfoContract.listReservoirInfoView, GroupBoardToDoContract.GroupBoardToDoView {

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
    //    private String billId; //?????????
//    private String taskId;
    private int nowPosition;

//    private int num1;
//    private int num2;

    WaybillsBean bean = null;
    private boolean isAllOut = true;
    private List <WaybillsBean> mList;

    private HashMap <String, String> areas = new HashMap <>();

    private int waybillStatus = 5;//???????????? ?????? 5 ????????? 6 ???????????? ?????????

    private int currentNum = 0;// ????????????????????????
    private int residueNum = 0;// ??????????????????

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //?????? ?????? ?????? ??????
            if (bean.getSmInventorySummaryList() != null)
                showOutStorageListDialog(bean.getSmInventorySummaryList(), bean);
            else {
                ToastUtil.showDialogToast(InportDeliveryDetailActivity.this, "?????????????????????????????????????????????????????????????????????????????????????????????");
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_inport_delivery_detail;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        initView();
//        getAreaType();
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

        bean = (WaybillsBean) getIntent().getSerializableExtra("DATA");
        waybillStatus = getIntent().getIntExtra("waybillStatus", 5);
        if (bean == null) {
            finish();
        }
//        serialNumber.setText(bean.getSerialNumber());
        tvPickUpName.setText(bean.getPickUpGoodsUser());
        tvPickUpPhone.setText(bean.getPickUpGoodsUserPhone());
        tvPickUpCard.setText(bean.getPickUpGoodsUserCardId());

//        toolbar.setMainTitle(Color.WHITE,"??????("+num1+"/"+num2+")");
        toolbar.setMainTitle(Color.WHITE, "??????");
        rView.setLayoutManager(new LinearLayoutManager(this));

        mList = new ArrayList <>();
        mList.add(bean);
        mAdapter = new DeliveryDetailAdapter(mList);
        mAdapter.setDeliveryDetailInterface(new DeliveryDetailAdapter.DeliveryDetailInterface() {
            @Override
            public void outStorage(int position, String id, String outStorageUser) {
                if (mList.get(position).getSmInventorySummaryList() != null)
                    showOutStorageListDialog(mList.get(position).getSmInventorySummaryList(), mList.get(position));
                else {
                    ToastUtil.showDialogToast(InportDeliveryDetailActivity.this, "?????????????????????????????????????????????????????????????????????????????????????????????");
                }
//                deliveryInWaybill(
//                        position,
//                        id,
//                        outStorageUser,
//                        mList.get(position).getTallyingTotal()-mList.get(position).getOutboundNumber(),
//                        mList.get(position).getOverWieght(),
//                        mList.get(position).getOverWieghtCount());
            }

            @Override
            public void inputOverWeight(int position) {
                try {
                    SortingReturnGoodsDialogForNet dialog = new SortingReturnGoodsDialogForNet(InportDeliveryDetailActivity.this, mList.get(position).getId());
                    dialog.setOnClickListener(text -> {
                        getData();
                    });
                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void forkliftCost(int position) {
                try {
                    ForkliftCostDialogForNet dialog = new ForkliftCostDialogForNet(InportDeliveryDetailActivity.this, mList.get(position).getId(), mList.get(position).getWaybillCode());
                    dialog.setOnClickListener(text -> {
                        getData();
                    });
                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void outStorageRecords(int position) {
                try {
                    PickGoodsRecordsDialogForNet dialog = new PickGoodsRecordsDialogForNet(InportDeliveryDetailActivity.this, mList.get(position).getId());
                    dialog.setOnClickListener(text -> {
                        getData();
                    });
                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        rView.setAdapter(mAdapter);

//        btnConfirm.setOnClickListener(v -> {
//            //??????????????? ???????????? ????????????
//            if (isAllOut)
//                deliveryComplet();
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(0, 500);

    }


    //    //??????????????????????????????
//    private void getData() {
//        mPresenter = new ArrivalDeliveryInfoPresenter(this);
//        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
//        entity.setBillId(bean.getSerialNumber());
//        ((ArrivalDeliveryInfoPresenter)mPresenter).arrivalDataSave(entity);
//    }

    private void getData() {
        mPresenter = new GroupBoardToDoPresenter(this);
        BaseFilterEntity <WaybillsBean> entity = new BaseFilterEntity();
        entity.setSize(Constants.PAGE_SIZE);
        entity.setCurrent(1);
        WaybillsBean waybillsBean = new WaybillsBean();
        waybillsBean.setWaybillStatus(waybillStatus);
        waybillsBean.setWaybillCode(bean.getWaybillCode());
        entity.setFilter(waybillsBean);
        ((GroupBoardToDoPresenter) mPresenter).searchWaybillByWaybillCode(entity);
    }

    //??????
    private void deliveryInWaybill(List <WaybillArea> result) {


//        PutCargoInputDialog dialog = new PutCargoInputDialog();
//        dialog.setData(this,waitPutCargo,overWeight,overWeightCount);
//        dialog.setPutCargoInputListener(new PutCargoInputDialog.PutCargoInputListener() {
//            @Override
//            public void onConfirm(int data,int carNum) {
//                nowPosition = position;
//                mPresenter = new  ArrivalDeliveryInfoPresenter (InportDeliveryDetailActivity.this);
//                BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
//                entity.setCreateUser(outStorageUser);
//                entity.setWaybillId(id);
//                entity.setOutboundNumber(data);
//                entity.setForkliftTruckNumber(carNum);
////                entity.setOutStorageUser(outStorageUser);
//                currentNum = data;
//                residueNum = waitPutCargo - data;
//                ((ArrivalDeliveryInfoPresenter)mPresenter).deliveryInWaybill(entity);
//
//            }
//        });
//        dialog.show(getSupportFragmentManager(),"321");
//
//        Log.e("22222","1111---->"+position);


    }

    /**
     * @param
     * @return
     * @method
     * @description ??????????????????????????? ???????????? ??????
     * @date: 2020/4/15 17:17
     * @author: ??????
     */
    private void showOutStorageListDialog(List <WaybillArea> result, WaybillsBean waybillsBean) {
        OutStoragePopWindow outStoragePopWindow = new OutStoragePopWindow(this, result,waybillsBean, new OutStoragePopWindow.OnItemSelectedLisener() {
            @Override
            public void resultData(List <WaybillArea> waybillAreaBeans,long time) {
                List <BaseFilterEntity> list = new ArrayList <>();
                int total = 0;
                for (WaybillArea waybillAreaBean : waybillAreaBeans) {
                    if (waybillAreaBean.getOutboundNumber() > waybillAreaBean.getNumber()) {
                        ToastUtil.showToast("???????????????????????????????????????");
                        return;
                    }
                    BaseFilterEntity outboundAreaInfoEntity = new BaseFilterEntity();
                    outboundAreaInfoEntity.setOutboundNumber(waybillAreaBean.getOutboundNumber());
                    outboundAreaInfoEntity.setWaybillId(waybillsBean.getId());
                    outboundAreaInfoEntity.setAreaId(waybillAreaBean.getAreaId());
                    outboundAreaInfoEntity.setCreateUser(UserInfoSingle.getInstance().getUserId());
                    outboundAreaInfoEntity.setOutStorageTime(time);
                    list.add(outboundAreaInfoEntity);
                    currentNum += waybillAreaBean.getOutboundNumber();
                    total += waybillAreaBean.getNumber();
                }
                residueNum = total - currentNum;
                mPresenter = new ArrivalDeliveryInfoPresenter(InportDeliveryDetailActivity.this);
                ((ArrivalDeliveryInfoPresenter) mPresenter).deliveryInWaybill(list);
            }
        });
        outStoragePopWindow.showAtLocation(tvPickUpCard, Gravity.BOTTOM, 0, 0);

    }
//    //??????
//    private void deliveryComplet(){
//        mPresenter = new ArrivalDeliveryInfoPresenter(this);
//        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
//        entity.setBillId(bean.getSerialNumber());
//        entity.setTaskId(bean.getTaskId());
//        entity.setCompleteUser(UserInfoSingle.getInstance().getUserId());
//        ((ArrivalDeliveryInfoPresenter)mPresenter).completDelivery(entity);
//    }

    @Override
    public void arrivalDeliveryInfoResult(ArrivalDeliveryInfoBean result) {

        mList.clear();
        mList.addAll(result.getWaybills());
        for (WaybillsBean mWaybillsBean : mList) {
            mWaybillsBean.setRqName(areas.get(mWaybillsBean.getWarehouseArea()));
        }
        mAdapter.notifyDataSetChanged();
        int already = 0;
        for (WaybillsBean mWaybillsBean : mList) {
            if (mWaybillsBean.getWaybillStatus() == 6)
                already++;
        }
        isAllOut = true;
        for (WaybillsBean mWaybillsBean : mList) {
            if (mWaybillsBean.getWaybillStatus() != 6) {
                isAllOut = false;
                break;
            }
        }
        setBtnSuerState(isAllOut);

        toolbar.setMainTitle(Color.WHITE, "??????(" + already + "/" + mList.size() + ")");

    }

    private void setBtnSuerState(boolean isAllOut) {
        if (isAllOut) {
            btnConfirm.setEnabled(true);
            btnConfirm.setBackground(getDrawable(R.drawable.background_submit_press));
        } else {
            btnConfirm.setEnabled(false);
            btnConfirm.setBackgroundResource(R.color.gray_8f);
        }

    }

    @Override
    public void deliveryInWaybillResult(String result) {
//        if (result.getTotalNumberPackages()-result.getOutboundNumber() == 0){
//            mList.get(nowPosition).setOutStorageTime(result.getOutStorageTime());
//            mList.get(nowPosition).setWaybillStatus(result.getWaybillStatus());
//
//            num1 = num1+1;
//            toolbar.setMainTitle(Color.WHITE,"??????("+num1+"/"+num2+")");
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
        ToastUtil.showToast("?????????????????????" + currentNum + "???????????????" + residueNum + "???");
        currentNum = 0;
        residueNum = 0;
        getData();
//        showDetailsDialog(this, "?????????????????????" + currentNum + "???????????????" + residueNum + "???");

    }

    private void showDetailsDialog(Context context, String str) {
        //????????????
        CommonDialog dialog = new CommonDialog(context);
        dialog.setTitle("??????")
                .setMessage(str)
                .setNegativeButton("??????")
                .isCanceledOnTouchOutside(true)
                .isCanceled(true)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        currentNum = 0;
                        residueNum = 0;
                        getData();
                    }
                }).show();
    }

    @Override
    public void completDeliveryResult(String result) {
        finish();
    }

    @Override
    public void toastView(String error) {
        if (currentNum > 0) {
            ToastUtil.showErrorDialog("???????????????????????????!");
        } else
            ToastUtil.showErrorDialog(error);

    }

    @Override
    public void showNetDialog() {
        showProgessDialog("???????????????");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void listReservoirInfoResult(List <ReservoirArea> mReservoirAreas) {
//        areas.clear();
//        for (ReservoirArea mReservoirArea:mReservoirAreas){
//            areas.put(mReservoirArea.getId(),mReservoirArea.getReservoirName());
//        }
//        getData();
    }

    @Override
    public void getGroupBoardToDoResult(FilterTransportDateBase transportListBeans) {

    }

    @Override
    public void getOverWeightToDoResult(FilterTransportDateBase transportListBeans) {

    }

    @Override
    public void getScooterByScooterCodeResult(List <GetInfosByFlightIdBean> getInfosByFlightIdBean) {

    }

    @Override
    public void searchWaybillByWaybillCodeResult(List <WaybillsBean> waybillsBeans) {
        if (waybillsBeans != null && waybillsBeans.size() > 0) {
            mList.clear();
            mList.addAll(waybillsBeans);
            mAdapter.notifyDataSetChanged();
        } else
            finish();

    }
}
