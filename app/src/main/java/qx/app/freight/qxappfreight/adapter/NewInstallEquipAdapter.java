package qx.app.freight.qxappfreight.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import qx.app.freight.qxappfreight.activity.ErrorReportActivity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CollapsableLinearLayout;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;

/**
 * 使用服务器原始数据的装卸机代办适配器
 */
public class NewInstallEquipAdapter extends BaseQuickAdapter <LoadAndUnloadTodoBean, BaseViewHolder> {
    private OnSlideStepListener onSlideStepListener;
    private OnFlightSafeguardListenner onFlightSafeguardListenner;
    private OnReOpenLoadTaskListener onReOpenLoadTaskListener;
    private boolean showReOpenBtn;

    private boolean showExReport; //是否显示 异常上报按钮

    private boolean showLook; //是否显示  装机单 卸机单

    private boolean showPhoto;//是否显示 拍照记录

    public NewInstallEquipAdapter(@Nullable List <LoadAndUnloadTodoBean> data) {
        super(R.layout.item_install_equip, data);
    }

    public NewInstallEquipAdapter(@Nullable List <LoadAndUnloadTodoBean> data, boolean showReOpenBtn) {
        this(data);
        this.showReOpenBtn = showReOpenBtn;
    }

    public NewInstallEquipAdapter(@Nullable List <LoadAndUnloadTodoBean> data, boolean showReOpenBtn, boolean showExReport) {
        this(data);
        this.showReOpenBtn = showReOpenBtn;
//        this.showExReport = showExReport;
        this.showLook = showExReport;
    }

    public NewInstallEquipAdapter(@Nullable List <LoadAndUnloadTodoBean> data, boolean showReOpenBtn, boolean showExReport, boolean showPhoto) {
        this(data);
        this.showReOpenBtn = showReOpenBtn;
        this.showExReport = showExReport;
        this.showLook = showExReport;
        this.showPhoto = showPhoto;
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadAndUnloadTodoBean item) {
        helper.setIsRecyclable(false);
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
        Button btnReopen = helper.getView(R.id.btn_reopen_load_task);
        if (showReOpenBtn) {//显示重新开启装机任务按钮，只有装机已办页面中有使用
            btnReopen.setVisibility(View.VISIBLE);
            btnReopen.setOnClickListener(v -> {
                if (onReOpenLoadTaskListener != null) {
                    if (!Tools.isFastClick())
                        return;
                    onReOpenLoadTaskListener.onReOpenLoadTask(helper.getAdapterPosition());
                }
            });
        } else {
            btnReopen.setVisibility(View.GONE);
        }

        //异常上报
        Button btnExReport = helper.getView(R.id.btn_ex_report);
        if (showExReport) {
            btnExReport.setVisibility(View.VISIBLE);
            btnExReport.setOnClickListener(b -> {
                if (!Tools.isFastClick())
                    return;
                Intent intent = new Intent(mContext, ErrorReportActivity.class);
                intent.putExtra("task_id", item.getTaskId());//任务id
                intent.putExtra("area_id", item.getSeat());//area_id
                if (item.getMovement() == 4) {
                    if (item.getStartLoadTime() > 0||item.getEndUnloadTime() > 0) {// 卸机结束结束 装机异常
                        if (item.getRelateInfoObj() != null) {
                            intent.putExtra("flight_number", item.getRelateInfoObj().getFlightNo());//航班号
                            intent.putExtra("flight_id", item.getRelateInfoObj().getFlightId());//Flight id
                        } else {
                            intent.putExtra("flight_number", item.getFlightNo());//航班号
                        }
                        intent.putExtra("step_code", "FreightLoadFinish");//step_code //默认为
                        intent.putExtra("error_type", 1);// 装机异常
                    } else  {//卸机异常
                        intent.putExtra("flight_number", item.getFlightNo());//航班号
                        intent.putExtra("flight_id", item.getFlightId());//Flight id
                        intent.putExtra("error_type", 2);//卸机异常
                        intent.putExtra("step_code", "FreightUnLoadFinish");//step_code //默认为
                    }
                } else {
                    intent.putExtra("flight_number", item.getFlightNo());//航班号
                    intent.putExtra("flight_id", item.getFlightId());//Flight id
                    if (item.getMovement() == 1) //进港 卸机
                    {
                        intent.putExtra("step_code", "FreightUnLoadFinish");//step_code //默认为
                        intent.putExtra("error_type", 2);//卸机异常
                    }
                    else{
                        intent.putExtra("step_code", "FreightLoadFinish");//step_code //默认为
                        intent.putExtra("error_type", 1);//装机异常
                    }



                }
                mContext.startActivity(intent);
            });
        } else {
            btnExReport.setVisibility(View.GONE);
        }

        Button btnFS = helper.getView(R.id.btn_flight_safeguard);
        btnFS.setOnClickListener(v -> {
            if (!Tools.isFastClick())
                return;
            onFlightSafeguardListenner.onFlightSafeguardClick(helper.getAdapterPosition());
        });
        Button btnClear = helper.getView(R.id.btn_seat_clear);
        btnClear.setVisibility(View.GONE);
        btnClear.setOnClickListener(v -> {
            if (!Tools.isFastClick())
                return;
            onFlightSafeguardListenner.onClearClick(helper.getAdapterPosition());
        });
        if (showPhoto) {
            Button btnPhotoRecord = helper.getView(R.id.btn_photo_record);
            btnPhotoRecord.setVisibility(View.VISIBLE);
            btnPhotoRecord.setOnClickListener(v -> {
                if (!Tools.isFastClick())
                    return;
                onFlightSafeguardListenner.onUploadPhoto(helper.getAdapterPosition());
            });
        }

        if (showLook) {

            Button btnLookInstall = helper.getView(R.id.btn_look_install);
            Button btnLookUnInstall = helper.getView(R.id.btn_look_un_install);
            if (item.getMovement() == 4 && item.getRelateInfoObj() != null) {
                btnLookInstall.setVisibility(View.VISIBLE);
                btnLookInstall.setOnClickListener(v -> {
                    if (!Tools.isFastClick())
                        return;
                    onFlightSafeguardListenner.onLookLoadInstall(helper.getAdapterPosition());
                });
                btnLookUnInstall.setVisibility(View.VISIBLE);
                btnLookUnInstall.setOnClickListener(v -> {
                    if (!Tools.isFastClick())
                        return;
                    onFlightSafeguardListenner.onLookUnloadInstall(helper.getAdapterPosition());
                });
            } else {
                if (item.getMovement() == 1 || item.getMovement() == 4) {//卸机
                    btnLookUnInstall.setVisibility(View.VISIBLE);
                    btnLookUnInstall.setOnClickListener(v -> {
                        if (!Tools.isFastClick())
                            return;
                        onFlightSafeguardListenner.onLookUnloadInstall(helper.getAdapterPosition());
                    });
                    btnLookInstall.setVisibility(View.GONE);
                } else {
                    btnLookInstall.setVisibility(View.VISIBLE);
                    btnLookInstall.setOnClickListener(v -> {
                        if (!Tools.isFastClick())
                            return;
                        onFlightSafeguardListenner.onLookLoadInstall(helper.getAdapterPosition());
                    });
                    btnLookUnInstall.setVisibility(View.GONE);

                }
            }

        }

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
        LinearLayout llLink = helper.getView(R.id.ll_link);
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
        LinearLayout container = helper.getView(R.id.ll_flight_info_container);
        FlightInfoLayout layout = new FlightInfoLayout(mContext, item.getFlightInfoList());
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.removeAllViews();
        container.addView(layout, paramsMain);
        helper.setText(R.id.tv_seat, StringUtil.toText(item.getSeat()));
        ImageView ivDone = helper.getView(R.id.iv_done); //已办图片
        if (item.getOperationStepObj()!=null && item.getOperationStepObj().size()>0&&!StringUtil.isEmpty(item.getOperationStepObj().get(item.getOperationStepObj().size() - 1).getStepDoneDate())) {
            btnFS.setVisibility(View.GONE);
            ivDone.setVisibility(View.VISIBLE);
        } else {
            ivDone.setVisibility(View.GONE);
            btnFS.setVisibility(View.VISIBLE);
        }
        RecyclerView rvStep = helper.getView(R.id.rv_step);
        rvStep.setLayoutManager(new LinearLayoutManager(mContext));
        NewInstallEquipStepAdapter adapter = new NewInstallEquipStepAdapter(item.getOperationStepObj());
        rvStep.setAdapter(adapter);
        adapter.setOnSlideListener(pos -> {
            if (onSlideStepListener != null) {
                onSlideStepListener.onSlideStep(helper.getAdapterPosition(), adapter, pos);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
                if (item.getTaskType() == 5) {//装卸机2合1代办单独处理
                    item.getOperationStepObj().get(pos).setStepDoneDate(sdf.format(new Date()) + "-");
                    if (pos == 3) {
//                        if (TextUtils.isEmpty(item.getSeat())) {
//                            ToastUtil.showToast("当前航班未分配机位，不能进行卸机操作");
//                        } else {
//                            if (!isWidePlane) {//窄体机卸机才到卸机页面
//                            Intent intent = new Intent(mContext, UnloadPlaneActivity.class);
//                            intent.putExtra("flight_type", item.getFlightType());
//                            intent.putExtra("plane_info", item);
//                            mContext.startActivity(intent);
//                            } else {
                            item.getOperationStepObj().get(pos).setStepDoneDate(sdf.format(new Date()) + "-" + sdf.format(new Date()));
                            item.getOperationStepObj().get(pos).setItemType(Constants.TYPE_STEP_OVER);//滑动的那个item马上设置为已完成的步骤类型显示
                            item.getOperationStepObj().get(pos + 1).setItemType(Constants.TYPE_STEP_NOW);
//                            }
//                        }
                    } else if (pos == 4) {
//                        Intent intent = new Intent(mContext, LoadPlaneActivity.class);
//                        intent.putExtra("plane_info", item);
//                        intent.putExtra("position", 5);
//                        mContext.startActivity(intent);
                        item.getOperationStepObj().get(pos).setStepDoneDate(sdf.format(new Date()) + "-" + sdf.format(new Date()));
                        item.getOperationStepObj().get(pos).setItemType(Constants.TYPE_STEP_OVER);//滑动的那个item马上设置为已完成的步骤类型显示
                        item.getOperationStepObj().get(pos + 1).setItemType(Constants.TYPE_STEP_NOW);
                    } else {
                        item.getOperationStepObj().get(pos).setItemType(Constants.TYPE_STEP_OVER);//滑动的那个item马上设置为已完成的步骤类型显示
                        item.getOperationStepObj().get(pos).setStepDoneDate(sdf.format(new Date()));//设置显示时间
                        if (pos != 5) {//只要滑动的不是第六步，则下一个步骤item设置为应该操作的步骤样式
                            item.getOperationStepObj().get(pos + 1).setItemType(Constants.TYPE_STEP_NOW);
                        }
                    }
                } else {
                    if (pos == 3) {
                        item.getOperationStepObj().get(pos).setStepDoneDate(sdf.format(new Date()) + "-");
//                        if (isWidePlane && item.getTaskType() == 2) {
                        item.getOperationStepObj().get(pos).setStepDoneDate(sdf.format(new Date()) + "-" + sdf.format(new Date()));
                        item.getOperationStepObj().get(pos).setItemType(Constants.TYPE_STEP_OVER);//滑动的那个item马上设置为已完成的步骤类型显示
                        item.getOperationStepObj().get(pos + 1).setItemType(Constants.TYPE_STEP_NOW);
//                        } else {
//                        Intent intent;
//                        if (item.getTaskType() == 1) {
//                            intent = new Intent(mContext, LoadPlaneActivity.class);
//                            intent.putExtra("position", 3);
//                        } else {
//                            intent = new Intent(mContext, UnloadPlaneActivity.class);
//                            intent.putExtra("flight_type", item.getOperationStepObj().get(pos).getFlightType());
//                        }
//                        intent.putExtra("plane_info", item);
//                        mContext.startActivity(intent);
//                        }
                    } else {
                        item.getOperationStepObj().get(pos).setItemType(Constants.TYPE_STEP_OVER);//滑动的那个item马上设置为已完成的步骤类型显示
                        item.getOperationStepObj().get(pos).setStepDoneDate(sdf.format(new Date()));//设置显示时间
                        if (item.getOperationStepObj().size() > 2) {
                            if (pos != 4) {//只要滑动的不是第五步，则下一个步骤item设置为应该操作的步骤样式
                                item.getOperationStepObj().get(pos + 1).setItemType(Constants.TYPE_STEP_NOW);
                            }
                        }
                    }
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
    }

    public interface OnSlideStepListener {
        void onSlideStep(int bigPos, NewInstallEquipStepAdapter adapter, int smallPos);
    }

    public void setOnSlideStepListener(OnSlideStepListener onSlideStepListener) {
        this.onSlideStepListener = onSlideStepListener;
    }

    public interface OnFlightSafeguardListenner {
        void onFlightSafeguardClick(int position);

        void onClearClick(int position);

        void onUploadPhoto(int position);

        void onLookUnloadInstall(int position);

        void onLookLoadInstall(int position);
    }

    public void setOnFlightSafeguardListenner(OnFlightSafeguardListenner onFlightSafeguardListenner) {
        this.onFlightSafeguardListenner = onFlightSafeguardListenner;
    }

    public interface OnReOpenLoadTaskListener {
        void onReOpenLoadTask(int pos);
    }

    public void setOnReOpenLoadTaskListener(OnReOpenLoadTaskListener onReOpenLoadTaskListener) {
        this.onReOpenLoadTaskListener = onReOpenLoadTaskListener;
    }
}
