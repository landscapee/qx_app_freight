package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 单个装机单数据
 */
@Data
public class LoadingListBean implements Serializable{
    /**
     * status : 200
     * message : 正确
     * rowCount : null
     * data : [{"id":"e02e0e3f54c8e8875c22bbad0160f837","flightId":"529079d8693445dcaf79a5ffc102bae5","version":"1","content":"","reviewStatus":0,"createDate":1557390815343,"createUser":"liuyuhuan","updateDate":1557393717477,"updateUser":null,"documentType":2,"loadingAdvice":0,"loadingUser":"大张伟","contentObject":[{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"22L","serialInd":"AKE78253CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"22R","serialInd":"AKE74385CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"23L","serialInd":"AKE77772CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"23R","serialInd":"AKE76098CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"24L","serialInd":"AKE76350CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"24R","serialInd":"AKE77396CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"25L","serialInd":"AKE76469CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"25R","serialInd":"AKE78249CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"26L","serialInd":"AKE74294CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"26R","serialInd":"AKE73733CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"3","estWgt":"1868","location":"32P","serialInd":"PMC42767CZ","cont":"PMC","actWgt":"1868","restrictedCargo":""},{"dst":"","pri":"A21","tailer":"","dest":"PEK","type":"C","cargoStatus":0,"pos":"3","estWgt":"440","location":"34L","serialInd":"AKE76622CZ","cont":"LD3","actWgt":"440","restrictedCargo":""},{"dst":"","pri":"A31","tailer":"","dest":"PEK","type":"M","cargoStatus":0,"pos":"3","estWgt":"158","location":"34R","serialInd":"AKE78504CZ","cont":"LD3","actWgt":"158","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"4","estWgt":"90","location":"41L","serialInd":"AKE75866CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"4","estWgt":"90","location":"41R","serialInd":"AKE74977CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"A01","tailer":"","dest":"PEK","type":"BY","cargoStatus":0,"pos":"4","estWgt":"790","location":"43L","serialInd":"AKE73428CZ","cont":"LD3","actWgt":"790","restrictedCargo":""},{"dst":"","pri":"A31","tailer":"","dest":"PEK","type":"BY","cargoStatus":0,"pos":"4","estWgt":"103","location":"43R","serialInd":"AKE77538CZ","cont":"LD3","actWgt":"103","restrictedCargo":""},{"cargoStatus":0,"dst":"","pos":"5","pri":"A01","tailer":"","estWgt":"59","serialInd":"","cont":"BULK","dest":"PEK","type":"T","actWgt":"59","restrictedCargo":""},{"cargoStatus":0,"dst":"","pos":"5","pri":"A01","tailer":"","estWgt":"144","serialInd":"","cont":"BULK","dest":"PEK","type":"BY","actWgt":"144","restrictedCargo":""}],"returnReason":null,"preContent":"","cgoContent":"","autoLoadInstalledSingle":0,"flightNo":"CZ6184"}]
     */

    private String status;
    private String message;
    private Object rowCount;
    private List<DataBean> data;

    @Data
    public static class DataBean implements Serializable {
        /**
         * id : e02e0e3f54c8e8875c22bbad0160f837
         * flightId : 529079d8693445dcaf79a5ffc102bae5
         * version : 1
         * content :
         * reviewStatus : 0
         * createDate : 1557390815343
         * createUser : liuyuhuan
         * updateDate : 1557393717477
         * updateUser : null
         * documentType : 2
         * loadingAdvice : 0
         * loadingUser : 大张伟
         * contentObject : [{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"22L","serialInd":"AKE78253CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"22R","serialInd":"AKE74385CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"23L","serialInd":"AKE77772CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"23R","serialInd":"AKE76098CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"24L","serialInd":"AKE76350CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"24R","serialInd":"AKE77396CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"25L","serialInd":"AKE76469CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"25R","serialInd":"AKE78249CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"26L","serialInd":"AKE74294CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"2","estWgt":"90","location":"26R","serialInd":"AKE73733CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"3","estWgt":"1868","location":"32P","serialInd":"PMC42767CZ","cont":"PMC","actWgt":"1868","restrictedCargo":""},{"dst":"","pri":"A21","tailer":"","dest":"PEK","type":"C","cargoStatus":0,"pos":"3","estWgt":"440","location":"34L","serialInd":"AKE76622CZ","cont":"LD3","actWgt":"440","restrictedCargo":""},{"dst":"","pri":"A31","tailer":"","dest":"PEK","type":"M","cargoStatus":0,"pos":"3","estWgt":"158","location":"34R","serialInd":"AKE78504CZ","cont":"LD3","actWgt":"158","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"4","estWgt":"90","location":"41L","serialInd":"AKE75866CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"1","tailer":"","dest":"PEK","type":"X","cargoStatus":0,"pos":"4","estWgt":"90","location":"41R","serialInd":"AKE74977CZ","cont":"LD3","actWgt":"90","restrictedCargo":""},{"dst":"","pri":"A01","tailer":"","dest":"PEK","type":"BY","cargoStatus":0,"pos":"4","estWgt":"790","location":"43L","serialInd":"AKE73428CZ","cont":"LD3","actWgt":"790","restrictedCargo":""},{"dst":"","pri":"A31","tailer":"","dest":"PEK","type":"BY","cargoStatus":0,"pos":"4","estWgt":"103","location":"43R","serialInd":"AKE77538CZ","cont":"LD3","actWgt":"103","restrictedCargo":""},{"cargoStatus":0,"dst":"","pos":"5","pri":"A01","tailer":"","estWgt":"59","serialInd":"","cont":"BULK","dest":"PEK","type":"T","actWgt":"59","restrictedCargo":""},{"cargoStatus":0,"dst":"","pos":"5","pri":"A01","tailer":"","estWgt":"144","serialInd":"","cont":"BULK","dest":"PEK","type":"BY","actWgt":"144","restrictedCargo":""}]
         * returnReason : null
         * preContent :
         * cgoContent :
         * autoLoadInstalledSingle : 0
         * flightNo : CZ6184
         */
        private boolean showDetail;
        private String id;
        private String flightId;
        private String version;
        private String content;
        private int reviewStatus;
        private long createDate;
        private String createUser;
        private long updateDate;
        private Object updateUser;
        private int documentType;
        private int loadingAdvice;
        private String loadingUser;
        private Object returnReason;
        private String preContent;
        private String cgoContent;
        private int autoLoadInstalledSingle;
        private String flightNo;
        private List<ContentObjectBean> contentObject;

        @Data
        public static class ContentObjectBean implements Serializable{
            /**
             * dst :
             * pri : 1
             * tailer :
             * dest : PEK
             * type : X
             * cargoStatus : 0
             * pos : 2
             * estWgt : 90
             * location : 22L
             * serialInd : AKE78253CZ
             * cont : LD3
             * actWgt : 90
             * restrictedCargo :
             */

            private String dst;
            private String pri;
            private String tailer;
            private String dest;
            private String type;
            private int cargoStatus;
            private String pos;
            private String startBerth;
            private String estWgt;
            private String location;
            private String serialInd;
            private String cont;
            private String actWgt;
            private String restrictedCargo;
            /**
             * 缺少的数据
             */
            private boolean showPullDown = false;
            private String uldNumber = "-";
            private String goodsPosition = "";
            private int number = 0;
            private boolean locked;
        }
    }
}
