package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.constant.Constants;

/**
 * 装舱建议列表数据适配器
 */
public class LoadPlaneInstallAdapter extends BaseQuickAdapter <LoadingListBean.DataBean.ContentObjectBean.ScooterBean, BaseViewHolder> {

    private OnBerthChoseListener onBerthChoseListener;
    private OnGoodsPosChoseListener onGoodsPosChoseListener;
    private OnLockClickListener onLockClickListener;
    private UnloadPlaneAdapter.OnDataCheckListener onDataCheckListener;
    private int mWidthairflag;

    private boolean notShowPull;

    public LoadPlaneInstallAdapter(@Nullable List <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> list, int widthairflag) {
        super(R.layout.item_load_plane_install, list);
        this.mWidthairflag = widthairflag;
    }

    public LoadPlaneInstallAdapter(@Nullable List <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> list, int widthairflag, boolean notShowPull) {
        super(R.layout.item_load_plane_install, list);
        this.mWidthairflag = widthairflag;
        this.notShowPull = notShowPull;
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadingListBean.DataBean.ContentObjectBean.ScooterBean item) {
        Spinner spBerth = helper.getView(R.id.sp_berth);
        Spinner spGoodsPos = helper.getView(R.id.sp_goods_position);


        ((ImageView) helper.getView(R.id.iv_lock_status)).setImageResource(R.mipmap.icon_unlock_data);
        helper.getView(R.id.iv_lock_status).setOnClickListener(v -> {
            if (item.isLocked()) {
                ((ImageView) helper.getView(R.id.iv_lock_status)).setImageResource(R.mipmap.icon_unlock_data);
            } else {
                ((ImageView) helper.getView(R.id.iv_lock_status)).setImageResource(R.mipmap.icon_lock_data);
            }
            item.setLocked(!item.isLocked());
            onLockClickListener.onLockClicked(helper.getAdapterPosition());
        });

        if (1 == mWidthairflag) { //窄体机
            helper.getView(R.id.tv_model).setVisibility(View.GONE);
            helper.getView(R.id.tv_volume).setVisibility(View.GONE);

            helper.setText(R.id.tv_scooter_number, item.getScooterCode() != null ? item.getScooterCode() : "--")
                    .setText(R.id.tv_weight, item.getWeight() != 0 ? item.getWeight() + "" : "--");
            if (item.getWaybillList() != null && item.getWaybillList().size() > 0)
                helper.setText(R.id.tv_mailtype, item.getWaybillList().get(0).getWaybillCode().contains("xxx") ? "X" : item.getType() != null ? item.getType() : "--");
            else
                helper.setText(R.id.tv_mailtype, item.getType() != null ? item.getType() : "--");
            Button btnPull = helper.getView(R.id.tv_pull);
            if (notShowPull) {
                btnPull.setVisibility(View.VISIBLE);
                //设置 拉下的状态
                btnPull.setOnClickListener(v -> {
                    if (item.getExceptionFlag() == 1) {
                        item.setExceptionFlag(0);
                        btnPull.setTextColor(mContext.getResources().getColor(R.color.black_3));
                    } else {
                        item.setExceptionFlag(1);
                        btnPull.setTextColor(mContext.getResources().getColor(R.color.red));
                    }
                });
                if (item.getExceptionFlag() == 1) {
                    btnPull.setTextColor(mContext.getResources().getColor(R.color.red));
                } else {
                    btnPull.setTextColor(mContext.getResources().getColor(R.color.black_3));
                }
            } else {
                btnPull.setVisibility(View.GONE);
            }


            TextView tv1 = helper.getView(R.id.tv_scooter_number);
            TextView tv2 = helper.getView(R.id.tv_total);
            TextView tv3 = helper.getView(R.id.tv_weight);
            TextView tv4 = helper.getView(R.id.tv_volume);
            TextView tv5 = helper.getView(R.id.tv_suggestRepository);
            TextView tv6 = helper.getView(R.id.tv_specialCode);
            TextView tv7 = helper.getView(R.id.tv_mailtype);
//            TextView tv7 = helper.getView(R.id.tv_volume);
            TextView[] tvList = {tv1, tv2, tv3, tv4, tv5, tv6, tv7};
            if (item.getSpecialCode() != null && item.getSpecialCode().equals("AVI")) {//活体颜色标注
                helper.itemView.setBackgroundColor(Color.parseColor("#c68a9e"));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#000000"));
                }
            } else if (item.getSpecialCode() != null && item.getSpecialCode().equals(Constants.DANGER)) {//枪支
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.orangered));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#000000"));
                }
            } else if (item.getWaybillList().get(0).getCargoCn() != null && item.getWaybillList().get(0).getCargoCn().equals(Constants.YCS)) {//压舱沙
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#000000"));
                }
            } else {
                helper.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#000000"));
                }
            }
        } else if (0 == mWidthairflag) {//宽体机
            helper.getView(R.id.tv_suggestRepository).setVisibility(View.GONE);
            helper.getView(R.id.tv_model).setVisibility(View.VISIBLE);
            TextView tv1 = helper.getView(R.id.tv_model);
            TextView tv2 = helper.getView(R.id.tv_total);
            TextView tv3 = helper.getView(R.id.tv_weight);
            TextView tv4 = helper.getView(R.id.tv_volume);
            TextView tv5 = helper.getView(R.id.tv_specialCode);
            TextView tv6 = helper.getView(R.id.tv_mailtype);
            TextView tv7 = helper.getView(R.id.tv_scooter_number);
            Button btnPull = helper.getView(R.id.tv_pull);
            if (notShowPull) {
                btnPull.setVisibility(View.VISIBLE);
                //设置 拉下的状态
                btnPull.setOnClickListener(v -> {
                    if (item.getExceptionFlag() == 1) {
                        item.setExceptionFlag(0);
                        btnPull.setTextColor(mContext.getResources().getColor(R.color.black_3));
                    } else {
                        item.setExceptionFlag(1);
                        btnPull.setTextColor(mContext.getResources().getColor(R.color.red));
                    }
                });
                if (item.getExceptionFlag() == 1) {
                    btnPull.setTextColor(mContext.getResources().getColor(R.color.red));
                } else {
                    btnPull.setTextColor(mContext.getResources().getColor(R.color.black_3));
                }
            } else {
                btnPull.setVisibility(View.GONE);
            }

            TextView[] tvList = {tv1, tv2, tv3, tv4, tv5, tv6, tv7};
            helper.setText(R.id.tv_model, item.getSerialInd() != null ? item.getSerialInd() : "--")
                    .setText(R.id.tv_scooter_number, item.getScooterCode() != null ? item.getScooterCode()  : "--")
                    .setText(R.id.tv_total, item.getTotal() != 0 ? item.getTotal() + "" : "--")
                    .setText(R.id.tv_weight, item.getWeight() != 0 ? item.getWeight() + "" : "--")
                    .setText(R.id.tv_specialCode, item.getSpecialCode() != null ? item.getSpecialCode() : "--")
                    .setText(R.id.tv_mailtype, item.getType() != null ? item.getType() : "--");
            if (item.getSpecialCode() != null && item.getSpecialCode().equals("AVI")) {//活体颜色标注
                helper.itemView.setBackgroundColor(Color.parseColor("#c68a9e"));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#000000"));
                }
            } else if (item.getSpecialCode() != null && item.getSpecialCode().equals(Constants.DANGER)) {//枪支
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#000000"));
                }
            } else if (item.getWaybillList().get(0).getCargoCn() != null && item.getWaybillList().get(0).getCargoCn().equals(Constants.YCS)) {//压舱沙
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#000000"));
                }
            } else {
                helper.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#000000"));
                }
            }
        }
    }

    public interface OnBerthChoseListener {
        void onBerthChosed(int itemPos, String berth);
    }

    public interface OnGoodsPosChoseListener {
        void onGoodsPosChosed(int itemPos, String goodsPos);
    }

    public interface OnLockClickListener {
        void onLockClicked(int itemPos);
    }

    public void setOnBerthChoseListener(OnBerthChoseListener onBerthChoseListener) {
        this.onBerthChoseListener = onBerthChoseListener;
    }

    public void setOnGoodsPosChoseListener(OnGoodsPosChoseListener onGoodsPosChoseListener) {
        this.onGoodsPosChoseListener = onGoodsPosChoseListener;
    }

    public void setOnLockClickListener(OnLockClickListener onLockClickListener) {
        this.onLockClickListener = onLockClickListener;
    }
}
