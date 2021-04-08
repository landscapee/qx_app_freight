package qx.app.freight.qxappfreight.contract;

import com.beidouapp.et.client.domain.UserInfo;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.GpsInfoEntity;
import qx.app.freight.qxappfreight.bean.request.OnlineStutasEntity;
import qx.app.freight.qxappfreight.bean.request.UserBean;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;

/**
 * @ProjectName:
 * @Package: qx.app.freight.qxappfreight.contract
 * @ClassName: OnlineStatusContract
 * @Description: 发送登录状态 给服务器
 * @Author: 张耀
 * @CreateDate: 2021/3/17 14:37
 * @UpdateUser: 更新者：
 * @UpdateDate: 2021/3/17 14:37
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class OnlineStatusContract {
    public interface OnlineStatusModel {
        void onlineStatus(OnlineStutasEntity userInfo, IResultLisenter lisenter);
    }

    public interface OnlineStatusView extends IBaseView {
        void onlineStatusResult(String result);
    }
}
