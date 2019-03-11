package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

/**
 * <p>
 * 运行时航班板车中间表
 * </p>
 *
 * @author nacol
 * @since 2019-01-22
 */
@Data
public class FtRuntimeFlightScooter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 板车id(该航班板车)
     */
    private String id;

    /**
     * 板车id
     */
    private String scooterId;

    /**
     * 航班id
     */
    private String flightId;

    /**
     * 装载数量
     */
    private Integer total;

    /**
     * 装载重量
     */
    private Double weight;

    /**
     * 装载体积
     */
    private Double volume;

    /**
     * 目的地
     */
    private String flightDestination;

    /**
     * 建议仓位
     */
    private String suggestRepository;

    /**
     * uld id
     */
    private String uldId;

    /**
     * 创建时间
     */
    private Long createDate;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改时间
     */
    private Long updateDate;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 删除标识: 0删除 , 1未删除.
     */
    private Integer delFlag;

    /**
     * 复重重量
     */
    private Double reWeight;
    /**
     * 复重差值
     */
    private Double reDifference;
    /**
     * 复重差率
     */
    private Double reDifferenceRate;

    /**
     * 复重完成标识: null待复重, 1完成, 2复重异常
     */
    private Short reWeightFinish;

    /**
     * 人工干预差值
     */
    private Double personUpdateValue;

    //------------------------------------------------------
    /**
     * 板车上的收运记录
     */
    private List<FtGroupScooter> groupScooters;
    /**
     *
     * 1:update
     * 2:insert
     * 3:delete
     * 0:unchange
     */
    private Short updateStatus;

    /**
     * 板车的编号
     */
    private String scooterCode;

    /**
     * 1:大板  2:小板
     */
    private int scooterType;

    /**
     * 集装箱编号
     */
    private String uldCode;

    /**
     * 集装箱类型
     */
    private String uldType;

    /**
     * 航司二制码
     */
    private String iata;

    /**
     * 板车自重
     */
    private int scooterWeight;

    /**
     * 集装箱自重
     */
    private int uldWeight;

    /**
     * 是否为本航班的板
     * 1: false
     * 0: true
     */
    private Short inFlight;

    /**
     * 是否被选中
     */
    private boolean isSelect;
    /**
     * 是否被锁定
     */
    private boolean isLock;
}
