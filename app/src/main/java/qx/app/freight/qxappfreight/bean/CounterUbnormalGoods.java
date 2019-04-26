package qx.app.freight.qxappfreight.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zx
 * @since 2019-02-12
 *
 * 分拣 - 异常实体类
 *  guohao - 2-19/4/25
 */
@Data
public class CounterUbnormalGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 异常货物ID
     */
    private String id;

    /**
     * 航班号
     */
    private String flightNum;

    /**
     * 航班日期  用于查询航班id
     */
    private Long flightDate;

    /**
     * 运单号
     */
    private String waybillCode;

    /**
     * 异常来源
     */
    private String ubnormalSource;

    /**
     * 异常类型
     * 异常类型 1.件数异常; 2.有单无货; 3.破损; 4.腐烂; 5.有货无单;
     * 6.泄露; 7.错运; 8.扣货; 9.无标签; 10.对方未发报文;  11.其他; 12:死亡
     *
     * 13:多收邮路单 14:多收货运单 15:多收邮袋 16:有邮袋无邮路单 17：少收货运单 18:有邮路单无邮袋
     */
    private List<Integer> ubnormalType = new ArrayList();

    /**
     * 其他异常
     */
    private String ubnormalOther;

    /**
     * 异常图片
     */
    private List<String> ubnormalPic=new ArrayList<>();
    private List<String> localPath=new ArrayList<>();
    /**
     * 异常说明
     */
    private String ubnormalDescription;

    /**
     * 异常件数
     */
    private Integer ubnormalNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 追报天数
     */
    private Integer telegramDay;

    /**
     * 发报次数
     */
//    @TableField(exist = false)
    private Integer telegramNum;

    /**
     * 状态 0：未结案 1：已结案
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 运单id
     */
    private String waybillId;

    /**
     * 删除标志
     */
    private Integer delFlag;


    /**
     * 航班id
     */
    private String flightId;

    /**
     * 进出港标志
     */
    private Integer inOutFlag;

    /**
     * 开始时间（查询条件）
     */
    private Long beginTime;
    /**
     * 结束时间（查询条件）
     */
    private Long endTime;
    /**
     * 查询关键字（查询条件）
     */
    private String param;

}
