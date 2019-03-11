package qx.app.freight.qxappfreight.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import qx.app.freight.qxappfreight.R;

/**
 * 应用内普通样式对话框
 * Created by swd on 2019/3/1.
 */
public class CommonDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private String message;
    private String positiveBtn;
    private String negativeBtn;
    private String title;
    private OnClickListener listener;

    public CommonDialog(Context context) {
        super(context, R.style.CommomDialog);
        this.mContext = context;
        setCanceledOnTouchOutside(false);
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        setCanceledOnTouchOutside(false);
    }

    public CommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommonDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public CommonDialog setPositiveButton(String name) {
        this.positiveBtn = name;
        return this;
    }

    public CommonDialog setNegativeButton(String name) {
        this.negativeBtn = name;
        return this;
    }

    public CommonDialog setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
        return this;
    }

    /**
     * 是否设置点击dialog区域外，dialog消失
     *
     * @param cancel
     */
    public CommonDialog isCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 是否设置点击返回键，dialog消失
     *
     * @param cancel
     */
    public CommonDialog isCanceled(boolean cancel) {
        setCancelable(cancel);
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common);
        initView();
    }

    private void initView() {
        TextView messageTv = findViewById(R.id.messageTv);
        TextView titleTxt = findViewById(R.id.titleTv);
        TextView positiveTv = findViewById(R.id.positiveTv);
        TextView negativeTv = findViewById(R.id.negativeTv);

        positiveTv.setOnClickListener(this);
        negativeTv.setOnClickListener(this);

        messageTv.setText(message);
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
