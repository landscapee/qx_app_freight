package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.PageListBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class MessageAdapter extends BaseQuickAdapter<PageListBean.RecordsBean, BaseViewHolder>  {

    private OnDeleteClick mOnDeleteClick;

    public MessageAdapter(@Nullable List<PageListBean.RecordsBean> data) {
        super(R.layout.item_receive_good, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PageListBean.RecordsBean item) {
        helper.setText(R.id.tv_title,item.getContent())
                .setText(R.id.tv_name,item.getCreateUser())
                .setText(R.id.tv_time, TimeUtils.date2Tasktime6(item.getCreateDate()));
    }

    public void setOnDeleteClickListener(OnDeleteClick mOnDeleteClick) {
        this.mOnDeleteClick = mOnDeleteClick;
    }

    public interface OnDeleteClick {
        void onDeleteClick(View view, int position);
    }

}
