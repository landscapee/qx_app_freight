package qx.app.freight.qxappfreight.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import qx.app.freight.qxappfreight.R;

public class SortingInfoAdapter extends RecyclerView.Adapter<SortingInfoAdapter.Myholder> {

    Context context;
    List mList;

    public SortingInfoAdapter(Context context, List mList){
        this.context = context;
        this.mList = mList;
    }


    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sorting_info, viewGroup, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder myholder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class Myholder extends RecyclerView.ViewHolder{

        TextView noTv, numberTv, areaTv, postionTv, remarkTv;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            noTv = itemView.findViewById(R.id.tv_orderid);
            numberTv = itemView.findViewById(R.id.tv_goods_number);
            areaTv = itemView.findViewById(R.id.tv_area);
            postionTv = itemView.findViewById(R.id.tv_postion);
        }
    }
}
