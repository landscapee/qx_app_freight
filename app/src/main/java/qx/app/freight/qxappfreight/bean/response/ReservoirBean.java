package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class ReservoirBean {

    /**
     * records : [{"id":"1693112a40c66bdd4626b2f2bf017f1c","depId":null,"code":"wf_put_out","reservoirName":"冻货1","reservoirType":2},{"id":"e4e039ca93e21ca80b78ad68a22bdd04","depId":null,"code":"wf_put_out","reservoirName":"危险1","reservoirType":3},{"id":"255c36daf5a1a0c835a48389a7340161","depId":null,"code":"wf_put_out","reservoirName":"活体1","reservoirType":1},{"id":"c0e7045e4d5708ded1b71c91eb19cf6a","depId":null,"code":"wf_put_out","reservoirName":"贵货1","reservoirType":5},{"id":"e14c9f02527629e41a0043a0b73911e3","depId":null,"code":"wf_put_out","reservoirName":"普货1","reservoirType":0},{"id":"041bdf1ba7678e9bf050fc20a43fec88","depId":null,"code":"wf_put_out","reservoirName":"临货1","reservoirType":4},{"id":"1b3e91f87078317125841a17060fd8fd","depId":null,"code":"wf_put_out","reservoirName":"暂存1","reservoirType":6},{"id":"7c2426d0cf4e5f969e5b4f036846e56f","depId":null,"code":"wf_put_out","reservoirName":"快提1","reservoirType":7}]
     * total : 8
     * size : 10
     * current : 1
     * searchCount : true
     * pages : 1
     */

    private int total;
    private int size;
    private int current;
    private boolean searchCount;
    private int pages;
    private List<RecordsBean> records;

    @Data
    public static class RecordsBean {
        /**
         * id : 1693112a40c66bdd4626b2f2bf017f1c
         * depId : null
         * code : wf_put_out
         * reservoirName : 冻货1
         * reservoirType : 2
         */

        private String id;
        private Object depId;
        private String code;
        private String reservoirName;
        private int reservoirType;
    }
}
