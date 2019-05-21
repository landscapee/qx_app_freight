package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class ListWaybillCodeBean {

    /**
     * status : 200
     * message : 正确
     * rowCount : null
     * data : [{"id":"2c9b20176d559c5dd861f600775fe001","waybillCode":"abc-fgiwerfn"}]
     * flag : null
     */

    private String status;
    private String message;
    private Object rowCount;
    private Object flag;
    private List<DataBean> data;

    @Data
    public static class DataBean {
        /**
         * id : 2c9b20176d559c5dd861f600775fe001
         * waybillCode : abc-fgiwerfn
         */

        private String id;
        private String waybillCode;
    }
}
