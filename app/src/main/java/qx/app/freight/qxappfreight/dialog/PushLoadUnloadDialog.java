package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.presenter.LoadAndUnloadTodoPresenter;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.SlideRightExecuteView;

/**
 * 装卸机推送弹窗
 */
public class PushLoadUnloadDialog extends DialogFragment implements LoadAndUnloadTodoContract.loadAndUnloadTodoView {
    private List<LoadAndUnloadTodoBean> list;
    private Context context;
    private View convertView;
    private OnDismissListener onDismissListener;
    private TextView mTvTitle;
    private ImageView mIvGif;
    private SlideRightExecuteView mSlideView;
    private DialogLoadUnloadPushAdapter mAdapter;

    public void setData(Context context, List<LoadAndUnloadTodoBean> list, OnDismissListener onDismissListener) {
        this.context = context;
        this.list = list;
        this.onDismissListener = onDismissListener;
    }

    private void initViews() {
        RecyclerView mRvData = convertView.findViewById(R.id.rv_load_unload_list);
        mTvTitle = convertView.findViewById(R.id.tv_title_new_task);
        mIvGif = convertView.findViewById(R.id.iv_start_gif);
        mSlideView = convertView.findViewById(R.id.slide_right_start);
        mRvData.setLayoutManager(new LinearLayoutManager(context));
        mAdapter=new DialogLoadUnloadPushAdapter(list);
        mRvData.setAdapter(mAdapter);
        setListeners();
    }

    public void refreshData() {
        mAdapter.notifyDataSetChanged();
        setListeners();
    }

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
                LoadAndUnloadTodoPresenter mPresenter = new LoadAndUnloadTodoPresenter(PushLoadUnloadDialog.this);
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
                            entity.setOperationCode("FreightPass_receive");
                        } else {
                            entity.setOperationCode("FreightPass_receive");
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
                    dismiss();
                    onDismissListener.refreshUI(true);
                }, throwable -> {
                    Log.e("tagTest", "循环结束，调接口出错了");
                    dismiss();
                    onDismissListener.refreshUI(false);
                });
            }

            @Override
            public void onOpenLockCancel() {
                mIvGif.setVisibility(View.VISIBLE);
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_load_unload);
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
    public void showDialog(FragmentManager fragmentManager) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag(getTag());
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, getTag());
    }

    @Override
    public void loadAndUnloadTodoResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {

    }

    @Override
    public void slideTaskResult(String result) {
        if ("正确".equals(result)) {
            Log.e("tagPush", "循环调取领受接口正确");
        }
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
        void refreshUI(boolean success);
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
            boolean hasActualTime = item.getActualArriveTime() == 0;
            TextView tvTime = helper.getView(R.id.tv_time);
            tvTime.setText(hasActualTime ? TimeUtils.getHMDay(item.getActualArriveTime()) : TimeUtils.getHMDay(item.getScheduleTime()));
            Drawable drawableLeft;
            if (item.getTaskType() == 1) {
                ivType.setImageResource(R.mipmap.li);
                drawableLeft = mContext.getResources().getDrawable(R.mipmap.ji);
            } else {
                ivType.setImageResource(R.mipmap.jin);//应该显示  ===进
                if (hasActualTime) {
                    drawableLeft = mContext.getResources().getDrawable(R.mipmap.shi);
                } else {
                    drawableLeft = mContext.getResources().getDrawable(R.mipmap.ji);
                }
            }
            tvTime.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
            tvTime.setCompoundDrawablePadding(3);
            List<String> result = new ArrayList<>();
            if (item.getRoute() != null && item.getRoute().contains(",")) {
                String[] placeArray = item.getRoute().split(",");
                List<String> placeList = new ArrayList<>(Arrays.asList(placeArray));
                for (String str : placeList) {
                    String temp = str.replaceAll("[^a-z^A-Z]", "");
                    result.add(temp);
                }
            }
            if (result.size() == 0) {//没有航线信息
                helper.getView(R.id.tv_start_place).setVisibility(View.GONE);
                helper.getView(R.id.iv_two_place).setVisibility(View.GONE);
                helper.getView(R.id.tv_middle_place).setVisibility(View.GONE);
                helper.getView(R.id.tv_end_place).setVisibility(View.GONE);
            } else {
                if (result.size() == 2) {//只有起点和终点
                    helper.getView(R.id.tv_start_place).setVisibility(View.VISIBLE);
                    helper.getView(R.id.iv_two_place).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_middle_place).setVisibility(View.GONE);
                    helper.getView(R.id.tv_end_place).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_start_place, result.get(0));
                    helper.setText(R.id.tv_end_place, result.get(result.size() - 1));
                } else {//至少有三个地方
                    helper.getView(R.id.tv_start_place).setVisibility(View.VISIBLE);
                    helper.getView(R.id.iv_two_place).setVisibility(View.GONE);
                    helper.getView(R.id.tv_middle_place).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_end_place).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_start_place, result.get(0));
                    helper.setText(R.id.tv_middle_place, result.get(1));
                    helper.setText(R.id.tv_end_place, result.get(result.size() - 1));
                }
            }
            helper.setText(R.id.tv_seat, item.getSeat());
        }
    }
}
