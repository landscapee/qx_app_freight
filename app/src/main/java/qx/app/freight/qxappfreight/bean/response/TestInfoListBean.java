package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class TestInfoListBean implements Serializable {


    /**
     * insInfo : {"id":"9225ebd87d2a1b3d9036b7c00b1dfb54","insFile":"","waybillId":"9225ebd87d2a1b3d9036b7c00b1dfb54","insCheck":0,"fileCheck":0,"packaging":0,"require":0,"spotResult":0,"unspotReson":null,"insStatus":0,"insUserName":null,"insDangerStart":null,"insDangerEnd":null,"insStartTime":null,"insEndTime":null,"insUserHead":null,"type":null,"taskId":null,"userId":null}
     * freightInfo : [{"id":"95987307b85cb1ee1ba751bfaaef58ed","inspectionName":"朱宇航","inspectionCode":"001","inspectionCard":"510184199511190015","inspectionBookEnd":1556640000000,"inspectionBookStart":1554220800000,"delFlag":0,"dangerBookStart":null,"dangerBookEnd":null,"inspectionHead":null,"freightId":"dep2ed3f0be85e647b7a9c85a0ed6d7d55e","inspectionUserAptitudes":null}]
     */

    private InsInfoBean insInfo;
    private List<FreightInfoBean> freightInfo;


    @Data
    public static class InsInfoBean implements Serializable{
        /**
         * id : 9225ebd87d2a1b3d9036b7c00b1dfb54
         * insFile :
         * waybillId : 9225ebd87d2a1b3d9036b7c00b1dfb54
         * insCheck : 0
         * fileCheck : 0
         * packaging : 0
         * require : 0
         * spotResult : 0
         * unspotReson : null
         * insStatus : 0
         * insUserName : null
         * insDangerStart : null
         * insDangerEnd : null
         * insStartTime : null
         * insEndTime : null
         * insUserHead : null
         * type : null
         * taskId : null
         * userId : null
         */

        private String id;
        private String insFile;
        private String waybillId;
        private int insCheck;
        private int fileCheck;
        private int packaging;
        private int require;
        private int spotResult;
        private String unspotReson;
        private int insStatus;
        private String insUserName;
        /**
         * 报检员危险开始时间
         */
        private long insDangerStart;
        /**
         * 报检员危险结束时间
         */
        private long insDangerEnd;
        /**
         * 报检员资质起始时间
         */

        private long insStartTime;
        /**
         * 报检员资质结束时间
         */

        private long insEndTime;
        /**
         * 报检员头像地址
         */
        private String insUserHead;
        private String type;
        private String taskId;
        private String userId;
    }

    @Data
    public static class FreightInfoBean implements Serializable{
        /**
         * id : 95987307b85cb1ee1ba751bfaaef58ed
         * inspectionName : 朱宇航
         * inspectionCode : 001
         * inspectionCard : 510184199511190015
         * inspectionBookEnd : 1556640000000
         * inspectionBookStart : 1554220800000
         * delFlag : 0
         * dangerBookStart : null
         * dangerBookEnd : null
         * inspectionHead : null
         * freightId : dep2ed3f0be85e647b7a9c85a0ed6d7d55e
         * inspectionUserAptitudes : null
         */
        private String id;
        private String inspectionName;
        private String inspectionCode;
        private String inspectionCard;
        /**
         * 报检证书期限(结束)
         */
        private long inspectionBookEnd;
        /**
         * 报检证书期限（开始）
         */
        private long inspectionBookStart;
        private int delFlag;
        /**
         * 危险资质起始时间
         */
        private long dangerBookStart;
        /**
         * 危险资质结束时间
         */
        private long dangerBookEnd;
        private String inspectionHead;
        private String freightId;
        private String inspectionUserAptitudes;
    }
}
