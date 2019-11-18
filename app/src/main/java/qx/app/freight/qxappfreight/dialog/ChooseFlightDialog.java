package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ChooseFlightAdapter;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.listener.ChooseDialogInterface;

/**
 * 选择库区dialog
 */
public class ChooseFlightDialog extends DialogFragment {
    private RecyclerView mRecyclerView;
    private Button btnConfirm;
    private ImageView ivCancel;


    private ChooseFlightAdapter adapter;
    private List<GetInfosByFlightIdBean> mList;
    private Context context;
    private int selectorPosition = 10000;

    private ChooseDialogInterface listener;

    public void setData(List<GetInfosByFlightIdBean> mList, Context context) {
        this.mList = mList;
//        this.selectorPosition = selectorPosition;
        this.context = context;
    }

    public void setChooseDialogInterface(ChooseDialogInterface listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_flight);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);
        mRecyclerView = dialog.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ChooseFlightAdapter(mList);
        adapter.setOnItemClickListener((adapter, view, position) -> updateUI(position));
        mRecyclerView.setAdapter(adapter);
        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        ivCancel = dialog.findViewById(R.id.iv_cancel);
        ivCancel.setOnClickListener(v -> {
            dismiss();
        });
        return dialog;
    }

    private void updateUI(int position) {
        listener.confirm(position);
        dismiss();
    }

}
