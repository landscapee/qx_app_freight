package qx.app.freight.qxappfreight.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.DriverOutDoingActivity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoMyBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskMyBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CollapsableLinearLayout;

/**
 * 运输adapter
 *
 */
public class DriverOutTaskAdapter extends BaseQuickAdapter<AcceptTerminalTodoBean, BaseViewHolder>{

    private OnStepListener mOnStepListener;

    public DriverOutTaskAdapter(List<AcceptTerminalTodoBean> mDatas) {
        super(R.layout.item_driver_out_task, mDatas);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    protected void convert(BaseViewHolder helper, AcceptTerminalTodoBean item) {

        helper.setText(R.id.tv_task_id,item.getTaskNumber());
        helper.setText(R.id.tv_task_num,"任务单号:"+item.getTaskId());
        helper.setText(R.id.tv_task_type,item.getProjectName());
        helper.setText(R.id.tv_task_status,"#执行中#");
        //列表设置
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);
        RecyclerView mRecyclerView = helper.getView(R.id.rv_step);
        mRecyclerView.setLayoutManager(manager);

        List<List<OutFieldTaskBean>> list1 = new ArrayList<>();
        list1.addAll(item.getUseTasks());
        TaskStepAdapter mTaskStepAdapter = new TaskStepAdapter(list1);
        mRecyclerView.setAdapter(mTaskStepAdapter);
        CollapsableLinearLayout collView = helper.getView(R.id.coll_listview);

        ImageView imageExpand =  helper.getView(R.id.iv_expand);
        View view = helper.getConvertView();
        view.setOnClickListener(v -> {

            if (item.isExpand()) {
                mRecyclerView.setVisibility(View.GONE);
                collView.collapse();
                item.setExpand(false);
                imageExpand.setImageResource(R.mipmap.down);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                collView.expand();
                item.setExpand(true);
                imageExpand.setImageResource(R.mipmap.up);
            }
        });

        if (item.isExpand()){
            mRecyclerView.setVisibility(View.VISIBLE);
            collView.expand();
            imageExpand.setImageResource(R.mipmap.up);
        }
        else {
            mRecyclerView.setVisibility(View.GONE);
            collView.collapse();
            imageExpand.setImageResource(R.mipmap.down);

        }
        mTaskStepAdapter.setOnSlideExecuteListener(new TaskStepAdapter.onSlideExecuteListener() {
            @Override
            public void onSlideExecuteListener(int step, int position) {
                switch (step){
                    case 0:
                        if ("device".equals(item.getUseTasks().get(position).get(0).getCargoType()))//空板运输
                            mOnStepListener.onStepListener(step,helper.getAdapterPosition(),position);
                        else
                            DriverOutDoingActivity.startActivity(helper.getConvertView().getContext(),item.getUseTasks().get(position));
                        break;
                    case 1:
                        if ("device".equals(item.getUseTasks().get(position).get(0).getCargoType()))
                            mOnStepListener.onStepListener(step,helper.getAdapterPosition(),position);
                        else
                        DriverOutDoingActivity.startActivity(helper.getConvertView().getContext(),item.getUseTasks().get(position));
                        break;
                }
            }
            @Override
            public void onClickListener(int step, int position) {
                switch (step){
                    case 0:
                        DriverOutDoingActivity.startActivity(helper.getConvertView().getContext(),item.getUseTasks().get(position));
                        break;
                    case 1:
                        DriverOutDoingActivity.startActivity(helper.getConvertView().getContext(),item.getUseTasks().get(position));
                        break;

                }
            }
        });
    }

    public interface OnStepListener{

        void onStepListener(int step,int parentPosition,int position);

    }

    public void setmOnStepListener(OnStepListener onStepListener){
        this.mOnStepListener = onStepListener;
    }


}
