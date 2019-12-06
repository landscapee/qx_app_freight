package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WaybillCommodities implements Serializable {
    /**
     * itemId : 926661ffa4b5166e0cc86fbc1a01cbb4
     * waybillId : 8b8c902532374181bc74566e3fe4ce0c
     * cargoId : 62f2fe8b-40f1-41bd-9683-39c684d54619
     * number : null
     * weight : null
     * volume : null
     * packagingType : null
     * cargoCn : 鸡苗
     * type : 0
     */

    private String itemId;
    private String waybillId;
    private String cargoId;
    private int number;
    private double weight;
    private double volume;
    private String packagingType;
    private String cargoCn;
    private int type;


}
