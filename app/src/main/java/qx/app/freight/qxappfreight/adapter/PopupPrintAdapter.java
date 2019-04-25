package qx.app.freight.qxappfreight.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.utils.MapValue;

public class PopupPrintAdapter extends PagerAdapter {

    private Context context;

    private List<MyAgentListBean> list;

    public PopupPrintAdapter(Context context, List<MyAgentListBean> list){

        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.popup_print_preview,null);
        TextView tvWaybillCode = (TextView) view.findViewById(R.id.tv_waybill_number);
        TextView tvNum = (TextView) view.findViewById(R.id.tv_num);
        TextView tvWeight = (TextView) view.findViewById(R.id.tv_weight);
        TextView tvArea = (TextView) view.findViewById(R.id.tv_cabin_area);
        TextView tvCabinNum = (TextView) view.findViewById(R.id.tv_cabin_num);
        TextView tvHandcar = (TextView) view.findViewById(R.id.tv_handcar);

        tvWaybillCode.setText(list.get(position).getWaybillCode());
        tvNum.setText(list.get(position).getNumber()+"件");
        tvWeight.setText(list.get(position).getWeight()+"kg");
        tvArea.setText(list.get(position).getRepName());
        tvCabinNum.setText("无");
        String str = "   第"+(position+1)+"板/共"+list.size()+"板";
        tvHandcar.setText(MapValue.getCarTypeValue(list.get(position).getScooterType()+"")+list.get(position).getScooterCode()+str);

        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container,position,object); 这一句要删除，否则报错
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
