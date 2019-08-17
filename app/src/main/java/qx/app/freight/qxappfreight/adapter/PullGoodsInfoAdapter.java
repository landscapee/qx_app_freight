package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.ScanManagerActivity;
import qx.app.freight.qxappfreight.bean.PullGoodsInfoBean;
import qx.app.freight.qxappfreight.bean.PullGoodsShowInterface;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.dialog.ChosePullReasonDialog;
import qx.app.freight.qxappfreight.dialog.EditBoardsDialog;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.RoundCheckBox;

/**
 * 拉货上报信息列表适配器
 */
public class PullGoodsInfoAdapter<T extends PullGoodsShowInterface> extends BaseMultiItemQuickAdapter<T, BaseViewHolder> {
    private PullGoodsInfoBean.PullWaybillsBean entity;
    private int size;

    public PullGoodsInfoAdapter(List<T> data) {
        super(data);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        addItemType(Constants.TYPE_PULL_BOARD, R.layout.item_pull_goods_board_info);
        addItemType(Constants.TYPE_PULL_BILL, R.layout.item_pull_goods_bill_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        TextView tvInfo = helper.getView(R.id.tv_goods_info);
        TextView tvBoardNumber = helper.getView(R.id.tv_board_number);
        ImageView ivType = helper.getView(R.id.iv_pull_type);
        RoundCheckBox rcbBox = helper.getView(R.id.cb_select);
        switch (item.getItemType()) {
            case Constants.TYPE_PULL_BOARD:
                tvInfo.setText(String.format(mContext.getString(R.string.format_goods_info)
                        , ((PullGoodsInfoBean.PullScootersBean) item).getNumber()
                        , ((PullGoodsInfoBean.PullScootersBean) item).getWeight()
                        , ((PullGoodsInfoBean.PullScootersBean) item).getVolume()));
                tvBoardNumber.setText(((PullGoodsInfoBean.PullScootersBean) item).getScooterCode());
                ivType.setImageResource((((PullGoodsInfoBean.PullScootersBean) item).getCreateUserType() == 0) ? R.mipmap.icon_pei : R.mipmap.icon_jian);//任务发起类型为0，则是配载发起，否则是监装发起
                helper.itemView.setBackgroundColor(((PullGoodsInfoBean.PullScootersBean) item).getStatus() == 0 ? Color.parseColor("#ffffff") : Color.parseColor("#00ff00"));//任务状态为0，则从未提交过，否则提交过
                rcbBox.setChecked(((PullGoodsInfoBean.PullScootersBean) item).getStatus() == 1);
                if (!rcbBox.isChecked()) {//未选中
                    rcbBox.setOnClickListener(v -> {
                        ChosePullReasonDialog dialog = new ChosePullReasonDialog();
                        dialog.setData((pullReasonType, remark,cancel) -> {
                            if (!cancel) {
                                ((PullGoodsInfoBean.PullScootersBean) item).setPullReason(pullReasonType);
                                ((PullGoodsInfoBean.PullScootersBean) item).setRemark(remark);
                                if (TextUtils.isEmpty(pullReasonType)) {
                                    rcbBox.setChecked(false);
                                    ((PullGoodsInfoBean.PullScootersBean) item).setChecked(false);
                                } else {
                                    rcbBox.setChecked(true);
                                    ToastUtil.showToast("操作成功");
                                    ((PullGoodsInfoBean.PullScootersBean) item).setChecked(true);
                                }
                            }
                        }, mContext);
                        dialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "333");
                    });
                }
                RecyclerView rvBill = helper.getView(R.id.rv_bill_info);
                List<PullGoodsInfoBean.PullScootersBean.SpCargoPullsBean> data = ((PullGoodsInfoBean.PullScootersBean) item).getSpCargoPulls();
                List<PullGoodsInfoBean.PullScootersBean.SpCargoPullsBean> showData = new ArrayList<>(data);
                size = data.size();
                if (data.size() != 0) {
                    PullGoodsInfoBean.PullScootersBean.SpCargoPullsBean title = new PullGoodsInfoBean.PullScootersBean.SpCargoPullsBean();
                    title.setTitle(true);
                    if (showData.size() == size) {
                        showData.add(0, title);
                    }
                    rvBill.setLayoutManager(new LinearLayoutManager(mContext));
                    rvBill.setAdapter(new BoardBillListAdapter(showData));
                }
                LinearLayout llContainer = helper.getView(R.id.ll_container);
                llContainer.setOnClickListener(v -> {
                    boolean show = rvBill.getVisibility() == View.VISIBLE;
                    if (show) {
                        rvBill.setVisibility(View.GONE);
                    } else {
                        rvBill.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case Constants.TYPE_PULL_BILL:
                TextView tvBillNumber = helper.getView(R.id.tv_bill_number);
                TextView tvBindBoard = helper.getView(R.id.tv_bind_board);
                TextView tvScanBoard = helper.getView(R.id.tv_scan_board);
                ImageView ivModifyBoards = helper.getView(R.id.iv_modify_boards);
                LinearLayout llBindBoards = helper.getView(R.id.ll_bind_board);
                if (((PullGoodsInfoBean.PullWaybillsBean) item).getStatus() == 1) {//已经提交过了,右边的选择框未被置空时隐藏扫描按钮
                    llBindBoards.setVisibility(View.GONE);
                    tvScanBoard.setVisibility(View.GONE);
                } else {
                    llBindBoards.setVisibility(View.VISIBLE);
                    tvScanBoard.setVisibility(View.VISIBLE);
                }
                tvScanBoard.setOnClickListener(v -> {
                    entity = (PullGoodsInfoBean.PullWaybillsBean) item;
                    ScanManagerActivity.startActivity(mContext, "PullGoodsInfoAdapter_Scan");
                });
                tvBillNumber.setText(((PullGoodsInfoBean.PullWaybillsBean) item).getWaybillCode());
                tvInfo.setText(String.format(mContext.getString(R.string.format_goods_info)
                        , ((PullGoodsInfoBean.PullWaybillsBean) item).getNumber()
                        , ((PullGoodsInfoBean.PullWaybillsBean) item).getWeight()
                        , ((PullGoodsInfoBean.PullWaybillsBean) item).getVolume()));
                StringBuilder sbRemote = new StringBuilder();
                if (((PullGoodsInfoBean.PullWaybillsBean) item).getScooterCodes() != null && ((PullGoodsInfoBean.PullWaybillsBean) item).getScooterCodes().size() != 0) {
                    for (String code : ((PullGoodsInfoBean.PullWaybillsBean) item).getScooterCodes()) {
                        sbRemote.append(code);
                        sbRemote.append(",");
                    }
                }
                tvBoardNumber.setText("原板车号:" + ((TextUtils.isEmpty(sbRemote.toString())) ? "" : sbRemote.toString().substring(0, sbRemote.toString().length() - 1)));
                StringBuilder sbLocal = new StringBuilder();
                if (((PullGoodsInfoBean.PullWaybillsBean) item).getPushScooterCodes() != null && ((PullGoodsInfoBean.PullWaybillsBean) item).getPushScooterCodes().size() != 0) {
                    for (String code : ((PullGoodsInfoBean.PullWaybillsBean) item).getPushScooterCodes()) {
                        sbLocal.append(code);
                        sbLocal.append(",");
                    }
                }
                tvBindBoard.setText("新绑板车号:" + ((TextUtils.isEmpty(sbLocal.toString())) ? "" : sbLocal.toString().substring(0, sbLocal.toString().length() - 1)));
                ivModifyBoards.setOnClickListener(v -> {
                    if (((PullGoodsInfoBean.PullWaybillsBean) item).getPushScooterCodes() != null && ((PullGoodsInfoBean.PullWaybillsBean) item).getPushScooterCodes().size() != 0) {
                        EditBoardsDialog dialog = new EditBoardsDialog();
                        dialog.setData(chosedBoards -> {
                            tvBindBoard.setText("新绑板车号：" + chosedBoards);
                            if (TextUtils.isEmpty(chosedBoards)) {
                                entity.getPushScooterCodes().clear();
                            } else {
                                if (chosedBoards.contains(",")) {
                                    entity.getPushScooterCodes().clear();
                                    String[] boards = chosedBoards.split(",");
                                    entity.getPushScooterCodes().addAll(Arrays.asList(boards));
                                }
                            }
                        }, tvBindBoard.getText().toString().substring(6), mContext);
                        dialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "334");
                    }
                });
                ivType.setImageResource((((PullGoodsInfoBean.PullWaybillsBean) item).getCreateUserType() == 0) ? R.mipmap.icon_pei : R.mipmap.icon_jian);//任务发起类型为0，则是配载发起，否则是监装发起
                helper.itemView.setBackgroundColor(((PullGoodsInfoBean.PullWaybillsBean) item).getStatus() == 0 ? Color.parseColor("#ffffff") : Color.parseColor("#00ff00"));//任务状态为0，则从未提交过，否则提交过
                rcbBox.setChecked(((PullGoodsInfoBean.PullWaybillsBean) item).getStatus() == 1);
                if (!rcbBox.isChecked()) {//未选中
                    rcbBox.setOnClickListener(v -> {
                        ChosePullReasonDialog dialog = new ChosePullReasonDialog();
                        dialog.setData((pullReasonType, remark,cancel) -> {
                            if (!cancel) {
                                ((PullGoodsInfoBean.PullWaybillsBean) item).setPullReason(pullReasonType);
                                ((PullGoodsInfoBean.PullWaybillsBean) item).setRemark(remark);
                                if (TextUtils.isEmpty(pullReasonType)) {
                                    rcbBox.setChecked(false);
                                    ((PullGoodsInfoBean.PullWaybillsBean) item).setChecked(false);
                                } else {
                                    ToastUtil.showToast("操作成功");
                                    rcbBox.setChecked(true);
                                    ((PullGoodsInfoBean.PullWaybillsBean) item).setChecked(true);
                                }
                            }
                        }, mContext);
                        dialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "333");
                    });
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("PullGoodsInfoAdapter_Scan".equals(result.getFunctionFlag())) {
            //根据扫一扫获取的板车信息查找板车内容
            if (!entity.getPushScooterCodes().contains(result.getData())) {
                entity.getPushScooterCodes().add(result.getData());
                notifyDataSetChanged();
            } else {
                ToastUtil.showToast("操作不合法，同一运单不能重复扫描同一板车");
            }
        }
    }

}
