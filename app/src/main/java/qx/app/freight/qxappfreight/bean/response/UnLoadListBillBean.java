package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class UnLoadListBillBean {

    /**
     * status : 200
     * message : 正确
     * rowCount : null
     * data : {"id":"2a8bfae4c938cfe5c7261c9e7085cbb4","flightId":"4c16467cdb9d4fbf9ad9927cbd89b7ce","version":"1","content":"","reviewStatus":0,"createDate":1559462807530,"createUser":"liuyuhan","updateDate":1559462840992,"updateUser":null,"documentType":4,"loadingAdvice":0,"loadingUser":null,"contentObject":[{"dst":"","pos":"","pri":"1","estWgt":"642","serialInd":"","cont":"LD3","dest":"CTU","type":"B","actWgt":"642","restrictedCargo":""},{"dst":"","pos":"","pri":"1","estWgt":"642","serialInd":"","cont":"LD3","dest":"CTU","type":"B","actWgt":"563","restrictedCargo":""}],"returnReason":null,"preContent":"","cgoContent":"","autoLoadInstalledSingle":1,"flightNo":"MU2219"}
     * flag : null
     */

    private String status;
    private String message;
    private Object rowCount;
    private DataBean data;
    private Object flag;

    @Data
    public static class DataBean {
        /**
         * id : 2a8bfae4c938cfe5c7261c9e7085cbb4
         * flightId : 4c16467cdb9d4fbf9ad9927cbd89b7ce
         * version : 1
         * content :
         * reviewStatus : 0
         * createDate : 1559462807530
         * createUser : liuyuhan
         * updateDate : 1559462840992
         * updateUser : null
         * documentType : 4
         * loadingAdvice : 0
         * loadingUser : null
         * contentObject : [{"dst":"","pos":"","pri":"1","estWgt":"642","serialInd":"","cont":"LD3","dest":"CTU","type":"B","actWgt":"642","restrictedCargo":""},{"dst":"","pos":"","pri":"1","estWgt":"642","serialInd":"","cont":"LD3","dest":"CTU","type":"B","actWgt":"563","restrictedCargo":""}]
         * returnReason : null
         * preContent :
         * cgoContent :
         * autoLoadInstalledSingle : 1
         * flightNo : MU2219
         */

        private String id;
        private String flightInfoId;
        private String version;
        private String content;
        private int reviewStatus;
        private long createTime;
        private String createUser;
        private long updateDate;
        private Object updateUser;
        private int documentType;
        private int loadingAdvice;
        private Object loadingUser;
        private Object returnReason;
        private String preContent;
        private String cgoContent;
        private int autoLoadInstalledSingle;
        private String flightNo;
        private List<ContentObjectBean> contentObject;

        @Data
        public static class ContentObjectBean {
            /**
             * dst :
             * pos :
             * pri : 1
             * estWgt : 642
             * serialInd :
             * cont : LD3
             * dest : CTU
             * type : B
             * actWgt : 642
             * restrictedCargo :
             */

            private String dst;
            private String pos;
            private String pri;
            private String estWgt;
            private String serialInd;
            private String cont;
            private String dest;
            private String type;
            private String actWgt;
            private String restrictedCargo;
        }
    }
}
