package qx.app.freight.qxappfreight.utils;

import android.util.Log;

import java.util.Arrays;

/**
 *  拷贝测试代码片段  到main方法内 第11行 绿色小按钮 可以运行 和debug 代码片段
 *  by zyy
 */
public class TestCode {

//    public static void main(String[] args) {
//        int[] arr = { 3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48 };
//        int count = 0;
//        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
//            // 对数组元素进行分组
//            for (int i = gap; i < arr.length; i++) {
//                // 遍历各组中的元素
//                for (int j = i - gap; j >= 0; j -= gap) {
//                    // 交换元素
//                    if (arr[j] > arr[j + gap]) {
//                        int temp = arr[j];
//                        arr[j] = arr[j + gap];
//                        arr[j + gap] = temp;
//                    }
//                    count++;
//                }
//            }
//        }
//
//        System.out.println(Arrays.toString(arr));
//        System.out.println("次数"+count);
//    }
    public static void main(String[] args) {
        String str = "01M/AVI";
        str= str.substring(0,str.indexOf("/"));
        Log.e("sss",str);

    }
    /**
     * 相差几天
     *
     * @return
     */
    public static String getMinToDay(int time) {
        int day = 0;
        int hour = 0;
        int min = 0;
        day = (int) time/(24*60);
        hour = (int)(time - (day*24*60))/60;
        min = time - (day*24*60) - (hour*60);
        String date = "";
        if (day > 0)
            date = date+day+"d";
        if (hour > 0)
            date = date+hour+"h";
        if (min > 0)
            date = date+min+"m";
        return date;
    }
}
