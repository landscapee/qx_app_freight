package qx.app.freight.qxappfreight.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import qx.app.freight.qxappfreight.R;

public class HeartBeatView extends View {

    private Paint mPaint;

    public HeartBeatView(Context context) {
        super(context);
        init();
    }

    public HeartBeatView(Context context,  @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeartBeatView(Context context,  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HeartBeatView(Context context,  @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mPaint = new Paint();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //处理wrap_content情况
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, 300);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, 300);
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取各个边距的padding值
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        //获取绘制的View的宽度
        int width = getWidth() - paddingLeft - paddingRight;
        //获取绘制的View的高度
        int height = getHeight() - paddingTop - paddingBottom;
        //绘制View，左上角坐标（0+paddingLeft,0+paddingTop），右下角坐标（width+paddingLeft,height+paddingTop）
        mPaint.setColor(getResources().getColor(R.color.blue_2e8));
        canvas.drawRect(0 + paddingLeft, 0 + paddingTop, width + paddingLeft, height + paddingTop, mPaint);
        mPaint.setColor(getResources().getColor(R.color.red));
        //绘制自定义图片
        canvas.drawCircle(width/2+paddingLeft,height/2+paddingTop,width/2,mPaint);
//        int i, j;
//        double x, y, r;
//        for (i = 0; i <= 90; i++) {
//            for (j = 0; j <= 90; j++) {
//                r = Math.PI / 45 * i * (1 - Math.sin(Math.PI / 45 * j))
//                        * 20;
//                x = r * Math.cos(Math.PI / 45 * j)
//                        * Math.sin(Math.PI / 45 * i) + 320 / 2;
//                y = -r * Math.sin(Math.PI / 45 * j) + 400 / 4;
//                canvas.drawPoint((float) x, (float) y, mPaint);
//            }
//        }
    }
}
