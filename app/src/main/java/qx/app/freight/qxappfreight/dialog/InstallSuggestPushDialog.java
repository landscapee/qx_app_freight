package qx.app.freight.qxappfreight.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import qx.app.freight.qxappfreight.adapter.LnstallationListAdapter;
import qx.app.freight.qxappfreight.bean.response.LnstallationInfoBean;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.SlideRightExecuteView;

/**
 * 装机单和货邮舱单推送
 */
public class InstallSuggestPushDialog extends Dialog {
    private Context mContext;
    private View convertView;
    @BindView(R.id.slide_right_start)
    SlideRightExecuteView mSlideRightExecuteView;
    @BindView(R.id.iv_start_gif)
    ImageView ivStartGif;
    @BindView(R.id.mfrv_data)
    RecyclerView mRvData;//货邮舱单信息列表
    private OnTpPushListener mOnTpPushListener;
    @BindView(R.id.tv_flight_no)
    TextView tvFlightNo;

    private String flightNo;
    private  List<LoadingListBean.DataBean.ContentObjectBean.ScooterBean> mList1;

    private boolean ring = true;

    public InstallSuggestPushDialog(@NonNull Context context, int themeResId, List<LoadingListBean.DataBean.ContentObjectBean.ScooterBean> mList1,String flightNo, OnTpPushListener mOnTpPushListener) {
        super(context, themeResId);
        mContext = context;
        this.mOnTpPushListener = mOnTpPushListener;
        this.flightNo = flightNo;

        if (Build.VERSION.SDK_INT >= 26) {
            getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        else
            Objects.requireNonNull(getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        convertView = getLayoutInflater().inflate(R.layout.popup_install_suggest, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(convertView);
        ButterKnife.bind(this, convertView);
        this.mList1 = mList1;
        initViews();
    }
    public InstallSuggestPushDialog(@NonNull Context context, int themeResId, List<LoadingListBean.DataBean.ContentObjectBean.ScooterBean> mList1,String flightNo, boolean ring,OnTpPushListener mOnTpPushListener) {
        super(context, themeResId);
        mContext = context;
        this.mOnTpPushListener = mOnTpPushListener;
        this.flightNo = flightNo;
        this.ring = ring;

        if (Build.VERSION.SDK_INT >= 26) {
            getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        else
            Objects.requireNonNull(getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        convertView = getLayoutInflater().inflate(R.layout.popup_install_suggest, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(convertView);
        ButterKnife.bind(this, convertView);
        this.mList1 = mList1;
        initViews();

    }
    private void screenData( List<LoadingListBean.DataBean.ContentObjectBean.ScooterBean> mList1) {

        tvFlightNo.setText(flightNo);
        LnstallationInfoBean.ScootersBean title = new LnstallationInfoBean.ScootersBean();
        title.setCargoName("舱位");
        title.setLocation("货位");
        title.setScooterCode("板车号");
        title.setSerialInd("ULD号");
        title.setDestinationStation("目的站");
        title.setType("类型");
        title.setWeight("重量");
        title.setTotal("体积");
//        title.setSpecialNumber("特货代码");
        title.setSpecialCode("特货代码");
        title.setExceptionFlag(1);
//        mList.add(0, title);
        List <LnstallationInfoBean.ScootersBean> mList2 = new ArrayList <>();
        mList2.add(title);
        for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean :mList1 ){
            LnstallationInfoBean.ScootersBean scootersBean1 = new LnstallationInfoBean.ScootersBean();
            scootersBean1.setOldCargoName(scooterBean.getOldCargoName());
            scootersBean1.setCargoName(scooterBean.getCargoName());
            scootersBean1.setLocation(scooterBean.getLocation());
            scootersBean1.setScooterCode(scooterBean.getScooterCode());
            scootersBean1.setSerialInd(scooterBean.getSerialInd());
            scootersBean1.setDestinationStation(scooterBean.getDestinationStation());
            scootersBean1.setType(scooterBean.getType());
            scootersBean1.setWeight(scooterBean.getWeight()+"");
            scootersBean1.setTotal(scooterBean.getTotal()+"");
            scootersBean1.setChange(scooterBean.isChange());
            scootersBean1.setSplit(scooterBean.isSplit());
            scootersBean1.setExceptionFlag(scooterBean.getExceptionFlag());
            scootersBean1.setSpecialCode(scooterBean.getSpecialCode());
//            if (scooterBean.getWaybillList() != null&& scooterBean.getWaybillList().size()> 0)
//                scootersBean1.setSpecialNumber(scooterBean.getWaybillList().get(0).getSpecialCode());
//            else
//                scootersBean1.setSpecialNumber(null);
            mList2.add(scootersBean1);
        }
        LnstallationListAdapter adapter = new LnstallationListAdapter(mList2,true);
        mRvData.setAdapter(adapter);

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

    @Override
    public void show() {
        super.show();
        Tools.wakeupScreen(mContext);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);

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
        mRvData.setLayoutManager(new LinearLayoutManager(getContext()));
        screenData(mList1);
    }

    public interface OnTpPushListener {
        void onSureBtnCallBack();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            if (ring)
                Tools.startVibrator(mContext.getApplicationContext(),true,R.raw.ring);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Tools.closeVibrator(mContext.getApplicationContext());
    }
}
