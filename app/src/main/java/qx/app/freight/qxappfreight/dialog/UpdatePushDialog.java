package qx.app.freight.qxappfreight.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.SlideRightExecuteView;

/**
 * 装机单和货邮舱单推送
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
    private String title;
    private OnTpPushListener mOnTpPushListener;

    public UpdatePushDialog(@NonNull Context context, int themeResId, String title, OnTpPushListener mOnTpPushListener) {
        super(context, themeResId);
        mContext = context;
        this.mOnTpPushListener = mOnTpPushListener;
        if (Build.VERSION.SDK_INT >= 26) {
            getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        else {
            Objects.requireNonNull(getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        convertView = getLayoutInflater().inflate(R.layout.popup_manifest_update, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(convertView);
        ButterKnife.bind(this, convertView);
        this.title = title;
        initViews();
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
        p.flags |= WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED; //解决锁屏 dialog弹不出问题
        getWindow().setAttributes(p);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        TextView titleTv = convertView.findViewById(R.id.tv_content);
        titleTv.setText(title);
        Glide.with(mContext).load(R.mipmap.swiperight_gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivStartGif);
        ivStartGif.setOnTouchListener((v, event) -> {
            ivStartGif.setVisibility(View.GONE);
            return false;
        });
        mSlideRightExecuteView.setLockListener(new SlideRightExecuteView.OnLockListener() {
            @Override
            public void onOpenLockSuccess() {
                mOnTpPushListener.onSureBtnCallBack();
                dismiss();
            }

            @Override
            public void onOpenLockCancel() {
                ivStartGif.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void show() {
        super.show();
        Tools.wakeupScreen(mContext);
    }

    public interface OnTpPushListener {
        void onSureBtnCallBack();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Tools.startVibrator(mContext.getApplicationContext(),true,R.raw.ring);
        }

    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Tools.startVibrator(mContext.getApplicationContext(),true,R.raw.ring);
//    }

    @Override
    protected void onStop() {
        super.onStop();
        Tools.closeVibrator(mContext.getApplicationContext());
    }
}
