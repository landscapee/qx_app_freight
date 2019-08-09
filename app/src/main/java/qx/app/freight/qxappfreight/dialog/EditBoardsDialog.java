package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import qx.app.freight.qxappfreight.adapter.EditBoardsAdapter;
import qx.app.freight.qxappfreight.bean.BoardChoseBean;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * 编辑板车的dialog
 */
public class EditBoardsDialog extends DialogFragment {
    private Context context;
    private String chosedBoards;
    private OnChoseListener onChoseListener;

    public void setData(OnChoseListener onChoseListener, String chosedBoards, Context context) {
        this.context = context;
        this.chosedBoards = chosedBoards;
        this.onChoseListener = onChoseListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_boards);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER; // 紧贴底部
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);
        ImageView ivClose = dialog.findViewById(R.id.iv_close_dialog);
        ivClose.setOnClickListener(v -> {
            onChoseListener.onChosed(chosedBoards);
            dismiss();
        });
        RecyclerView rvBoards = dialog.findViewById(R.id.rv_boards);
        TextView tvCommit = dialog.findViewById(R.id.tv_commit);
        List<BoardChoseBean> boards = new ArrayList<>();
        if (chosedBoards.contains(",")) {
            String[] text = chosedBoards.split(",");
            for (String s : text) {
                BoardChoseBean bean = new BoardChoseBean();
                bean.setBoardName(s);
                boards.add(bean);
            }
        } else {
            BoardChoseBean bean = new BoardChoseBean();
            bean.setBoardName(chosedBoards);
            boards.add(bean);
        }
        EditBoardsAdapter adapter = new EditBoardsAdapter(boards);
        rvBoards.setLayoutManager(new GridLayoutManager(context, 3));
        rvBoards.setAdapter(adapter);
        tvCommit.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            for (BoardChoseBean bean : boards) {
                if (bean.isChosed()) {
                    sb.append(bean.getBoardName());
                    sb.append(",");
                }
            }
            onChoseListener.onChosed(TextUtils.isEmpty(sb.toString()) ? "" : (sb.toString().substring(0, sb.toString().length() - 1)));
            dismiss();
        });
        return dialog;
    }

    public interface OnChoseListener {
        void onChosed(String chosedBoards);
    }
}
