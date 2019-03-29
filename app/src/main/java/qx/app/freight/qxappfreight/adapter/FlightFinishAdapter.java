package qx.app.freight.qxappfreight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.MilepostBean;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.BubbleTextVew;

/**
 * TODO :  航班完成进度适配器
 * Created by owen
 * on 2016-09-18.
 */
public class FlightFinishAdapter extends BaseAdapter {
    private Context mContext;
    private List<MilepostBean> mList;
    private LayoutInflater mInflater;
    private onItemClickLinstener mItemClickLinstener;

    public FlightFinishAdapter(Context context, List<MilepostBean> list, onItemClickLinstener onItemClickLinstener) {
        mContext = context;
        mList = list;
        mItemClickLinstener = onItemClickLinstener;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_flight_finish, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final MilepostBean model = mList.get(i);
        if (i % 2 == 0) {
            holder.mItemLayoutLeft.setVisibility(View.INVISIBLE);
            holder.mItemLayoutRight.setVisibility(View.VISIBLE);
            holder.mTvContentRight.setText(model.getMilestoneName());
            holder.mTvTimeRight.setText(Tools.returnTime(model.getCreateTime()) + "|" + Tools.returnTime(model.getActualTime()));
            setBubbColor(holder.mTvContentRight, holder.mRound, model);
        } else {
            holder.mTvContentLeft.setText(model.getMilestoneName());
            holder.mTvTimeLeft.setText(Tools.returnTime(model.getCreateTime()) + "|" + Tools.returnTime(model.getActualTime()));
            holder.mItemLayoutLeft.setVisibility(View.VISIBLE);
            holder.mItemLayoutRight.setVisibility(View.INVISIBLE);
            setBubbColor(holder.mTvContentLeft, holder.mRound, model);
        }
        holder.mItemLayout.setOnClickListener(view1 -> mItemClickLinstener.clickItem(i));
        return view;
    }

    private void setBubbColor(BubbleTextVew v, View round, MilepostBean model) {
        v.setTextColor(mContext.getResources().getColor(R.color.white));
        round.setBackgroundResource(R.drawable.shape_white_dot);
        v.setBubbleColor(mContext.getResources().getColor(R.color.white));
        v.setTextColor(mContext.getResources().getColor(R.color.deepskyblue));
    }

    static class ViewHolder {
        @BindView(R.id.tv_time_left)
        TextView mTvTimeLeft;
        @BindView(R.id.tv_content_left)
        BubbleTextVew mTvContentLeft;
        @BindView(R.id.item_layout_left)
        RelativeLayout mItemLayoutLeft;
        @BindView(R.id.tv_content_right)
        BubbleTextVew mTvContentRight;
        @BindView(R.id.tv_time_right)
        TextView mTvTimeRight;
        @BindView(R.id.item_layout_right)
        RelativeLayout mItemLayoutRight;
        @BindView(R.id.round)
        View mRound;
        @BindView(R.id.item_layout)
        RelativeLayout mItemLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface onItemClickLinstener {
        void clickItem(int position);
    }
}
