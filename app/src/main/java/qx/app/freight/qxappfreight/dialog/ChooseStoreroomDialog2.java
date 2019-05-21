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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.listener.ChooseDialogInterface;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * 选择库区dialog
 */
public class ChooseStoreroomDialog2 extends DialogFragment {
    private RecyclerView mRecyclerView;
    private Button btnConfirm;
    private ImageView ivCancel;


    private ChooseStoreroomAdapter adapter;
    private List<TestBean> mList;
    private Context context;
    private int selectorPosition = 10000;

    private ChooseDialogInterface listener;

    public void setData(List<TestBean> mList, Context context) {
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
        dialog.setContentView(R.layout.dialog_choose_storeroom);
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
                ToastUtil.showToast("请选择库区");
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

    public static class TestBean {
        private int type;
        private int number;

        private String name;
        private boolean isChoose;

        private String id;

        public TestBean(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public TestBean(String name, boolean isChoose) {
            this.name = name;
            this.isChoose = isChoose;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }
    }

    public static class ChooseStoreroomAdapter extends BaseQuickAdapter<TestBean, BaseViewHolder> {

        public ChooseStoreroomAdapter(@Nullable List<TestBean> data) {
            super(R.layout.item_choose_storeroom, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, TestBean item) {
            helper.setText(R.id.tv_name, item.getName());
            if (item.isChoose()) {
                helper.setImageResource(R.id.iv_choose, R.mipmap.qualifled);
            } else {
                helper.setImageResource(R.id.iv_choose, R.mipmap.normal);
            }

        }
    }

}
