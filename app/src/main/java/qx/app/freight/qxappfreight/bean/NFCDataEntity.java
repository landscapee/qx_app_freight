package qx.app.freight.qxappfreight.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @ProjectName: qxstationsite
 * @Package: qx.app.station.site.bean
 * @ClassName: NFCDataEntity
 * @Description: 用于EventBus 发送 NFC 感应的板车数据
 * @Author: 张耀
 * @CreateDate: 2020/4/13 17:08
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/4/13 17:08
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
@Getter
@Setter
public class NFCDataEntity {
    /**
     * 类型（暂时未使用）
     */
    private int type;
    /**
     * 板车号
     */
    private String ScooterCode;

}
