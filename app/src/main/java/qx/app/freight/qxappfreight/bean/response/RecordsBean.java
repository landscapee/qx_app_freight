package qx.app.freight.qxappfreight.bean.response;


import java.io.Serializable;

import lombok.Data;

@Data
public class RecordsBean implements Serializable {

    private String id;
    private String  reservoirName;
    private String  reservoirType;
    private String value;
    private String type;
    private String des;
    private String outletType;

    private String name;

    private String reservoirSaveType;
    private String code;
}
