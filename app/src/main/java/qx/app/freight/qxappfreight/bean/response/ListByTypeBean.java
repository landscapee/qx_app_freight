package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class ListByTypeBean {
    /**
     * status : 200
     * message : 正确
     * rowCount : null
     * data : {"records":[{"id":"37f2af79605083e29222781460029cd5","name":"AKE","value":"1","type":1}],"total":1,"size":50,"current":1,"taskHandler":null,"searchCount":true,"pages":1}
     * flag : null
     */

    private String flag;


        /**
         * records : [{"id":"37f2af79605083e29222781460029cd5","name":"AKE","value":"1","type":1}]
         * total : 1
         * size : 50
         * current : 1
         * taskHandler : null
         * searchCount : true
         * pages : 1
         */

        private int total;
        private int size;
        private int current;
        private Object taskHandler;
        private boolean searchCount;
        private int pages;
        private List<RecordsBean> records;


        @Data
        public static class RecordsBean {
            /**
             * id : 37f2af79605083e29222781460029cd5
             * name : AKE
             * value : 1
             * type : 1
             */

            private String id;
            private String name;
            private String value;
            private int type;

        }

//    private String id;
//    private String name;
//    private String value;
//    private int type;

}
