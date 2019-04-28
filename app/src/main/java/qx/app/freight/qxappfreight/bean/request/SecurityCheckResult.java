package qx.app.freight.qxappfreight.bean.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class SecurityCheckResult implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 已检查id
     */
    private String id;

    /**
     * 安检id
     */
    private String securityId;

    /**
     * 件数
     */
    private int piece;

    /**
     * 品名
     */
    private String commodity;

    /**
     * 0退运 1扣货 2移交公安
     */
    private int processMode;

    /**
     * 原因
     */
    private String reason;

    /**
     * 0删除 1正常
     */
    private Short delFlag;

    /**
     * 品名id
     */
    private String cargoId;


    /**
     * 0收验 1收运
     */
    private Integer reType;
}
