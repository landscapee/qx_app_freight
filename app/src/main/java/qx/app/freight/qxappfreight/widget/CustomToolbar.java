package qx.app.freight.qxappfreight.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.utils.DisplayUtil;

/**
 * 顶部通用的状态栏
 */
public class CustomToolbar extends LinearLayout {
    private ImageView mLeftIv;//左边图标
    private TextView mLeftTextTv;//左边文字
    private TextView mTitleTv;//中间标题
    private TextView mRightTextTv;//右边文字
    private ImageView mRightIv;//右边图标

    public CustomToolbar(Context context) {
        this(context, null);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_simple_toolbar, this);
        mLeftIv = findViewById(R.id.left_icon_iv);
        mLeftTextTv = findViewById(R.id.left_text_tv);
        mTitleTv = findViewById(R.id.title_tv);
        mRightTextTv = findViewById(R.id.right_text_tv);
        mRightIv = findViewById(R.id.right_icon_iv);
    }

    /**
     * 设置左边图标
     *
     * @param show     是否显示
     * @param iconSrc  资源id
     * @param listener 点击监听
     */
    public void setLeftIconView(int show, int iconSrc, OnClickListener listener) {
        mLeftIv.setVisibility(show);
        mLeftIv.setImageResource(iconSrc);
        mLeftIv.setOnClickListener(listener);
    }

    /**
     * 设置左边文字
     *
     * @param show     是否显示
     * @param color    文字颜色
     * @param text     文字内容
     * @param listener 点击监听
     */
    public void setLeftTextView(int show, int color, String text, OnClickListener listener) {
        mLeftTextTv.setVisibility(show);
        mLeftTextTv.setTextColor(color);
        mLeftTextTv.setText(text);
        mLeftTextTv.setOnClickListener(listener);
    }

    /**
     * 设置中间标题
     *
     * @param color 文字颜色
     * @param text  文字内容
     */
    public void setMainTitle(int color, String text) {
        mTitleTv.setTextColor(color);
        mTitleTv.setText(text);
    }

    /**
     * 设置右边文字
     *
     * @param show     是否显示
     * @param color    文字颜色
     * @param text     文字内容
     * @param listener 点击监听
     */
    public void setRightTextView(int show, int color, String text, OnClickListener listener) {
        mRightTextTv.setVisibility(show);
        mRightTextTv.setTextColor(color);
        mRightTextTv.setText(text);
        mRightTextTv.setOnClickListener(listener);
    }
    /**
     * 设置右边文字
     *
     * @param show     是否显示
     * @param color    文字颜色
     * @param text     文字内容
     * @param listener 点击监听
     */
    public void setRightTextViewImage(Context context,int show, int color, String text, int left, OnClickListener listener) {
        mRightTextTv.setVisibility(show);
        mRightTextTv.setTextColor(color);
        mRightTextTv.setText(text);
        mRightTextTv.setOnClickListener(listener);
//        mRightTextTv.setHeight(DisplayUtil.dip2px(context,27));
//        mRightTextTv.setWidth(DisplayUtil.dip2px(context,60));
        Drawable drawable = context.getDrawable(left);
        mRightTextTv.setPadding(DisplayUtil.dip2px(context,10),DisplayUtil.dip2px(context,5),DisplayUtil.dip2px(context,10),DisplayUtil.dip2px(context,5));
        mRightTextTv.setBackgroundResource(R.drawable.background_press_white);
        mRightTextTv.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
        mRightTextTv.setCompoundDrawablePadding(5);
    }

    /**
     * 设置右边图标
     *
     * @param show     是否显示
     * @param iconSrc  资源id
     * @param listener 点击监听
     */
    public void setRightIconView(int show, int iconSrc, OnClickListener listener) {
        mRightIv.setVisibility(show);
        mRightIv.setImageResource(iconSrc);
        mRightIv.setOnClickListener(listener);
    }

    public void setRightIconViewVisiable(boolean isShow){
        if (isShow){
            mRightIv.setVisibility(View.VISIBLE);
        }else {
            mRightIv.setVisibility(View.GONE);
        }

    }
}
