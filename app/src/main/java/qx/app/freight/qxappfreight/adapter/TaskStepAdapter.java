package qx.app.freight.qxappfreight.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

//        ImageView ivLeftGif = helper.getView(R.id.iv_left_gif);//开始滑动的GIF
//        ImageView ivLeftGifE = helper.getView(R.id.iv_left_gif_e);// 结束滑动的GIF


        RelativeLayout rlAccept = helper.getView(R.id.rl_back_accept);
        RelativeLayout rlStart = helper.getView(R.id.rl_back_start);
        RelativeLayout rlEnd = helper.getView(R.id.rl_back_end);

        SlideLeftExecuteView mSlideLeftExecuteViewA = helper.getView(R.id.slide_left_accept);
        SlideLeftExecuteView mSlideLeftExecuteViewS = helper.getView(R.id.slide_left_start);
        SlideLeftExecuteView mSlideLeftExecuteViewE = helper.getView(R.id.slide_left_end);

        TextView tvAccept = helper.getView(R.id.tv_step_time_accept);
        TextView tvStart = helper.getView(R.id.tv_step_time_start);
        TextView tvEnd = helper.getView(R.id.tv_step_time_end);

        Button btnFS = helper.getView(R.id.btn_flight_safeguard);
        btnFS.setOnClickListener(v -> {
            listener.onFlightSafeguardClick(helper.getAdapterPosition());
        });

        /**
         *  领受是否可以滑动 （任务有了领受时间 就不能再次滑动领受了）
         */
        if(item.get(0).getAcceptTime() > 0){
            rlAccept.setBackgroundResource(R.drawable.shape_rect_green_light);
            tvAccept.setText(TimeUtils.date2Tasktime3(item.get(0).getAcceptTime()));
            mSlideLeftExecuteViewA.setVisibility(View.GONE);
            tvAccept.setVisibility(View.VISIBLE);
        }
        else {
            rlStart.setBackgroundResource(R.drawable.shape_rect_gray_dark);
            mSlideLeftExecuteViewA.setVisibility(View.VISIBLE);
            tvAccept.setVisibility(View.GONE);
        }
        /**
         *  开始是否可以滑动 （任务有了领受时间 并且 没有 开始时间 才能滑动开始）
         */
        if(item.get(0).getTaskBeginTime() > 0){
            rlStart.setBackgroundResource(R.drawable.shape_rect_green_light);
            tvStart.setText(TimeUtils.date2Tasktime3(item.get(0).getTaskBeginTime()));
            mSlideLeftExecuteViewS.setVisibility(View.GONE);
            tvStart.setVisibility(View.VISIBLE);
        } else if (item.get(0).getAcceptTime() > 0){
            rlStart.setBackgroundResource(R.drawable.shape_rect_gray_dark);
            mSlideLeftExecuteViewS.setVisibility(View.VISIBLE);
            tvStart.setVisibility(View.GONE);
        }
        else {
            rlStart.setBackgroundResource(R.drawable.shape_rect_gray_dark);
            mSlideLeftExecuteViewS.setVisibility(View.GONE);
            tvStart.setVisibility(View.GONE);
        }
        /**
         *  结束是否可以滑动 （任务有了开始时间 并且 没有 结束时间 才能滑动结束）
         */
        if(item.get(0).getTaskEndTime() > 0){
            rlEnd.setBackgroundResource(R.drawable.shape_rect_green_light);
            tvEnd.setText(TimeUtils.date2Tasktime3(item.get(0).getTaskEndTime()));
            mSlideLeftExecuteViewE.setVisibility(View.GONE);
            tvEnd.setVisibility(View.VISIBLE);
        }
        else if (item.get(0).getTaskBeginTime() > 0){
            rlEnd.setBackgroundResource(R.drawable.shape_rect_gray_dark);
            mSlideLeftExecuteViewE.setVisibility(View.VISIBLE);
            tvEnd.setVisibility(View.GONE);
        }
        else {
            rlEnd.setBackgroundResource(R.drawable.shape_rect_gray_dark);
            mSlideLeftExecuteViewE.setVisibility(View.GONE);
            tvEnd.setVisibility(View.GONE);
        }
        LinearLayout llFlightInfo =  helper.getView(R.id.ll_flight_info);
        if (item.get(0).getFlights() == null){
            llFlightInfo.setVisibility(View.GONE);
        }
        else {
            llFlightInfo.setVisibility(View.VISIBLE);
            //列表设置
            RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);
            RecyclerView mRecyclerViewFlight = helper.getView(R.id.rv_flight);
            mRecyclerViewFlight.setLayoutManager(manager);

            List<OutFieldTaskBean> list1 = new ArrayList<>();
            list1.addAll(item);
            TaskFlightAdapter mTaskFlightAdapter = new TaskFlightAdapter(list1);
            mRecyclerViewFlight.setAdapter(mTaskFlightAdapter);
        }


//        Glide.with(mContext).load(R.mipmap.slide_do_gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivLeftGif);
//        mSlideLeftExecuteViewS.setVisibility(View.GONE);
//        Glide.with(mContext).load(R.mipmap.slide_do_gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivLeftGifE);
//        mSlideLeftExecuteViewE.setVisibility(View.GONE);


//        ivLeftGif.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                ivLeftGif.setVisibility(View.GONE);
//                mSlideLeftExecuteViewS.setVisibility(View.VISIBLE);
//                return false;
//            }
//        });
//        ivLeftGifE.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                ivLeftGifE.setVisibility(View.GONE);
//                mSlideLeftExecuteViewE.setVisibility(View.VISIBLE);
//                return false;
//            }
//        });
        mSlideLeftExecuteViewA.setLockListener(() -> {
            listener.onSlideExecuteListener(2,helper.getAdapterPosition());
        });
        mSlideLeftExecuteViewS.setLockListener(() -> {
            listener.onSlideExecuteListener(0,helper.getAdapterPosition());
        });
        mSlideLeftExecuteViewE.setLockListener(() -> {
            listener.onSlideExecuteListener(1,helper.getAdapterPosition());
        });
        //滑动取消 显示gif
        mSlideLeftExecuteViewS.setLockCancelListener(() ->{
//            ivLeftGif.setVisibility(View.VISIBLE);
//            mSlideLeftExecuteViewS.setVisibility(View.GONE);
        });
        mSlideLeftExecuteViewE.setLockCancelListener(() ->{
//            ivLeftGifE.setVisibility(View.VISIBLE);
//            mSlideLeftExecuteViewE.setVisibility(View.GONE);
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
        void onFlightSafeguardClick(int position);

    }

    public void setOnSlideExecuteListener(onSlideExecuteListener listener){

        this.listener = listener;
    }
}
