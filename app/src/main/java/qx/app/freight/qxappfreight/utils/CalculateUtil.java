package qx.app.freight.qxappfreight.utils;

import java.math.BigDecimal;

/**
 * 计算帮助类
 */
public class CalculateUtil {

    /**计算差率
     *
     * @param num  保存几位小数
     * @param a  被除数
     * @param b 除数
     * @return 返回一个乘了100的数  加%就是百分比
     */
    public static double calculateGradient(int num,double a ,double b){
        if (b==0){
            return 100d;
        }
        BigDecimal c = new BigDecimal( a / b);
        c = c.setScale(num, BigDecimal.ROUND_HALF_UP);
        double d = c.doubleValue();
        d = d*100;
        return d;
//        return ((new BigDecimal( a / b).setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue())*100);
    }
}
