package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;

public class ChooseExceptionDialog extends DialogFragment {

    Context context;

    /**
     * 异常类型
     * 异常类型 1.件数异常; 2.有单无货; 3.破损; 4.腐烂; 5.有货无单;
     * 6.泄露; 7.错运; 8.扣货; 9.无标签; 10.对方未发报文;  11.其他; 12:死亡
     *
     * 13:多收邮路单 14:多收货运单 15:多收邮袋 16:有邮袋无邮路单 17：少收货运单 18:有邮路单无邮袋
     */
    private Integer[] ubnormalType;

    List<Integer> chooseedList = new ArrayList<>(25);

    OnExceptionChooseListener onExceptionChooseListener;



    Button btnConfirm;//确认按钮
    ImageView ivCancel;//取消按钮
    LinearLayout mLl;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.dialog2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_exception);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = 1000;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);

        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        ivCancel = dialog.findViewById(R.id.iv_cancel);
        mLl = dialog.findViewById(R.id.ll_mll);

        btnConfirm.setOnClickListener(v -> {
            for(int i=0; i<mLl.getChildCount(); i++){
                if(((CheckBox)mLl.getChildAt(i)).isChecked()){
                    chooseedList.add(i+2);
                }
            }
            //组装数据
            Integer[] result = new Integer[chooseedList.size()];
            for(int i = 0; i<chooseedList.size(); i++){
                result[i] = chooseedList.get(i);
            }
            //返回选择结果
            onExceptionChooseListener.onExceptionChoose(result);
            dismiss();
        });
        ivCancel.setOnClickListener(v -> {
            dismiss();
        });
        return dialog;
    }

    public void setOnExceptionChooseListener(OnExceptionChooseListener listener){
        onExceptionChooseListener = listener;
    }

    public interface OnExceptionChooseListener{
        void onExceptionChoose(Integer[] exceptionTypes);
    }
}
