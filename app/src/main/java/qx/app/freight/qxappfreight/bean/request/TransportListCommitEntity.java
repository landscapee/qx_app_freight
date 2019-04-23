package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.bean.response.RcDeclareWaybill;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class TransportListCommitEntity {
    private String type;
    private String taskId;
    private String userId;
    private String waybillId;
    private DeclareWaybillBean waybillInfo;
    private List<RcInfosEntity> rcInfos;
    private SecurityCheckResult securityResultList;

    @Data
    public static class RcInfosEntity {
        private String id;
        private String waybillId;
        private String waybillCode;
        private String cargoId;
        private String cargoCn;
        private int number;
        private double weight;
        private double volume;
        private List<String> packagingType;
        private String scooterId;
        private String uldId;
        private int overWeight;
        private String repType;
        private String repPlaceId;
        private String reservoirName;
        private String reservoirType;
    }
    @Data
    public class SecurityCheckResult {

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
        private Long piece;

        /**
         * 品名
         */
        private String commodity;

        /**
         * 0退运 1扣货 2移交公安
         */
        private Integer processMode;

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

}
