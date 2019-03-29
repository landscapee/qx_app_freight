package qx.app.freight.qxappfreight.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.beidouapp.imlibapi.IMLIBContext;

import org.greenrobot.eventbus.EventBus;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.MainActivity;
import qx.app.freight.qxappfreight.activity.MsgDialogActivity;
import qx.app.freight.qxappfreight.bean.response.PushBaseBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.AppUtil;
import qx.app.freight.qxappfreight.utils.NotifiationUtil;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.Tools;

/**
 * 新消息广播
 * Created by mm on 2016/10/31.
 */
public class MessageReciver extends BroadcastReceiver {

    private Context mContext;

    public MessageReciver(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isGroup = intent.getBooleanExtra("isGroup", false); //是否群组消息
        String receiveMessage = intent.getStringExtra("receiveMessage"); //消息内容
        String friendName = intent.getStringExtra("friendName"); //消息发送者名称
        String friendImid = intent.getStringExtra("friendImid"); //消息发送者Imid,即登录名
        String receiveImid = intent.getStringExtra("receiveImid");//消息接受者Imid,即登录名
        Intent newIntent = null;
        if (isGroup) {
            String groupName = intent.getStringExtra("groupName"); //群组名称
            String groupImid = intent.getStringExtra("groupImid"); //群组imid
            String groupType = intent.getStringExtra("groupType"); //群组类型：个人群组、保障群组
            newIntent = IMLIBContext.getInstance().getGroupChatIntent(context, groupImid, groupName, groupType, receiveImid);
            if (StringUtil.isContains(receiveMessage, ("@" + Tools.getLoginName() + ":" + Tools.getRealName())) || StringUtil.isContains(receiveMessage, "@ALL")) {//@自己的消息或@所有人的消息发送强制提醒通知
                PushBaseBean pushBaseBean = new PushBaseBean();
                pushBaseBean.setContent("收到来自" + groupName + "群组的@消息,请前往查看！");
                pushBaseBean.setIsforcedispose(true);
                pushBaseBean.setMsgtype(Constants.PUSH_TYPE_SPECAIL_NOTICE);
                pushBaseBean.setExtra("");
                pushBaseBean.setOther(groupImid + "-" + groupName + "-" + groupType + "-" + receiveImid);
                if (Tools.isBackground(mContext)) {
                    MainActivity.startActivity(mContext);
                }
                if (AppUtil.isLocked(mContext)) {
                    MsgDialogActivity.startActivity(mContext, pushBaseBean);
                }
                EventBus.getDefault().post(pushBaseBean);
                //NotifiationUtil.showNotification(context, groupName + "-" + friendName + ":" + receiveMessage, "有人@我：" + groupName, friendName + ":" + receiveMessage, Constant.NOTIFY_ID_CHAT_2, R.raw.im_notice, newIntent);
            } else {
                NotifiationUtil.showNotification(context, groupName + "-" + friendName + ":" + receiveMessage, groupName, friendName + ":" + receiveMessage, Constants.NOTIFY_ID_CHAT, R.raw.im_notice, newIntent);
            }
        } else {
            newIntent = IMLIBContext.getInstance().getPersonalChatIntent(context, friendImid, friendName, receiveImid);
            NotifiationUtil.showNotification(context, friendName + ":" + receiveMessage, friendName, receiveMessage, Constants.NOTIFY_ID_CHAT, R.raw.im_notice, newIntent);
        }
    }
}
