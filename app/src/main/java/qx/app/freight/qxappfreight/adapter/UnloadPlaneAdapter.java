package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.loadinglist.LeftRvAdapter;
import qx.app.freight.qxappfreight.adapter.loadinglist.RightRvAdapter;
import qx.app.freight.qxappfreight.bean.UnloadPlaneEntity;
import qx.app.freight.qxappfreight.bean.UnloadPlaneVersionEntity;
import qx.app.freight.qxappfreight.bean.loadinglist.RegularEntity;
import qx.app.freight.qxappfreight.bean.loadinglist.ScrollEntity;
import qx.app.freight.qxappfreight.widget.CollapsableLinearLayout;

/**
 * 装机版本列表适配器
 */
public class UnloadPlaneAdapter extends BaseQuickAdapter<UnloadPlaneVersionEntity, BaseViewHolder> {
    private OnOverLoadListener onOverLoadListener;
    public UnloadPlaneAdapter(@Nullable List<UnloadPlaneVersionEntity> data) {
        super(R.layout.item_unload_plane_version, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UnloadPlaneVersionEntity item) {
        if (helper.getAdapterPosition() == 0) {
            helper.setText(R.id.tv_version_type, "最新版本");
            helper.getView(R.id.tv_send_over).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_send_over).setOnClickListener(v -> onOverLoadListener.onOverLoad(item));
        } else {
            helper.setText(R.id.tv_version_type, "历史版本");
            helper.getView(R.id.tv_send_over).setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_version_name, String.format(mContext.getString(R.string.format_version_name), item.getVersion()));
        ImageView ivControl = helper.getView(R.id.iv_control);
        CollapsableLinearLayout llDetail = helper.getView(R.id.cll_version_detail);
        List<RegularEntity> leftData = new ArrayList<>();
        List<ScrollEntity> rightData = new ArrayList<>();
        boolean shouldShowGoodsPos = false;
        for (UnloadPlaneEntity entity : item.getList()) {
            RegularEntity left = new RegularEntity();
            left.setBerth(entity.getBerth());
            left.setShowPull(entity.isShowPullDown());
            shouldShowGoodsPos = !TextUtils.isEmpty(entity.getGoodsPosition());
            if (shouldShowGoodsPos) {
                left.setGoodsPosition(entity.getGoodsPosition());
            }
            leftData.add(left);
            ScrollEntity right = new ScrollEntity();
            right.setBoardNumber(entity.getBoardNumber());
            right.setUldNumber(entity.getUldNumber());
            right.setTarget(entity.getTarget());
            right.setType(entity.getType());
            right.setWeight(String.valueOf(entity.getWeight()));
            right.setNumber(String.valueOf(entity.getNumber()));
            right.setShowPull(entity.isShowPullDown());
            rightData.add(right);
        }
        RegularEntity titleLeft = new RegularEntity();
        titleLeft.setBerth("舱位");
        if (shouldShowGoodsPos) {
            titleLeft.setGoodsPosition("货位");
        }
        leftData.add(0, titleLeft);
        ScrollEntity titleRight = new ScrollEntity();
        titleRight.setBoardNumber("板号");
        titleRight.setUldNumber("ULD号");
        titleRight.setTarget("目的地");
        titleRight.setType("类型");
        titleRight.setWeight("重量");
        titleRight.setNumber("件数");
        rightData.add(0, titleRight);
        RecyclerView rvLeft = helper.getView(R.id.rv_regular);
        RecyclerView rvRight = helper.getView(R.id.rv_scroll_data);
        LeftRvAdapter leftRvAdapter = new LeftRvAdapter(leftData);
        rvLeft.setLayoutManager(new LinearLayoutManager(mContext));
        rvLeft.setAdapter(leftRvAdapter);
        RightRvAdapter rightRvAdapter = new RightRvAdapter(rightData);
        rvRight.setLayoutManager(new LinearLayoutManager(mContext));
        rvRight.setAdapter(rightRvAdapter);
        if (item.isShowDetail()) {
            rvLeft.setVisibility(View.VISIBLE);
            rvRight.setVisibility(View.VISIBLE);
            ivControl.setImageResource(R.mipmap.right);
            llDetail.expand();
        } else {
            rvLeft.setVisibility(View.GONE);
            rvRight.setVisibility(View.GONE);
            ivControl.setImageResource(R.mipmap.down);
            llDetail.collapse();
        }
        ivControl.setOnClickListener(v -> {
            item.setShowDetail(!item.isShowDetail());
            if (item.isShowDetail()) {
                rvLeft.setVisibility(View.VISIBLE);
                rvRight.setVisibility(View.VISIBLE);
                ivControl.setImageResource(R.mipmap.right);
                llDetail.expand();
            } else {
                rvLeft.setVisibility(View.GONE);
                rvRight.setVisibility(View.GONE);
                ivControl.setImageResource(R.mipmap.down);
                llDetail.collapse();
            }
        });
    }

    public void setOnOverLoadListener(OnOverLoadListener onOverLoadListener) {
        this.onOverLoadListener = onOverLoadListener;
    }

    public interface OnOverLoadListener{
        void onOverLoad(UnloadPlaneVersionEntity entity);
    }
}
