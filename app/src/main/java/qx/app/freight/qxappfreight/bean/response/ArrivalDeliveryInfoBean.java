package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class ArrivalDeliveryInfoBean {


        /**
         * waybills : [{"id":"a2c4963e2e646e9e8660c03511282661","waybillCode":"082-90987876","indicator":"D","flightNo":"EU2232","flightInfoId":"11cd8a93b76b4827b5ab9d18b4730695","flightId":11894585,"mailType":"AWBA","documentDelivery":1,"totalNumberPackages":800,"totalWeight":1500.02,"specialCargoCode":"NPS","commodityName":"兰花,主办,外壳,上衣","consignee":"王麻子","consigneePhone":"13690809889","consigneeCarid":"5104333","forwarderId":"1241231231221","forwarderName":"四川泰顺物流有限公司","forwarderPhone":"15909887611","priority":0,"buckleGoods":0,"transit":0,"refrigerated":0,"devanning":0,"originatingStation":"NKG","originatingStationCn":"南京","destinationStation":"CTU","destinationStationCn":"程度","manifestTotal":800,"manifestWeight":1500.2,"warehouseArea":null,"warehouseLocation":null,"putStorageTime":null,"putStorageUser":null,"outStorageTime":null,"outStorageUser":null,"tallyingTotal":null,"tallyingWeight":null,"waybillStatus":3,"waybillCodeScanUrl":"","commodityScanUrl":"","waybillScanUrl":"","createDate":1551175629446,"createUser":"big weiwei","updateDate":1551322223058,"updateUser":"dazhangwei333","errors":null}]
         * counterBillInfo : {"status":"200","message":"正确","data":{"id":"20190220102450842397","billingWeight":500,"charge":210,"statementWay":0,"payWay":1,"statementType":1,"waybillNum":1,"statementUserId":"","createTime":1550629490969,"createUser":"u04c18f91c3d04aefbebe67634fe72ffc","createUserName":null,"payTime":null,"status":0,"statementUser":"enbc55b2a4218a4261ac7083ccaa69c288","statementUserType":null,"consigneeName":null,"consigneePhone":null,"consigneeIdentityCard":null,"returnFlag":0,"receiptNum":null,"receiptType":null,"receiptCode":null,"receiptTime":null,"counterCharges":null,"taskId":null}}
         */

        private CounterBillInfoBean counterBillInfo;
        private List<WaybillsBean> waybills;


        @Data
        public static class CounterBillInfoBean {
            /**
             * status : 200
             * message : 正确
             * data : {"id":"20190220102450842397","billingWeight":500,"charge":210,"statementWay":0,"payWay":1,"statementType":1,"waybillNum":1,"statementUserId":"","createTime":1550629490969,"createUser":"u04c18f91c3d04aefbebe67634fe72ffc","createUserName":null,"payTime":null,"status":0,"statementUser":"enbc55b2a4218a4261ac7083ccaa69c288","statementUserType":null,"consigneeName":null,"consigneePhone":null,"consigneeIdentityCard":null,"returnFlag":0,"receiptNum":null,"receiptType":null,"receiptCode":null,"receiptTime":null,"counterCharges":null,"taskId":null}
             */

            private String status;
            private String message;
            private DataBean data;

            @Data
            public static class DataBean {
                /**
                 * id : 20190220102450842397
                 * billingWeight : 500
                 * charge : 210
                 * statementWay : 0
                 * payWay : 1
                 * statementType : 1
                 * waybillNum : 1
                 * statementUserId :
                 * createTime : 1550629490969
                 * createUser : u04c18f91c3d04aefbebe67634fe72ffc
                 * createUserName : null
                 * payTime : null
                 * status : 0
                 * statementUser : enbc55b2a4218a4261ac7083ccaa69c288
                 * statementUserType : null
                 * consigneeName : null
                 * consigneePhone : null
                 * consigneeIdentityCard : null
                 * returnFlag : 0
                 * receiptNum : null
                 * receiptType : null
                 * receiptCode : null
                 * receiptTime : null
                 * counterCharges : null
                 * taskId : null
                 */

                private String id;
                private int billingWeight;
                private int charge;
                private int statementWay;
                private int payWay;
                private int statementType;
                private int waybillNum;
                private String statementUserId;
                private long createTime;
                private String createUser;
                private Object createUserName;
                private Object payTime;
                private int status;
                private String statementUser;
                private Object statementUserType;
                private Object consigneeName;
                private Object consigneePhone;
                private Object consigneeIdentityCard;
                private int returnFlag;
                private Object receiptNum;
                private Object receiptType;
                private Object receiptCode;
                private Object receiptTime;
                private Object counterCharges;
                private Object taskId;

            }
        }

        @Data
        public static class WaybillsBean {
            /**
             * id : a2c4963e2e646e9e8660c03511282661
             * waybillCode : 082-90987876
             * indicator : D
             * flightNo : EU2232
             * flightInfoId : 11cd8a93b76b4827b5ab9d18b4730695
             * flightId : 11894585
             * mailType : AWBA
             * documentDelivery : 1
             * totalNumberPackages : 800
             * totalWeight : 1500.02
             * specialCargoCode : NPS
             * commodityName : 兰花,主办,外壳,上衣
             * consignee : 王麻子
             * consigneePhone : 13690809889
             * consigneeCarid : 5104333
             * forwarderId : 1241231231221
             * forwarderName : 四川泰顺物流有限公司
             * forwarderPhone : 15909887611
             * priority : 0
             * buckleGoods : 0
             * transit : 0
             * refrigerated : 0
             * devanning : 0
             * originatingStation : NKG
             * originatingStationCn : 南京
             * destinationStation : CTU
             * destinationStationCn : 程度
             * manifestTotal : 800
             * manifestWeight : 1500.2
             * warehouseArea : null
             * warehouseLocation : null
             * putStorageTime : null
             * putStorageUser : null
             * outStorageTime : null
             * outStorageUser : null
             * tallyingTotal : null
             * tallyingWeight : null
             * waybillStatus : 3
             * waybillCodeScanUrl :
             * commodityScanUrl :
             * waybillScanUrl :
             * createDate : 1551175629446
             * createUser : big weiwei
             * updateDate : 1551322223058
             * updateUser : dazhangwei333
             * errors : null
             */

            private String id;
            private String waybillCode;
            private String indicator;
            private String flightNo;
            private String flightInfoId;
            private int flightId;
            private String mailType;
            private int documentDelivery;
            private String totalNumberPackages;
            private String totalWeight;
            private String specialCargoCode;
            private String commodityName;
            private String consignee;
            private String consigneePhone;
            private String consigneeCarid;
            private String forwarderId;
            private String forwarderName;
            private String forwarderPhone;
            private int priority;
            private int buckleGoods;
            private int transit;
            private int refrigerated;
            private int devanning;
            private String originatingStation;
            private String originatingStationCn;
            private String destinationStation;
            private String destinationStationCn;
            private int manifestTotal;
            private double manifestWeight;
            private Object warehouseArea;
            private Object warehouseLocation;
            private Object putStorageTime;
            private Object putStorageUser;
            private long outStorageTime;
            private String outStorageUser;
            private String tallyingTotal;
            private String tallyingWeight;
            private int waybillStatus;
            private String waybillCodeScanUrl;
            private String commodityScanUrl;
            private String waybillScanUrl;
            private long createDate;
            private String createUser;
            private long updateDate;
            private String updateUser;
            private Object errors;
        }
}
