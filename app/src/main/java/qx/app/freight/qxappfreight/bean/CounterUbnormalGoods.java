package qx.app.freight.qxappfreight.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
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
     *
     *  //	CHNM("CHNM",1,"更名类不正常"),
     *                 //	DEAD("DEAD",2,"死亡"),
     *                 //	DFLD("DFLD",3,"确已装机"),
     *                 //	DMG("DMG",4,"货物破损"),
     *                 //	FDAV("FDAV",5,"多收邮路单"),
     *                 //	FDAW("FDAW",6,"多收货运单"),			//有单无货
     *                 //	FDAW("MSCA",6,"多收货运单"),			//有单无货
     *                 //	FDCA("FDCA",7,"多收货物"),
     *                 //	FDMB("FDMB",8,"多收邮袋"),
     *                 //	GDAM("GDAM",9,"货物破损"),
     *                 //  HWFL("HWFL",10,"腐烂丢弃"),
     *                 //	ROT("ROT",10,"腐烂"),
     *                 //	KOUH("KOUH",11,"扣货"),
     *                 //	MSAV("MSAV",12,"有邮袋无邮路单"),
     *                 //	MSAW("MSAW",13,"少收货运单"),			//有货无单
     *                 //	MSCA("S/L",14,"少收货物"),
     *                 //  MSCA("MSCA",14,"少收货物"),
     *                 //	MSMB("MSMB",15,"有邮路单无邮袋"),
     *                 //	NLAB("NLAB",16,"无标签"),
     *                 //	OFLD("OFLD",17,"临时拉下货物"),
     *                 //	OTHR("OTHR",18,"其他不正常"),
     *                 //	OVCD("OVCD",19,"漏卸货物/运输文件"),
     *                 //	SPLT("SPLT",20,"分批不正常"),
     *                 //	SPSL("SPSL",21,"货物不齐"),
     *                 //	SSPD("SSPD",22,"漏装货物/文件"),
     *                 //	TDTH("TDTH",23,"退单退货"),
     *                 //	WRTQ("WRTQ",24,"无人提取"),
     *                 //	WLXFS("WLXFS",25,"无联系方式"),
     *                 //	WET("WET",26,"受潮"),
     *                 //	LEAK("WET",27,"泄漏"),
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
