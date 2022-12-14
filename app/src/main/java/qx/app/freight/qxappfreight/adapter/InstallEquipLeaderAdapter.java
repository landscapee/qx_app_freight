package qx.app.freight.qxappfreight.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.AssignInstallEquipMemberActivity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CollapsableLinearLayout;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;

/**
 * 装卸员小组长操作任务的代办适配器
 */
public class InstallEquipLeaderAdapter extends BaseQuickAdapter <LoadAndUnloadTodoBean, BaseViewHolder> {
    private OnSlideStepListener onSlideStepListener;
    private OnClearSeatListener onClearSeatListener;

    private boolean showReOpenBtn;
    private boolean showExReport; //是否显示 异常上报按钮

    private boolean showLook; //是否显示  装机单 卸机单

    private boolean showPhoto;//是否显示 拍照记录

    public InstallEquipLeaderAdapter(@Nullable List <LoadAndUnloadTodoBean> data) {
        super(R.layout.item_install_equip, data);
    }
    public InstallEquipLeaderAdapter(@Nullable List <LoadAndUnloadTodoBean> data, boolean showReOpenBtn, boolean showExReport,boolean showLook ,boolean showPhoto) {
        this(data);
        this.showReOpenBtn = showReOpenBtn;
        this.showExReport = showExReport;
        this.showLook = showLook;
        this.showPhoto = showPhoto;
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadAndUnloadTodoBean item) {
        LinearLayout llBg = helper.getView(R.id.ll_bg);
        if (!item.isAcceptTask()) {
            llBg.setBackgroundColor(Color.parseColor("#ffac00"));
        } else {
            llBg.setBackgroundColor(Color.parseColor("#ffffff"));
        }
//        boolean isWidePlane = item.getWidthAirFlag() == 0;
//        helper.setText(R.id.tv_plane_type, isWidePlane ? "宽体机" : "窄体机");
        helper.setText(R.id.tv_plane_type, item.getAircraftType());
        ImageView ivType = helper.getView(R.id.iv_operate_type);
        TextView tvTime = helper.getView(R.id.tv_time);
        tvTime.setText(item.getTimeForShow());
        Drawable drawableLeft = null;
        if (item.getMovement() == 1 || item.getMovement() == 4) {//装机
            ivType.setImageResource(R.mipmap.jin);//应该显示  ===进
        } else {
            ivType.setImageResource(R.mipmap.li);
        }
        switch (item.getTimeType()) {
            case Constants.TIME_TYPE_AUTUAL:
                drawableLeft = mContext.getResources().getDrawable(R.mipmap.shi);
                break;
            case Constants.TIME_TYPE_EXCEPT:
                drawableLeft = mContext.getResources().getDrawable(R.mipmap.yu);
                break;
            case Constants.TIME_TYPE_PLAN:
                drawableLeft = mContext.getResources().getDrawable(R.mipmap.ji);
                break;
        }
        tvTime.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
        tvTime.setCompoundDrawablePadding(5);
        helper.setText(R.id.tv_plane_info, StringUtil.toText(item.getFlightNo()));
        helper.setText(R.id.tv_craft_number, StringUtil.toText(item.getAircraftno()));
        LinearLayout container = helper.getView(R.id.ll_flight_info_container);
        FlightInfoLayout layout = new FlightInfoLayout(mContext, item.getFlightInfoList());

        LinearLayout llLink = helper.getView(R.id.ll_link);

        if (showLook) {

            Button btnLookInstall = helper.getView(R.id.btn_look_install);
            btnLookInstall.setText("查看装机单");
            Button btnLookUnInstall = helper.getView(R.id.btn_look_un_install);
            btnLookUnInstall.setText("查看卸机单");
            if (item.getMovement() == 4 && item.getRelateInfoObj() != null) {
                btnLookInstall.setVisibility(View.VISIBLE);
                btnLookInstall.setOnClickListener(v -> {
                    if (!Tools.isFastClick())
                        return;
                    onClearSeatListener.onLookLoadInstall(helper.getAdapterPosition());
                });
                btnLookUnInstall.setVisibility(View.VISIBLE);
                btnLookUnInstall.setOnClickListener(v -> {
                    if (!Tools.isFastClick())
                        return;
                    onClearSeatListener.onLookUnloadInstall(helper.getAdapterPosition());
                });
            } else {
                if (item.getMovement() == 1 || item.getMovement() == 4) {//卸机
                    btnLookUnInstall.setVisibility(View.VISIBLE);
                    btnLookUnInstall.setOnClickListener(v -> {
                        if (!Tools.isFastClick())
                            return;
                        onClearSeatListener.onLookUnloadInstall(helper.getAdapterPosition());
                    });
                    btnLookInstall.setVisibility(View.GONE);
                } else {
                    btnLookInstall.setVisibility(View.VISIBLE);
                    btnLookInstall.setOnClickListener(v -> {
                        if (!Tools.isFastClick())
                            return;
                        onClearSeatListener.onLookLoadInstall(helper.getAdapterPosition());
                    });
                    btnLookUnInstall.setVisibility(View.GONE);

                }
            }

        }
        if (showPhoto) {
            Button btnPhotoRecord = helper.getView(R.id.btn_photo_record);
            btnPhotoRecord.setVisibility(View.VISIBLE);
            btnPhotoRecord.setOnClickListener(v -> {
                if (!Tools.isFastClick())
                    return;
                onClearSeatListener.onUploadPhoto(helper.getAdapterPosition());
            });
        }
        //连班航班
        if (item.getMovement() == 4 && item.getRelateInfoObj() != null) {
            llLink.setVisibility(View.VISIBLE);
            ImageView ivTypeLink = helper.getView(R.id.iv_operate_type_link);
            helper.setText(R.id.tv_plane_type_link, item.getAircraftType());
            TextView tvTimeLink = helper.getView(R.id.tv_time_link);
            helper.setText(R.id.tv_plane_info_link, StringUtil.toText(item.getRelateInfoObj().getFlightNo()));
            helper.setText(R.id.tv_craft_number_link, StringUtil.toText(item.getRelateInfoObj().getAircraftno()));
            helper.setText(R.id.tv_seat_link, StringUtil.toText(item.getRelateInfoObj().getSeat()));
            tvTimeLink.setText(item.getRelateInfoObj().getTimeForShow());
            if (item.getRelateInfoObj().getMovement() == 1 || item.getRelateInfoObj().getMovement() == 4) {//装机
                ivTypeLink.setImageResource(R.mipmap.jin);//应该显示  ===进
            } else {
                ivTypeLink.setImageResource(R.mipmap.li);
            }
            Drawable drawableLeftLink = null;
            switch (item.getRelateInfoObj().getTimeType()) {
                case Constants.TIME_TYPE_AUTUAL:
                    drawableLeftLink = mContext.getResources().getDrawable(R.mipmap.shi);
                    break;
                case Constants.TIME_TYPE_EXCEPT:
                    drawableLeftLink = mContext.getResources().getDrawable(R.mipmap.yu);
                    break;
                case Constants.TIME_TYPE_PLAN:
                    drawableLeftLink = mContext.getResources().getDrawable(R.mipmap.ji);
                    break;
            }
            tvTimeLink.setCompoundDrawablesWithIntrinsicBounds(drawableLeftLink, null, null, null);
            tvTimeLink.setCompoundDrawablePadding(5);
            LinearLayout containerLink = helper.getView(R.id.ll_flight_info_container_link);
            if (item.getRelateInfoObj().getFlightInfoList() != null) {
                FlightInfoLayout layoutLink = new FlightInfoLayout(mContext, item.getRelateInfoObj().getFlightInfoList());
                LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                containerLink.removeAllViews();
                containerLink.addView(layoutLink, paramsMain);
            }
        } else
            llLink.setVisibility(View.GONE);

        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.removeAllViews();
        container.addView(layout, paramsMain);
        helper.setText(R.id.tv_seat, StringUtil.toText(item.getSeat()));

        RecyclerView rvStep = helper.getView(R.id.rv_step);
        rvStep.setLayoutManager(new LinearLayoutManager(mContext));
        LeaderInstallEquipStepAdapter adapter = new LeaderInstallEquipStepAdapter(item.getOperationStepObj());
        rvStep.setAdapter(adapter);
        adapter.setOnSlideListener(pos -> {
            if (onSlideStepListener != null) {
                if (pos == 0 && item.getAcceptTime() == 0)
                    onSlideStepListener.onSlideStep(helper.getAdapterPosition(), adapter, pos);
                else if (pos != 0)
                    onSlideStepListener.onSlideStep(helper.getAdapterPosition(), adapter, pos);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
                item.getOperationStepObj().get(pos).setStepDoneDate(sdf.format(new Date()));
                if (pos == 0) {
                    Intent intent = new Intent(mContext, AssignInstallEquipMemberActivity.class);
                    intent.putExtra("task_id", item.getTaskId());
                    mContext.startActivity(intent);
                }
                item.getOperationStepObj().get(pos).setItemType(Constants.TYPE_STEP_OVER);//滑动的那个item马上设置为已完成的步骤类型显示
                if (pos != 3) {
                    item.getOperationStepObj().get(pos + 1).setItemType(Constants.TYPE_STEP_NOW);
                }
            }
        });
        CollapsableLinearLayout collView = helper.getView(R.id.coll_listview);
        if (item.isShowDetail()) {
            rvStep.setVisibility(View.VISIBLE);
            collView.expand();
        } else {
            rvStep.setVisibility(View.GONE);
            collView.collapse();
        }
        llBg.setOnClickListener(v -> {
            item.setShowDetail(!item.isShowDetail());
            if (item.isShowDetail()) {
                rvStep.setVisibility(View.VISIBLE);
                collView.expand();
            } else {
                rvStep.setVisibility(View.GONE);
                collView.collapse();
            }
        });

        Button btnFS = helper.getView(R.id.btn_flight_safeguard);
        Button btnClear = helper.getView(R.id.btn_seat_clear);
        btnFS.setVisibility(View.VISIBLE);
        btnClear.setVisibility(View.VISIBLE);
        ImageView ivDone = helper.getView(R.id.iv_done); //已办图片

        if (item.getOperationStepObj() != null && item.getOperationStepObj().size() > 0 && !StringUtil.isEmpty(item.getOperationStepObj().get(item.getOperationStepObj().size() - 1).getStepDoneDate())) {
            ivDone.setVisibility(View.VISIBLE);
        } else {
            ivDone.setVisibility(View.GONE);
        }
        btnClear.setOnClickListener(v -> onClearSeatListener.onClearClicked(helper.getAdapterPosition()));
        btnFS.setOnClickListener(v -> onClearSeatListener.onFlightSafeguardClick(helper.getAdapterPosition()));
    }

    public interface OnSlideStepListener {
        void onSlideStep(int bigPos, LeaderInstallEquipStepAdapter adapter, int smallPos);
    }

    public void setOnSlideStepListener(OnSlideStepListener onSlideStepListener) {
        this.onSlideStepListener = onSlideStepListener;
    }

    public interface OnClearSeatListener {
        void onClearClicked(int position);

        void onFlightSafeguardClick(int position);

        void onUploadPhoto(int position);

        void onLookUnloadInstall(int position);

        void onLookLoadInstall(int position);
    }

    public void setOnClearSeatListener(OnClearSeatListener onClearSeatListener) {
        this.onClearSeatListener = onClearSeatListener;
    }
}
