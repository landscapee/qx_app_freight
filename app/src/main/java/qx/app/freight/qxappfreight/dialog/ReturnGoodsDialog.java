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
import qx.app.freight.qxappfreight.adapter.OverweightRecordAdapter;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;

public class ReturnGoodsDialog extends Dialog {
    private Context mContext;
    private OnClickListener listener;

    private RecyclerView dataRc;
    private ImageView ivClose;
    private RelativeLayout rlAdd;
    private Button btnSure;
    private EditText etNum,etWeight,etVolume,etOverweight;
    private OverweightRecordAdapter overweightRecordAdapter;

//    private int collectorNum;
//    private int collectorWeight;


    List<RcInfoOverweight> rcInfoOverweight;

    public ReturnGoodsDialog(Context context) {
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
        overweightRecordAdapter = new OverweightRecordAdapter(rcInfoOverweight);
        dataRc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        dataRc.setAdapter(overweightRecordAdapter);
        ivClose.setOnClickListener((v) -> {
            dismiss();
        });

        rlAdd.setOnClickListener((v) -> {
            RcInfoOverweight mRcInfoOverweight = new RcInfoOverweight();
            if (!StringUtil.isEmpty(etNum.getText().toString())&&!StringUtil.isEmpty(etWeight.getText().toString())){
//                if (Integer.valueOf(etNum.getText().toString())>collectorNum){
//                    ToastUtil.showToast("????????????????????????????????????");
//                    return;
//                }
//                if (Integer.valueOf(etWeight.getText().toString())>collectorWeight){
//                    ToastUtil.showToast("????????????????????????????????????");
//                    return;
//                }
                mRcInfoOverweight.setCount(Integer.valueOf(etNum.getText().toString()));
                mRcInfoOverweight.setWeight(Integer.valueOf(etWeight.getText().toString()));
//                mRcInfoOverweight.setVolume(Integer.valueOf(etVolume.getText().toString()));
//                mRcInfoOverweight.setOverWeight(Integer.valueOf(etOverweight.getText().toString()));
                rcInfoOverweight.add(mRcInfoOverweight);
                overweightRecordAdapter.notifyDataSetChanged();
                etNum.setText("");
                etWeight.setText("");
//                etVolume.setText("");
//                etOverweight.setText("");
                etNum.setFocusable(true);

            }
            else {
                ToastUtil.showToast("??????????????????????????????");
            }

        });

        btnSure.setOnClickListener((v) -> {
            int overweight = 0;
            for (RcInfoOverweight mRcInfoOverweight:rcInfoOverweight){
                overweight += mRcInfoOverweight.getWeight();

            }
            if (listener != null) {
                listener.onClick(overweight+"kg");
            }
            dismiss();
//            mEdtOverWeight.setText(overweight+"kg");

        });
        overweightRecordAdapter.setOnDeleteClickListener((view1, position) -> {
            rcInfoOverweight.remove(position);
            overweightRecordAdapter.notifyDataSetChanged();
        });
    }

    /**
     *
     * ??????????????????
     */
    public ReturnGoodsDialog setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
        return this;
    }

    /**
     * ??????????????????dialog????????????dialog??????
     *
     * @param cancel
     */
    public ReturnGoodsDialog isCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * ??????????????????????????????dialog??????
     *
     * @param cancel
     */
    public ReturnGoodsDialog isCanceled(boolean cancel) {
        setCancelable(cancel);
        return this;
    }

    public ReturnGoodsDialog setData(List<RcInfoOverweight> list){
        rcInfoOverweight = list;
//        this.collectorNum = collectorNum;
//        this.collectorWeight = collectorWeight;
        return this;
    }

    public interface OnClickListener {
        void onClick(String text);
    }
}
