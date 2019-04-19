package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class TestInfoListBean {
    /**
     * id
     */
    private String id;

    /**
     * 报检员姓名
     */
    private String inspectionName;

    /**
     * 报检员编号
     */
    private String inspectionCode;

    /**
     * 报检员身份证
     */
    private String inspectionCard;

    /**
     * 报检证书期限(结束)
     */
    private long inspectionBookEnd;

    /**
     * 报检证书期限（开始）
     */
    private long inspectionBookStart;

    /**
     * 删除标记 0正常 1删除
     */
    private Integer delFlag;

    /**
     * 危险资质起始时间
     */
    private long dangerBookStart;

    /**
     * 危险资质结束时间
     */
    private long dangerBookEnd;

    /**
     * 报检员头像地址
     */
    private String inspectionHead;
    /**
     * 所属货代公司
     */
    private String freightId;

}
