package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;

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
    private List<RcInfosEntity> rcInfos;

    @Data
    public static class RcInfosEntity {
        private String id;
        private String waybillId;
        private String waybillCode;
        private String cargoId;
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
}
