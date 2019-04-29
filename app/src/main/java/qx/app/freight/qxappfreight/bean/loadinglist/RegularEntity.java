package qx.app.freight.qxappfreight.bean.loadinglist;

import lombok.Data;

/**
 * 装机单左边的数据
 */
@Data
public class RegularEntity {
    private String berth;//舱位
    private String goodsPosition;//货位
    private boolean showPull;//是否显示拉下
}
