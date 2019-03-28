package qx.app.freight.qxappfreight.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;

import qx.app.freight.qxappfreight.bean.response.NoticeBean;

public class MyTabAdapter extends CommonNavigatorAdapter {
    private Context mContext;
    private List<NoticeBean> mList;


    public MyTabAdapter(Context context, List<NoticeBean> noticeBeanList) {
        mContext = context;
        mList = noticeBeanList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int index) {
        SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
        //默认颜色
        simplePagerTitleView.setNormalColor(Color.parseColor("#888888"));
        //选中颜色
        simplePagerTitleView.setSelectedColor(Color.parseColor("#2E81FD"));
        //文字
        simplePagerTitleView.setText(mList.get(index).getPages() + "");
        simplePagerTitleView.setPadding(13, 13, 13, 13);
        //大小
        simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
        linePagerIndicator.setColors(Color.parseColor("#2E81FD"));
        linePagerIndicator.setLineHeight(6f);
        linePagerIndicator.setRoundRadius(4f);
        linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        return linePagerIndicator;
    }
}
