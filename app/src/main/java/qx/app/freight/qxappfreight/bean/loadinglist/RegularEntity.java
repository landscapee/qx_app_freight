package qx.app.freight.qxappfreight.bean.loadinglist;

import java.util.List;

import lombok.Data;

/**
 * 装机单左边的数据
 */
@Data
public class RegularEntity {
    private boolean locked;//是否被锁定
    private String lockTitle;
    private String berth;//舱位
    private String goodsPosition;//货位
    private boolean showPull;//是否显示拉下
    private List<String> berthList;//当前航班的舱位信息列表
    private List<String> goodsPosList;//当前航班的货位信息列表
}
