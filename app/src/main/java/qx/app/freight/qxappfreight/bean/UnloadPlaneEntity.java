package qx.app.freight.qxappfreight.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class UnloadPlaneEntity implements Serializable {
    private String berth;//舱位
    private String goodsPosition;//货位
    private String boardNumber;//板号
    private String uldNumber;//ULD号
    private String target;//目的地
    private String type;//类型
    private double weight;//重量
}
