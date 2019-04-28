package qx.app.freight.qxappfreight.utils;

import java.util.Arrays;
import java.util.List;

import qx.app.freight.qxappfreight.model.TestBean;

public class ExceptionUtils {

    public static final List<TestBean> testBeanList = Arrays.asList( new TestBean("件数异常", false), new TestBean("有单无货", false), new TestBean("破损", false), new TestBean("腐烂", false), new TestBean("有货无单", false), new TestBean("有货无单", false), new TestBean("泄露", false), new TestBean("错运", false), new TestBean("扣货", false), new TestBean("无标签", false), new TestBean("对方未发报文", false), new TestBean("其他", false), new TestBean("死亡", false), new TestBean("多收邮路单", false), new TestBean("多收货运单", false), new TestBean("多收邮袋", false), new TestBean("有邮袋无邮路单", false), new TestBean("少收货运单", false), new TestBean("有邮路单无邮袋", false));

    public static String typeToString(int type) {
        String result = "未知类型";
        switch (type) {
            case 1:
                result = "件数异常";
                break;
            case 2:
                result = "有单无货";
                break;
            case 3:
                result = "破损";
                break;
            case 4:
                result = "腐烂";
                break;
            case 5:
                result = "有货无单";
                break;
            case 6:
                result = "泄露";
                break;
            case 7:
                result = "错运";
                break;
            case 8:
                result = "扣货";
                break;
            case 9:
                result = "无标签";
                break;
            case 10:
                result = "对方未发报文";
                break;
            case 11:
                result = "其他";
                break;
            case 12:
                result = "死亡";
                break;
            case 13:
                result = "多收邮路单";
                break;
            case 14:
                result = "多收货运单";
                break;
            case 15:
                result = "多收邮袋";
                break;
            case 16:
                result = "有邮袋无邮路单";
                break;
            case 17:
                result = "少收货运单";
                break;
            case 18:
                result = "有邮路单无邮袋";
                break;
        }
        return result;
    }
}
