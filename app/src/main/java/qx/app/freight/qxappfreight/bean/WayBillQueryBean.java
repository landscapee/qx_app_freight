package qx.app.freight.qxappfreight.bean;

import lombok.Data;

@Data
public class WayBillQueryBean {
    private String wayBillCode;

    public WayBillQueryBean(String wayBillCode) {
        this.wayBillCode = wayBillCode;
    }
}
