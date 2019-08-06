package qx.app.freight.qxappfreight.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

/**
 * 圆形的选择框
 */
public class RoundCheckBox extends AppCompatCheckBox {

    public  RoundCheckBox(Context context) {
        this(context, null);
    }

    public  RoundCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.radioButtonStyle);
    }

    public  RoundCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
