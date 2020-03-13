package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.PageListBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class MessageAdapter extends BaseQuickAdapter<PageListBean.RecordsBean, BaseViewHolder>  {

//    private OnDeleteClick mOnDeleteClick;

    public MessageAdapter(@Nullable List<PageListBean.RecordsBean> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PageListBean.RecordsBean item) {

        helper.setText(R.id.tv_title,item.getContent())
                .setText(R.id.tv_time, TimeUtils.date2Tasktime6(item.getCreateTime()));

        if (item.getReadingStatus() ==0){
            helper.setVisible(R.id.ll_red_point,true);
        }else {
            helper.setVisible(R.id.ll_red_point,false);
        }

//        helper.getView(R.id.btn_delete).setOnClickListener(v -> mOnDeleteClick.onDeleteClick(v, helper.getAdapterPosition()));
    }
//
//    public void setOnDeleteClickListener(OnDeleteClick mOnDeleteClick) {
//        this.mOnDeleteClick = mOnDeleteClick;
//    }
//
//    public interface OnDeleteClick {
//        void onDeleteClick(View view, int position);
//    }

}
