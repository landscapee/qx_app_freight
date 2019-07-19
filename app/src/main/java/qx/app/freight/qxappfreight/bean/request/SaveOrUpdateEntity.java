package qx.app.freight.qxappfreight.bean.request;

import lombok.Data;

@Data
public class SaveOrUpdateEntity {
    private String uldCode;
    private String uldType;
    private String applyType;
    private String bulkingType;
    private String uldWeight;
    private String weight;
    private String airType;
    private String iata;
    private int delFlag;

    public void setUldType(String uldType) {
        uldType = uldType.toUpperCase();
        this.uldType = uldType;
    }

    public void setIata(String iata) {
        iata = iata.toUpperCase();
        this.iata = iata;
    }
}
