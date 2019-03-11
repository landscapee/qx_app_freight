package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class SubmissionEntity {
    private String type;
    private String taskId;
    private String userId;
    private String waybillId;
    private List<RcInfosBean> rcInfos;

    @Data
    public class RcInfosBean {
        private String waybillId;
        private String waybillCode;
        private String cargoId;
        private int number;
        private int weight;
        private int volume;
        private String scooterId;
        private String uldId;
        private int overWeight;
        private String repType;
        private String repPlaceId;
        private List<String> packagingType;

    }

}
