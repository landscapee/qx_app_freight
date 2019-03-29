package qx.app.freight.qxappfreight.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.PushBaseBean;

/**
 * TODO : 锁屏消息列表数据 适配器
 * Created by ym
 * on 2016-11-14.
 */
public class RecLockAdapter extends RecyclerView.Adapter<RecLockAdapter.ViewHolder> {
    private List<PushBaseBean> mList;
    private Context mContext;

    public RecLockAdapter(Context mContext, List<PushBaseBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    public void updateLists(List<PushBaseBean> lists) {
        this.mList = lists;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lock_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PushBaseBean model = mList.get(position);
        holder.mTvContent.setText(model.getContent());
        String extra = model.getExtra();
        setTypeTitle(model.getMsgtype(), holder.mTvTitle);
    }

    private void setTypeTitle(String type, TextView title) {
        title.setText("推送通知");
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.lin_task_item_layout)
        LinearLayout mLinTaskItemLayout;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
