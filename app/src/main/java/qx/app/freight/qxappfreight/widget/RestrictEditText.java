package qx.app.freight.qxappfreight.widget;


import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * @ProjectName: qx_app_freight
 * @Package: qx.app.freight.qxappfreight.widget
 * @ClassName: RestrictEditText
 * @Description:  约束输入框
 * @Author: 张耀
 * @CreateDate: 2020/12/31 15:28
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/12/31 15:28
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class RestrictEditText  extends AppCompatEditText {

    public RestrictEditText(Context context) {
        super(context);
    }

    public RestrictEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RestrictEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(watcher);
    }
}
