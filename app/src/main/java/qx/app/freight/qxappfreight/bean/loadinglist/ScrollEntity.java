package qx.app.freight.qxappfreight.bean.loadinglist;

import lombok.Data;

/**
 * 装机单右边的数据
 */
@Data
public class ScrollEntity {
    private String boardNumber;//板号
    private String uldNumber;//ULD号
    private String target;//目的地
    private String type;//类型 服务器返回 C:货物，M:邮件，B:行李，T:转港行李，BY:Y舱行李，BT:过站行李，CT:过站货物，X：空集装箱
    private String weight;//重量
    private String number;//件数
    private boolean showPull;//是否显示拉下
}
