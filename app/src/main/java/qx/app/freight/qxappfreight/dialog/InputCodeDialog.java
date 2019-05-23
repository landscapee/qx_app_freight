package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * created by swd
 * 2019/5/23 17:01
 */
public class InputCodeDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private String message;
    private String positiveBtn;
    private String negativeBtn;
    private String title;
    private OnClickListener listener;
    private EditText messageTv;
    private EditText messageTv2;
    private TextView titleTxt;
    private TextView positiveTv;
    private TextView negativeTv;
    private boolean isEditChange;

    public InputCodeDialog(Context context) {
        super(context, R.style.CommomDialog);
        this.mContext = context;
        setCanceledOnTouchOutside(false);
    }

    public InputCodeDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public InputCodeDialog setHint(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        if (TextUtils.isEmpty(messageTv.getText().toString())){
            return "";
        }else {
            return messageTv.getText().toString()+"-"+messageTv2.getText().toString();
        }
    }

    public InputCodeDialog setPositiveButton(String name) {
        this.positiveBtn = name;
        return this;
    }

    public InputCodeDialog setNegativeButton(String name) {
        this.negativeBtn = name;
        return this;
    }

    public InputCodeDialog setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
        return this;
    }

    /**
     * 是否设置点击dialog区域外，dialog消失
     *
     * @param cancel
     */
    public InputCodeDialog isCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 是否设置点击返回键，dialog消失
     *
     * @param cancel
     */
    public InputCodeDialog isCanceled(boolean cancel) {
        setCancelable(cancel);
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_code);
        initView();
    }

    private void initView() {
        messageTv = findViewById(R.id.messageEt);
        messageTv2 = findViewById(R.id.messageEt2);
        titleTxt = findViewById(R.id.titleTv);
        positiveTv = findViewById(R.id.positiveTv);
        negativeTv = findViewById(R.id.negativeTv);

        positiveTv.setOnClickListener(this);
        negativeTv.setOnClickListener(this);

        messageTv2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = s.toString().trim();
                if (!TextUtils.isEmpty(ss)&&ss.length() ==8){
                    String s1 = ss.substring(0,7);
                    String s2 = ss.substring(7,8);
                    isEditChange = Integer.parseInt(s1)%7 == Integer.parseInt(s2);
                }else {
                    isEditChange =false;
                }
                if (isEditChange){
                    messageTv2.setTextColor(Color.parseColor("#071c1a"));
                }else {
                    messageTv2.setTextColor(Color.parseColor("#ff0000"));
                }
            }
        });


        if (!TextUtils.isEmpty(positiveBtn)) {
            View positiveLine = findViewById(R.id.positiveLine);
            positiveLine.setVisibility(View.VISIBLE);

            positiveTv.setVisibility(View.VISIBLE);
            positiveTv.setText(positiveBtn);
        }
        if (!TextUtils.isEmpty(message)) {
            messageTv.setHint(message);
        }

        if (!TextUtils.isEmpty(negativeBtn)) {
            negativeTv.setText(negativeBtn);
        }

        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.negativeTv:
                if (!TextUtils.isEmpty(messageTv.getText().toString().trim())&&isEditChange){
                    if (listener != null) {
                        listener.onClick(this, false);
                    }
                    this.dismiss();
                }else {
                    ToastUtil.showToast("请输入正确的运单号");

                }
                break;
            case R.id.positiveTv:
                if (listener != null) {
                    listener.onClick(this, true);
                }
                this.dismiss();
                break;
        }
    }

    public interface OnClickListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}
