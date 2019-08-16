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
    private List<String> berthList;//当前航班的舱位信息列表
    private List<String> goodsPosList;//当前航班的货位信息列表
    private String scooterId;//板车id
    private boolean hasLiveGoods;//是否有活体
    private int moveSize;//左边数据显示下移的数量
}
