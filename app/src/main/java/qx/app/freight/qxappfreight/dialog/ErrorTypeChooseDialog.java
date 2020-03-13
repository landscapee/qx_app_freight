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

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ChooseStoreroomAdapter;
import qx.app.freight.qxappfreight.listener.ChooseDialogInterface;
import qx.app.freight.qxappfreight.model.TestBean;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;

/**
 * 选择异常类型dialog
 */
public class ErrorTypeChooseDialog extends DialogFragment {
    private RecyclerView mRecyclerView;
    private Button btnConfirm;
    private ImageView ivCancel;


    private ChooseStoreroomAdapter adapter;
    private List<TestBean> mList;
    private ArrayList<TestBean> temList;
    private Context context;
    private int selectorPosition = 10000;

    private ChooseDialogInterface listener;

    public void setData(List<TestBean> mList, Context context) {

        try {
            temList = new ArrayList<>();
            temList.addAll(mList);
            this.mList  = Tools.deepCopy(temList);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        dialog.setContentView(R.layout.dialog_choose_error_type);
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
        adapter = new ChooseStoreroomAdapter(mList);
        adapter.setOnItemClickListener((adapter, view, position) -> updateUI(position));
        mRecyclerView.setAdapter(adapter);
        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        ivCancel = dialog.findViewById(R.id.iv_cancel);
        btnConfirm.setOnClickListener(v -> {
            if (selectorPosition == 10000) {
                ToastUtil.showToast("请选择异常类型");
            } else {
                listener.confirm(selectorPosition);
                dismiss();
            }
        });
        ivCancel.setOnClickListener(v -> {
            dismiss();
        });
        return dialog;
    }

    private void updateUI(int position) {
        selectorPosition = position;
        if (mList.get(position).isChoose()) {

        } else {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).isChoose()) {
                    mList.get(i).setChoose(false);
                }
            }
            mList.get(position).setChoose(true);
            adapter.notifyDataSetChanged();
        }
    }

}
