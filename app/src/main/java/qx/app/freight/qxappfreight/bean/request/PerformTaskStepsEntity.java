package qx.app.freight.qxappfreight.bean.request;


import lombok.Data;

@Data
public class PerformTaskStepsEntity {

    private String id;

    private String userId;

    private String userName;

    private String terminalId;

    private String flightTaskId;

    /**
     * CargoOutTransportReceived  领受
     * CargoOutTransportArrived  到位
     * CargoOutTransportStart  开始
     * CargoOutTransportEnd  结束
     *
     *
     * FreightPass_close 关货仓门
     * FreightPass_ready 到位
     * FreightPass_open 开货仓门
     * FreightPass_receive 领受
     * FreightPass_load 装机
     * FreightPass_unload 卸机
     **/
    private String operationCode;

    private long flightId;

    /**
     * 纬度
     */
    private String longitude;

    /**
     * 经度
     */
    private String latitude;
    /**
     * 执行步骤时间戳
     */
    private long createTime;
    /**
     * 类型标识：装卸机：1
     */
    private int type;
    private String loadUnloadDataId;
}
