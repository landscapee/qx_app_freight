package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
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
    private String userName;
    private String waybillId;
    private String taskTypeCode;
    private String addOrderId;
    private DeclareWaybillBean waybillInfo;
    private List<MyAgentListBean> rcInfos;
    private List<SecurityCheckResult> securityResultList;

//    @Data
//    public static class RcInfosEntity {
//        private String id;
//        private String taskTypeCode;
//        private String addOrderId;
//        private String waybillId;
//        private String waybillCode;
//        private String[] cargoId;
//        private String cargoCn;
//        private int number;
//        private double weight;
//        private double volume;
//        private List<String> packagingType;
//        private String scooterId;
//        private String uldId;
//        private int overWeight;
//        private String repType;
//        private String repPlaceId;
//        private String reservoirName;
//        private String reservoirType;
//        private String repName;
//        private String repPlaceNum;
//        private String scooterType;
//        private String scooterCode;
//        private String scooterWeight;
//        private String uldCode;
//        private String uldType;
//        private int uldWeight;
//        private int delFlag;
//        private long createDate;
//        private  List<RcInfoOverweight> rcInfoOverweight;
//
//    }

}
