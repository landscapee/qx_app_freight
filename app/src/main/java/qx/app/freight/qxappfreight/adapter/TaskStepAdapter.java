package qx.app.freight.qxappfreight.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.TestActivity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldFlightBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskMyBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.SlideLeftExecuteView;

public class TaskStepAdapter extends BaseQuickAdapter<List<OutFieldTaskBean>, BaseViewHolder> {

    private  onSlideExecuteListener listener;

    public TaskStepAdapter(List<List<OutFieldTaskBean>> list) {
        super(R.layout.item_slide_left_execute, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, List<OutFieldTaskBean> item) {

        helper.setText(R.id.tv_step_accept, "领受");
        helper.setText(R.id.tv_step_start, "开始");
        helper.setText(R.id.tv_step_name_end, "结束");

        helper.setText(R.id.tv_step_time_accept, TimeUtils.date2Tasktime3(item.get(0).getAcceptTime()));
        helper.setText(R.id.tv_step_time_start,"12:12");
        helper.setText(R.id.tv_step_time_end,"12:22");

        RelativeLayout rlStart = helper.getView(R.id.rl_back_start);
        RelativeLayout rlEnd = helper.getView(R.id.rl_back_end);

        SlideLeftExecuteView mSlideLeftExecuteViewS = helper.getView(R.id.slide_left_start);
        SlideLeftExecuteView mSlideLeftExecuteViewE = helper.getView(R.id.slide_left_end);


        if(item.get(0).getTaskBeginTime() > 0){
            rlStart.setBackgroundResource(R.drawable.shape_rect_green_light);
            helper.setText(R.id.tv_step_time_start,TimeUtils.date2Tasktime3(item.get(0).getTaskBeginTime()));
            mSlideLeftExecuteViewE.setVisibility(View.GONE);
        }
        else {
            rlStart.setBackgroundResource(R.drawable.shape_rect_gray_dark);
            helper.setText(R.id.tv_step_time_start,"");
            mSlideLeftExecuteViewE.setVisibility(View.VISIBLE);

        }
        if(item.get(0).getTaskEndTime() > 0){
            rlEnd.setBackgroundResource(R.drawable.shape_rect_green_light);
            helper.setText(R.id.tv_step_time_end,TimeUtils.date2Tasktime3(item.get(0).getTaskEndTime()));
            mSlideLeftExecuteViewE.setVisibility(View.GONE);
        }
        else {
            rlEnd.setBackgroundResource(R.drawable.shape_rect_gray_dark);
            helper.setText(R.id.tv_step_time_end,"");
            mSlideLeftExecuteViewE.setVisibility(View.GONE);
        }

        //列表设置
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);
        RecyclerView mRecyclerViewFlight = helper.getView(R.id.rv_flight);
        mRecyclerViewFlight.setLayoutManager(manager);

        List<OutFieldTaskBean> list1 = new ArrayList<>();
        list1.addAll(item);
        TaskFlightAdapter mTaskFlightAdapter = new TaskFlightAdapter(list1);
        mRecyclerViewFlight.setAdapter(mTaskFlightAdapter);



        mSlideLeftExecuteViewS.setLockListener(() -> {
            listener.onSlideExecuteListener(0,helper.getAdapterPosition());
        });


        mSlideLeftExecuteViewE.setLockListener(() -> {
            listener.onSlideExecuteListener(1,helper.getAdapterPosition());
        });

        rlStart.setOnClickListener(v ->{

            if(item.get(0).getTaskBeginTime() > 0)
                listener.onClickListener(0,helper.getAdapterPosition());

        });
        rlEnd.setOnClickListener(v ->{
            if(item.get(0).getTaskEndTime() > 0)
                listener.onClickListener(1,helper.getAdapterPosition());

        });

    }


    public interface onSlideExecuteListener{

        void onSlideExecuteListener(int step,int position);
        void onClickListener(int step,int position);

    }

    public void setOnSlideExecuteListener(onSlideExecuteListener listener){

        this.listener = listener;
    }
}
