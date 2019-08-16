package qx.app.freight.qxappfreight.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.constant.Constants;

@Data
public class PullGoodsInfoBean {
    /**
     * flightInfoId : 57c01f7eb03d480bb4879b8218723e55
     * pullScooters : [{"scooterCode":"24504","scooterId":"e6e295fb36644c26adefe494d7becc82","weight":210,"volume":1,"number":2100,"flightInfoId":"57c01f7eb03d480bb4879b8218723e55","createUserType":0,"status":1,"spCargoPulls":[{"waybillId":"8abcd503c91f400d9b68a8c8ba645bf2","weight":210,"number":2100,"waybillCode":"991-43622316","specialCode":"--"}]},{"scooterCode":"24701","scooterId":"f390177e20284218855e3f068b7c74da","weight":100,"volume":1,"number":1000,"flightInfoId":"57c01f7eb03d480bb4879b8218723e55","createUserType":0,"status":0,"spCargoPulls":[{"waybillId":"b71823a05a744dd088e3b761f13c9fe3","weight":100,"number":1000,"waybillCode":"991-27060025","specialCode":"--"}]},{"scooterCode":"24503","scooterId":"fbc9bd8ef8704b4da577a54fd7e004a3","weight":310,"volume":1,"number":1200,"flightInfoId":"57c01f7eb03d480bb4879b8218723e55","createUserType":0,"status":0,"spCargoPulls":[{"waybillId":"8abcd503c91f400d9b68a8c8ba645bf2","weight":310,"number":1200,"waybillCode":"991-43622316","specialCode":"--"}]}]
     * pullWaybills : [{"flightInfoId":"57c01f7eb03d480bb4879b8218723e55","waybillId":"37eca3a1f150443ebd85dc0e321aaa57","weight":200,"volume":1,"number":2100,"status":1,"scooterCodes":["24627"],"waybillCode":"991-10992306","createUserType":0}]
     */

    private String flightInfoId;
    private List<PullScootersBean> pullScooters;
    private List<PullWaybillsBean> pullWaybills;

    @Data
    public static class PullScootersBean implements PullGoodsShowInterface {
        /**
         * scooterCode : 24504
         * scooterId : e6e295fb36644c26adefe494d7becc82
         * weight : 210
         * volume : 1
         * number : 2100
         * flightInfoId : 57c01f7eb03d480bb4879b8218723e55
         * createUserType : 0
         * status : 1
         * spCargoPulls : [{"waybillId":"8abcd503c91f400d9b68a8c8ba645bf2","weight":210,"number":2100,"waybillCode":"991-43622316","specialCode":"--"}]
         */

        private String scooterCode;
        private String scooterId;
        private double weight;
        private double volume;
        private int number;
        private String flightInfoId;
        private int createUserType;//0，配载；1，监装人员
        private String remark;//备注
        private String pullReason;//拉货原因
        private int status;//0，未提交；1，提交过了
        private List<SpCargoPullsBean> spCargoPulls;
        private boolean checked;

        @Override
        public int getItemType() {
            return Constants.TYPE_PULL_BOARD;
        }

        @Data
        public static class SpCargoPullsBean {
            /**
             * waybillId : 8abcd503c91f400d9b68a8c8ba645bf2
             * weight : 210
             * number : 2100
             * waybillCode : 991-43622316
             * specialCode : --
             */

            private String waybillId;
            private double weight;
            private int number;
            private String waybillCode;
            private String specialCode;
            private boolean hasLiveGoods;
            private boolean title;
        }
    }

    @Data
    public static class PullWaybillsBean implements PullGoodsShowInterface {
        /**
         * flightInfoId : 57c01f7eb03d480bb4879b8218723e55
         * waybillId : 37eca3a1f150443ebd85dc0e321aaa57
         * weight : 200
         * volume : 1
         * number : 2100
         * status : 1
         * scooterCodes : ["24627"]
         * waybillCode : 991-10992306
         * createUserType : 0
         */

        private String flightInfoId;
        private String waybillId;
        private double weight;
        private double volume;
        private int number;
        private String waybillCode;
        private int createUserType;//0，配载；1，监装人员
        private int status;//0，未提交；1，提交过了
        private List<String> scooterCodes;//原定的板车
        private List<String> pushScooterCodes = new ArrayList<>();//新增扫描的板车
        private String remark;//备注
        private String pullReason;//拉货原因
        private boolean checked;

        @Override
        public int getItemType() {
            return Constants.TYPE_PULL_BILL;
        }
    }
}
