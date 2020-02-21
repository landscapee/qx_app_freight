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
import qx.app.freight.qxappfreight.adapter.PickGoodsRecordAdapterForNet;
import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.ForkliftWorkingCostBean;
import qx.app.freight.qxappfreight.bean.response.PickGoodsRecordsBean;
import qx.app.freight.qxappfreight.contract.ForkliftCostContract;
import qx.app.freight.qxappfreight.contract.PickGoodsRecordsContract;
import qx.app.freight.qxappfreight.presenter.ForkLiftPresenter;
import qx.app.freight.qxappfreight.presenter.PickGoodsRecordsPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * created by zyy
 * 2019/5/23 13:07
 */
public class PickGoodsRecordsDialogForNet extends Dialog implements PickGoodsRecordsContract.pickGoodsView {
    private Context mContext;
    private OnClickListener listener;

    private RecyclerView dataRc;
    private ImageView ivClose;
    private Button btnSure;
    private PickGoodsRecordAdapterForNet pickGoodsRecordAdapterForNet;

    List <PickGoodsRecordsBean> pickGoodsRecordsBeanList = new ArrayList <>();

    public BasePresenter mPresenter;

    private String waybillId;

    public MaterialDialog mProgessbarDialog;
    public TextView mTextView = null;

    public PickGoodsRecordsDialogForNet(Context context, String waybillId) {
        super(context, R.style.CommomDialog);
        this.mContext = context;
        this.waybillId = waybillId;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pickgoods_records);
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
        btnSure = findViewById(R.id.btn_sure);

        pickGoodsRecordAdapterForNet = new PickGoodsRecordAdapterForNet(pickGoodsRecordsBeanList);
        dataRc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        dataRc.setAdapter(pickGoodsRecordAdapterForNet);
        ivClose.setOnClickListener((v) -> {
            dismiss();
        });

        btnSure.setOnClickListener((v) -> {
            dismiss();

        });
        pickGoodsRecordAdapterForNet.setOnDeleteClickListener(new PickGoodsRecordAdapterForNet.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                revocation(pickGoodsRecordsBeanList.get(position));
            }
        });
    }

    private void revocation(PickGoodsRecordsBean pickGoodsRecordsBean) {
        mPresenter = new PickGoodsRecordsPresenter(this);
        ((PickGoodsRecordsPresenter) mPresenter).revokeInboundDelevery(pickGoodsRecordsBean);
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
    public PickGoodsRecordsDialogForNet setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
        return this;
    }

    /**
     * 是否设置点击dialog区域外，dialog消失
     *
     * @param cancel
     */
    public PickGoodsRecordsDialogForNet isCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 是否设置点击返回键，dialog消失
     *
     * @param cancel
     */
    public PickGoodsRecordsDialogForNet isCanceled(boolean cancel) {
        setCancelable(cancel);
        return this;
    }

    public void loadData() {
        mPresenter = new PickGoodsRecordsPresenter(this);
        ((PickGoodsRecordsPresenter) mPresenter).getOutboundList(waybillId);
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
    public void getOutboundListResult(List <PickGoodsRecordsBean> result) {
        if (result!=null){
            pickGoodsRecordsBeanList.clear();
            pickGoodsRecordsBeanList.addAll(result);
        }
        pickGoodsRecordAdapterForNet.notifyDataSetChanged();

    }

    @Override
    public void revokeInboundDeleveryResult(String result) {
        ToastUtil.showToast("撤销成功");
        loadData();
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
