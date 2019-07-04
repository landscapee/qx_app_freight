package qx.app.freight.qxappfreight.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.ReturnWeighingEntity;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.contract.GetScooterByScooterCodeContract;
import qx.app.freight.qxappfreight.presenter.GetScooterByScooterCodePresenter;
import qx.app.freight.qxappfreight.utils.CalculateUtil;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 复重扫一扫页面
 * Created by swd
 */
public class AllocaaateScanActivity extends BaseActivity implements GetScooterByScooterCodeContract.GetScooterByScooterCodeView {
    @BindView(R.id.ll_scan)
    LinearLayout llScan;
    @BindView(R.id.ll_other)
    LinearLayout llOther;
    @BindView(R.id.tv_scan)
    TextView tvScan;
    @BindView(R.id.tv_name_front)
    TextView tvNameFront;
    @BindView(R.id.tv_deadweight_front)
    TextView tvDeadweightFront;
    @BindView(R.id.tv_flightid)
    TextView tvFlightid;
    @BindView(R.id.tv_netweight_front)
    TextView tvNetweightFront;
    @BindView(R.id.tv_netweight_fz)
    TextView tvetNweightFz;
    @BindView(R.id.tv_dvalue_front)
    TextView tvDvalueFront;
    @BindView(R.id.tv_grossweight_front)
    EditText tvGrossweightFront;
    @BindView(R.id.tv_gradient_front)
    TextView tvGradientFront;
    @BindView(R.id.tv_revise_front)
    EditText tvReviseFront;
    @BindView(R.id.et_other)
    EditText etOther;
    @BindView(R.id.tv_uld)
    TextView tvUld;
    @BindView(R.id.tv_uld_self)
    TextView tvUldSelf;
    @BindView(R.id.btn_read)
    Button btnRead;
    @BindView(R.id.btn_return)
    Button btnReturn;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private List<String> mRemarksList; //库区
    private String chenNum; //秤号
    private String mScooterCode;//板车号
    private double dValue; //差值
    private double dRate; //差率
    private double reviseWeight; //修订重量
    private double crossWeight; //毛重
    private double goodsWeight; //复重净重
    private int selectorOption = 10;
    private GetInfosByFlightIdBean mData;
    private CustomToolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_allocate_scan;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
//        chenNum = getIntent().getStringExtra("chenNum");
//        mScooterCode = getIntent().getStringExtra("scooterCode");
        toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
//        toolbar.setMainTitle(Color.WHITE, chenNum);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        mPresenter = new GetScooterByScooterCodePresenter(this);

        initData();

        initView();
    }

    private void initData() {
        mData = (GetInfosByFlightIdBean) getIntent().getSerializableExtra("dataBean");

        if (mData == null){
            ToastUtil.showToast("无该板车信息");
            finish();
            return;
        }
        switch (mData.getReWeightFinish()){
            case 0:
                tvFlightid.setText(mData.getFlightNo());
                tvNameFront.setText(mData.getScooterCode());
                tvDeadweightFront.setText(mData.getScooterWeight()+"kg");
                String mType,mCode,mIata;

                if (TextUtils.isEmpty(mData.getUldType())){
                    mType = "-";
                }else {
                    mType = mData.getUldType();
                }
                if (TextUtils.isEmpty(mData.getUldCode())){
                    mCode = "-";
                }else {
                    mCode = mData.getUldCode();
                }
                if (TextUtils.isEmpty(mData.getIata())){
                    mIata = "-";
                }else {
                    mIata = mData.getIata();
                }

                tvUld.setText(mType+" "+mCode+" "+mIata);
                tvUldSelf.setText(mData.getUldWeight()+"kg");
                //收运净重
                tvNetweightFront.setText(mData.getWeight()+"kg");
                break;
            case 1:
                ToastUtil.showToast("该板车已复重");
                finish();
                break;
            case 2:
                ToastUtil.showToast("该板车复重异常");
                finish();
                break;
        }
    }

    private void initView() {
//        ScanManagerActivity.startActivity(this);
        changeClicked(false);
//        getScooterInfo(mScooterCode);
        //品名
        mRemarksList = new ArrayList<>();
        mRemarksList.add("加雨棚");
        mRemarksList.add("加垫板");
        mRemarksList.add("其他");

        //负重重量
        tvGrossweightFront.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = s.toString();
                if (TextUtils.isEmpty(ss)){
                    changeClicked(false);
                }else {
                    changeClicked(true);
                }
                if (StringUtil.isDouble(ss)){
                    calculateWeight();
                }
            }
        });
        //人工干预值
        tvReviseFront.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = s.toString();
                if (TextUtils.isEmpty(ss)){
                    ss ="0";
                }
                if (StringUtil.isDouble(ss)){
                    calculateWeight();
                }
            }
        });

    }

    @OnClick({R.id.btn_read, R.id.btn_return, R.id.btn_confirm, R.id.ll_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_read:
                if (mData!=null){
                    ((GetScooterByScooterCodePresenter) mPresenter).getWeight("pb1");
                }
                break;
            case R.id.btn_return:
                returnScooter();
                break;
            case R.id.btn_confirm:
                saveScooter();
                break;
            case R.id.ll_scan:
                showPickView();
                break;
        }
    }

    /**
     * 获取板车列表信息
     */
    private void getScooterInfo(String scooterCode) {
        BaseFilterEntity<GetInfosByFlightIdBean> entity = new BaseFilterEntity();
//        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        entity.setUserId("weighter");
        ((GetScooterByScooterCodePresenter) mPresenter).getInfosByFlightId(entity);
    }

    /**
     * 退回板车
     */
    private void returnScooter() {
        if (-3<dRate&&dRate<3){
            ToastUtil.showToast("复重差率合格,不能退回");
            return;
        }
        if (selectorOption ==2){
            mData.setRemark(etOther.getText().toString());
        }else {
            mData.setRemark(tvScan.getText().toString());
        }
        mData.setReWeight(crossWeight);
        mData.setReDifference(dValue);
        mData.setReDifferenceRate(dRate);
//        mData.setWeight(goodsWeight);
        mData.setLogUserId(UserInfoSingle.getInstance().getUserId());
        ReturnWeighingEntity returnWeighingEntity = new ReturnWeighingEntity();

        returnWeighingEntity.setScooter(mData);

        ((GetScooterByScooterCodePresenter) mPresenter).returnWeighing(returnWeighingEntity);

    }

    /**
     * 保存班车信息
     */
    private void saveScooter() {
        String textInfo;

        if (-3>dRate||dRate>3){
            textInfo = "本板复重误差超过±3%，不予放行";
        }else {
            textInfo = "本次复重正常，是否提交？";
        }

        if (reviseWeight!=0){
            if (selectorOption ==10){
                ToastUtil.showToast("人工干预的情况下备注不能为空");
                return;
            }else if (selectorOption ==2){
                if (TextUtils.isEmpty(etOther.getText().toString())){
                    ToastUtil.showToast("人工干预的情况下备注不能为空");
                    return;
                }else {
                    mData.setRemark(etOther.getText().toString());
                    mData.setPersonUpdateValue(reviseWeight);
                }
            }else {
                mData.setRemark(tvScan.getText().toString());
                mData.setPersonUpdateValue(reviseWeight);
            }
        }
        //设置复重毛重，复重差值，复重差率，复重净重
        mData.setReWeight(crossWeight);
        mData.setReDifference(dValue);
        mData.setReDifferenceRate(dRate);
        mData.setRePureWeight(goodsWeight);
//        mData.setWeight(goodsWeight);
        mData.setLogUserId(UserInfoSingle.getInstance().getUserId());
        mData.setReWeighedUserId(UserInfoSingle.getInstance().getUserId());
        mData.setReWeighedTime(System.currentTimeMillis());
        //提交弹窗
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle("提示")
                .setMessage(textInfo)
                .setPositiveButton("取消")
                .setNegativeButton("确定")
                .isCanceledOnTouchOutside(true)
                .isCanceled(true)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {

                        } else {
                            ((GetScooterByScooterCodePresenter) mPresenter).saveScooter(mData);
                        }
                    }
                }).show();
    }

    /**
     * 备注选择
     */
    private void showPickView(){
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvScan.setText(mRemarksList.get(options1));
                selectorOption = options1;
                switch (options1){
                    case 0:
                    case 1:
                        llOther.setVisibility(View.GONE);
                        break;
                    case 2:
                        llOther.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }).build();
        pickerView.setPicker(mRemarksList);
        pickerView.setTitleText("备注");
        pickerView.show();
    }

    /**控制确定按钮是否能够点击
     *
     * @param canClick 能否点击
     */
    private void changeClicked(boolean canClick){
        btnReturn.setEnabled(canClick);
        btnConfirm.setEnabled(canClick);
        if (canClick){
            btnReturn.setTextColor(Color.parseColor("#333333"));
            btnConfirm.setBackgroundColor(Color.parseColor("#2e81fd"));
        }else {
            btnReturn.setTextColor(Color.parseColor("#bcbcbc"));
            btnConfirm.setBackgroundColor(Color.parseColor("#bcbcbc"));
        }
    }

    /**计算差值差率
     *
     */
    private void calculateWeight(){
        //获取复磅毛重
        String s1 = tvGrossweightFront.getText().toString().trim();
        if (TextUtils.isEmpty(s1)){
            crossWeight =0;
        }else {
            crossWeight = Double.valueOf(s1);
        }
        //获取人工干预值
        String s2 = tvReviseFront.getText().toString().trim();
        if (TextUtils.isEmpty(s2)){
            reviseWeight =0;
        }else {
            reviseWeight = Double.valueOf(s2);
        }

        //复重净重=本次复磅毛重-板车自重-ULD自重-人工干预
        goodsWeight = crossWeight -(mData.getScooterWeight()+mData.getUldWeight()+reviseWeight);
        //复重差值 = 复重净重 - 组板净重
        dValue =goodsWeight -mData.getWeight();
        //复重差率=（（复重净重-组板净重）/组板净重）*100%
        dRate = (double) Math.round( CalculateUtil.calculateGradient(2, dValue, crossWeight) * 100) / 100;

        //复磅差值
        tvDvalueFront.setText(dValue+"kg");
        //复磅差率
        tvGradientFront.setText(dRate+"%");
        if (-3<dRate&&dRate<3){
            tvGradientFront.setTextColor(Color.parseColor("#333333"));
        }else{
            tvGradientFront.setTextColor(Color.parseColor("#FF0000"));
        }
        //复重净重
        tvetNweightFz.setText(goodsWeight+"kg");

//        //收运净重
//        tvNetweightFront.setText(goodsWeight+"kg");
    }

    @Override
    public void getInfosByFlightIdResult(List<GetInfosByFlightIdBean> getInfosByFlightIdBeans) {

        for (GetInfosByFlightIdBean getInfosByFlightIdBean : getInfosByFlightIdBeans) {

            if (Objects.equals(mScooterCode, getInfosByFlightIdBean.getScooterCode())){
                mData = getInfosByFlightIdBean;
                break;
            }
        }
        if (mData == null){
            ToastUtil.showToast("无该板车信息");
            finish();
            return;
        }
        switch (mData.getReWeightFinish()){
            case 0:
                tvFlightid.setText(mData.getFlightNo());
                tvNameFront.setText(mData.getScooterCode());
                tvDeadweightFront.setText(mData.getScooterWeight()+"kg");
                String mType,mCode,mIata;

                if (TextUtils.isEmpty(mData.getUldType())){
                    mType = "-";
                }else {
                    mType = mData.getUldType();
                }
                if (TextUtils.isEmpty(mData.getUldCode())){
                    mCode = "-";
                }else {
                    mCode = mData.getUldCode();
                }
                if (TextUtils.isEmpty(mData.getIata())){
                    mIata = "-";
                }else {
                    mIata = mData.getIata();
                }

                tvUld.setText(mType+" "+mCode+" "+mIata);
                tvUldSelf.setText(mData.getUldWeight()+"kg");
                //收运净重
                tvNetweightFront.setText(mData.getWeight()+"kg");
                break;
            case 1:
                ToastUtil.showToast("该板车已复重");
                finish();
                break;
            case 2:
                ToastUtil.showToast("该板车复重异常");
                finish();
                break;
        }

    }

    /**
     * 未使用
     * @param bean
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void getScooterByScooterCodeResult(GetInfosByFlightIdBean bean) {
//        if (bean!=null){
//            mData = bean;
//            tvFlightid.setText(bean.getFlightNo());
//            tvNameFront.setText(bean.getScooterCode());
//            tvDeadweightFront.setText(bean.getScooterWeight()+"kg");
//            tvUld.setText(bean.getUldType()+" "+bean.getUldCode()+" "+bean.getIata());
//            tvUldSelf.setText(bean.getUldWeight()+"kg");
//            //收运净重
//            tvNetweightFront.setText(bean.getWeight()+"kg");
////            tvNetweightFront.setText(bean.getWeight()+"kg");
//
//        }
    }

    @Override
    public void saveScooterResult(String result) {
        ToastUtil.showToast("保存成功");
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public void returnWeighingResult(String result) {
        ToastUtil.showToast("退回理货成功");
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public void getWeightResult(String result) {
        tvGrossweightFront.setText(result);
        changeClicked(true);
        calculateWeight();
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
        Log.e("2222", "toastView: "+error );
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("数据提交中……");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

}
