package qx.app.freight.qxappfreight.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * @ProjectName: qxstationsite
 * @Package: qx.app.station.site.utils
 * @ClassName: AppRestartUtils
 * @Description: 防止点击logo 重启 启动页
 * @Author: 张耀
 * @CreateDate: 2020/4/20 16:11
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/4/20 16:11
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class AppRestartUtils {

    public static void init(Activity activity) {
        //防止 重新点击app 图标 重启这个activity
        if ((activity.getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            activity.finish();
            return;
        }
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!activity.isTaskRoot()) { // 判断当前activity是不是所在任务栈的根
            Intent intent = activity.getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    activity.finish();
                    return;
                }
            }
        }
    }

}
