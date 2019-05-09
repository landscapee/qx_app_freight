package qx.app.freight.qxappfreight.utils;

import java.util.Arrays;
import java.util.List;

import qx.app.freight.qxappfreight.model.TestBean;

/**
 * 异常信息帮助类
 * <p>
 * create by guohao - 2019/4/28
 */
public class ExceptionUtils {

    /**
     * 异常类型信息选择框基本数据
     */
    public static final List<TestBean> testBeanList = Arrays.asList(
            new TestBean("1、更名类不正常", false),
            new TestBean("2、死亡", false),
            new TestBean("3、确已装机", false),
            new TestBean("4、货物破损", false),
            new TestBean("5、多收邮路单", false),
            new TestBean("6、多收货运单", false),
            new TestBean("7、多收货物", false),
            new TestBean("8、多收邮袋", false),
            new TestBean("9、货物破损(国际)", false),
            new TestBean("10、腐烂丢弃", false),
            new TestBean("11、扣货", false),
            new TestBean("12、有邮袋无邮路单", false),
            new TestBean("13、少收货运单", false),
            new TestBean("14、少收货物", false),
            new TestBean("15、有邮路单无邮袋", false),
            new TestBean("16、无标签", false),
            new TestBean("17、临时拉下货物", false),
            new TestBean("18、其他不正常", false),
            new TestBean("19、漏卸货物/运输文件", false),
            new TestBean("20、分批不正常", false),
            new TestBean("21、货物不齐", false),
            new TestBean("22、漏装货物/文件", false),
            new TestBean("23、退单退货", false),
            new TestBean("24、无人提取", false)
    );

    /**
     * 异常信息选择框 index 转为 异常实际名称
     *
     * @param type
     * @return
     */
    public static String typeToString(int type) {
        String result = "未知类型";
        switch (type) {
            case 1:
                result = "更名类不正常";
                break;
            case 2:
                result = "死亡";
                break;
            case 3:
                result = "确已装机";
                break;
            case 4:
                result = "货物破损";
                break;
            case 5:
                result = "多收邮路单";
                break;
            case 6:
                result = "多收货运单";
                break;
            case 7:
                result = "多收货物";
                break;
            case 8:
                result = "多收邮袋";
                break;
            case 9:
                result = "货物破损";
                break;
            case 10:
                result = "腐烂丢弃";
                break;
            case 11:
                result = "扣货";
                break;
            case 12:
                result = "有邮袋无邮路单";
                break;
            case 13:
                result = "少收货运单";
                break;
            case 14:
                result = "少收货物";
                break;
            case 15:
                result = "有邮路单无邮袋";
                break;
            case 16:
                result = "无标签";
                break;
            case 17:
                result = "临时拉下货物";
                break;
            case 18:
                result = "其他不正常";
                break;
            case 19:
                result = "漏卸货物/运输文件";
                break;
            case 20:
                result = "分批不正常";
                break;
            case 21:
                result = "货物不齐";
                break;
            case 22:
                result = "漏装货物/文件";
                break;
            case 23:
                result = "退单退货";
                break;
            case 24:
                result = "无人提取";
                break;
        }
        return result;
    }
}