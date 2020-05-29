package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import qx.app.freight.qxappfreight.R;

public class InputDialog extends Dialog implements OnClickListener {
    private Context mContext;
    private String message;
    private String positiveBtn;
    private String negativeBtn;
    private String title;
    private OnClickListener listener;
    private EditText messageTv;
    private TextView titleTxt;
    private TextView positiveTv;
    private TextView negativeTv;

    public InputDialog(Context context) {
        super(context, R.style.CommomDialog);
        this.mContext = context;
        setCanceledOnTouchOutside(false);
    }

    public InputDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public InputDialog setHint(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        if (TextUtils.isEmpty(messageTv.getText().toString())){
            return "";
        }else {
            return messageTv.getText().toString();
        }
    }

    public InputDialog setPositiveButton(String name) {
        this.positiveBtn = name;
        return this;
    }

    public InputDialog setNegativeButton(String name) {
        this.negativeBtn = name;
        return this;
    }

    public InputDialog setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
        return this;
    }

    /**
     * 是否设置点击dialog区域外，dialog消失
     *
     * @param cancel
     */
    public InputDialog isCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 是否设置点击返回键，dialog消失
     *
     * @param cancel
     */
    public InputDialog isCanceled(boolean cancel) {
        setCancelable(cancel);
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input);
        initView();
    }

    private void initView() {
        messageTv = findViewById(R.id.messageEt);
        titleTxt = findViewById(R.id.titleTv);
        positiveTv = findViewById(R.id.positiveTv);
        negativeTv = findViewById(R.id.negativeTv);

        positiveTv.setOnClickListener(this);
        negativeTv.setOnClickListener(this);


        if (!TextUtils.isEmpty(positiveBtn)) {
            //有两个按钮
//            View btnLine = findViewById(R.id.btnLine);
//            LinearLayout btnLayout = findViewById(R.id.btnLayout);
//
//            btnLine.setVisibility(View.GONE);
//            btnLayout.setBackgroundResource(R.drawable.bg_dialog_button_light_green);

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
                if (listener != null) {
                    listener.onClick(this, false);
                }
                this.dismiss();
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
