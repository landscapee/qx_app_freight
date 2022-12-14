package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.presenter.LoadAndUnloadTodoPresenter;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;
import qx.app.freight.qxappfreight.widget.SlideRightExecuteView;

/**
 * 装卸机推送弹窗
 */
public class PushLoadUnloadDialog extends Dialog implements LoadAndUnloadTodoContract.loadAndUnloadTodoView {
    private List <LoadAndUnloadTodoBean> list;
    private Context context;
    private View convertView;
    private OnDismissListener onDismissListener;
    private TextView mTvTitle;
    private ImageView mIvGif;
    private SlideRightExecuteView mSlideView;
    private DialogLoadUnloadPushAdapter mAdapter;

    private int count = 0;// 任务 领取条数

    public PushLoadUnloadDialog(@NonNull Context context, int themeResId, List <LoadAndUnloadTodoBean> list, OnDismissListener onDismissListener) {
        super(MyApplication.getContext(), themeResId);
        this.list = list;
        this.context = MyApplication.getContext();
        this.onDismissListener = onDismissListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_load_unload);
        convertView = findViewById(R.id.content_view);
        setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 26) {
            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.flags |= WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED; //解决锁屏 dialog弹不出问题

        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);
        setCancelable(false);
        initViews();
    }

    @Override
    public void show() {
        super.show();
        Tools.wakeupScreen(context);//唤醒
    }

    public void setData(Context context, List <LoadAndUnloadTodoBean> list, OnDismissListener onDismissListener) {
        this.context = MyApplication.getContext();
        this.list = list;
        this.onDismissListener = onDismissListener;
    }

    private void initViews() {
        RecyclerView mRvData = convertView.findViewById(R.id.rv_load_unload_list);
        mTvTitle = convertView.findViewById(R.id.tv_title_new_task);
        mIvGif = convertView.findViewById(R.id.iv_start_gif);
        mSlideView = convertView.findViewById(R.id.slide_right_start);
        setCancelable(false);
        mRvData.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new DialogLoadUnloadPushAdapter(list);
        mRvData.setAdapter(mAdapter);
        setListeners();
    }

    public void refreshData() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        setListeners();
    }


//    @Override
//    public void show(FragmentManager manager, String tag) {
//        try {
//            Class c = Class.forName("android.support.v4.app.DialogFragment");
//            Constructor con = c.getConstructor();
//            Object obj = con.newInstance();
//            Field dismissed = c.getDeclaredField("mDismissed");
//            dismissed.setAccessible(true);
//            dismissed.set(obj, false);
//            Field shownByMe = c.getDeclaredField("mShownByMe");
//            shownByMe.setAccessible(true);
//            shownByMe.set(obj, false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        FragmentTransaction ft = manager.beginTransaction();
//            ft.add(this, tag);
//            ft.commitAllowingStateLoss();
//
//    }

    private void setListeners() {
        mTvTitle.setText(context.getString(R.string.format_new_task_push, list.size()));
        Glide.with(context).load(R.mipmap.swiperight_gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mIvGif);
        mIvGif.setOnTouchListener((v, event) -> {
            mIvGif.setVisibility(View.GONE);
            return false;
        });
        mSlideView.setLockListener(new SlideRightExecuteView.OnLockListener() {
            @Override
            public void onOpenLockSuccess() {
                EventBus.getDefault().post("MsgDialogAct_finish");
                count = 0;
                LoadAndUnloadTodoPresenter mPresenter = new LoadAndUnloadTodoPresenter(PushLoadUnloadDialog.this);
                for (LoadAndUnloadTodoBean bean : list) {
                    PerformTaskStepsEntity entity = new PerformTaskStepsEntity();
                    entity.setType(1);
                    entity.setLoadUnloadDataId(bean.getId());
                    entity.setFlightId(Long.valueOf(bean.getFlightId()));
                    entity.setFlightTaskId(bean.getTaskId());
                    entity.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude()+"");
                    entity.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude()+"");
                    if (bean.getTaskType() == 1) {
                        entity.setOperationCode("FreightLoadReceived");
                    } else if (bean.getTaskType() == 2 || bean.getTaskType() == 5) {
                        entity.setOperationCode("FreightUnloadReceived");
                    } else if (bean.getTaskType() == 3) {
                        entity.setOperationCode("PassengerLoadReceived");
                    }
                    entity.setTerminalId(DeviceInfoUtil.getDeviceInfo(getContext()).get("deviceId"));
                    entity.setUserId(UserInfoSingle.getInstance().getUserId());
                    entity.setUserName(UserInfoSingle.getInstance().getUsername());
                    entity.setCreateTime(System.currentTimeMillis());
                    mPresenter.slideTask(entity);
                }
            }

            @Override
            public void onOpenLockCancel() {
                mIvGif.setVisibility(View.VISIBLE);
            }
        });
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        Dialog dialog = new Dialog(context, R.style.dialog2);
//
//        return dialog;
//    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            Tools.startVibrator(context.getApplicationContext(), true, R.raw.ring);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tools.closeVibrator(context.getApplicationContext());
    }

    @Override
    public void loadAndUnloadTodoResult(List <LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {

    }

    @Override
    public void slideTaskResult(String result) {
        count++;
        if ("正确".equals(result)) {
            if (count == list.size()) {
                dismiss();
                onDismissListener.refreshUI(true);
            }

        }
    }

    @Override
    public void startClearTaskResult(String result) {

    }


    @Override
    public void toastView(String error) {
        count++;
        if (error != null)
            ToastUtil.showToast(error);
        if (count == list.size()) {
            dismiss();
            onDismissListener.refreshUI(false);
        }

    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }

    public interface OnDismissListener {
        void refreshUI(boolean success);
    }

    private class DialogLoadUnloadPushAdapter extends BaseQuickAdapter <LoadAndUnloadTodoBean, BaseViewHolder> {
        DialogLoadUnloadPushAdapter(@Nullable List <LoadAndUnloadTodoBean> data) {
            super(R.layout.item_push_load_unload_rv, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, LoadAndUnloadTodoBean item) {
            helper.setText(R.id.tv_flight_number, item.getFlightNo());
            helper.setText(R.id.tv_flight_info, item.getAircraftno());
            ImageView ivType = helper.getView(R.id.iv_task_type);
            TextView tvTime = helper.getView(R.id.tv_time);
            Drawable drawableLeft = null;
            if (item.getMovement() == 1 || item.getMovement() == 4) {//装机
                ivType.setImageResource(R.mipmap.jin);//应该显示  ===进
            } else {
                ivType.setImageResource(R.mipmap.li);
            }
            String time;
            int timeType;
            if (item.getTaskType() == 2 || item.getTaskType() == 5) {//是卸机
                if (!StringUtil.isTimeNull(String.valueOf(item.getAta()))) {
                    time = TimeUtils.getHMDay(item.getAta());
                    timeType = Constants.TIME_TYPE_AUTUAL;
                } else if (!StringUtil.isTimeNull(String.valueOf(item.getEta()))) {
                    time = TimeUtils.getHMDay(item.getEta());
                    timeType = Constants.TIME_TYPE_EXCEPT;
                } else {
                    time = TimeUtils.getHMDay(item.getSta());
                    timeType = Constants.TIME_TYPE_PLAN;
                }
            } else {
                if (!StringUtil.isTimeNull(String.valueOf(item.getAtd()))) {
                    time = TimeUtils.getHMDay(item.getAtd());
                    timeType = Constants.TIME_TYPE_AUTUAL;
                } else if (!StringUtil.isTimeNull(String.valueOf(item.getEtd()))) {
                    time = TimeUtils.getHMDay(item.getEtd());
                    timeType = Constants.TIME_TYPE_EXCEPT;
                } else {
                    time = TimeUtils.getHMDay(item.getStd());
                    timeType = Constants.TIME_TYPE_PLAN;
                }
            }
            switch (timeType) {
                case Constants.TIME_TYPE_AUTUAL:
                    drawableLeft = mContext.getResources().getDrawable(R.mipmap.shi);
                    break;
                case Constants.TIME_TYPE_EXCEPT:
                    drawableLeft = mContext.getResources().getDrawable(R.mipmap.yu);
                    break;
                case Constants.TIME_TYPE_PLAN:
                    drawableLeft = mContext.getResources().getDrawable(R.mipmap.ji);
                    break;
            }
            tvTime.setText(time);
            tvTime.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
            tvTime.setCompoundDrawablePadding(3);
            List <String> result = StringUtil.getFlightList(item.getRoute());
            FlightInfoLayout layout = new FlightInfoLayout(context, result);
            LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout llContainer = helper.getView(R.id.ll_flight_info_container);
            llContainer.removeAllViews();
            llContainer.addView(layout, paramsMain);
            helper.setText(R.id.tv_seat, item.getSeat());
        }
    }
}
