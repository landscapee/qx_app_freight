package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.UldInfo;

public class ShowUldInfoAdapter extends BaseQuickAdapter <UldInfo, BaseViewHolder> {

    public ShowUldInfoAdapter(@Nullable List <UldInfo> data) {
        super(R.layout.item_show_uld_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UldInfo item) {
        helper.setText(R.id.tv_uld, item.getUldName());
        helper.setText(R.id.tv_uld_weight, item.getUldWeight() + "kg");

    }
}
