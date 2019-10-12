package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.request.PrintBean;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;


public class SingerDialog extends Dialog {
    Spinner mSpChose;
    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.tv_intro)
    TextView tvIntro;

    private Context mContext;
    protected OnClickListener listener;

    private String printIndex;
    private String inTro;

    public SingerDialog(Context context,String inTro) {
        super(context);
        mContext = context;
        this.inTro = inTro;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_singer, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        initView();
    }
    /**
     * 是否设置点击dialog区域外，dialog消失
     *
     * @param cancel
     */
    public SingerDialog isCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 是否设置点击返回键，dialog消失
     *
     * @param cancel
     */
    public SingerDialog isCanceled(boolean cancel) {
        setCancelable(cancel);
        return this;
    }

    private void initView() {
        mSpChose = findViewById(R.id.sp_chose);
        tvIntro.setText(inTro);
        btnSure.setOnClickListener(v -> {
            if (printIndex!=null){
                listener.onClick(printIndex);
            }
            else {
                ToastUtil.showToast("请选择打印机");
            }
        });
        mIvClose.setOnClickListener(v -> dismiss());
    }

    public void setData(List<PrintBean> oAuserInfos){
        show();
        List<String> array = new ArrayList();
        for (PrintBean printBean:oAuserInfos){
            array.add(printBean.getPrintName());
        }
        ArrayAdapter <String> spinnerAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_spinner_dropdown_item, array);
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_general);
        mSpChose.setAdapter(spinnerAdapter);
        mSpChose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                printIndex = oAuserInfos.get(position).getPrintCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public interface OnClickListener {
        void onClick(String userInfo);
    }
    public void setOnClickListener(OnClickListener mOnClickListener){
        this.listener = mOnClickListener;
    }

}
