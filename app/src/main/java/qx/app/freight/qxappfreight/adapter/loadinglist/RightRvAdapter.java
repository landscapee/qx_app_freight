package qx.app.freight.qxappfreight.adapter.loadinglist;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.OnBoardBillsAdapter;
import qx.app.freight.qxappfreight.adapter.UnloadPlaneAdapter;
import qx.app.freight.qxappfreight.bean.loadinglist.ScrollEntity;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.utils.doubleClickUtil.ClickFilter;

/**
 * 装机单位置右边的适配器
 */
public class RightRvAdapter extends BaseQuickAdapter<ScrollEntity, BaseViewHolder> {
    private OnPullCheckListener onPullCheckListener;
    private UnloadPlaneAdapter.OnDataCheckListener onDataCheckListener;

    public RightRvAdapter(@Nullable List<ScrollEntity> data) {
        super(R.layout.item_loading_list_right, data);
    }
    public RightRvAdapter(@Nullable List<ScrollEntity> data,UnloadPlaneAdapter.OnDataCheckListener onDataCheckListener) {
        this(data);
        this.onDataCheckListener=onDataCheckListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, ScrollEntity item) {
        TextView tvBoardNumber = helper.getView(R.id.tv_board_number);
        TextView tvUldNumber = helper.getView(R.id.tv_uld_number);
        TextView tvTarget = helper.getView(R.id.tv_target_name);
        TextView tvWeight = helper.getView(R.id.tv_weight);
        TextView tvType = helper.getView(R.id.tv_type);
        TextView[] tvList = {tvBoardNumber, tvUldNumber, tvTarget, tvType, tvWeight};
        TextView tvPullDownTitle = helper.getView(R.id.tv_pull_title);
        TextView tvPullDown = helper.getView(R.id.tv_pull);
        helper.setText(R.id.tv_board_number, StringUtil.toText(String.valueOf(item.getBoardNumber()))).setText(R.id.tv_uld_number, StringUtil.toText(item.getUldNumber()))
                .setText(R.id.tv_target_name, StringUtil.toText(item.getTarget())).setText(R.id.tv_weight, StringUtil.toText(String.valueOf(item.getWeight()))).setText(R.id.tv_type, StringUtil.toText(item.getType()));
        if (helper.getAdapterPosition() == 0) {//title栏显示拉下文字，
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#e5e5e5"));
                tv.setBackgroundColor(Color.parseColor("#7b8a9b"));
            }
            tvPullDownTitle.setVisibility(View.VISIBLE);
            tvPullDownTitle.setBackgroundColor(Color.parseColor("#7b8a9b"));
            tvPullDownTitle.setTextColor(Color.parseColor("#e5e5e5"));
            tvPullDown.setVisibility(View.GONE);
        } else {
            RecyclerView rvBill=helper.getView(R.id.rv_all_bill);
            List<LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean> data=item.getData();
            if (data!=null&&data.size()!=0){
                LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean title = new LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean();
                title.setTitle(true);
                data.add(0,title);
                rvBill.setLayoutManager(new LinearLayoutManager(mContext));
                rvBill.setAdapter(new OnBoardBillsAdapter(data));
            }
            if (item.isHasLiveGoods()){//有活体运单时背景设为桃红色
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.red_avi));
                for (TextView tv : tvList) {
                    tv.setTextColor(mContext.getResources().getColor(R.color.white));
                }
            }
            else if ((item.isHasGUNGoods())){
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                for (TextView tv : tvList) {
                    tv.setTextColor(mContext.getResources().getColor(R.color.white));
                }
            }
            else{
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                for (TextView tv : tvList) {
                    tv.setTextColor(mContext.getResources().getColor(R.color.text_666));
                }
            }


            tvPullDownTitle.setVisibility(View.GONE);
            if ("行李".equals(item.getType())){
                tvPullDown.setVisibility(View.INVISIBLE);
            }else {
                tvPullDown.setVisibility(View.VISIBLE);
            }
//            for (TextView tv : tvList) {
//                tv.setTextColor(mContext.getResources().getColor(R.color.text_666));
//                tv.setBackgroundColor(mContext.getResources().getColor(R.color.login_txt));
//            }
            tvPullDown.setOnClickListener(v -> {
                if (!Tools.isFastClick())
                    return;
                if (!item.isLocked()) {
                    if (item.isPull()) {
                        item.setPull(false);
                        tvPullDown.setTextColor(mContext.getResources().getColor(R.color.gray_888));
                    } else {
                        item.setPull(true);
                        tvPullDown.setTextColor(mContext.getResources().getColor(R.color.blue_2e8));
                    }
                    onPullCheckListener.onPullChecked(helper.getAdapterPosition(), item.isPull());
                    onDataCheckListener.onDataChecked(item.getScooterId());
                }else {
                    ToastUtil.showToast("数据已锁定修改，请检查");
                }
            });
        }
    }
    public interface OnPullCheckListener{
        void onPullChecked(int pos,boolean checked);
    }

    public void setOnPullCheckListener(OnPullCheckListener onPullCheckListener) {
        this.onPullCheckListener = onPullCheckListener;
    }
}
