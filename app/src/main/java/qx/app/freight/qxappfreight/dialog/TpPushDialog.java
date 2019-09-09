package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.TaskFlightAdapter;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.Tools;

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

    private AcceptTerminalTodoBean mAcceptTerminalTodoBean;
    private  List<OutFieldTaskBean> list;

    private OnTpPushListener mOnTpPushListener;

    public TpPushDialog( @NonNull Context context) {
        super(context);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Tools.startVibrator(mContext.getApplicationContext(),true,R.raw.ring);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tools.closeVibrator(mContext.getApplicationContext());
    }

    public TpPushDialog(@NonNull Context context, int themeResId, AcceptTerminalTodoBean mAcceptTerminalTodoBean, OnTpPushListener mOnTpPushListener) {
        super(context, themeResId);
        mContext = context;
        this.mOnTpPushListener = mOnTpPushListener;
        this.mAcceptTerminalTodoBean = mAcceptTerminalTodoBean;
        if (Build.VERSION.SDK_INT >= 26) {
            getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        else
            Objects.requireNonNull(getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        convertView = getLayoutInflater().inflate(R.layout.popup_new_tp_task, null);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(convertView);
        ButterKnife.bind(this,convertView);
        initViews();

    }
    protected TpPushDialog( @NonNull Context context, boolean cancelable,  @Nullable DialogInterface.OnCancelListener cancelListener) {
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
        getWindow().setAttributes(p);
    }

    private void initViews() {

        setCancelable(false);
        setCanceledOnTouchOutside(false);
        //列表设置
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);
        rcNewTask.setLayoutManager(manager);
        list = new ArrayList<>();
        TaskFlightAdapter mTaskFlightAdapter = new TaskFlightAdapter(list);
        rcNewTask.setAdapter(mTaskFlightAdapter);

        btnSure.setOnClickListener(v -> {

            mOnTpPushListener.onSureBtnCallBack(mAcceptTerminalTodoBean.getTasks());

            dismiss();

        });

        setView();

    }

    private void setView() {
        if (mAcceptTerminalTodoBean != null){
//            for (List<OutFieldTaskBean> mlist:mAcceptTerminalTodoBean.getUseTasks()){
//
//                list.addAll(mlist);
//            }
            tvTpType.setText(mAcceptTerminalTodoBean.getProjectName());
            //把大任务的 运输板车类型赋值给子任务
            for (OutFieldTaskBean mOutFieldTaskBeans : mAcceptTerminalTodoBean.getTasks()){
                mOutFieldTaskBeans.setTransfortType(mAcceptTerminalTodoBean.getTransfortType());
            }
            list.addAll(mAcceptTerminalTodoBean.getTasks());

        }
    }

    public interface OnTpPushListener{

            void onSureBtnCallBack(List<OutFieldTaskBean> list);

    }
}
