package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.NoticeBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class NoticeAdapter extends BaseQuickAdapter<NoticeBean.RecordsBean, BaseViewHolder> {

    private OnDeleteClick mOnDeleteClick;

    public NoticeAdapter(@Nullable List<NoticeBean.RecordsBean> data) {
        super(R.layout.item_notice, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeBean.RecordsBean item) {

        helper.setText(R.id.tv_title,item.getTitle())
                .setText(R.id.tv_name,"发布单位："+item.getCreateOrg())
                .setText(R.id.tv_time, TimeUtils.date2Tasktime6(item.getCreateDate()));

        if (item.getReadingStatus() ==0){
            helper.setVisible(R.id.ll_red_point,true);
        }else {
            helper.setVisible(R.id.ll_red_point,false);
        }

        helper.getView(R.id.btn_delete).setOnClickListener(v -> mOnDeleteClick.onDeleteClick(v, helper.getAdapterPosition()));
    }

    public void setOnDeleteClickListener(OnDeleteClick mOnDeleteClick) {
        this.mOnDeleteClick = mOnDeleteClick;
    }

    public interface OnDeleteClick {
        void onDeleteClick(View view, int position);
    }

}
