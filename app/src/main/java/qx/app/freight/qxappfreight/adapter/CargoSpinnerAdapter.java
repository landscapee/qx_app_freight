package qx.app.freight.qxappfreight.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import qx.app.freight.qxappfreight.R;

public class CargoSpinnerAdapter extends ArrayAdapter <String> {
    private Context mContext;
    private  List<String> mStringArray;
    private int resId;
    public CargoSpinnerAdapter(Context context,int resId, List<String> stringArray) {
        super(context, resId, stringArray);
        mContext = context;
        mStringArray=stringArray;
        this.resId =resId;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //修改Spinner展开后的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(resId, parent,false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.tv_content);
        tv.setText(mStringArray.get(position));
        tv.setTextSize(22f);
        tv.setTextColor(Color.RED);

        return convertView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 修改Spinner选择后结果的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(resId, parent, false);
        }
        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.tv_content);
        tv.setText(mStringArray.get(position));
        tv.setTextSize(14f);
        tv.setTextColor(mContext.getResources().getColor(R.color.black_3));
        return convertView;
    }
}
