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
    private String type;//类型 服务器返回 C:货物，M:邮件，B:行李，T:转港行李，BY:Y舱行李，BT:过站行李，CT:过站货物，X：空集装箱
    private double weight;//重量
    private int number;//件数
}
