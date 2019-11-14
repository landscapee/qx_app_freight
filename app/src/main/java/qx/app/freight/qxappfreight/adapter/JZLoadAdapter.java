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

import cn.label.library.LabelView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.ErrorReportActivity;
import qx.app.freight.qxappfreight.activity.LoadPlaneActivity;
import qx.app.freight.qxappfreight.activity.UnloadPlaneActivity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CollapsableLinearLayout;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;

/**
 * 使用服务器原始数据的装卸机代办适配器
 */
public class JZLoadAdapter extends BaseQuickAdapter<LoadAndUnloadTodoBean, BaseViewHolder> {
    private OnSlideStepListener onSlideStepListener;
    private OnFlightSafeguardListenner onFlightSafeguardListenner;
    private OnReOpenLoadTaskListener onReOpenLoadTaskListener;
    private boolean showReOpenBtn;

    private boolean showExReport; //是否显示 异常上报按钮

    public JZLoadAdapter(@Nullable List<LoadAndUnloadTodoBean> data) {
        super(R.layout.item_jz_load, data);
    }

    public JZLoadAdapter(@Nullable List<LoadAndUnloadTodoBean> data, boolean showReOpenBtn) {
        this(data);
        this.showReOpenBtn = showReOpenBtn;
    }

    public JZLoadAdapter(@Nullable List<LoadAndUnloadTodoBean> data, boolean showReOpenBtn, boolean showExReport) {
        this(data);
        this.showReOpenBtn = showReOpenBtn;
        this.showExReport = showExReport;
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadAndUnloadTodoBean item) {
        helper.setIsRecyclable(false);

//        boolean isWidePlane = item.getWidthAirFlag() == 0;
//        helper.setText(R.id.tv_plane_type, isWidePlane ? "宽体机" : "窄体机");
        helper.setText(R.id.tv_plane_type, item.getAircraftType());
        ImageView ivType = helper.getView(R.id.iv_operate_type);
        helper.setText(R.id.tv_atd, TimeUtils.datetimeTo4(item.getAtd()));
        helper.setText(R.id.tv_etd, TimeUtils.datetimeTo4(item.getEtd()));
        helper.setText(R.id.tv_std, TimeUtils.datetimeTo4(item.getStd()));

        LabelView mLabelView = helper.getView(R.id.task_lable);
//        mLabelView.setVisibility(View.GONE);



        Button btnReopen = helper.getView(R.id.btn_reopen_load_task);
        if (showReOpenBtn) {//显示重新开启装机任务按钮，只有装机已办页面中有使用
            btnReopen.setVisibility(View.VISIBLE);
            btnReopen.setOnClickListener(v -> {
                if (onReOpenLoadTaskListener != null) {
                    onReOpenLoadTaskListener.onReOpenLoadTask(helper.getAdapterPosition());
                }
            });
        } else {
            btnReopen.setVisibility(View.GONE);
        }

        //异常上报
        Button btnExReport = helper.getView(R.id.btn_ex_report);
        if (showExReport){
            if (item.getAcceptTime()> 0&&item.getPassengerLoadSend()> 0){
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.blue_btn_bg_color));
            }
            else {
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
            btnExReport.setVisibility(View.VISIBLE);
            btnExReport.setOnClickListener(b->{
                Intent intent = new Intent(mContext, ErrorReportActivity.class);
                intent.putExtra("flight_number", item.getFlightNo());//航班号
                intent.putExtra("task_id", item.getTaskId());//任务id
                intent.putExtra("flight_id",item.getFlightId());//Flight id
                intent.putExtra("area_id",item.getSeat());//area_id
                intent.putExtra("step_code", item.getOperationStepObj().get(1).getOperationCode());//step_code //默认为 舱单送达步骤
                intent.putExtra("error_type", 4);//偏离上报
                mContext.startActivity(intent);
            });
        }
        else {
            btnExReport.setVisibility(View.GONE);
        }

        Button btnFS = helper.getView(R.id.btn_flight_safeguard);
        btnFS.setOnClickListener(v -> {
            onFlightSafeguardListenner.onFlightSafeguardClick(helper.getAdapterPosition());
        });
        Button btnClear = helper.getView(R.id.btn_seat_clear);
        btnClear.setVisibility(View.GONE);
        btnClear.setOnClickListener(v -> {
            onFlightSafeguardListenner.onClearClick(helper.getAdapterPosition());
        });

        Drawable drawableLeft = null;
        if (item.getMovement() == 1 || item.getMovement() == 4) {//装机
            ivType.setImageResource(R.mipmap.jin);//应该显示  ===进
        } else {
            ivType.setImageResource(R.mipmap.li);
        }
//        switch (item.getTimeType()) {
//            case Constants.TIME_TYPE_AUTUAL:
//                drawableLeft = mContext.getResources().getDrawable(R.mipmap.shi);
//                break;
//            case Constants.TIME_TYPE_EXCEPT:
//                drawableLeft = mContext.getResources().getDrawable(R.mipmap.yu);
//                break;
//            case Constants.TIME_TYPE_PLAN:
//                drawableLeft = mContext.getResources().getDrawable(R.mipmap.ji);
//                break;
//        }
//        tvTime.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
//        tvTime.setCompoundDrawablePadding(5);
        //连班航班显示 连班航班号
        if ((item.getMovement() == 4||item.getMovement() == 8) && item.getRelateInfoObj() != null){
            helper.setText(R.id.tv_plane_info, StringUtil.toText(item.getFlightNo()+"("+item.getRelateInfoObj().getFlightNo()+")"));
        }
        else
            helper.setText(R.id.tv_plane_info, StringUtil.toText(item.getFlightNo()));

        helper.setText(R.id.tv_craft_number, StringUtil.toText(item.getAircraftno()));
        LinearLayout llLink = helper.getView(R.id.ll_link);

        LinearLayout mLayoutLable = helper.getView(R.id.layout_lable);

        if (item.getLoadingAndUnloadBean()!=null){
            helper.setText(R.id.item_tv_seat, item.getSeat()!=null?item.getSeat():"-");
            helper.setText(R.id.item_tv_boarding, item.getLoadingAndUnloadBean().getGate()!=null?item.getLoadingAndUnloadBean().getGate():"-" );
            helper.setText(R.id.item_tv_type, item.getLoadingAndUnloadBean().getAircraftType()+"/"+item.getLoadingAndUnloadBean().getModel());
            helper.setText(R.id.item_tv_xingli, item.getLoadingAndUnloadBean().getCarousel()!=null?item.getLoadingAndUnloadBean().getCarousel():"-");
            helper.setText(R.id.tv_ctot, "CTOT \n"+TimeUtils.datetimeTo4(item.getLoadingAndUnloadBean().getCtot()));
            helper.setText(R.id.tv_xg, "协关 \n"+TimeUtils.datetimeTo4(item.getLoadingAndUnloadBean().getUnifiedCloseTime()));
            helper.setText(R.id.tv_pre, "前飞 \n"+TimeUtils.datetimeTo4(item.getRelateInfoObj()!=null?item.getRelateInfoObj().getLoadingAndUnloadBean()!=null?item.getRelateInfoObj().getLoadingAndUnloadBean().getPreAtd():0:0));
            helper.setText(R.id.tv_eta, "预达 \n"+TimeUtils.datetimeTo4(item.getRelateInfoObj()!=null?item.getRelateInfoObj().getEta()>0?item.getRelateInfoObj().getEta():0:0));
            if (!StringUtil.isEmpty(item.getLoadingAndUnloadBean().getElecState())){
                showLable(item.getLoadingAndUnloadBean().getElecState(),mLayoutLable);
            }
            if (item.getLoadingAndUnloadBean().getVipMark()!=null && item.getLoadingAndUnloadBean().getVipMark().equals("0")){
                showLable(8,mLayoutLable);
            }
            if (!StringUtil.isEmpty(item.getLoadingAndUnloadBean().getFlightStatus())){
                if (item.getLoadingAndUnloadBean().getFlightStatus().equals("已达"))// 已达
                {
                    mLabelView.setVisibility(View.VISIBLE);
                    mLabelView.setLabelBackGroundColor(mContext.getResources().getColor(R.color.flight_a));
                }
                else if(item.getLoadingAndUnloadBean().getFlightStatus().equals("正常")){//正常
                    mLabelView.setVisibility(View.VISIBLE);
                    mLabelView.setLabelBackGroundColor(mContext.getResources().getColor(R.color.lightgreen));
                }
                else if (item.getLoadingAndUnloadBean().getFlightStatus().equals("起飞")) // 起飞
                {
                    mLabelView.setLabelBackGroundColor(mContext.getResources().getColor(R.color.flight_d));
                } else if (item.getLoadingAndUnloadBean().getFlightStatus().equals("前起")) {//前起
                    mLabelView.setVisibility(View.VISIBLE);
                    mLabelView.setLabelBackGroundColor(mContext.getResources().getColor(R.color.flight_pre_atd));
                } else if (item.getLoadingAndUnloadBean().getFlightStatus().equals("允登")) {
                    mLabelView.setVisibility(View.VISIBLE);
                    mLabelView.setLabelBackGroundColor(mContext.getResources().getColor(R.color.flight_yundeng));
                } else if (item.getLoadingAndUnloadBean().getFlightStatus().equals("登机")) {
                    mLabelView.setVisibility(View.VISIBLE);
                    mLabelView.setLabelBackGroundColor(mContext.getResources().getColor(R.color.flight_dengji));
                }
                else if(item.getLoadingAndUnloadBean().getFlightStatus().equals("完登")){//完成登机
                    mLabelView.setVisibility(View.VISIBLE);
                    mLabelView.setLabelBackGroundColor(mContext.getResources().getColor(R.color.flight_wanchengdengji));
                }
                else if(item.getLoadingAndUnloadBean().getFlightStatus().equals("撤轮档")){//撤轮档
                    mLabelView.setVisibility(View.VISIBLE);
                    mLabelView.setLabelBackGroundColor(mContext.getResources().getColor(R.color.imlib_yellowa));
                }
                else if(item.getLoadingAndUnloadBean().getFlightStatus().equals("推出")){//飞机推出
                    mLabelView.setVisibility(View.VISIBLE);
                    mLabelView.setLabelBackGroundColor(mContext.getResources().getColor(R.color.red));
                }
                mLabelView.setTextContent(item.getLoadingAndUnloadBean().getFlightStatus());
            }


        }

//        //连班航班
//        if (item.getMovement() == 4 && item.getRelateInfoObj() != null) {
//            llLink.setVisibility(View.VISIBLE);
//            ImageView ivTypeLink = helper.getView(R.id.iv_operate_type_link);
//            helper.setText(R.id.tv_plane_type_link, item.getAircraftType());
//            TextView tvTimeLink = helper.getView(R.id.tv_time_link);
//            helper.setText(R.id.tv_plane_info_link, StringUtil.toText(item.getRelateInfoObj().getFlightNo()));
//            helper.setText(R.id.tv_craft_number_link, StringUtil.toText(item.getRelateInfoObj().getAircraftno()));
//            helper.setText(R.id.tv_seat_link, StringUtil.toText(item.getRelateInfoObj().getSeat()));
//            tvTimeLink.setText(item.getRelateInfoObj().getTimeForShow());
//            if (item.getRelateInfoObj().getMovement() == 1 || item.getRelateInfoObj().getMovement() == 4) {//装机
//                ivTypeLink.setImageResource(R.mipmap.jin);//应该显示  ===进
//            } else {
//                ivTypeLink.setImageResource(R.mipmap.li);
//            }
//            Drawable drawableLeftLink = null;
//            switch (item.getRelateInfoObj().getTimeType()) {
//                case Constants.TIME_TYPE_AUTUAL:
//                    drawableLeftLink = mContext.getResources().getDrawable(R.mipmap.shi);
//                    break;
//                case Constants.TIME_TYPE_EXCEPT:
//                    drawableLeftLink = mContext.getResources().getDrawable(R.mipmap.yu);
//                    break;
//                case Constants.TIME_TYPE_PLAN:
//                    drawableLeftLink = mContext.getResources().getDrawable(R.mipmap.ji);
//                    break;
//            }
//            tvTimeLink.setCompoundDrawablesWithIntrinsicBounds(drawableLeftLink, null, null, null);
//            tvTimeLink.setCompoundDrawablePadding(5);
//            LinearLayout containerLink = helper.getView(R.id.ll_flight_info_container_link);
//            if (item.getRelateInfoObj().getFlightInfoList() != null) {
//                FlightInfoLayout layoutLink = new FlightInfoLayout(mContext, item.getRelateInfoObj().getFlightInfoList());
//                LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                containerLink.removeAllViews();
//                containerLink.addView(layoutLink, paramsMain);
//            }
//        } else
            llLink.setVisibility(View.GONE);

        LinearLayout container = helper.getView(R.id.ll_flight_info_container);
        FlightInfoLayout layout = new FlightInfoLayout(mContext, item.getFlightInfoList());
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.removeAllViews();
        container.addView(layout, paramsMain);

        ImageView ivDone = helper.getView(R.id.iv_done); //已办图片
        if (!StringUtil.isEmpty(item.getOperationStepObj().get(item.getOperationStepObj().size() - 1).getStepDoneDate())) {
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
                        if (TextUtils.isEmpty(item.getSeat())) {
                            ToastUtil.showToast("当前航班未分配机位，不能进行卸机操作");
                        } else {
//                            if (!isWidePlane) {//窄体机卸机才到卸机页面
                            Intent intent = new Intent(mContext, UnloadPlaneActivity.class);
                            intent.putExtra("flight_type", item.getFlightType());
                            intent.putExtra("plane_info", item);
                            mContext.startActivity(intent);
//                            } else {
//                                item.getOperationStepObj().get(pos).setStepDoneDate(sdf.format(new Date()) + "-" + sdf.format(new Date()));
//                                item.getOperationStepObj().get(pos).setItemType(Constants.TYPE_STEP_OVER);//滑动的那个item马上设置为已完成的步骤类型显示
//                                item.getOperationStepObj().get(pos + 1).setItemType(Constants.TYPE_STEP_NOW);
//                            }
                        }
                    } else if (pos == 4) {
                        Intent intent = new Intent(mContext, LoadPlaneActivity.class);
                        intent.putExtra("plane_info", item);
                        intent.putExtra("position", 5);
                        mContext.startActivity(intent);
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
//                            item.getOperationStepObj().get(pos).setStepDoneDate(sdf.format(new Date()) + "-" + sdf.format(new Date()));
//                            item.getOperationStepObj().get(pos).setItemType(Constants.TYPE_STEP_OVER);//滑动的那个item马上设置为已完成的步骤类型显示
//                            item.getOperationStepObj().get(pos + 1).setItemType(Constants.TYPE_STEP_NOW);
//                        } else {
                        Intent intent;
                        if (item.getTaskType() == 1) {
                            intent = new Intent(mContext, LoadPlaneActivity.class);
                            intent.putExtra("position", 3);
                        } else {
                            intent = new Intent(mContext, UnloadPlaneActivity.class);
                            intent.putExtra("flight_type", item.getOperationStepObj().get(pos).getFlightType());
                        }
                        intent.putExtra("plane_info", item);
                        mContext.startActivity(intent);
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

        //货邮舱单和装机单待办 不显示 步凑列表
        if (showExReport){
            rvStep.setVisibility(View.GONE);
            LinearLayout llBg = helper.getView(R.id.ll_bg);
            if (!item.isAcceptTask()) {
                llBg.setBackgroundColor(Color.parseColor("#ffac00"));
            } else {
                llBg.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
            }
//            CollapsableLinearLayout collView = helper.getView(R.id.coll_listview);
            if (item.isShowDetail()) {
                rvStep.setVisibility(View.VISIBLE);
//                collView.expand();
            } else {
                rvStep.setVisibility(View.GONE);
//                collView.collapse();
            }
            llBg.setOnClickListener(v -> {
                item.setShowDetail(!item.isShowDetail());
                if (item.isShowDetail()) {
                    rvStep.setVisibility(View.VISIBLE);
//                    collView.expand();
                } else {
                    rvStep.setVisibility(View.GONE);
//                    collView.collapse();
                }
            });
        }
        else
        {
            rvStep.setVisibility(View.GONE);
        }

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

    /**
     * TODO: 控制航班特殊标签显示
     *
     * @param lable
     * @param layout
     */
    private void showLable(int lable, LinearLayout layout) {
        TextView tv = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 0, 0, 0);
        tv.setLayoutParams(params);
        tv.setTextSize(12);
        tv.setTextColor(mContext.getResources().getColor(R.color.white));
        tv.setPadding(5, 3, 5, 3);
//        view.setBackground(mContext.getResources().getDrawable(R.drawable.shape_card_board_nomal));//任务默认背景
        switch (lable) {
            case 1://重点保障任务
                tv.setText("重");
                tv.setBackgroundResource(R.drawable.shape_main_task_label_1);
                break;
            case 2://特情
                tv.setText("特");
                tv.setBackgroundResource(R.drawable.shape_main_task_label_2);
                break;
            case 3://航班变更
                tv.setText("变");
                tv.setBackgroundResource(R.drawable.shape_main_task_label_7);
                break;
            case 4:// 任务变更(用卡片布局背景颜色表示)
                //view.setBackground(mContext.getResources().getDrawable(R.drawable.shape_card_board_1));
                break;
            case 5://进港
                tv.setText("进");
                //view.setBackgroundResource(R.drawable.shape_color_task_bg_blue);
                tv.setBackgroundResource(R.drawable.shape_main_task_label_5);
                break;
            case 6:// 出港
                tv.setText("出");
                //view.setBackgroundResource(R.drawable.shape_color_task_bg_d);
                tv.setBackgroundResource(R.drawable.shape_main_task_label_6);
                break;
            case 7:// 过站
                tv.setText("连进");
               //TODO 客梯传送部门区分过站航班
//                if (!StringUtil.isEmpty(Tools.getDeptcode()) && Tools.getDeptcode().contains(Constant.DEPT_KETICHUANNSONG))
//                    tv.setBackgroundResource(R.drawable.shape_main_task_label_4);
//                else
                    tv.setBackgroundResource(R.drawable.shape_main_task_label_5);
                break;
            case 8://VIP
                tv.setText("V");
                tv.setBackgroundResource(R.drawable.shape_main_task_label_8);
                break;
            case 9://连出
                tv.setText("连出");
                //TODO 客梯传送部门区分过站航班(特殊颜色区分)
//                if (!StringUtil.isEmpty(Tools.getDeptcode()) && Tools.getDeptcode().contains(Constant.DEPT_KETICHUANNSONG))
//                    tv.setBackgroundResource(R.drawable.shape_main_task_label_4);
//                else
                    //view.setBackgroundResource(R.drawable.shape_color_task_bg_d);
                    tv.setBackgroundResource(R.drawable.shape_main_task_label_6);
                break;
            case 10://是否延误
                tv.setText("D");
                tv.setBackgroundResource(R.drawable.shape_main_task_label_9);
                break;
            case 11://是否备降
                tv.setText("备");
                tv.setBackgroundResource(R.drawable.shape_main_task_label_9);
                break;
            case 12://是否返回
                tv.setText("返");
                tv.setBackgroundResource(R.drawable.shape_main_task_label_9);
                break;
            case 13://快速过站
                tv.setText("快");
                tv.setBackgroundResource(R.drawable.shape_main_task_label_9);
//                view.setBackgroundResource(R.drawable.shape_card_board_guozhan);
                break;
            case 14://快速过站
                tv.setText("重");
                tv.setBackgroundResource(R.drawable.shape_main_task_label_9);
                break;
            case 200://电子进程的状态
                tv.setText("重");
                tv.setBackgroundResource(R.drawable.shape_main_task_label_9);
                break;
            default:
                break;
        }
        layout.addView(tv);
    }
    /**
     * TODO: 控制航班特殊标签显示
     *
     * @param
     * @param layout
     */
    private void showLable(String content, LinearLayout layout) {
        TextView tv = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 0, 0, 0);
        tv.setLayoutParams(params);
        tv.setTextSize(12);
        tv.setTextColor(mContext.getResources().getColor(R.color.white));
        tv.setPadding(5, 3, 5, 3);
//        view.setBackground(mContext.getResources().getDrawable(R.drawable.shape_card_board_nomal));//任务默认背景
        tv.setText(content);
        tv.setBackgroundResource(R.drawable.shape_main_task_label_9);
        layout.addView(tv);
    }
}
