package qx.app.freight.qxappfreight.bean.response;


import lombok.Data;

@Data
public class ForwardInfoBean {

    private String id;

    /**
     * 货代公司名称
     */
    private String freightName;

    /**
     * 货代标识
     */
    private String freightCode;

    /**
     * 社会信用代码
     */
    private String creditCode;

    /**
     * 营业执照号
     */
    private String businessLicense;

    /**
     * 营业执照期限(结束)
     */
    private long businessEnd;

    /**
     * 营业执照期限(开始)
     */
    private long businessStart;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系方式
     */
    private String contactsPhone;

    /**
     * 联系地址
     */
    private String contactsAddress;

    /**
     * 收费类型 0现结 1记账
     */
    private Integer chargeType;

    /**
     * 发票类型 0普票 1专票
     */
    private Integer invoiceType;

    /**
     * 收验抽验比率
     */
    private String spotProbability;

    /**
     * 甲方
     */
    private String partya;

    /**
     * 乙方
     */
    private String partyb;

    /**
     * 签署时间
     */
    private long signDate;

    /**
     * 折扣率
     */
    private String discount;

    /**
     * 0现付现结 1现付月结 2月付月结
     */
    private Integer tollType;

    /**
     * 资质列表
     */
    private String[] freightAptitude;

    /**
     * 资质列表名
     */
    private String[] freightAptitudeName;
}
