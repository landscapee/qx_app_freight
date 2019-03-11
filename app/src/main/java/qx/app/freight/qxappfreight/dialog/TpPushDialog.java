package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;

/**
 * 外场运输待办推送
 */
public class TpPushDialog extends Dialog {

    private Context mContext;
    private View convertView;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_tp_type)
    TextView tvTpType;
    @BindView(R.id.rc_new_task)
    RecyclerView rcNewTask;
    @BindView(R.id.btn_sure)
    Button btnSure;


    private OnTpPushListener mOnTpPushListener;

    public TpPushDialog( @NonNull Context context) {
        super(context);
    }

    public TpPushDialog(@NonNull Context context, int themeResId,OnTpPushListener mOnTpPushListener) {
        super(context, themeResId);
        mContext = context;
        this.mOnTpPushListener = mOnTpPushListener;
        Objects.requireNonNull(getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        convertView = getLayoutInflater().inflate(R.layout.popup_new_tp_task, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(convertView);
        ButterKnife.bind(this,convertView);
        initViews();
    }
    protected TpPushDialog( @NonNull Context context, boolean cancelable,  @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initViews() {



    }
    public interface OnTpPushListener{

            void onSureBtnCallBack();

    }
}
