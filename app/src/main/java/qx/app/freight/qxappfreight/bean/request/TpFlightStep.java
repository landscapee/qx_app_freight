package qx.app.freight.qxappfreight.bean.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class TpFlightStep implements Serializable {

    private String id;

    private String userId;

    private String userName;

    private String terminalId;

    private String flightTaskId;

    private String loadUnloadDataId;

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
     *
     */
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

    private long createTime;

    /**
     * 0 代表外场
     * 1 代表装卸机
     */
    private int type;

    private String deptId;
}

