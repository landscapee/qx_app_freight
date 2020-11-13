package qx.app.freight.qxappfreight.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.FlightOfScooterBean;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 运输adapter
 */
public class DriverOutTaskDoingAdapter extends BaseQuickAdapter<FlightOfScooterBean, BaseViewHolder> {

    private DriverOutTaskDoingAdapter.OnDeleteClickLister mDeleteClickListener;

    private DriverOutTaskDoingAdapter.OnCheckBoxClick mCheckBoxClick;

    private boolean isEnable = false;

    private boolean ismIsSlide = true;

    public DriverOutTaskDoingAdapter(List<FlightOfScooterBean> mDatas) {
        super(R.layout.item_task_flight_select, mDatas);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    protected void convert(BaseViewHolder helper, FlightOfScooterBean item) {

        helper.setText(R.id.tv_flight_number, item.getFlightNo());
        helper.setText(R.id.tv_flight_type, item.getPlaneType());
        helper.setText(R.id.tv_flight_place, item.getPlanePlace());

        helper.setText(R.id.tv_arrive_time, StringUtil.format(mContext, R.string.format_arrive_info, TimeUtils.date2Tasktime3(item.getEtd()), TimeUtils.getDay(item.getEtd())));
        if(item.getNum()>0){
            helper.setText(R.id.tv_flight_task_type, item.getNum() + "个" + MapValue.getCarTypeValue(item.getCarType()));
        }
        else {
            helper.setText(R.id.tv_flight_task_type, "");
        }

        helper.setChecked(R.id.cb_flight, item.isSelect());

        helper.setText(R.id.tv_begin, MapValue.getLocationValue(item.getMTransportTodoListBeans().get(0).getTpStartLocate()) + item.getMTransportTodoListBeans().get(0).getBeginAreaId());
        helper.setText(R.id.tv_end, MapValue.getLocationValue(item.getMTransportTodoListBeans().get(0).getTpDestinationLocate()) + item.getMTransportTodoListBeans().get(0).getEndAreaId());
        FlightInfoLayout layout = new FlightInfoLayout(mContext, item.getFlightRoute());
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout container = helper.getView(R.id.ll_flight_info_container);
        container.removeAllViews();
        container.addView(layout, paramsMain);
        ImageView ivFlag = helper.getView(R.id.iv_flag);
        if ("离".equals(item.getMTransportTodoListBeans().get(0).getTpType()) || "拉".equals(item.getMTransportTodoListBeans().get(0).getTpType())) {
            ivFlag.setImageResource(R.mipmap.li);
        } else {
            ivFlag.setImageResource(R.mipmap.jin);
        }
        CheckBox checkBox = helper.getView(R.id.cb_flight);
        checkBox.setEnabled(isEnable);

        if (isEnable) {
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }

        //列表设置
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);

        SlideRecyclerView doingSlideRecyclerView = helper.getView(R.id.slrv_handcar);
        doingSlideRecyclerView.setLayoutManager(manager);

        HandcarBacklogTPAdapter mHandcarBacklogTPAdapter = new HandcarBacklogTPAdapter(item.getMTransportTodoListBeans());
        doingSlideRecyclerView.setAdapter(mHandcarBacklogTPAdapter);
        doingSlideRecyclerView.setIsmIsSlide(ismIsSlide);
        mHandcarBacklogTPAdapter.setOnDeleteClickListener((view12, position) -> {
            doingSlideRecyclerView.closeMenu();
            mDeleteClickListener.onDeleteClick(view12, helper.getAdapterPosition(), position);

        });
        mHandcarBacklogTPAdapter.setOnItemClickListener((adapter, view1, position) -> {

        });

        checkBox.setOnClickListener(v ->
                mCheckBoxClick.onCheckBoxClick(v, helper.getAdapterPosition()));
    }

    public void setCheckBoxEnable(boolean isEnable) {
        this.isEnable = isEnable;
        notifyDataSetChanged();
    }

    public void setIsmIsSlide(boolean ismIsSlide) {

        this.ismIsSlide = ismIsSlide;
        notifyDataSetChanged();
    }


    public void setOnDeleteClickListener(DriverOutTaskDoingAdapter.OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int parentPosition, int childPosition);
    }

    public void setCheckBoxListener(DriverOutTaskDoingAdapter.OnCheckBoxClick listener) {

        this.mCheckBoxClick = listener;
    }

    public interface OnCheckBoxClick {

        void onCheckBoxClick(View view, int position);
    }

}
