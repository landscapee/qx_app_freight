package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;

@Data
public class LoadingListOverBean {

    /**
     * version : 1
     * flightId : 64caafd0df504cd090976611b7b951c0
     * operationUser : zhangwei3
     * documentType : 2
     * data : [{"id":"42e9719614c14e6d927b0eaecbbfdb3a","scooterId":"7678040d9bee49c6a81bf89d3e038978","flightId":"64caafd0df504cd090976611b7b951c0","cargoType":1,"goodsLocation":"","total":88,"weight":88,"volume":88,"suggestRepository":"H1","uldId":"e273839e8c53f2d072774b80bc31c181","createDate":1552289763567,"createUser":"u2c115ffc6a9043bbad2df7c875eb719f","delFlag":0,"reWeight":0,"reDifference":0,"reDifferenceRate":0,"reWeightFinish":0,"personUpdateValue":0,"scooterCode":"12022","scooterType":1,"uldCode":"12334","uldType":"PMB","scooterWeight":560,"uldWeight":1200,"status":0,"updateStatus":0,"inFlight":0}]
     */

    private String version;
    private String flightId;
    private String operationUser;
    private String documentType;
    private List<DataBean> data;

    @Data
    public static class DataBean {
        /**
         * id : 42e9719614c14e6d927b0eaecbbfdb3a
         * scooterId : 7678040d9bee49c6a81bf89d3e038978
         * flightId : 64caafd0df504cd090976611b7b951c0
         * cargoType : 1
         * goodsLocation :
         * total : 88
         * weight : 88.0
         * volume : 88.0
         * suggestRepository : H1
         * uldId : e273839e8c53f2d072774b80bc31c181
         * createDate : 1552289763567
         * createUser : u2c115ffc6a9043bbad2df7c875eb719f
         * delFlag : 0
         * reWeight : 0.0
         * reDifference : 0.0
         * reDifferenceRate : 0.0
         * reWeightFinish : 0
         * personUpdateValue : 0.0
         * scooterCode : 12022
         * scooterType : 1
         * uldCode : 12334
         * uldType : PMB
         * scooterWeight : 560.0
         * uldWeight : 1200.0
         * status : 0
         * updateStatus : 0
         * inFlight : 0
         */

        private String id;
        private String scooterId;
        private String flightId;
        private int cargoType;
        private String goodsLocation;
        private int total;
        private double weight;
        private double volume;
        private String suggestRepository;
        private String uldId;
        private long createDate;
        private String createUser;
        private int delFlag;
        private double reWeight;
        private double reDifference;
        private double reDifferenceRate;
        private int reWeightFinish;
        private double personUpdateValue;
        private String scooterCode;
        private int scooterType;
        private String uldCode;
        private String uldType;
        private double scooterWeight;
        private double uldWeight;
        private int status;
        private int updateStatus;
        private int inFlight;
    }
}
