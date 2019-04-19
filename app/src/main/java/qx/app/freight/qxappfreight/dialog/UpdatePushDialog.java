package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.MainActivity;
import qx.app.freight.qxappfreight.adapter.TaskFlightAdapter;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.widget.SlideRightExecuteView;

/**
 * 外场运输待办推送
 */
public class UpdatePushDialog extends Dialog {

    private Context mContext;
    private View convertView;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.slide_right_start)
    SlideRightExecuteView mSlideRightExecuteView;
    @BindView(R.id.iv_start_gif)
    ImageView ivStartGif;

    private String flightId;

    private OnTpPushListener mOnTpPushListener;

    public UpdatePushDialog(@NonNull Context context) {
        super(context);

    }

    public UpdatePushDialog(@NonNull Context context, int themeResId,String flightId, OnTpPushListener mOnTpPushListener) {
        super(context, themeResId);
        mContext = context;
        this.mOnTpPushListener = mOnTpPushListener;
        Objects.requireNonNull(getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        convertView = getLayoutInflater().inflate(R.layout.popup_manifest_update, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(convertView);
        ButterKnife.bind(this,convertView);
        this.flightId = flightId;

        initViews();

    }
    protected UpdatePushDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Objects.requireNonNull(getWindow()).setGravity(Gravity.TOP); //显示在顶部
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
        p.height = d.getHeight();
        getWindow().setAttributes(p);
    }

    private void initViews() {

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        Glide.with(mContext).load(R.mipmap.swiperight_gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivStartGif);

        ivStartGif.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ivStartGif.setVisibility(View.GONE);

                return false;
            }
        });

        mSlideRightExecuteView.setLockListener(new SlideRightExecuteView.OnLockListener() {
            @Override
            public void onOpenLockSuccess() {
                mOnTpPushListener.onSureBtnCallBack(flightId);
                dismiss();
            }

            @Override
            public void onOpenLockCancel() {
                ivStartGif.setVisibility(View.VISIBLE);
            }
        });

        setView();

    }

    private void setView() {

    }

    public interface OnTpPushListener{

            void onSureBtnCallBack(String s);

    }
}
