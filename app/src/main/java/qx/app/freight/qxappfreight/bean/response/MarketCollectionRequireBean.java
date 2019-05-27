package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by guohao on 2019/5/27 15:07
 *
 * @title 航空公司资质返回值实体
 */

@Data
public class MarketCollectionRequireBean implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 收运要求
     */
    private String colRequire;

    /**
     * 生效日期
     */
    private Long startTime;

    /**
     * 失效日期
     */
    private Long endTime;

    /**
     * 适用航司
     */
    private String[] applyAirline;
}
