package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

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

/**
 * 装卸机推送弹窗
 */
public class PushLoadUnloadDialog extends DialogFragment implements LoadAndUnloadTodoContract.loadAndUnloadTodoView {
    private List<LoadAndUnloadTodoBean> list;
    private Context context;
    private View convertView;
    private OnDismissListener onDismissListener;

    public void setData(Context context, List<LoadAndUnloadTodoBean> list, OnDismissListener onDismissListener) {
        this.context = context;
        this.list = list;
        this.onDismissListener = onDismissListener;
    }

    private void initViews() {
        RecyclerView rvData = convertView.findViewById(R.id.rv_load_unload_list);
        TextView tvAccept = convertView.findViewById(R.id.tv_accept);
        rvData.setLayoutManager(new LinearLayoutManager(context));
        rvData.setAdapter(new DialogLoadUnloadPushAdapter(list));
        tvAccept.setOnClickListener(v -> {
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
                super.dismiss();
                onDismissListener.refreshUI(true);
            }, throwable -> onDismissListener.refreshUI(false));
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
        lp.gravity = Gravity.CENTER; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);
        initViews();
        return dialog;
    }
//
//    @Override
//    public void show(FragmentManager manager, String tag) {
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.add(this, tag);
//        // 这里吧原来的commit()方法换成了commitAllowingStateLoss()
//        ft.commitAllowingStateLoss();
//    }

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
        public DialogLoadUnloadPushAdapter(@Nullable List<LoadAndUnloadTodoBean> data) {
            super(R.layout.item_push_load_unload_rv, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, LoadAndUnloadTodoBean item) {
            helper.setText(R.id.tv_plane_info, item.getFlightNo());
            helper.setText(R.id.tv_craft_number, item.getAircraftno());
            if (item.getRoute() != null &&item.getRoute().contains(",")){
                String [] placeArray=item.getRoute().split(",");
                List<String> placeList=new ArrayList<>();
                List<String> result=new ArrayList<>();
                placeList.addAll(Arrays.asList(placeArray));
                for (String str:placeList){
                    String temp=str.replaceAll("[^a-z^A-Z]", "");
                    result.add(temp);
                }
                helper.setText(R.id.tv_start_place, result.get(0));
                helper.setText(R.id.tv_middle_place, result.size() == 2 ? "-" : result.get(1));
                helper.setText(R.id.tv_end_place, result.get(result.size()-1));
            }else {
                helper.setText(R.id.tv_start_place, "-");
                helper.setText(R.id.tv_middle_place, "-");
                helper.setText(R.id.tv_end_place, "-");
            }
            helper.setText(R.id.tv_time, TimeUtils.getHMDay(item.getScheduleTime()));
        }
    }
}
