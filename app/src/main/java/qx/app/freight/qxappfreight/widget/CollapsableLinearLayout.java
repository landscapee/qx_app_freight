package qx.app.freight.qxappfreight.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * 可折叠式布局
 */
public class CollapsableLinearLayout extends LinearLayout {

    /**
     * 是否已经展开
     */
    public boolean expanded = false;
    /**
     * expand animation time展开动画时长
     */
    public static int EXPAND_TIME = 300;
    /**
     * collapse animation time折叠动画时长
     */
    public static int COLLAPSE_TIME = 300;

    public CollapsableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CollapsableLinearLayout(Context context) {
        super(context);
    }

    /**
     * default收起
     */
    public void init() {
        final int initialHeight = getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t) {
                if (interpolatedTime == 1) {
                    setVisibility(View.GONE);
                } else {
                    getLayoutParams().height = initialHeight
                            - (int) (initialHeight * interpolatedTime);
                    requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration(0);
        startAnimation(a);
        expanded = false;
    }


    public void toggle() {
        if (expanded) {
            collapse();
        } else {
            expand();
        }
    }

    /**
     * 收起其他的view
     */
    public void collapseOther() {
        collapse();
    }

    /**
     * 展开
     */
    public void expand() {
        measure(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targtetHeight = getMeasuredHeight();

        getLayoutParams().height = 0;
        this.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t) {
                getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
                        : (int) (targtetHeight * interpolatedTime);
                requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration(EXPAND_TIME);
        startAnimation(a);
        expanded = true;
        if (toggleView != null) {
            rotateToggleView(toggleView, expanded);
        }
    }

    /**
     * 展开
     */
    public void expand(int duration) {
        measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targtetHeight = getMeasuredHeight();

        getLayoutParams().height = 0;
        this.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t) {
                getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
                        : (int) (targtetHeight * interpolatedTime);
                requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration(duration);
        startAnimation(a);
        expanded = true;
        if (toggleView != null) {
            rotateToggleView(toggleView, expanded);
        }
    }

    /**
     * 收起
     */
    public void collapse() {
        final int initialHeight = getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t) {
                if (interpolatedTime == 1) {
                    setVisibility(View.GONE);
                } else {
                    getLayoutParams().height = initialHeight
                            - (int) (initialHeight * interpolatedTime);
                    requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(COLLAPSE_TIME);
        startAnimation(a);

        expanded = false;
        //旋转箭头
        if (toggleView != null) {
            rotateToggleView(toggleView, expanded);
        }
    }

    private View toggleView;

    public void setToggleView(View toggleView) {
        this.toggleView = toggleView;
    }

    private void rotateToggleView(View view, boolean isExpand) {
        // view.clearAnimation();
        float fromDegress = 0;
        float toDegress = 0;
        if (isExpand) {
            // Log.i("test", "向上");
            fromDegress = 0;
            toDegress = -180;
        } else {
            // Log.i("test", "向下");
            fromDegress = -180;
            toDegress = 0;
        }
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegress,
                toDegress, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(300);
        rotateAnimation.setFillAfter(true);
        view.startAnimation(rotateAnimation);
    }

    public boolean isExpanded() {
        return expanded;
    }


}