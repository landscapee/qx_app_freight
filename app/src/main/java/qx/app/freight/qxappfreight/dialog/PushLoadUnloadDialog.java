package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;

/**
 * 装卸机推送弹窗
 */
public class PushLoadUnloadDialog extends Dialog {
    private List<LoadAndUnloadTodoBean> list;
    private Context context;
    private View convertView;
    public PushLoadUnloadDialog(Context context) {
        super(context);
    }

    public PushLoadUnloadDialog(Context context, List<LoadAndUnloadTodoBean> list, Context context1) {
        super(context);
        this.list = list;
        this.context = context1;
        Objects.requireNonNull(getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        convertView = getLayoutInflater().inflate(R.layout.dialog_load_unload, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(convertView);
        ButterKnife.bind(this,convertView);
        initViews();
    }

    private void initViews() {
        RecyclerView rvData=convertView.findViewById(R.id.rv_load_unload_list);
        TextView tvAccept=convertView.findViewById(R.id.tv_accept);

    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Objects.requireNonNull(getWindow()).setGravity(Gravity.BOTTOM); //显示在顶部
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
        getWindow().setAttributes(p);
    }
    private class DialogLoadUnloadPushAdapter extends BaseQuickAdapter<LoadAndUnloadTodoBean,BaseViewHolder> {
        public DialogLoadUnloadPushAdapter(@Nullable List<LoadAndUnloadTodoBean> data) {
            super(data);
        }

        @Override
        protected void convert(BaseViewHolder helper, LoadAndUnloadTodoBean item) {

        }
    }
}
