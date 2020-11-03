package qx.app.freight.qxappfreight.utils;

import android.support.annotation.NonNull;
import android.text.method.NumberKeyListener;

/**
 * @ProjectName: qx_app_freight
 * @Package: qx.app.freight.qxappfreight.utils
 * @ClassName: MyKeyListener
 * @Description: java类作用描述
 * @Author: 张耀
 * @CreateDate: 2020/7/15 14:25
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/7/15 14:25
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MyKeyListener extends NumberKeyListener {
    @NonNull
    @Override
    protected char[] getAcceptedChars() {
        char[] chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'X', '-', 'D', 'N'};
        return chars;
    }
    /**
     * 0：无键盘,键盘弹不出来
     * 1：英文键盘
     * 2：模拟键盘
     * 3：数字键盘
     *
     * @return
     */
    @Override
    public int getInputType() {
        return 3;
    }
}
