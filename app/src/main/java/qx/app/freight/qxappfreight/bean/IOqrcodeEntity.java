package qx.app.freight.qxappfreight.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IOqrcodeEntity implements Serializable {
    private String outletId;//营业点code
    private String BusinessName;//营业点名称
    private String DepotID;//库房ID
    private String DepotName;//库房名称
    private String DepotCode;//库房code

}
