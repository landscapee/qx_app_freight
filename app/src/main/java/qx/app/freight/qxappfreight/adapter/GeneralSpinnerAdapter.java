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
import qx.app.freight.qxappfreight.bean.request.GeneralSpinnerBean;

/**
 * 常用选项适配器
 */
public class GeneralSpinnerAdapter<T extends GeneralSpinnerBean.BaseViolationT> extends BaseAdapter {
    private List<T> mDevList;
    private LayoutInflater mInflater;

    public GeneralSpinnerAdapter(Context context, List<T> taskList) {
        mDevList = taskList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDevList == null ? 0 : mDevList.size();
    }

    @Override
    public T getItem(int i) {
        return mDevList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_spinner_general, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final T model = mDevList.get(i);
        holder.mTvContent.setText(model.getValue());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_content)
        TextView mTvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
