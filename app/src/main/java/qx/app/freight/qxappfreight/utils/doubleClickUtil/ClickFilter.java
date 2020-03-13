package qx.app.freight.qxappfreight.utils.doubleClickUtil;

import android.view.View;

import java.lang.reflect.Field;

public class ClickFilter {
    public static void setFilter(View view) {
        try {
            Field field = View.class.getDeclaredField("mListenerInfo");
            field.setAccessible(true);
            Class listInfoType = field.getType();
            Object listinfo = field.get(view);
            Field onclickField = listInfoType.getField("mOnClickListener");
            View.OnClickListener origin = (View.OnClickListener) onclickField.get(listinfo);
            onclickField.set(listinfo, new ClickProxy(origin));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
