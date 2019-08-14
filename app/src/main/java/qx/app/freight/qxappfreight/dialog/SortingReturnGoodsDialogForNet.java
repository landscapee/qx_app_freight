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
import qx.app.freight.qxappfreight.adapter.OverweightRecordAdapter2;
import qx.app.freight.qxappfreight.adapter.OverweightRecordAdapterForNet;
import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.OverweightBean;
import qx.app.freight.qxappfreight.contract.OverweightContract;
import qx.app.freight.qxappfreight.presenter.OverweightPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * created by swd
 * 2019/5/23 13:07
 */
public class SortingReturnGoodsDialogForNet extends Dialog implements OverweightContract.OverweightView {
    private Context mContext;
    private OnClickListener listener;

    private RecyclerView dataRc;
    private ImageView ivClose;
    private RelativeLayout rlAdd;
    private Button btnSure;
    private EditText etNum, etWeight, etVolume, etOverweight;
    private OverweightRecordAdapterForNet overweightRecordAdapter;

    List <OverweightBean> rcInfoOverweight = new ArrayList <>();

    public BasePresenter mPresenter;

    private String waybillId;

    public MaterialDialog mProgessbarDialog;
    public TextView mTextView = null;

    public SortingReturnGoodsDialogForNet(Context context, String waybillId) {
        super(context, R.style.CommomDialog);
        this.mContext = context;
        this.waybillId = waybillId;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_overweight);
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
        overweightRecordAdapter = new OverweightRecordAdapterForNet(rcInfoOverweight);
        dataRc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        dataRc.setAdapter(overweightRecordAdapter);
        ivClose.setOnClickListener((v) -> {
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
//                ToastUtil.showToast("请填写完整的超重记录");
//            }
            List<OverweightBean> overweightBeans = new ArrayList <>();
            OverweightBean overweightBean = new OverweightBean();
            overweightBean.setCount(Integer.valueOf(etNum.getText().toString()));
            overweightBean.setWeight(Integer.valueOf(etWeight.getText().toString()));
            overweightBean.setVolume(Integer.valueOf(etVolume.getText().toString()));
            overweightBean.setOverWeight(Integer.valueOf(etOverweight.getText().toString()));
            overweightBean.setInWaybillRecordId(waybillId);
            overweightBeans.add(overweightBean);
            addOverweight(overweightBeans);

        });
        btnSure.setVisibility(View.GONE);
//        btnSure.setOnClickListener((v) -> {
//            int overweight = 0;
//            for (RcInfoOverweight mRcInfoOverweight:rcInfoOverweight){
//                overweight += mRcInfoOverweight.getOverWeight();
//            }
//            if (listener != null) {
//                listener.onClick(overweight+"kg");
//            }
//            dismiss();
////            mEdtOverWeight.setText(overweight+"kg");
//
//        });
        overweightRecordAdapter.setOnDeleteClickListener((view1, position) -> {
            OverweightBean overweightBean = new OverweightBean();
            overweightBean.setOverweightInfoId(rcInfoOverweight.get(position).getId());
            deleteOverweight(overweightBean);
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
     * 设置点击事件
     */
    public SortingReturnGoodsDialogForNet setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
        return this;
    }

    /**
     * 是否设置点击dialog区域外，dialog消失
     *
     * @param cancel
     */
    public SortingReturnGoodsDialogForNet isCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 是否设置点击返回键，dialog消失
     *
     * @param cancel
     */
    public SortingReturnGoodsDialogForNet isCanceled(boolean cancel) {
        setCancelable(cancel);
        return this;
    }

    /**
     * 获取运单所有超重记录
     */
    public void loadData() {
        mPresenter = new OverweightPresenter(this);
        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
        baseFilterEntity.setWaybillId(waybillId);
        ((OverweightPresenter) mPresenter).getOverweight(baseFilterEntity);
    }
    /**
     * 添加超重记录
     */
    public void addOverweight(List<OverweightBean> overweightBean) {
        mPresenter = new OverweightPresenter(this);
        ((OverweightPresenter) mPresenter).addOverweight(overweightBean);
    }
    /**
     *  删除超重记录
     */
    public void deleteOverweight(OverweightBean overweightBean) {
        mPresenter = new OverweightPresenter(this);
        ((OverweightPresenter) mPresenter).deleteOverweight(overweightBean);
    }


    @Override
    public void getOverWeightResult(List <OverweightBean> result) {
        rcInfoOverweight.clear();
        rcInfoOverweight.addAll(result);
        overweightRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void addOverWeightResult(String result) {
        loadData();
    }

    @Override
    public void deleteOverWeightResult(String result) {
        loadData();
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
            mTextView.setText("加载中……");
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
