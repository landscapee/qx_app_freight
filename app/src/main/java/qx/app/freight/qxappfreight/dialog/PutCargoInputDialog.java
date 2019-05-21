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
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**行李上报，输入行李信息的弹窗
 *
 * create by swd
 */
public class PutCargoInputDialog extends DialogFragment {
    private Context context;

    private PutCargoInputListener mListener;

    private TextView tvConfirm;
    private EditText etNumber;

    private int num = 0;

    public void setData(Context context, int num) {
        this.context = context;
        this.num =num;
    }
    public void setPutCargoInputListener(PutCargoInputListener listener){
        this.mListener = listener;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_putcargo_input);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);

        //初始化控件
        tvConfirm = dialog.findViewById(R.id.tv_confirm);
        etNumber = dialog.findViewById(R.id.et_number);
        etNumber.setText(num+"");
        //初始化点击事件
        tvConfirm.setOnClickListener(v -> {
            toConFirm();
        });

        return dialog;
    }

    /**
     * 确定操作
     */
    private void toConFirm() {
        String sNumber = etNumber.getText().toString().trim();

        if (TextUtils.isEmpty(sNumber)){
            ToastUtil.showToast("出库数量不能为空");
            return;
        }
        if (Integer.valueOf(sNumber)>num){
            ToastUtil.showToast("出库数量不能大于待出货数量");
            return;
        }
        mListener.onConfirm(Integer.valueOf(sNumber));
        dismiss();
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


    public interface PutCargoInputListener{
        void onConfirm(int data);
    }
}
