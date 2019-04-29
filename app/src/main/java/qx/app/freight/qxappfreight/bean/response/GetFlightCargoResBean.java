package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

/**
 * 装机单数据
 */
@Data
public class GetFlightCargoResBean {

    /**
     * id : 6d8b25d6470b6d6fce529506cea1c32c
     * flightId : d3b66988060c4c13b7d9574b80279a11
     * version : 3
     * content :
     * reviewStatus : 0
     * createDate : 1551672846441
     * createUser : uf34b6a5306694183be6d88b9026865d3
     * updateDate : null
     * updateUser : null
     * documentType : 2
     * contentObject : [{"reDifferenceRate":0,"updateDate":1551866529948,"groupScooters":[{"runTimeScooterId":"a962f45f9cc24e2b8aaf919c283454e1","scooterType":1,"weight":200,"inFlight":0,"scooterWeight":555,"delFlag":0,"scooterId":"852cda98f16f496cbf72a43b62ebdfe3","volume":200,"number":200,"repPlaceNum":"系统分配","updateStatus":0,"waybillId":"026623b4d96e443024b07167368642ad","scooterCode":"12923","id":"84772bc2c0d3d06c369e1cbc31cab9ae","createDate":1551865400426}],"scooterType":1,"flightId":"d43c2d74fc54498184f60afa55fdf6b2","weight":200,"uldType":"PMY","inFlight":0,"uldId":"e273839e8c53f2d072774b80bc31c184","delFlag":0,"reWeightFinish":1,"scooterWeight":555,"scooterId":"852cda98f16f496cbf72a43b62ebdfe3","uldWeight":1200,"volume":200,"updateStatus":0,"reWeight":0,"scooterCode":"12923","id":"a962f45f9cc24e2b8aaf919c283454e1","suggestRepository":"H1","reDifference":0,"personUpdateValue":0,"createDate":1551865400424,"uldCode":"17401"}]
     */
    private String tpOperator;
    private String id;
    private String taskId;
    private String tpFlightId;
    private String version;
    private String content;
    private int reviewStatus;
    private long createDate;
    private String createUser;
    private Object updateDate;
    private Object updateUser;
    private int documentType;
    private List<ContentObjectBean> contentObject;
    @Data
    public static class ContentObjectBean {
        /**
         * reDifferenceRate : 0.0
         * updateDate : 1551866529948
         * groupScooters : [{"runTimeScooterId":"a962f45f9cc24e2b8aaf919c283454e1","scooterType":1,"weight":200,"inFlight":0,"scooterWeight":555,"delFlag":0,"scooterId":"852cda98f16f496cbf72a43b62ebdfe3","volume":200,"number":200,"repPlaceNum":"系统分配","updateStatus":0,"waybillId":"026623b4d96e443024b07167368642ad","scooterCode":"12923","id":"84772bc2c0d3d06c369e1cbc31cab9ae","createDate":1551865400426}]
         * scooterType : 1
         * flightId : d43c2d74fc54498184f60afa55fdf6b2
         * weight : 200.0
         * uldType : PMY
         * inFlight : 0
         * uldId : e273839e8c53f2d072774b80bc31c184
         * delFlag : 0
         * reWeightFinish : 1
         * scooterWeight : 555.0
         * scooterId : 852cda98f16f496cbf72a43b62ebdfe3
         * uldWeight : 1200.0       uld自重
         * volume : 200.0
         * updateStatus : 0
         * reWeight : 0.0           复重重量
         * scooterCode : 12923
         * id : a962f45f9cc24e2b8aaf919c283454e1
         * suggestRepository : H1
         * reDifference : 0.0
         * personUpdateValue : 0.0
         * createDate : 1551865400424
         * uldCode : 17401
         */

        private double reDifferenceRate;
        private long updateDate;
        private int scooterType;
        private String flightId;
        private double weight;
        private String uldType;
        private int inFlight;
        private String uldId;
        private int delFlag;
        private int reWeightFinish;
        private double scooterWeight;
        private String scooterId;
        private double uldWeight;
        private double volume;
        private int updateStatus;
        private double reWeight;
        private String scooterCode;
        private String id;
        private String suggestRepository;
        private double reDifference;
        private double personUpdateValue;
        private long createDate;
        private String uldCode;
        private String goodsLocation;
        private String cargoType;
        private int total;
        private int cargoStatus;//默认为 《0》正常  1 表示 拉货
        private List<GroupScootersBean> groupScooters;
        @Data
        public static class GroupScootersBean {
            /**
             * runTimeScooterId : a962f45f9cc24e2b8aaf919c283454e1
             * scooterType : 1
             * weight : 200.0
             * inFlight : 0
             * scooterWeight : 555.0
             * delFlag : 0
             * scooterId : 852cda98f16f496cbf72a43b62ebdfe3
             * volume : 200.0
             * number : 200
             * repPlaceNum : 系统分配
             * updateStatus : 0
             * waybillId : 026623b4d96e443024b07167368642ad
             * scooterCode : 12923
             * id : 84772bc2c0d3d06c369e1cbc31cab9ae
             * createDate : 1551865400426
             */

            private String runTimeScooterId;
            private int scooterType;
            private double weight;
            private int inFlight;
            private double scooterWeight;
            private int delFlag;
            private String scooterId;
            private double volume;
            private int number;
            private String repPlaceNum;
            private int updateStatus;
            private String waybillId;
            private String waybillCode;
            private String scooterCode;
            private String id;
            private long createDate;
        }
    }
}
