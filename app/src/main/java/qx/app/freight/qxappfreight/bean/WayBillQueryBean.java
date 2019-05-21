package qx.app.freight.qxappfreight.bean;

import lombok.Data;

@Data
public class WayBillQueryBean {
    private String wayBillCode;
    private String id;

    public WayBillQueryBean(String wayBillCode, String id) {
        this.wayBillCode = wayBillCode;
        this.id = id;
    }
}
