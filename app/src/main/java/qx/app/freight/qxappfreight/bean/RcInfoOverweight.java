package qx.app.freight.qxappfreight.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class RcInfoOverweight implements Serializable {

    /**
     * 超重记录
     */
    private String id;

    /**
     * 收运记录id
     */
    private String rcId;

    /**
     * 品名
     */
    private String cargoCn;

    /**
     * 件数
     */
    private Integer count;

    /**
     * 重量
     */
    private Integer weight;

    /**
     * 体积
     */
    private Integer volume;

    /**
     * 超重重量
     */
    private Integer overWeight;

    /**
     * 1删除
     */
    private Integer delFlag;

}
