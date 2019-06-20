package qx.app.freight.qxappfreight.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import qx.app.freight.qxappfreight.R;

/**
 * 展示航线信息的自定义控件
 */
public class FlightInfoLayout extends LinearLayout {
    private List<String> flghtInfo;
    private Context context;

    public FlightInfoLayout(Context context) {
        super(context);
    }

    public FlightInfoLayout(Context context, List<String> flghtInfo) {
        this(context);
        this.context = context;
        this.flghtInfo = flghtInfo;
        if (flghtInfo.size() != 0) {
            initViewAndListener();
        }
    }

    private void initViewAndListener() {
        final LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_flight_info_base, null, false);
        int size = (flghtInfo.size() == 2) ? 2 : 3;
        for (int j = 0; j < size; j++) {
            TextView tv = new TextView(context);
            String text;
            if (flghtInfo.size() == 2 || flghtInfo.size() == 3) {
                text = flghtInfo.get(j);
            } else {
                text = (j == 2) ? flghtInfo.get(flghtInfo.size() - 1) : ((j == 1) ? "……" : flghtInfo.get(j));
            }
            tv.setText(text);
            tv.setTextColor(Color.parseColor("#333333"));
            tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tv.setTextSize(14);
            tv.setGravity(Gravity.CENTER);
            LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT);
            params.weight = 1;
            params.gravity = Gravity.CENTER;
            view.addView(tv, params);
            if (j != size - 1) {
                ImageView ivPlane = new ImageView(context);
                ivPlane.setImageResource(R.mipmap.airlane);
                view.addView(ivPlane, params);
            }
        }
        if (flghtInfo.size() > 3) {
            view.setOnClickListener(v -> {
                View view1 = LayoutInflater.from(context).inflate(R.layout.layout_flight_info_dialog, null, false);
                final PopupWindow popupWindow = new PopupWindow(view1, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
                LinearLayout llContainer = view1.findViewById(R.id.ll_info_container);
                popupWindow.setTouchable(true);
                for (int i = 0; i < flghtInfo.size(); i++) {
                    TextView tv = new TextView(context);
                    tv.setText(flghtInfo.get(i));
                    tv.setTextColor(Color.parseColor("#333333"));
                    tv.setTextSize(14);
                    tv.setGravity(Gravity.CENTER);
                    LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT);
                    params.weight = 1;
                    params.gravity = Gravity.CENTER;
                    llContainer.addView(tv, params);
                    if (i != flghtInfo.size() - 1) {
                        ImageView ivPlane = new ImageView(context);
                        ivPlane.setImageResource(R.mipmap.airlane);
                        llContainer.addView(ivPlane, params);
                    }
                }
                llContainer.setOnClickListener(v1 -> popupWindow.dismiss());
                popupWindow.setTouchInterceptor((v12, event) -> {
                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                });
                // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
                // 我觉得这里是API的一个bug
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_gray));
                // 设置好参数之后再show
                popupWindow.showAsDropDown(view);
            });
        }
        LayoutParams paramsMain = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(view, paramsMain);
    }
}
