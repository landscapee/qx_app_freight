package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class PageListBean {

    /**
     * records : [{"id":"3404c7e4601d48d8a578bc9f3ab36859","createDate":1548663150296,"createUser":"yqs","content":"ms","readingStatus":1},{"id":"88e2c5b8d43649298ec17013c866b342","createDate":1548663053825,"createUser":"yqs","content":"消息","readingStatus":1}]
     * total : 2
     * size : 10
     * current : 1
     * pages : 1
     */

    private int total;
    private int size;
    private int current;
    private int pages;
    private List<RecordsBean> records;

    @Data
    public static class RecordsBean {
        /**
         * id : 3404c7e4601d48d8a578bc9f3ab36859
         * createDate : 1548663150296
         * createUser : yqs
         * content : ms
         * readingStatus : 1
         */
        private String id;
        private long createTime;
        private String createUser;
        private String content;
        private int readingStatus;
    }
}
