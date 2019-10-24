package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 结载角色 单独需要的数据
 */
@Getter
@Setter
public class LoadingAndUnloadBean implements Serializable {
    /**
     * 行李转盘
     */
    private String carousel;
    /**
     * 登机口
     */
    private String gate;
    /**
     * ctot时间
     */
    private long ctot;
    /**
     * cobt
     */
    private long cobt;
    /**
     * tobt
     */
    private long tobt;
    /**
     * 下站预达
     */
    private long nxtEta;

    /**
     * 前站起飞
     */
    private long  preAtd;

    /**
     * VIP标记
     */
    private String vipMark;

    /**
     * VIP人数
     */
    private Long vipNo;
    /**
     * 机型
     */
    private String aircraftType;

    /**
     * 类型 B C D E F
     */
    private String model;

    /**
     * 地服协定关舱门时间
     */
    private long unifiedCloseTime;

    /**
     * 电子进程单状态
     */
    private String elecState;
    /**
     * 地服快速过站
     */
    private Integer quickFlag;
    /**
     * 延误状态
     */
    private Integer delayState;
    /**
     * 备降状态
     */
    private Integer fdivState;
    /**
     * 重点保障
     */
    private String keyMaintaince;
}
