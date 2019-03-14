package qx.app.freight.qxappfreight.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import qx.app.freight.qxappfreight.R;


/**
 * TODO :
 * Created by owen
 * on 2016-10-10.
 */
public class SlideRightExecuteView extends AppCompatTextView {
    private static final String TAG = "SlideLockView";
    private Bitmap mLockBitmap;
    private int mLockDrawableId;
    private Paint mPaint;
    private int mLockHeight;
    private int height;
    private float mLocationX;
    private boolean mIsDragable = false;
    private boolean isCanTouch = true;// 是否允许滑动
    private OnLockListener mLockListener;
    private OnTouchListener mOnTouchListener;

    private int imgWidth,imgHeight;

    public SlideRightExecuteView(Context context) {
        this(context, null);
    }

    public SlideRightExecuteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideRightExecuteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.SlideLockView, defStyleAttr, 0);
        mLockDrawableId = tp.getResourceId(R.styleable.SlideLockView_lock_drawable, -1);
        mLockHeight = tp.getDimensionPixelOffset(R.styleable.SlideLockView_lock_radius, 1);
        tp.recycle();
        if (mLockDrawableId == -1) {
            throw new RuntimeException("未设置滑动解锁图片");
        }
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight();
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mLockBitmap = BitmapFactory.decodeResource(context.getResources(), mLockDrawableId);

        imgWidth= mLockBitmap.getWidth();
        imgHeight = mLockBitmap.getHeight();

//        // 将图片进行缩小或者放大
        int oldSize = mLockBitmap.getHeight();
        int newSize = mLockHeight;
        float scale = newSize * 1.0f / oldSize;// 缩放值
        Matrix matrix = new Matrix();
        matrix.setScale(1, scale);
        mLockBitmap = Bitmap.createBitmap(mLockBitmap, 0, 0, imgWidth, imgHeight, matrix, true);

        mLockHeight = mLockBitmap.getHeight() / 2;

    }

    /**
     * TODO: 重绘控件
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int rightMax = getWidth() - imgWidth;
        // 保证滑动图片绘制居中 (height / 2 - mLockRadius)
        if (mLocationX < 0) {
            canvas.drawBitmap(mLockBitmap, 0, height / 2 - mLockHeight, mPaint);
        } else if (mLocationX > rightMax) {
            canvas.drawBitmap(mLockBitmap, rightMax, height / 2 - mLockHeight, mPaint);
        } else {
            canvas.drawBitmap(mLockBitmap, mLocationX, height / 2 - mLockHeight, mPaint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mIsDragable)
            getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    /**
     * TODO: 滑动事件
     * 1、当触摸屏幕是触发ACTION_DOWN事件，计算时候触摸到锁，只有当触到锁的时候才能滑动；
     * 2、手指移动时，获得新的位置后计算新的位置，然后重新绘制，若移动到另一端表示解锁成功，执行回调方法解锁成功；
     * 3、手指离开屏幕后重新reset View,动画回到初始位置
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rightMax = getWidth() - imgWidth;// 右滑成功距离
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://开始触摸
                callTouch(true);
                float xPos = event.getX();
                float yPos = event.getY();
                if (isCanTouch && isTouchLock(xPos, yPos)) {
                    Log.d(TAG, "触摸目标");
                    mLocationX = xPos - imgWidth/2;
                    mIsDragable = true;
                    invalidate();
                } else {
                    mIsDragable = false;
                }
                return true;
            case MotionEvent.ACTION_CANCEL://手势被取消了
                Log.e(TAG, "手势被取消");
                callTouch(false);
                if (!mIsDragable)
                    return true;
                resetLock();
                break;
            case MotionEvent.ACTION_MOVE://移动
                Log.e(TAG, "手势移动");
                // 如果不在焦点
                if (!mIsDragable)
                    return true;
                resetLocationX(event.getX(), rightMax);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP://抬起了手指
                callTouch(false);
                Log.e(TAG, "抬起手指");
                if (!mIsDragable)
                    return true;
                if (mLocationX >= rightMax) {
                    mIsDragable = false;
                    mLocationX = 0;
                    invalidate();
                    if (mLockListener != null) {
                        mLockListener.onOpenLockSuccess();
                    }
                    Log.e(TAG, "解锁成功");
                }
                else {

                    new Handler().postDelayed((Runnable) () -> {
                        mLockListener.onOpenLockCancel();
                    },300);
                }
                resetLock();
                break;
            case MotionEvent.ACTION_OUTSIDE://超出了正常的UI边界
                Log.e(TAG, "超出边界");
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * TODO: 返回是否在触摸该控件
     */
    private void callTouch(boolean isTouch) {
        if (mOnTouchListener != null)
            mOnTouchListener.onTouch(isTouch);
    }

    /**
     * TODO: 回到初始位置
     */
    private void resetLock() {
        ValueAnimator anim = ValueAnimator.ofFloat(mLocationX, 0);
        anim.setDuration(300);
        anim.addUpdateListener(valueAnimator -> {
            mLocationX = (Float) valueAnimator.getAnimatedValue();
            invalidate();
        });
        anim.start();
    }

    private void resetLocationX(float eventXPos, float rightMax) {
        mLocationX = eventXPos - imgWidth/2;
        if (mLocationX < 0) {
            mLocationX = 0;
        } else if (mLocationX >= rightMax) {
            mLocationX = rightMax;
        }
    }

    /**
     * TODO: 判断是不是在目标点上
     */
    private boolean isTouchLock(float xPos, float yPox) {
        float centerX = mLocationX + imgWidth/2;
        float diffX = xPos - centerX;
        float diffY = yPox - imgHeight/2;
        return diffX * diffX + diffY * diffY < (imgWidth/2) * (imgHeight/2);
    }

    public void setLockListener(OnLockListener lockListener) {
        this.mLockListener = lockListener;
    }

    public interface OnLockListener {
        void onOpenLockSuccess();
        void onOpenLockCancel();
    }

    // TODO: 是否在滑动
    public interface OnTouchListener {
        void onTouch(boolean isTouch);
    }
}
