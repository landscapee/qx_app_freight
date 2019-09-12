package qx.app.freight.qxappfreight.adapter.loadinglist;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.UnloadPlaneAdapter;
import qx.app.freight.qxappfreight.bean.loadinglist.RegularEntity;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * 装机单位置左边的适配器
 */
public class LeftRvAdapter extends BaseQuickAdapter<RegularEntity, BaseViewHolder> {
    private OnBerthChoseListener onBerthChoseListener;
    private OnGoodsPosChoseListener onGoodsPosChoseListener;
    private OnLockClickListener onLockClickListener;
    private UnloadPlaneAdapter.OnDataCheckListener onDataCheckListener;

    public LeftRvAdapter(@Nullable List<RegularEntity> data) {
        super(R.layout.item_loading_list_left, data);
    }
    public LeftRvAdapter(@Nullable List<RegularEntity> data,UnloadPlaneAdapter.OnDataCheckListener onDataCheckListener) {
        this(data);
        this.onDataCheckListener=onDataCheckListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, RegularEntity item) {
        Spinner spBerth = helper.getView(R.id.sp_berth);
        Spinner spGoodsPos = helper.getView(R.id.sp_goods_position);
        LinearLayout view = helper.getView(R.id.ll_container);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int size = (item.getMoveSize() == 0) ? 0 : (item.getMoveSize() + 1);
        layoutParams.bottomMargin = view.getBottom() + 80 * size;
        view.setLayoutParams(layoutParams);
        if (helper.getAdapterPosition() == 0) {//第一条数据是title，下拉view都隐藏
            helper.getView(R.id.tv_lock).setVisibility(View.VISIBLE);
            helper.getView(R.id.iv_lock_status).setVisibility(View.GONE);
            helper.getView(R.id.tv_berth).setVisibility(View.VISIBLE);
            ((TextView) helper.getView(R.id.tv_lock)).setTextColor(Color.parseColor("#e5e5e5"));
            helper.getView(R.id.tv_lock).setBackgroundColor(Color.parseColor("#7b8a9b"));
            ((TextView) helper.getView(R.id.tv_berth)).setTextColor(Color.parseColor("#e5e5e5"));
            helper.getView(R.id.tv_berth).setBackgroundColor(Color.parseColor("#7b8a9b"));
            ((TextView) helper.getView(R.id.tv_goods_position)).setTextColor(Color.parseColor("#e5e5e5"));
            helper.getView(R.id.tv_goods_position).setBackgroundColor(Color.parseColor("#7b8a9b"));
            if (TextUtils.isEmpty(item.getGoodsPosition())) {
                helper.getView(R.id.tv_goods_position).setVisibility(View.GONE);
            } else {
                helper.getView(R.id.tv_goods_position).setVisibility(View.VISIBLE);
            }
            spBerth.setVisibility(View.GONE);
            spGoodsPos.setVisibility(View.GONE);
            helper.setText(R.id.tv_berth, StringUtil.toText(item.getBerth()));
            helper.setText(R.id.tv_goods_position, item.getGoodsPosition());
        } else {//真实数据
            if (item.isHasLiveGoods()) {//有活体运单时背景设为桃红色
                helper.itemView.setBackgroundColor(Color.parseColor("#ee3f8e"));
            }
            else if ((item.isHasGUNGoods())){
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.red));
            }
            else
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white));

            helper.getView(R.id.iv_lock_status).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_lock).setVisibility(View.GONE);
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
            helper.getView(R.id.tv_berth).setVisibility(View.GONE);
            helper.getView(R.id.tv_goods_position).setVisibility(View.GONE);
            spBerth.setVisibility(View.VISIBLE);
//            if (item.isShowPull()) {
//                spinnerAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_red, item.getBerthList());
//            } else {
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_normal, item.getBerthList());
//            }
            spBerth.setAdapter(spinnerAdapter);
            SpinnerAdapter apsAdapter1 = spBerth.getAdapter(); //得到SpinnerAdapter对象
            int k = apsAdapter1.getCount();
            for (int i = 0; i < k; i++) {
                if (item.getBerth().equals(apsAdapter1.getItem(i).toString())) {
                    spBerth.setSelection(i, true);// 默认选中项
                    break;
                }
            }
            spBerth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int pos;

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (item.isLocked()) {
                        ToastUtil.showToast("数据已锁定，无法修改");
                        spBerth.setSelection(pos, true);
                    } else {
                        pos = position;
                        onBerthChoseListener.onBerthChosed(helper.getAdapterPosition(), item.getBerthList().get(position));
                        onDataCheckListener.onDataChecked();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (TextUtils.isEmpty(item.getGoodsPosition())) {//有货位数据
                spGoodsPos.setVisibility(View.GONE);
            } else {
                spGoodsPos.setVisibility(View.VISIBLE);
//                if (item.isShowPull()) {
//                    spGoodsAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_red, item.getGoodsPosList());
//                } else {
                ArrayAdapter<String> spGoodsAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_normal, item.getGoodsPosList());
                spGoodsPos.setAdapter(spGoodsAdapter);
                SpinnerAdapter apsAdapter2 = spGoodsPos.getAdapter(); //得到SpinnerAdapter对象
                int j = apsAdapter2.getCount();
                for (int i = 0; i < j; i++) {
                    if (item.getGoodsPosition().equals(apsAdapter2.getItem(i).toString())) {
                        spGoodsPos.setSelection(i, true);// 默认选中项
                        break;
                    }
                }
                spGoodsPos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    int pos;

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (item.isLocked()) {
                            ToastUtil.showToast("数据已锁定，无法修改");
                            spBerth.setSelection(pos, true);
                        } else {
                            pos = position;
                            onGoodsPosChoseListener.onGoodsPosChosed(helper.getAdapterPosition(), item.getGoodsPosList().get(position));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
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
