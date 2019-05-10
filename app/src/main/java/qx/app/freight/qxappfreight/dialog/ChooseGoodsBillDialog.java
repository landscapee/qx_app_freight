package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.LocalBillBean;

/**
 * 选择运单弹窗
 */
public class ChooseGoodsBillDialog extends DialogFragment {
    private Context context;
    private List<LocalBillBean> billList;
    private String boardText;
    private OnBillSelectListener onBillSelectListener;

    public void setData(Context context, String boardText, List<LocalBillBean> billList) {
        this.context = context;
        this.boardText = boardText;
        this.billList = billList;
//        this.billTexts.add(0,"请选择需要下拉的运单号");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_goods_bill);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);
        ImageView icClose = dialog.findViewById(R.id.iv_close_dialog);
        icClose.setOnClickListener(v -> {
            dismiss();
        });
        TextView tvBoard = dialog.findViewById(R.id.tv_board_number);
        tvBoard.setText("板车:" + boardText);
        Spinner spinner = dialog.findViewById(R.id.sp_chose_bill);
        List<String> billTexts = new ArrayList<>();
        for (LocalBillBean billBean : billList) {
            billTexts.add(billBean.getWayBillCode());
        }
        String[] array = billTexts.toArray(new String[billList.size()]);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, array);
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_general);
        spinner.setAdapter(spinnerAdapter);
        final int[] pos = {0};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos[0] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextView tvConfirm = dialog.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(v -> {
            if (onBillSelectListener != null) {
                onBillSelectListener.onBillSelected(pos[0]);
            }
            dismiss();
        });
        return dialog;
    }

    public interface OnBillSelectListener {
        void onBillSelected(int pos);
    }

    public void setOnBillSelectListener(OnBillSelectListener onBillSelectListener) {
        this.onBillSelectListener = onBillSelectListener;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
//        super.show(manager, tag);
        try {
            Class c = Class.forName("android.support.v4.app.DialogFragment");
            Constructor con = c.getConstructor();
            Object obj = con.newInstance();
            Field dismissed = c.getDeclaredField(" mDismissed");
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
}
