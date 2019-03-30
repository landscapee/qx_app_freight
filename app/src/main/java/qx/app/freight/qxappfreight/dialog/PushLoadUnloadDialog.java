package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
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
public class PushLoadUnloadDialog extends Dialog implements LoadAndUnloadTodoContract.loadAndUnloadTodoView {
    private List<LoadAndUnloadTodoBean> list;
    private Context context;
    private View convertView;
    private OnDismissListener onDismissListener;

    public PushLoadUnloadDialog(Context context) {
        super(context);
    }

    public PushLoadUnloadDialog(Context context, List<LoadAndUnloadTodoBean> list,OnDismissListener onDismissListener) {
        super(context);
        this.list = list;
        this.context = context;
        this.onDismissListener = onDismissListener;
        Objects.requireNonNull(getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        convertView = getLayoutInflater().inflate(R.layout.dialog_load_unload, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false); // 外部点击取消
        setContentView(convertView);
        ButterKnife.bind(this, convertView);
        initViews();
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
                PushLoadUnloadDialog.this.dissMiss();
                onDismissListener.refreshUI(true);
            }, throwable -> onDismissListener.refreshUI(false));
        });
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Objects.requireNonNull(getWindow()).setGravity(Gravity.BOTTOM); //显示在顶部
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
        getWindow().setAttributes(p);
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
        public DialogLoadUnloadPushAdapter(@Nullable List<LoadAndUnloadTodoBean> data) {
            super(R.layout.item_push_load_unload_rv, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, LoadAndUnloadTodoBean item) {
            helper.setText(R.id.tv_plane_info, item.getFlightNo());
            helper.setText(R.id.tv_craft_number, item.getAircraftno());
            String places = item.getRoute();
            places = places.substring(1, places.length() - 1).replaceAll("\",\"", ",");
            String[] placeArray = places.substring(1, places.length() - 1).split(",");
            helper.setText(R.id.tv_start_place, placeArray[0]);
            helper.setText(R.id.tv_middle_place, placeArray.length == 2 ? "-" : placeArray[1]);
            helper.setText(R.id.tv_end_place, placeArray[placeArray.length - 1]);
            helper.setText(R.id.tv_seat, item.getSeat());
            helper.setText(R.id.tv_time, TimeUtils.getHMDay(item.getScheduleTime()));
        }
    }
}
