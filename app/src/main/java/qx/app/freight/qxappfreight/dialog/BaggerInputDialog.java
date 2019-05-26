package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.donkingliang.labels.LabelsView;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**行李上报，输入行李信息的弹窗
 *
 * create by swd
 */
public class BaggerInputDialog extends DialogFragment {
    private Context context;
    private TransportTodoListBean bean;
    private String flightIndicator;

    private List<String> flightList;
    private OnBaggerInoutListener mListener;

    private TextView tvCancel,tvConfirm,tvBoardNumber;
    private EditText etNumber,etWeight,etVolume;
    private LabelsView labelsView;


    public void setData(Context context, TransportTodoListBean bean,String flightIndicator) {
        this.context = context;
        this.bean = bean;
        this.flightIndicator = flightIndicator;
    }
    public void setBaggerInoutListener(OnBaggerInoutListener listener){
        this.mListener = listener;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bagger_input);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);

        //初始化控件
        tvBoardNumber = dialog.findViewById(R.id.tv_board_number);
        tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvConfirm = dialog.findViewById(R.id.tv_confirm);
        etNumber = dialog.findViewById(R.id.et_number);
        etWeight = dialog.findViewById(R.id.et_weight);
        etVolume = dialog.findViewById(R.id.et_volume);
        labelsView = dialog.findViewById(R.id.labels_flight);
        //板车类型~板车号
        tvBoardNumber.setText("板车："+String.format(context.getString(R.string.format_allocate_ddress_info), MapValue.getCarTypeValue(bean.getTpScooterType()), bean.getTpScooterCode()));

        if (flightIndicator.equals("M")){
                //初始化标签栏
                flightList =new ArrayList<>();
                flightList.add("国内");
                flightList.add("国际");
                labelsView.setLabels(flightList);
                labelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
                    @Override
                    public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                        if (position ==0){
                            bean.setFlightIndicator("D");
                        }else {
                            bean.setFlightIndicator("I");
                        }
                    }
                });
                labelsView.setSelects(0);
        }else {
            bean.setFlightIndicator(flightIndicator);
        }

        //初始化点击事件
        tvConfirm.setOnClickListener(v -> {
            toConFirm();
        });
        tvCancel.setOnClickListener(v ->
                dismiss()
        );

        return dialog;
    }

    /**
     * 确定操作
     */
    private void toConFirm() {
        String sNumber = etNumber.getText().toString().trim();
        String sWeight = etWeight.getText().toString().trim();
        String sVolume = etVolume.getText().toString().trim();

        if (TextUtils.isEmpty(sNumber)){
            ToastUtil.showToast("行李数量不能为空");
            return;
        }
        if (TextUtils.isEmpty(sWeight)){
            ToastUtil.showToast("行李重量不能为空");
            return;
        }
        if (TextUtils.isEmpty(sVolume)){
            ToastUtil.showToast("行李体积不能为空");
            return;
        }
        //判断是不是double类型
        if (StringUtil.isDouble(sWeight)&&StringUtil.isDouble(sVolume)){
            bean.setTpCargoNumber(Integer.parseInt(sNumber));
            bean.setTpCargoWeight(Double.valueOf(sWeight));
            bean.setTpCargoVolume(Double.valueOf(sVolume));
            mListener.onConfirm(bean);
            dismiss();
        }else {
            ToastUtil.showToast("输入数值格式不对");
        }

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            Class c=Class.forName("android.support.v4.app.DialogFragment");
            Constructor con = c.getConstructor();
            Object obj = con.newInstance();
            Field dismissed = c.getDeclaredField(" mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(obj,false);
            Field shownByMe = c.getDeclaredField("mShownByMe");
            shownByMe.setAccessible(true);
            shownByMe.set(obj,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }


    public interface OnBaggerInoutListener{
        void onConfirm(TransportTodoListBean data);
    }
}
