package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.model.TestBean;

public class ChooseStoreroomAdapter extends BaseQuickAdapter<TestBean, BaseViewHolder> {

    public ChooseStoreroomAdapter( @Nullable List<TestBean> data) {
        super(R.layout.item_choose_storeroom, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestBean item) {
        helper.setText(R.id.tv_name,item.getName());
        if (item.isChoose()){
            helper.setImageResource(R.id.iv_choose,R.mipmap.qualifled);
        }else {
            helper.setImageResource(R.id.iv_choose,R.mipmap.normal);
        }

    }
}
