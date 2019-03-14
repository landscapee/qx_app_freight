package qx.app.freight.qxappfreight.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.CargoHandlingActivity;
import qx.app.freight.qxappfreight.activity.FFMActivity;
import qx.app.freight.qxappfreight.activity.InPortTallyActivity;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * 包含FFM查看按钮的复合型列表适配器
 */
public class TaskManifestAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public static final int TYPE_PEI_ZAI = 1;
    public static final int TYPE_INPORT_TALLY = 2;
    private int mType;

    private TaskManifestAdapter(List<String> list) {
        super(R.layout.item_task_manifest, list);
    }

    public TaskManifestAdapter(List<String> list, int type) {
        this(list);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_flight_number, item).setText(R.id.tv_arrive_time,String.format(mContext.getString(R.string.format_arrive_info),"12:34","09"));
        switch (mType) {
            case TYPE_INPORT_TALLY:
                helper.setText(R.id.tv_type,"进港理货");
                break;
            case TYPE_PEI_ZAI:
                helper.setText(R.id.tv_type,"舱单核查");
                break;
        }
        TextView tvType=helper.getView(R.id.tv_type);
        Button btnFfm=helper.getView(R.id.btn_ffm);
        tvType.setOnClickListener(v -> {
            switch (mType) {
                case TYPE_INPORT_TALLY:
                    mContext.startActivity(new Intent(mContext, InPortTallyActivity.class));
                    break;
                case TYPE_PEI_ZAI:
//                    CargoHandlingActivity.startActivity(mContext);
                    break;
            }
        });
        btnFfm.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, FFMActivity.class));
        });
    }
}
