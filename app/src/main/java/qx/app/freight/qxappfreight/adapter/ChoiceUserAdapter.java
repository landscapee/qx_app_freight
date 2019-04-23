package qx.app.freight.qxappfreight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TestInfoListBean;

public class ChoiceUserAdapter extends BaseAdapter {

    private Context mContext;
    private List<TestInfoListBean.FreightInfoBean> mList;
    private LayoutInflater mInflater;

    public ChoiceUserAdapter(Context context, List<TestInfoListBean.FreightInfoBean> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_choice_list, viewGroup, false);
            holder = new ChoiceUserAdapter.ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ChoiceUserAdapter.ViewHolder) view.getTag();
        }
        holder.mTvItemUser.setText(mList.get(position).getInspectionName());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_item_user)
        TextView mTvItemUser;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface onItemClickLinstener {
        void clickItem(int position);
    }
}
