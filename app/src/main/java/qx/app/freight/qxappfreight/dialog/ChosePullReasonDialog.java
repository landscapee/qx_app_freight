package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * 选择拉货原因dialog
 */
public class ChosePullReasonDialog extends DialogFragment {
    private Context context;
    private OnChoseListener onChoseListener;
    private int selectPos;

    public void setData(OnChoseListener onChoseListener, Context context) {
        this.context = context;
        this.onChoseListener = onChoseListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chose_pull_reason);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.anim_bottom_bottom);
        window.getDecorView().setPadding(30, 30, 30, 30);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        ImageView ivClose = dialog.findViewById(R.id.iv_close_dialog);
        ivClose.setOnClickListener(v -> {
            onChoseListener.onChosed("", "", true);
            dismiss();
        });
        Spinner spPullReason = dialog.findViewById(R.id.sp_pull_reason);
        EditText etRemark = dialog.findViewById(R.id.et_remark);
        TextView tvCommit = dialog.findViewById(R.id.tv_commit);
        List<String> billTexts = new ArrayList<>();
        billTexts.add("请选择拉货原因");
        billTexts.add("临时拉货");
        billTexts.add("载量不够");
        billTexts.add("舱位不够");
        billTexts.add("限载拉货");
        billTexts.add("其它");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, billTexts);
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_general);
        spPullReason.setAdapter(spinnerAdapter);
        spPullReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvCommit.setOnClickListener(v -> {
            if (selectPos != 0) {
                if (selectPos == 5 && TextUtils.isEmpty(etRemark.getText().toString())) {
                    ToastUtil.showToast("请输入备注再提交");
                } else {
                    onChoseListener.onChosed(billTexts.get(selectPos), etRemark.getText().toString(), false);
                }
                dismiss();
            } else {
                ToastUtil.showToast("请选择拉货原因再进行提交");
            }
        });
        return dialog;
    }

    public interface OnChoseListener {
        void onChosed(String pullReasonType, String remark, boolean cancel);
    }
}
