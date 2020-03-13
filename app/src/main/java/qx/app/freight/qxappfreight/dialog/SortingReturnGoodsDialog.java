package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.OverweightRecordAdapter2;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;
import qx.app.freight.qxappfreight.bean.response.OverweightBean;
import qx.app.freight.qxappfreight.contract.OverweightContract;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * created by swd
 * 2019/5/23 13:07
 */
public class SortingReturnGoodsDialog extends Dialog implements OverweightContract.OverweightView {
    private Context mContext;
    private OnClickListener listener;

    private RecyclerView dataRc;
    private ImageView ivClose;
    private RelativeLayout rlAdd;
    private Button btnSure;
    private EditText etNum,etWeight,etVolume,etOverweight;
    private OverweightRecordAdapter2 overweightRecordAdapter;

    List<RcInfoOverweight> rcInfoOverweight;


    public SortingReturnGoodsDialog(Context context) {
        super(context, R.style.CommomDialog);
        this.mContext = context;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_overweight);
        initView();
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
        dataRc =findViewById(R.id.rv_overweight);
        ivClose = findViewById(R.id.iv_close);
        rlAdd = findViewById(R.id.rl_add);
        btnSure = findViewById(R.id.btn_sure);

        etNum = findViewById(R.id.et_num);
        etWeight = findViewById(R.id.et_weight);
        etVolume =findViewById(R.id.et_volume);
        etOverweight = findViewById(R.id.et_overweight);
        overweightRecordAdapter = new OverweightRecordAdapter2(rcInfoOverweight);
        dataRc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        dataRc.setAdapter(overweightRecordAdapter);
        ivClose.setOnClickListener((v) -> {
            dismiss();
        });

        rlAdd.setOnClickListener((v) -> {
            RcInfoOverweight mRcInfoOverweight = new RcInfoOverweight();
            if (!StringUtil.isEmpty(etNum.getText().toString())&&!StringUtil.isEmpty(etWeight.getText().toString())&&!StringUtil.isEmpty(etVolume.getText().toString())&&!StringUtil.isEmpty(etOverweight.getText().toString())){

                mRcInfoOverweight.setCount(Integer.valueOf(etNum.getText().toString()));
                mRcInfoOverweight.setWeight(Integer.valueOf(etWeight.getText().toString()));
                mRcInfoOverweight.setVolume(Integer.valueOf(etVolume.getText().toString()));
                mRcInfoOverweight.setOverWeight(Integer.valueOf(etOverweight.getText().toString()));
                rcInfoOverweight.add(mRcInfoOverweight);
                overweightRecordAdapter.notifyDataSetChanged();
                etNum.setText("");
                etWeight.setText("");
                etVolume.setText("");
                etOverweight.setText("");
                etNum.setFocusable(true);

            }
            else {
                ToastUtil.showToast("请填写完整的超重记录");
            }

        });

        btnSure.setOnClickListener((v) -> {
            int overweight = 0;
            for (RcInfoOverweight mRcInfoOverweight:rcInfoOverweight){
                overweight += mRcInfoOverweight.getOverWeight();
            }
            if (listener != null) {
                listener.onClick(overweight+"kg");
            }
            dismiss();
//            mEdtOverWeight.setText(overweight+"kg");

        });
        overweightRecordAdapter.setOnDeleteClickListener((view1, position) -> {
//            if (TextUtils.isEmpty(rcInfoOverweight.get(position).getId())){
//                rcInfoOverweight.remove(position);
//            }else {
//                rcInfoOverweight.get(position).setDelFlag(1);
//            }
//            rcInfoOverweight.get(position).setDelFlag(1);
            rcInfoOverweight.remove(position);
            overweightRecordAdapter.notifyDataSetChanged();
        });
    }

    /**
     *
     * 设置点击事件
     */
    public SortingReturnGoodsDialog setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
        return this;
    }

    /**
     * 是否设置点击dialog区域外，dialog消失
     *
     * @param cancel
     */
    public SortingReturnGoodsDialog isCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 是否设置点击返回键，dialog消失
     *
     * @param cancel
     */
    public SortingReturnGoodsDialog isCanceled(boolean cancel) {
        setCancelable(cancel);
        return this;
    }

    public SortingReturnGoodsDialog setData(List<RcInfoOverweight> list){
        rcInfoOverweight = list;
        return this;
    }

    public void loadData(){



    }

    @Override
    public void getOverWeightResult(List <OverweightBean> result) {

    }

    @Override
    public void addOverWeightResult(String result) {

    }

    @Override
    public void deleteOverWeightResult(String result) {

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

    public interface OnClickListener {
        void onClick(String text);
    }
}
