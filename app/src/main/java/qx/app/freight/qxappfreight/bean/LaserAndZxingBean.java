package qx.app.freight.qxappfreight.bean;

import lombok.Data;

@Data
public class LaserAndZxingBean {
    private String data;
    private String typeName;

    public LaserAndZxingBean(String data, String typeName) {
        this.data = data;
        this.typeName = typeName;
    }
}
