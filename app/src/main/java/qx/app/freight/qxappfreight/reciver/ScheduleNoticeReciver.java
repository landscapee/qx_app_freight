package qx.app.freight.qxappfreight.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * 排班通知公告广播接收器
 * Created by mm on 2017/03/06
 */
public class ScheduleNoticeReciver extends BroadcastReceiver {

    private Context mContext;

    public ScheduleNoticeReciver(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        String noticeId = intent.getStringExtra("noticeId"); //排班公告id
//        String noticeTitle = intent.getStringExtra("noticeTitle"); //排班公告标题
//        Intent newIntent = new Intent(context, ImLibSchedualNoticeDetailActivity.class);
//        newIntent.putExtra("noticeId", noticeId);
//        NotifiationUtil.showNotification(mContext, "排班公告", "排班公告", noticeTitle,
//                Constant.NOTIFY_ID_SCHEDULE_NOTICE, -1, newIntent);
    }
}
