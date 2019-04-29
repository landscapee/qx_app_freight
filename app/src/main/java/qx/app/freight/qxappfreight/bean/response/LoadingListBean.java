package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class LoadingListBean {

    /**
     * id : 0aed275e60c9e4d96e5faefa28460a4d
     * flightId : faa5ab6ffdd24a75b4e7b20235cc4897
     * version : 1
     * content :
     * reviewStatus : 0
     * createDate : 1556428802628
     * createUser : u55e8f00267b145b4be3a010b6091c2c7
     * updateDate : null
     * updateUser : null
     * documentType : 2
     * loadingAdvice : 0
     * loadingUser : null
     * contentObject : [{"cargoType":"C","groupScooters":[{"groupScooterStatus":0,"runTimeScooterId":"d59a52236a0847f999f85755afa15bb2","scooterType":0,"repName":"理货区","weight":8,"inFlight":0,"scooterWeight":825,"delFlag":0,"scooterId":"45c325c5071d4ddfb4777e7e330429c5","volume":8,"number":8,"route":"成都-临沂","repPlaceNum":"dep6061008ccaa1403ca95e93f87f2e4e91","updateStatus":0,"waybillId":"6403103ef206408d9302710d5c59af26","waybillCode":"028-00000022","scooterCode":"01340","createUser":"u80fec77daa6a4c01b57c12adebad0565","id":"253f9741bb9bde89853f0cc4a914eda2","destinationStation":"临沂","createDate":1556428668279}],"scooterType":0,"flightId":"faa5ab6ffdd24a75b4e7b20235cc4897","delFlag":0,"reWeightFinish":0,"scooterId":"45c325c5071d4ddfb4777e7e330429c5","number":8,"total":0,"cargoStatus":0,"deleteStatus":0,"reWeight":0,"id":"d59a52236a0847f999f85755afa15bb2","suggestRepository":"1H","cargo":"货物","scooter":"大滚筒 01340","personUpdateValue":0,"createDate":1556428668276,"uldCode":"","reDifferenceRate":0,"scooterStatus":0,"edit":false,"weight":8,"inFlight":0,"scooterWeight":825,"volume":8,"updateStatus":0,"createUser":"u80fec77daa6a4c01b57c12adebad0565","scooterCode":"01340","reDifference":0}]
     * returnReason : null
     * preContent : null
     * cgoContent : null
     * autoLoadInstalledSingle : 0
     */

    private String id;
    private String flightId;
    private String version;
    private String content;
    private int reviewStatus;
    private long createDate;
    private String createUser;
    private Object updateDate;
    private Object updateUser;
    private int documentType;
    private int loadingAdvice;
    private Object loadingUser;
    private Object returnReason;
    private Object preContent;
    private Object cgoContent;
    private int autoLoadInstalledSingle;
    private List<ContentObjectBean> contentObject;

    @Data
    public static class ContentObjectBean {
        /**
         * cargoType : C
         * groupScooters : [{"groupScooterStatus":0,"runTimeScooterId":"d59a52236a0847f999f85755afa15bb2","scooterType":0,"repName":"理货区","weight":8,"inFlight":0,"scooterWeight":825,"delFlag":0,"scooterId":"45c325c5071d4ddfb4777e7e330429c5","volume":8,"number":8,"route":"成都-临沂","repPlaceNum":"dep6061008ccaa1403ca95e93f87f2e4e91","updateStatus":0,"waybillId":"6403103ef206408d9302710d5c59af26","waybillCode":"028-00000022","scooterCode":"01340","createUser":"u80fec77daa6a4c01b57c12adebad0565","id":"253f9741bb9bde89853f0cc4a914eda2","destinationStation":"临沂","createDate":1556428668279}]
         * scooterType : 0
         * flightId : faa5ab6ffdd24a75b4e7b20235cc4897
         * delFlag : 0
         * reWeightFinish : 0
         * scooterId : 45c325c5071d4ddfb4777e7e330429c5
         * number : 8
         * total : 0
         * cargoStatus : 0
         * deleteStatus : 0
         * reWeight : 0
         * id : d59a52236a0847f999f85755afa15bb2
         * suggestRepository : 1H
         * cargo : 货物
         * scooter : 大滚筒 01340
         * personUpdateValue : 0
         * createDate : 1556428668276
         * uldCode :
         * reDifferenceRate : 0
         * scooterStatus : 0
         * edit : false
         * weight : 8
         * inFlight : 0
         * scooterWeight : 825
         * volume : 8
         * updateStatus : 0
         * createUser : u80fec77daa6a4c01b57c12adebad0565
         * scooterCode : 01340
         * reDifference : 0
         */

        private String cargoType;
        private int scooterType;
        private String flightId;
        private int delFlag;
        private int reWeightFinish;
        private String scooterId;
        private int number;
        private int total;
        private int cargoStatus;
        private int deleteStatus;
        private int reWeight;
        private String id;
        private String suggestRepository;
        private String cargo;
        private String scooter;
        private int personUpdateValue;
        private long createDate;
        private String uldCode;
        private int reDifferenceRate;
        private int scooterStatus;
        private boolean edit;
        private int weight;
        private int inFlight;
        private int scooterWeight;
        private int volume;
        private int updateStatus;
        private String createUser;
        private String scooterCode;
        private int reDifference;
        private List<GroupScootersBean> groupScooters;

        @Data
        public static class GroupScootersBean {
            /**
             * groupScooterStatus : 0
             * runTimeScooterId : d59a52236a0847f999f85755afa15bb2
             * scooterType : 0
             * repName : 理货区
             * weight : 8
             * inFlight : 0
             * scooterWeight : 825
             * delFlag : 0
             * scooterId : 45c325c5071d4ddfb4777e7e330429c5
             * volume : 8
             * number : 8
             * route : 成都-临沂
             * repPlaceNum : dep6061008ccaa1403ca95e93f87f2e4e91
             * updateStatus : 0
             * waybillId : 6403103ef206408d9302710d5c59af26
             * waybillCode : 028-00000022
             * scooterCode : 01340
             * createUser : u80fec77daa6a4c01b57c12adebad0565
             * id : 253f9741bb9bde89853f0cc4a914eda2
             * destinationStation : 临沂
             * createDate : 1556428668279
             */

            private int groupScooterStatus;
            private String runTimeScooterId;
            private int scooterType;
            private String repName;
            private int weight;
            private int inFlight;
            private int scooterWeight;
            private int delFlag;
            private String scooterId;
            private int volume;
            private int number;
            private String route;
            private String repPlaceNum;
            private int updateStatus;
            private String waybillId;
            private String waybillCode;
            private String scooterCode;
            private String createUser;
            private String id;
            private String destinationStation;
            private long createDate;
        }
    }
}
