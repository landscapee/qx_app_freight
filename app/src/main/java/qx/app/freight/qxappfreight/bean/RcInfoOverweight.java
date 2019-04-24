package qx.app.freight.qxappfreight.bean;

import lombok.Data;

@Data
public class RcInfoOverweight {
    private String id;
    private String rcId;
    private String cargoCn;
    private int count;
    private int weight;
    private int volume;
    private int overWeight;
    private int delFlag;
}
