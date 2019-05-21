package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class TransportListBean implements Serializable {

    private List<TransportDataBase> records;
    private Integer total;
    private Integer size;
    private Integer current;
    private Integer pages;

    @Data
    public static class DeclareWaybillAdditionBean implements Serializable{
        /**
         * id : f777b4e451ea3f633f2187c5407102de
         * waybillId : b77f73826693dc6a3e6245404b7f103f
         * addtionInvoices : []
         */
        private String id;
        private String waybillId;
        private String addtionInvoices;
    }

}
