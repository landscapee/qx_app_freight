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
import android.widget.GridView;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.TestGridAdapter;
import qx.app.freight.qxappfreight.bean.PositionBean;
import qx.app.freight.qxappfreight.model.TestBean;

public class PopTestDialog extends DialogFragment {
    private GridView mGridView;
    private TestGridAdapter adapter;
    private List<TestBean> mList;
    private Context context;

    public void setData(List<TestBean> mList, Context context){
        this.mList = mList;
        this.context = context;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pop_test);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);

        mGridView = dialog.findViewById(R.id.grid_view);
        adapter=new TestGridAdapter(mList,context);
        adapter.setItemInterface(new TestGridAdapter.TestInterface() {
            @Override
            public void clickButton(int position) {
                updateUI(position);

            }
        });
        mGridView.setAdapter(adapter);
        return dialog;
    }

    private void updateUI(int position){
        switch (mList.get(position).getType()){
            case 0:
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getType()==2){
                        mList.get(i).setType(0);
                    }
                }
                mList.get(position).setType(2);
                adapter.notifyDataSetChanged();
                break;
            case 1:

                break;
            case 2:

                break;
        }
    }
}
