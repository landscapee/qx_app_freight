package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import io.reactivex.Observable;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.LoadUnloadLeaderContract;
import qx.app.freight.qxappfreight.presenter.LoadUnloadLeaderPresenter;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;

/**
 * * 装卸员小组长任务推送弹窗
 */
public class PushLoadUnloadLeaderDialog extends DialogFragment implements LoadUnloadLeaderContract.LoadUnloadLeaderView {
    private List<LoadAndUnloadTodoBean> list;
    private Context context;
    private View convertView;
    private OnDismissListener onDismissListener;
    private TextView mTvTitle;
    private TextView mTvAccept;
    private TextView mTvRefuse;
    private DialogLoadUnloadPushAdapter mAdapter;

    public void setData(Context context, List<LoadAndUnloadTodoBean> list, OnDismissListener onDismissListener) {
        this.context = context;
        this.list = list;
        this.onDismissListener = onDismissListener;
    }

    private void initViews() {
        RecyclerView mRvData = convertView.findViewById(R.id.rv_load_unload_list);
        mTvTitle = convertView.findViewById(R.id.tv_title_new_task);
        mTvAccept = convertView.findViewById(R.id.tv_accept);
        mTvRefuse = convertView.findViewById(R.id.tv_refuse);
        mRvData.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new DialogLoadUnloadPushAdapter(list);
        mRvData.setAdapter(mAdapter);
        TextView tvGoods=convertView.findViewById(R.id.tv_goods_weight);
        TextView tvMail=convertView.findViewById(R.id.tv_mail_weight);
        TextView tvBaggage=convertView.findViewById(R.id.tv_baggage_weight);
        tvGoods.setText("货物:"+list.get(0).getCargoWeight());
        tvMail.setText("邮件:"+list.get(0).getMailWeight());
        tvBaggage.setText("行李:"+list.get(0).getBaggageWeight());
        setListeners();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            Class c = Class.forName("android.support.v4.app.DialogFragment");
            Constructor con = c.getConstructor();
            Object obj = con.newInstance();
            Field dismissed = c.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(obj, false);
            Field shownByMe = c.getDeclaredField("mShownByMe");
            shownByMe.setAccessible(true);
            shownByMe.set(obj, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    private void setListeners() {
        mTvTitle.setText(context.getString(R.string.format_new_task_push, list.size()));
        LoadUnloadLeaderPresenter mPresenter = new LoadUnloadLeaderPresenter(PushLoadUnloadLeaderDialog.this);
        mTvAccept.setOnClickListener(v -> {
            Observable.just(list).all(loadAndUnloadTodoBeans -> {
                for (LoadAndUnloadTodoBean bean : loadAndUnloadTodoBeans) {
                    PerformTaskStepsEntity entity = new PerformTaskStepsEntity();
                    entity.setType(1);
                    entity.setLoadUnloadDataId(bean.getId());
                    entity.setFlightId(Long.valueOf(bean.getFlightId()));
                    entity.setFlightTaskId(bean.getTaskId());
                    entity.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude());
                    entity.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude());
                    if (bean.getTaskType() == 1) {
                        entity.setOperationCode("FreightLoadReceived");
                    } else {
                        entity.setOperationCode("FreightUnloadReceived");
                    }
                    entity.setTerminalId(DeviceInfoUtil.getDeviceInfo(getContext()).get("deviceId"));
                    entity.setUserId(UserInfoSingle.getInstance().getUserId());
                    entity.setUserName(bean.getWorkerName());
                    entity.setCreateTime(System.currentTimeMillis());
                    mPresenter.slideTask(entity);
                    Log.e("tagTest", "还在循环");
                }
                return true;
            }).subscribe(aBoolean -> {
                Log.e("tagTest", "循环结束，弹窗消失");
            }, throwable -> {
                Log.e("tagTest", "循环结束，调接口出错了");
                onDismissListener.refreshUI(-1);
            });
        });
        mTvRefuse.setOnClickListener(v -> {
            BaseFilterEntity entity = new BaseFilterEntity();
            entity.setTaskId(list.get(0).getTaskId());
            entity.setStaffId(UserInfoSingle.getInstance().getUserId());
            mPresenter.refuseTask(entity);
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_load_unload_leader);
        convertView = dialog.findViewById(R.id.content_view);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);
        dialog.setCancelable(false);
        initViews();
        return dialog;
    }

    @Override
    public void slideTaskResult(String result) {
        if ("正确".equals(result)) {
            onDismissListener.refreshUI(0);
        }
    }

    @Override
    public void refuseTaskResult(String result) {
        ToastUtil.showToast("拒绝任务操作成功");
        onDismissListener.refreshUI(1);
    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }

    public interface OnDismissListener {
        void refreshUI(int status);//1拒绝成功，0，领受成功，-1，调接口出错
    }

    private class DialogLoadUnloadPushAdapter extends BaseQuickAdapter<LoadAndUnloadTodoBean, BaseViewHolder> {
        DialogLoadUnloadPushAdapter(@Nullable List<LoadAndUnloadTodoBean> data) {
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
            List<String> result = StringUtil.getFlightList(item.getRoute());
            FlightInfoLayout layout = new FlightInfoLayout(context, result);
            LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout llContainer = helper.getView(R.id.ll_flight_info_container);
            llContainer.removeAllViews();
            llContainer.addView(layout, paramsMain);
            helper.setText(R.id.tv_seat, item.getSeat());
        }
    }
}
