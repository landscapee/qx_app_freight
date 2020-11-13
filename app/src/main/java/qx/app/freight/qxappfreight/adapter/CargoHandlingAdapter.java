package qx.app.freight.qxappfreight.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.FtRuntimeFlightScooter;
import qx.app.freight.qxappfreight.utils.MapValue;

public class CargoHandlingAdapter extends BaseQuickAdapter <FtRuntimeFlightScooter, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;
    private OnSpinnerClickLister mSpinnerClickLister;
    private OnLockClickLister mLockClickListener;

    public CargoHandlingAdapter(List <FtRuntimeFlightScooter> data) {
        super(R.layout.item_cargo_handling, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FtRuntimeFlightScooter item) {

        //删除按钮
        View viewDelete = helper.getView(R.id.rl_delete);
        viewDelete.setTag(helper.getAdapterPosition());
        if (!viewDelete.hasOnClickListeners()) {
            viewDelete.setOnClickListener(v -> {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                }
            });
        }
        //锁定按钮
        ImageView imgLock = helper.getView(R.id.iv_lock);
        ImageView imgComplete = helper.getView(R.id.iv_complete);

        if (item.isLock()) {
            imgComplete.setVisibility(View.VISIBLE);
            imgLock.setImageResource(R.mipmap.lock);
        } else {
            imgComplete.setVisibility(View.GONE);
            imgLock.setImageResource(R.mipmap.unlock);
        }
        View viewLock = helper.getView(R.id.rl_lock);
        viewLock.setTag(helper.getAdapterPosition());
        if (!viewLock.hasOnClickListeners()) {
            viewLock.setOnClickListener(v -> {
                if (mLockClickListener != null) {
                    mLockClickListener.mLockClickListener(v, (Integer) v.getTag());
                }
            });
        }

        View mSpinnerll = helper.getView(R.id.ll_product);
        mSpinnerll.setTag(helper.getAdapterPosition());
        if (!mSpinnerll.hasOnClickListeners()) {
            mSpinnerll.setOnClickListener(v -> {
                if ( mSpinnerClickLister != null) {
                    mSpinnerClickLister.mSpinnerClickListener(v,(Integer) v.getTag());
                }
            });
        }

        helper.setText(R.id.tv_handcar, MapValue.getCarTypeValue(item.getScooterType()+"") +item.getScooterCode());

        helper.setText(R.id.tv_weight,""+item.getWeight());
        helper.setText(R.id.tv_volume,""+item.getVolume());
        helper.setText(R.id.tv_cabin,""+item.getSuggestRepository());
        //库区
        helper.setText(R.id.tv_repName, ""+item.getRepName());

        if (item.getInFlight() == 1){
            helper.setTextColor(R.id.tv_handcar, ContextCompat.getColor(mContext,R.color.red));
            helper.setTextColor(R.id.tv_uld,ContextCompat.getColor(mContext,R.color.red));
            helper.setTextColor(R.id.tv_weight,ContextCompat.getColor(mContext,R.color.red));
            helper.setTextColor(R.id.tv_volume,ContextCompat.getColor(mContext,R.color.red));
            helper.setTextColor(R.id.tv_cabin,ContextCompat.getColor(mContext,R.color.red));
            helper.setTextColor(R.id.tv_repName,ContextCompat.getColor(mContext,R.color.red));
        }
        else if(item.getInFlightCourse() == 0){
            helper.setTextColor(R.id.tv_handcar,ContextCompat.getColor(mContext,R.color.yellow));
            helper.setTextColor(R.id.tv_uld,ContextCompat.getColor(mContext,R.color.yellow));
            helper.setTextColor(R.id.tv_weight,ContextCompat.getColor(mContext,R.color.yellow));
            helper.setTextColor(R.id.tv_volume,ContextCompat.getColor(mContext,R.color.yellow));
            helper.setTextColor(R.id.tv_cabin,ContextCompat.getColor(mContext,R.color.yellow));
            helper.setTextColor(R.id.tv_repName,ContextCompat.getColor(mContext,R.color.yellow));
        }
        else
        {
            helper.setTextColor(R.id.tv_handcar,ContextCompat.getColor(mContext,R.color.text_color));
            helper.setTextColor(R.id.tv_uld,ContextCompat.getColor(mContext,R.color.text_color));
            helper.setTextColor(R.id.tv_weight,ContextCompat.getColor(mContext,R.color.text_color));
            helper.setTextColor(R.id.tv_volume,ContextCompat.getColor(mContext,R.color.text_color));
            helper.setTextColor(R.id.tv_cabin,ContextCompat.getColor(mContext,R.color.text_color));
            helper.setTextColor(R.id.tv_repName,ContextCompat.getColor(mContext,R.color.text_color));
        }
    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }

    public void setOnSpinnerClickLister(OnSpinnerClickLister listener) {
        this.mSpinnerClickLister = listener;

    }

    public interface OnSpinnerClickLister {
        void mSpinnerClickListener(View view, int position);
    }
    public void setOnLockClickListener(OnLockClickLister listener) {
        this.mLockClickListener = listener;
    }

    public interface OnLockClickLister {
        void mLockClickListener(View view, int position);
    }


}
