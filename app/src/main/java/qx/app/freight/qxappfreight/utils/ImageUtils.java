package qx.app.freight.qxappfreight.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageUtils {


    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int heigth = dm.heightPixels;
        int width = dm.widthPixels;

        return width - 100;
    }

    public static void setImageHeightFoyWidth(ImageView imageView, int width, int wScale, int hScale) {

        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(
                imageView.getLayoutParams());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margin);
        layoutParams.width = width; // 设置图片的宽度
        layoutParams.height = width * hScale / wScale;// 设置图片的高度
        imageView.setLayoutParams(layoutParams);
    }

}
