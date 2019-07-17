package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.loadinglist.LeftRvAdapter;
import qx.app.freight.qxappfreight.adapter.loadinglist.RightRvAdapter;
import qx.app.freight.qxappfreight.bean.loadinglist.RegularEntity;
import qx.app.freight.qxappfreight.bean.loadinglist.ScrollEntity;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.widget.CollapsableLinearLayout;

/**
 * 装机版本列表适配器
 */
public class UnloadPlaneAdapter extends BaseQuickAdapter<LoadingListBean.DataBean, BaseViewHolder> {
    private OnOverLoadListener onOverLoadListener;

    public UnloadPlaneAdapter(@Nullable List<LoadingListBean.DataBean> data) {
        super(R.layout.item_unload_plane_version, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadingListBean.DataBean item) {
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
        LinearLayout llControl = helper.getView(R.id.ll_controler);
        CollapsableLinearLayout llDetail = helper.getView(R.id.cll_version_detail);
        List<RegularEntity> leftData = new ArrayList<>();
        List<ScrollEntity> rightData = new ArrayList<>();
        boolean shouldShowGoodsPos;
        List<String> berthList = new ArrayList<>();
        List<String> goodsPosList = new ArrayList<>();
        if (item.getContentObject() != null && item.getContentObject().size() != 0) {
            for (LoadingListBean.DataBean.ContentObjectBean entity : item.getContentObject()) {
                RegularEntity left = new RegularEntity();
                if (!berthList.contains(entity.getPos())) {
                    berthList.add(entity.getPos());
                }
                entity.setStartBerth(entity.getPos());
                left.setBerth(entity.getPos());
                left.setLocked(false);
                left.setShowPull(entity.isShowPullDown());
                shouldShowGoodsPos = !TextUtils.isEmpty(entity.getLocation());
                if (!goodsPosList.contains(entity.getLocation()) && !TextUtils.isEmpty(entity.getLocation())) {
                    goodsPosList.add(entity.getLocation());
                }
                if (shouldShowGoodsPos) {
                    left.setGoodsPosition(entity.getLocation());
                }
                leftData.add(left);
                ScrollEntity right = new ScrollEntity();
                right.setBoardNumber(entity.getTailer());
                right.setUldNumber(entity.getUldNumber());
                right.setTarget(entity.getDest());
                String type="邮件";
                if ("C".equals(entity.getType())){
                    type="货物";
                }else if ("BY".equals(entity.getType())){
                    type="行李";
                }
                right.setType(type);
                right.setWeight(String.valueOf(entity.getActWgt()));
                right.setNumber(String.valueOf(entity.getNumber()));
                right.setShowPull(entity.isShowPullDown());
                rightData.add(right);
            }
        }
        RegularEntity titleLeft = new RegularEntity();
        titleLeft.setLockTitle("锁定");
        titleLeft.setBerth("舱位");
        boolean showGoodPosTitle = false;//只要有一条数据有货位信息就应该显示货位那一列数据
        for (RegularEntity entity : leftData) {
            if (!TextUtils.isEmpty(entity.getGoodsPosition())) {
                showGoodPosTitle = true;
                break;
            }
        }
        if (showGoodPosTitle) {
            titleLeft.setGoodsPosition("货位");
        }
        leftData.add(0, titleLeft);
        ScrollEntity titleRight = new ScrollEntity();
        titleRight.setBoardNumber("板号");
        titleRight.setUldNumber("ULD号");
        titleRight.setTarget("目的地");
        titleRight.setType("类别");
        titleRight.setWeight("重量");
//        titleRight.setNumber("件数");
        rightData.add(0, titleRight);
        for (RegularEntity left : leftData) {
            left.setBerthList(berthList);
            left.setGoodsPosList(goodsPosList);
        }
        RecyclerView rvLeft = helper.getView(R.id.rv_regular);
        RecyclerView rvRight = helper.getView(R.id.rv_scroll_data);
        LeftRvAdapter leftRvAdapter = new LeftRvAdapter(leftData);
        rvLeft.setLayoutManager(new LinearLayoutManager(mContext));
        rvLeft.setAdapter(leftRvAdapter);
        leftRvAdapter.setOnLockClickListener(itemPos -> item.getContentObject().get(itemPos - 1).setLocked(true));
        leftRvAdapter.setOnBerthChoseListener((itemPos, berth) -> {
            if (berth.equals(item.getContentObject().get(itemPos - 1).getStartBerth())) {
                item.getContentObject().get(itemPos - 1).setCargoStatus(0);
            } else {
                item.getContentObject().get(itemPos - 1).setCargoStatus(1);
            }
            item.getContentObject().get(itemPos - 1).setPos(berth);
        });
        leftRvAdapter.setOnGoodsPosChoseListener((itemPos, goodsPos) -> item.getContentObject().get(itemPos - 1).setGoodsPosition(goodsPos));
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
        llControl.setOnClickListener(v -> {
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

    public interface OnOverLoadListener {
        void onOverLoad(LoadingListBean.DataBean entity);
    }
}
