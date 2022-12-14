package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ForkliftRecordAdapterForNet;
import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.ForkliftWorkingCostBean;
import qx.app.freight.qxappfreight.contract.ForkliftCostContract;
import qx.app.freight.qxappfreight.presenter.ForkLiftPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * created by zyy
 * 2019/5/23 13:07
 */
public class ForkliftCostDialogForNet extends Dialog implements ForkliftCostContract.forkliftView {
    private Context mContext;
    private OnClickListener listener;

    private RecyclerView dataRc;
    private ImageView ivClose;
    private RelativeLayout rlAdd;
    private Button btnSure;
    private EditText etNum, etWeight, etVolume, etOverweight;
    private ForkliftRecordAdapterForNet overweightRecordAdapter;

    List <ForkliftWorkingCostBean> rcInfoOverweight = new ArrayList <>();

    public BasePresenter mPresenter;

    private String waybillId;
    private String waybillCode;

    public MaterialDialog mProgessbarDialog;
    public TextView mTextView = null;

    public ForkliftCostDialogForNet(Context context, String waybillId,String waybillCode) {
        super(context, R.style.CommomDialog);
        this.mContext = context;
        this.waybillId = waybillId;
        this.waybillCode = waybillCode;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_forklift_cost);
        initView();
        initDialog();
        loadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Window window = this.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = mContext.getResources().getDisplayMetrics().widthPixels;
        window.setAttributes(attributes);
    }

    private void initView() {
        dataRc = findViewById(R.id.rv_overweight);
        ivClose = findViewById(R.id.iv_close);
        rlAdd = findViewById(R.id.rl_add);
        btnSure = findViewById(R.id.btn_sure);

        etNum = findViewById(R.id.et_num);
        etWeight = findViewById(R.id.et_weight);
        etVolume = findViewById(R.id.et_volume);
        etOverweight = findViewById(R.id.et_overweight);
        overweightRecordAdapter = new ForkliftRecordAdapterForNet(rcInfoOverweight);
        dataRc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        dataRc.setAdapter(overweightRecordAdapter);
        ivClose.setOnClickListener((v) -> {
            if (listener != null) {
                listener.onClick("");
            }
            dismiss();
        });

        rlAdd.setOnClickListener((v) -> {
//            RcInfoOverweight mRcInfoOverweight = new RcInfoOverweight();
//            if (!StringUtil.isEmpty(etNum.getText().toString())&&!StringUtil.isEmpty(etWeight.getText().toString())&&!StringUtil.isEmpty(etVolume.getText().toString())&&!StringUtil.isEmpty(etOverweight.getText().toString())){
//
//                mRcInfoOverweight.setCount(Integer.valueOf(etNum.getText().toString()));
//                mRcInfoOverweight.setWeight(Integer.valueOf(etWeight.getText().toString()));
//                mRcInfoOverweight.setVolume(Integer.valueOf(etVolume.getText().toString()));
//                mRcInfoOverweight.setOverWeight(Integer.valueOf(etOverweight.getText().toString()));
//                rcInfoOverweight.add(mRcInfoOverweight);
//                overweightRecordAdapter.notifyDataSetChanged();
//                etNum.setText("");
//                etWeight.setText("");
//                etVolume.setText("");
//                etOverweight.setText("");
//                etNum.setFocusable(true);
//
//            }
//            else {
//                ToastUtil.showToast("??????????????????????????????");
//            }
            if (!StringUtil.isEmpty(etNum.getText().toString())){

                List<ForkliftWorkingCostBean> overweightBeans = new ArrayList <>();
                ForkliftWorkingCostBean overweightBean = new ForkliftWorkingCostBean();
                overweightBean.setNumber(Integer.valueOf(etNum.getText().toString()));
                overweightBean.setCharge(Double.valueOf(Integer.valueOf(etNum.getText().toString())*50));
//                overweightBean.setVolume(Integer.valueOf(etVolume.getText().toString()));
//                overweightBean.setOverWeight(Integer.valueOf(etOverweight.getText().toString()));
                overweightBean.setCreateUser(UserInfoSingle.getInstance().getUserId());
                overweightBean.setCreateUserName(UserInfoSingle.getInstance().getUsername());
                overweightBean.setWaybillId(waybillId);
                overweightBean.setWaybillCode(waybillCode);
                overweightBeans.add(overweightBean);
                addOverweight(overweightBeans);
                etNum.setText("");
//                etWeight.setText("");
//                etVolume.setText("");
//                etOverweight.setText("");
                etNum.setFocusable(true);

            }
            else {
                ToastUtil.showToast("???????????????????????????");
            }


        });
        btnSure.setOnClickListener((v) -> {
//            int overweight = 0;
//            for (RcInfoOverweight mRcInfoOverweight:rcInfoOverweight){
//                overweight += mRcInfoOverweight.getOverWeight();
//            }
            if (listener != null) {
                listener.onClick("");
            }
            dismiss();
//            mEdtOverWeight.setText(overweight+"kg");

        });
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPresenter != null) {
            mPresenter.detach();
            mPresenter.interruptHttp();
        }
    }

    /**
     * ??????????????????
     */
    public ForkliftCostDialogForNet setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
        return this;
    }

    /**
     * ??????????????????dialog????????????dialog??????
     *
     * @param cancel
     */
    public ForkliftCostDialogForNet isCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * ??????????????????????????????dialog??????
     *
     * @param cancel
     */
    public ForkliftCostDialogForNet isCanceled(boolean cancel) {
        setCancelable(cancel);
        return this;
    }

    /**
     * ??????????????????????????????????????????
     */
    public void loadData() {
        mPresenter = new ForkLiftPresenter(this);
        ((ForkLiftPresenter) mPresenter).getForklifts(waybillId);
    }
    /**
     * ????????????
     */
    public void addOverweight(List<ForkliftWorkingCostBean> forkliftWorkingCostBeans) {
        mPresenter = new ForkLiftPresenter(this);
        ((ForkLiftPresenter) mPresenter).addForklift(forkliftWorkingCostBeans);
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

    @Override
    public void addForkliftResult(String result) {
        loadData();
    }

    @Override
    public void getForkliftsResult(List <ForkliftWorkingCostBean> result) {
        rcInfoOverweight.clear();
        rcInfoOverweight.addAll(result);
        overweightRecordAdapter.notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onClick(String text);
    }

    public void initDialog() {
        mProgessbarDialog = new MaterialDialog(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_progressbar, null);
        mTextView = mView.findViewById(R.id.progressbar_tv);
        mProgessbarDialog.setCanceledOnTouchOutside(true);
        mProgessbarDialog.setContentView(mView);
    }

    public void showProgessDialog(String message) {
        if (!TextUtils.isEmpty(message))
            mTextView.setText(message);
        else {
            mTextView.setText("???????????????");
        }
        mProgessbarDialog.show();
    }

    public void setProgressText(String message) {
        if (!TextUtils.isEmpty(message))
            mTextView.setText(message);
    }

    public void dismissProgessDialog() {
        mProgessbarDialog.dismiss();
    }
}
