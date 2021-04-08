package qx.app.freight.qxappfreight.bean.request;

import java.io.Serializable;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;

/**
 * @ProjectName: qx_app_freight
 * @Package: qx.app.freight.qxappfreight.bean.request
 * @ClassName: OnlineStutasEntity
 * @Description: java类作用描述
 * @Author: 张耀
 * @CreateDate: 2021/3/17 16:27
 * @UpdateUser: 更新者：
 * @UpdateDate: 2021/3/17 16:27
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
@Data
public class OnlineStutasEntity implements Serializable {
    /**
     * 平台类型
     */
    private String platform;
    private LoginResponseBean user;
}
