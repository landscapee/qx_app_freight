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
import java.util.Iterator;
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
    private OnDataCheckListener onDataCheckListener;

    public UnloadPlaneAdapter(@Nullable List<LoadingListBean.DataBean> data) {
        super(R.layout.item_unload_plane_version, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadingListBean.DataBean item) {
        if (helper.getAdapterPosition() == 0) {
            helper.setText(R.id.tv_version_type, "最新版本");
        } else {
            helper.setText(R.id.tv_version_type, "历史版本");
        }
        helper.setText(R.id.tv_version_name, String.format(mContext.getString(R.string.format_version_name), item.getVersion()));
        ImageView ivControl = helper.getView(R.id.iv_control);
        LinearLayout llControl = helper.getView(R.id.ll_controler);
        CollapsableLinearLayout llDetail = helper.getView(R.id.cll_version_detail);
        List<RegularEntity> leftData = new ArrayList<>();
        List<ScrollEntity> rightData = new ArrayList<>();
        List<String> berthList = new ArrayList<>();
        List<String> goodsPosList = new ArrayList<>();
        if (item.getContentObject() != null && item.getContentObject().size() != 0) {
            for (LoadingListBean.DataBean.ContentObjectBean entity : item.getContentObject()) {
                for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean : entity.getScooters()) {
                    RegularEntity left = new RegularEntity();
                    if (!berthList.contains(scooterBean.getCargoName())) {
                        berthList.add(scooterBean.getCargoName());
                    }
                    left.setBerth(scooterBean.getCargoName());
                    left.setLocked(false);
                    left.setScooterId(scooterBean.getId());
                    left.setMoveSize((scooterBean.getWaybillList() == null) ? 0 : scooterBean.getWaybillList().size());
                    boolean hasLiveGoods = false;
                    for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean bill : scooterBean.getWaybillList()) {
                        if (bill.getSpecialCode().equals("AVI")) {
                            hasLiveGoods = true;
                            break;
                        }
                    }
                    for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean bill : scooterBean.getWaybillList()) {
                        bill.setHasLiveGoods(bill.getSpecialCode().equals("AVI"));//运单判断是否有活体
                    }
                    boolean shouldShowGoodsPos = !TextUtils.isEmpty(scooterBean.getLocation());
                    if (!goodsPosList.contains(scooterBean.getLocation()) && !TextUtils.isEmpty(scooterBean.getLocation())) {
                        goodsPosList.add(scooterBean.getLocation());
                    }
                    left.setHasLiveGoods(hasLiveGoods);
                    if (shouldShowGoodsPos) {
                        left.setGoodsPosition(scooterBean.getLocation());
                    }
                    leftData.add(left);
                    ScrollEntity right = new ScrollEntity();
                    right.setBoardNumber(scooterBean.getScooterCode());
                    right.setUldNumber(scooterBean.getSerialInd());
                    right.setTarget(scooterBean.getDestinationStation());
                    right.setScooterId(scooterBean.getId());
                    right.setHasLiveGoods(hasLiveGoods);
                    String type = "行李";
                    if ("M".equals(scooterBean.getType())) {
                        type = "邮件";
                    } else if ("C".equals(scooterBean.getType()) || "CT".equals(scooterBean.getType())) {
                        type = "货物";
                    }
                    right.setType(type);
                    right.setWeight(String.valueOf(scooterBean.getWeight()));
                    right.setData(scooterBean.getWaybillList());
                    rightData.add(right);
                }
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
        List<LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean> titleBills = new ArrayList<>();
        titleRight.setData(titleBills);
        rightData.add(0, titleRight);
        for (RegularEntity left : leftData) {
            left.setBerthList(berthList);
            left.setGoodsPosList(goodsPosList);
        }
        RecyclerView rvLeft = helper.getView(R.id.rv_regular);
        RecyclerView rvRight = helper.getView(R.id.rv_scroll_data);
        LeftRvAdapter leftRvAdapter = new LeftRvAdapter(leftData, onDataCheckListener);
        rvLeft.setLayoutManager(new LinearLayoutManager(mContext));
        rvLeft.setAdapter(leftRvAdapter);
        leftRvAdapter.setOnLockClickListener(itemPos -> {
            for (LoadingListBean.DataBean.ContentObjectBean content : item.getContentObject()) {
                for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooter : content.getScooters()) {
                    if (leftData.get(itemPos).getScooterId().equals(scooter.getId())) {
                        scooter.setLocked(!scooter.isLocked());
                    }
                }
            }
        });
        leftRvAdapter.setOnBerthChoseListener((itemPos, berth) -> {
            LoadingListBean.DataBean.ContentObjectBean.ScooterBean operateScooter = null;
            for (LoadingListBean.DataBean.ContentObjectBean content : item.getContentObject()) {
                Iterator iterator = content.getScooters().iterator();
                while (iterator.hasNext()) {//将原有的舱位上的板车数据删除掉
                    LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooter = (LoadingListBean.DataBean.ContentObjectBean.ScooterBean) iterator.next();
                    if (leftData.get(itemPos).getScooterId().equals(scooter.getId())) {
                        operateScooter = scooter;
                        iterator.remove();
                        break;
                    }
                }
            }
            for (LoadingListBean.DataBean.ContentObjectBean content : item.getContentObject()) {//遍历寻找选择的舱位，将该板车加到对应舱位上
                if (content.getCargoName().equals(berth)) {
                    content.getScooters().add(operateScooter);
                    break;
                }
            }
        });
        leftRvAdapter.setOnGoodsPosChoseListener((itemPos, goodsPos) -> {
            for (LoadingListBean.DataBean.ContentObjectBean content : item.getContentObject()) {
                for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooter : content.getScooters()) {
                    if (leftData.get(itemPos).getScooterId().equals(scooter.getId())) {
                        scooter.setLocation(goodsPos);
                    }
                }
            }
        });
        RightRvAdapter rightRvAdapter = new RightRvAdapter(rightData, onDataCheckListener);
        rvRight.setLayoutManager(new LinearLayoutManager(mContext));
        rvRight.setAdapter(rightRvAdapter);
        rightRvAdapter.setOnPullCheckListener((pos, checked) -> {
            for (LoadingListBean.DataBean.ContentObjectBean content : item.getContentObject()) {
                for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooter : content.getScooters()) {
                    if (rightData.get(pos).getScooterId().equals(scooter.getId())) {
                        scooter.setExceptionFlag((checked) ? 1 : 0);
                    }
                }
            }
        });
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

    public interface OnDataCheckListener {
        void onDataChecked();
    }

    public void setOnDataCheckListener(OnDataCheckListener onDataCheckListener) {
        this.onDataCheckListener = onDataCheckListener;
    }
}
