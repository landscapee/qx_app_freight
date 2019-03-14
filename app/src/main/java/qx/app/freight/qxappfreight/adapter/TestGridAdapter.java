package qx.app.freight.qxappfreight.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.model.TestBean;

public class TestGridAdapter extends BaseAdapter {

    private List<TestBean> data;//数据

    private Context context;//上下文
    private TestInterface testInterface;

    public TestGridAdapter(List<TestBean> data, Context context) {
        this.data = data;
        this.context = context;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public TestBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            //加载子布局
            view = LayoutInflater.from(context).inflate(R.layout.item_test_grid, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = view.findViewById(R.id.text1);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.linearLayout = view.findViewById(R.id.ll_backgroud);

        switch (data.get(position).getType()){
            case 0:
                viewHolder.linearLayout.setBackgroundResource(R.color.test_1);
                viewHolder.textView.setTextColor(Color.parseColor("#888888"));
                break;
            case 1:
                viewHolder.linearLayout.setBackgroundResource(R.color.test_2);
                break;
            case 2:
                viewHolder.linearLayout.setBackgroundResource(R.color.test_3);

                viewHolder.textView.setTextColor(Color.WHITE);
                break;
        }

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testInterface.clickButton(position);
            }
        });
        viewHolder.textView.setText(""+data.get(position).getNumber());
        return view;

    }

    public void setItemInterface(TestInterface itemInter){
        this.testInterface = itemInter;
    }

    private class ViewHolder {
        LinearLayout linearLayout;
        TextView textView;
    }

    public interface TestInterface{
        void clickButton(int position);
    }
}
