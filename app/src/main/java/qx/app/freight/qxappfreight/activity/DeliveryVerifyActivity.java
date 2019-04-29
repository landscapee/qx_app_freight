package qx.app.freight.qxappfreight.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.BindViews;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.DeliveryVerifyAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.ChangeWaybillEntity;
import qx.app.freight.qxappfreight.bean.request.DeclareWaybillEntity;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.contract.DeliveryVerifyContract;
import qx.app.freight.qxappfreight.presenter.DeliveryVerifyPresenter;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 换单审核
 * create by guohao 2019/19/05
 *
 */
public class DeliveryVerifyActivity extends BaseActivity implements DeliveryVerifyContract.deliveryVerifyView {

    String id = "";//这是ChangeWaybill实体的id，来自前一个页面
    String taskId = "";//同上
    CustomToolbar customToolbar;

    DeliveryVerifyAdapter mAdapter;

    @BindView(R.id.tv_newDeclareWaybillCode)
    TextView newWayBillCodeTv;
    @BindView(R.id.tv_waybillCode)
    TextView oldWayBillCodeTv;
    @BindView(R.id.tv_flight_no)
    TextView flightNoTv;
    @BindView(R.id.tv_flight_data)
    TextView flightDataTv;
    @BindView(R.id.tv_flight_target)
    TextView targetTv;
    @BindView(R.id.tv_flight_start_station)
    TextView startStationTv;
    @BindView(R.id.tv_flight_mid_station)
    TextView midStationTv;
    @BindView(R.id.tv_flight_final_station)
    TextView finalStationTv;
    @BindView(R.id.tv_receive_name)
    TextView nameTv;
    @BindView(R.id.tv_receive_tel)
    TextView telTv;
    @BindView(R.id.tv_receive_postal)
    TextView postalTv;
    @BindView(R.id.tv_receive_addr)
    TextView addrTv;
    @BindView(R.id.mfrv_recyclerview)
    MultiFunctionRecylerView recylerView;
    @BindView(R.id.tv_goods_code)
    TextView goodsCodeTv;
    @BindView(R.id.tv_goods_size)
    TextView goodsSizeTv;
    @BindView(R.id.tv_goods_saveType)
    TextView goodsSaveTypeTv;
    @BindView(R.id.tv_goods_number)
    TextView goodsNumTv;
    @BindView(R.id.tv_goods_weight)
    TextView goodsWeightTv;
    @BindView(R.id.tv_goods_weight_withMoney)
    TextView getGoodsWeightWithMoneyTv;
    @BindView(R.id.btn_refuse)
    Button refuseBtn;
    @BindView(R.id.btn_accept)
    Button acceptBtn;

    /**
     * 跳转activity
     * @param context
     * @param id ChangeWaybill的id
     */
    public static void startActivity(Context context, String id, String taskId){
        Intent intent = new Intent(context, DeliveryVerifyActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("taskId", taskId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_delivery_verify;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        //获取id参数，从前一个页面
        id = getIntent().getStringExtra("id");
        taskId = getIntent().getStringExtra("taskId");
        setToolbarShow(View.VISIBLE);
        //设置toolbar
        customToolbar = getToolbar();
        customToolbar.setMainTitle(Color.WHITE, "换单审核");


        //获取换单审核数据
        DeclareWaybillEntity declareWaybillEntity = new DeclareWaybillEntity();
        declareWaybillEntity.setId(id);
        mPresenter = new DeliveryVerifyPresenter(this);
        ((DeliveryVerifyPresenter)mPresenter).getDeliveryVerify(declareWaybillEntity);

    }

    private void getChangeWaybill(){
//        ((DeliveryVerifyPresenter)mPresenter)
    }

    /**
     * 获取换单审核 新数据
     * @param declareWaybillBean
     */
    @SuppressLint("StringFormatMatches")
    @Override
    public void deliveryVerifyResult(DeclareWaybillBean declareWaybillBean) {

        //航班信息
        newWayBillCodeTv.setText(String.format(getResources().getString(R.string.format_delivery_verify_no_new), declareWaybillBean.getNewDeclareWaybillCode()));
        oldWayBillCodeTv.setText(String.format(getResources().getString(R.string.format_delivery_verify_no_origin), declareWaybillBean.getWaybillCode()));
        flightNoTv.setText("航班号:"+declareWaybillBean.getFlightNumber());
        if(declareWaybillBean.getFlightDate() == null){
            flightDataTv.setText("航班日期: -");
        }else{
            flightDataTv.setText("航班日期:\n"+ TimeUtils.getTime2_1(declareWaybillBean.getFlightDate()));
        }
        targetTv.setText("目的地:\n"+declareWaybillBean.getDestinationStation());
        startStationTv.setText("始发站:" + declareWaybillBean.getOriginatingStation());
        midStationTv.setText("中转站:" + declareWaybillBean.getTransferStation());
        finalStationTv.setText("终点站:\n" + declareWaybillBean.getDestinationStationCn());
        //收货人信息
        nameTv.setText(declareWaybillBean.getConsignee());
        telTv.setText(declareWaybillBean.getConsigneePhone());
        postalTv.setText(declareWaybillBean.getConsigneePostcode());
        addrTv.setText(declareWaybillBean.getConsigneeAddress());
        //货物信息
        goodsCodeTv.setText("特货代码:" + declareWaybillBean.getSpecialCargoCode());
        String bigSize = "";
        if(declareWaybillBean.getBigFlag() == 0)
            bigSize = "小件";
        else if(declareWaybillBean.getBigFlag() == 1)
            bigSize = "大件";
        else
            bigSize = "超大件";
        goodsSizeTv.setText("包装大小:" + bigSize);
        goodsSaveTypeTv.setText("储存类型:" + getColdStorage(declareWaybillBean));
        goodsNumTv.setText("总件数:" + declareWaybillBean.getTotalNumberPackages());
        goodsWeightTv.setText("总重量:" + declareWaybillBean.getTotalWeight());
        getGoodsWeightWithMoneyTv.setText("计费总量:" + declareWaybillBean.getBillingWeight());
        //recylerview
        mAdapter = new DeliveryVerifyAdapter(declareWaybillBean.getDeclareItem());
        recylerView.setLayoutManager(new LinearLayoutManager(this));
        recylerView.setAdapter(mAdapter);
        //点击事件
        refuseBtn.setOnClickListener(listener->{
            ChangeWaybillEntity changeWaybillEntity = new ChangeWaybillEntity();
            changeWaybillEntity.setFlag(0);//拒绝
            changeWaybillEntity.setDeclareWaybill(declareWaybillBean);
            changeWaybillEntity.setTaskId(taskId);
            changeWaybillEntity.setUserid(UserInfoSingle.getInstance().getUserId());
            //拒绝申请
            ((DeliveryVerifyPresenter)mPresenter).changeSubmit(changeWaybillEntity);
        });
        acceptBtn.setOnClickListener(listener->{
            ChangeWaybillEntity changeWaybillEntity = new ChangeWaybillEntity();
            changeWaybillEntity.setFlag(1);//接受
            changeWaybillEntity.setDeclareWaybill(declareWaybillBean);
            changeWaybillEntity.setTaskId(taskId);
            changeWaybillEntity.setUserid(UserInfoSingle.getInstance().getUserId());
            //接受申请
            ((DeliveryVerifyPresenter)mPresenter).changeSubmit(changeWaybillEntity);
        });
    }

    /**
     *  获取 提交 接口的结果
     * @param object
     */
    @Override
    public void changeSubmitResult(Object object) {
        EventBus.getDefault().post("collector_refresh");
        finish();
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void showNetDialog() {

        showProgessDialog("");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    private String getColdStorage(DeclareWaybillBean declareWaybillBean){
        String coldStr = "";
        switch (declareWaybillBean.getColdStorage()+""){
            case "0":
                coldStr = "普通  ";
                break;
            case "1":
                coldStr = "贵重  ";
                break;
            case "2":
                coldStr = "危险  ";
                break;
            case "3":
                coldStr = "活体  ";
                break;
            case "4":
                coldStr = "冷藏 | "+ declareWaybillBean.getRefrigeratedTemperature() + "℃";
                break;
                default: coldStr = "未知";
        }
        return coldStr;
    }
}
