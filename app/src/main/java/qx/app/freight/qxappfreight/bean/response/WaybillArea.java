package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @ProjectName: qx_app_freight
 * @Package: qx.app.freight.qxappfreight.bean.response
 * @ClassName: WaybillArea
 * @Description:   出库运单 分别存放的 库区 对象
 * @Author: 张耀
 * @CreateDate: 2020/4/19 10:55
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/4/19 10:55
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
@Getter
@Setter
public class WaybillArea implements Serializable {


    /**
     * id : 6ff28d913c9b43a1b748bb92d8b3954a
     * outletCode : ctu_airport_cargo_00002
     * areaId : dc40f72d5ecb5eff5cf205013296ad7c
     * binId : null
     * waybillCode : 880-52342345
     * number : 33
     * weight : 126
     * storageType : CTU_GARGO_STORAGE_TYPE_001
     * specialCode : AVI
     * cargoCn : 虾
     * mailType : null
     * areaTypeCode : ctu_airport_were_house_00007
     * mainId : 75dc4cbe439c4a9ca61c345858de4735
     * areaName : 理货区
     * applyUserId : null
     * applyUserName : null
     * applyTime : null
     * execTime : null
     * oldWaybillCode : null
     * startTime : null
     * endTime : null
     * waybillId : null
     */

    private String id;
    private String outletCode;
    private String areaId;
    private Object binId;
    private String waybillCode;
    private int number;
    private double weight;
    private String storageType;
    private String specialCode;
    private String cargoCn;
    private Object mailType;
    private String areaTypeCode;
    private String mainId;
    private String areaName;
    private Object applyUserId;
    private Object applyUserName;
    private Object applyTime;
    private Object execTime;
    private Object oldWaybillCode;
    private Object startTime;
    private Object endTime;
    private String waybillId;
    private int OutboundNumber;

}
