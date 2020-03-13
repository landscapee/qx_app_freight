package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class LikePageBean {

    private String id;
    private String uldCode;

    public void setUldType(String uldType) {
        uldType = uldType.toUpperCase();
        this.uldType = uldType;
    }

    public void setIata(String iata) {
        iata = iata.toUpperCase();
        this.iata = iata;
    }

    private String uldType;
    private String applyType;
    private String bulkingType;
    private String uldWeight;
    private String weight;
    private String airType;
    private String iata;
    private String delFlag;
}
