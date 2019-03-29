package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.UnloadPlaneEntity;
import qx.app.freight.qxappfreight.bean.UnloadPlaneVersionEntity;
import qx.app.freight.qxappfreight.widget.CollapsableLinearLayout;

/**
 * 装机版本列表适配器
 */
public class UnloadPlaneAdapter extends BaseQuickAdapter<UnloadPlaneVersionEntity, BaseViewHolder> {
    public UnloadPlaneAdapter(@Nullable List<UnloadPlaneVersionEntity> data) {
        super(R.layout.item_unload_plane_version, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UnloadPlaneVersionEntity item) {
        helper.setText(R.id.tv_version_name, String.format(mContext.getString(R.string.format_version_name), item.getVersion()));
        ImageView ivControl = helper.getView(R.id.iv_control);
        CollapsableLinearLayout llDetail = helper.getView(R.id.cll_version_detail);
        RecyclerView rvVersion = helper.getView(R.id.rv_version);
        boolean showGoodsPosition=true;//是否显示货位，默认显示
        for (UnloadPlaneEntity entity:item.getList()){
            if (TextUtils.isEmpty(entity.getGoodsPosition())){
                showGoodsPosition=false;
                break;
            }
        }
        if (showGoodsPosition){
            helper.getView(R.id.tv_goods_pos).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_divider).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.tv_goods_pos).setVisibility(View.GONE);
            helper.getView(R.id.tv_divider).setVisibility(View.GONE);
        }
        UnloadPlaneDetailAdapter adapter = new UnloadPlaneDetailAdapter(item.getList(),showGoodsPosition);
        rvVersion.setLayoutManager(new LinearLayoutManager(mContext));
        rvVersion.setAdapter(adapter);
        if (item.isShowDetail()) {
            rvVersion.setVisibility(View.VISIBLE);
            ivControl.setImageResource(R.mipmap.up);
            llDetail.expand();
        } else {
            rvVersion.setVisibility(View.GONE);
            ivControl.setImageResource(R.mipmap.down);
            llDetail.collapse();
        }
        ivControl.setOnClickListener(v -> {
            item.setShowDetail(!item.isShowDetail());
            if (item.isShowDetail()) {
                rvVersion.setVisibility(View.VISIBLE);
                ivControl.setImageResource(R.mipmap.up);
                llDetail.expand();
            } else {
                rvVersion.setVisibility(View.GONE);
                ivControl.setImageResource(R.mipmap.down);
                llDetail.collapse();
            }
        });
    }
}
