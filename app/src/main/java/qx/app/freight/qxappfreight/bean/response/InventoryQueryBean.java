package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class InventoryQueryBean {
    /**
     * records : [{"id":"90d344d3396655bac52d1f626119770b","taskType":1,"createTime":1557970623436,"endTime":null,"status":1,"handler":null,"createUser":"liulei"},{"id":"ad5ef9c0e428c27e4b6d29d0337220f2","taskType":1,"createTime":1557970592000,"endTime":null,"status":1,"handler":null,"createUser":"liulei"},{"id":"cef287d2caefcf5e7c23ef0ca985f362","taskType":2,"createTime":1557970692000,"endTime":null,"status":1,"handler":null,"createUser":"liulei"},{"id":"9a8eeb7e427afc4d64a0ed093ff592e4","taskType":2,"createTime":1557970692000,"endTime":null,"status":0,"handler":"zhangshuo","createUser":"liulei"},{"id":"f1849890bfb304a1eb552226410e8805","taskType":1,"createTime":1557970692000,"endTime":null,"status":0,"handler":"zhangshuo","createUser":"liulei"}]
     * total : 5
     * size : 10
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
         * id : 90d344d3396655bac52d1f626119770b
         * taskType : 1
         * createTime : 1557970623436
         * endTime : null
         * status : 1
         * handler : null
         * createUser : liulei
         */

        private String id;

        /**
         * 任务类型：(0：鲜活清库；1：全仓清库)
         */
        private int taskType;

        /**
         * 任务创建时间
         */
        private long createTime;

        /**
         * 任务结束时间
         */
        private long endTime;

        /**
         * 状态：(0：已完成；1：进行中)
         */
        private int status;

        /**
         * 审核处置人
         */
        private String handler;

        /**
         * 任务创建人
         */
        private String createUser;
        //任务创建人名字
        private String createUserName;

        //审核处置人名字
        private String handlerName;


    }


}
