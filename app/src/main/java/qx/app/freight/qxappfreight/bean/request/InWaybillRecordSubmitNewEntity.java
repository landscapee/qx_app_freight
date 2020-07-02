package qx.app.freight.qxappfreight.bean.request;

import java.util.HashMap;
import java.util.List;

import lombok.Data;

/**
 * Created by zzq On 2020/6/30 18:30 & Copyright (C), 青霄科技
 *
 * @文档说明:
 */
@Data
public class InWaybillRecordSubmitNewEntity {

    /**
     * flag : 0
     * flightInfoId : 6197223a4ca14a51a2e7600228d00275
     * flightId : 14267160
     * taskId : 75384358
     * userId : u165cdb5251c94ef088b5dc5a27bc76d6
     * userName : 彭维
     * flightNo : GY7183
     * charterReWeight :
     * cargos : {"贵阳":[{"delFlag":0,"cacheId":"45615aa9-db50-4dfb-aef3-0c5e2ad08ab4","waybillCode":"661-46081512","waybillCodeFirst":"661","waybillCodeSecond":"46081512","tallyingTotal":"2","tallyingWeight":"2","warehouseArea":"5b7e8ff9558a3c16644c4f7aa5122a5b","warehouseAreaDisplay":"平板区","warehouseAreaType":"CTU_GARGO_STORAGE_TYPE_001","warehouseLocation":null,"remark":"","transit":"","counterUbnormalGoodsList":[],"mailType":"","allArrivedFlag":0,"stray":0,"surplusNumber":"","surplusWeight":"","strayDisabled":false,"originatingStationCn":"贵阳"}]}
     */
    private int flag;
    private String flightInfoId;
    private String flightId;
    private String taskId;
    private String userId;
    private String userName;
    private String flightNo;
    private String charterReWeight;
    private HashMap<String, List<SingleLineBean>> cargos = new HashMap<>();

    @Data
    public static class SingleLineBean {
        /**
         * delFlag : 0
         * waybillCode : 661-46081512
         * waybillCodeFirst : 661
         * waybillCodeSecond : 46081512
         * tallyingTotal : 2
         * tallyingWeight : 2
         * warehouseArea : 5b7e8ff9558a3c16644c4f7aa5122a5b
         * warehouseAreaDisplay : 平板区
         * warehouseAreaType : CTU_GARGO_STORAGE_TYPE_001
         * warehouseLocation : null
         * remark :
         * transit :
         * counterUbnormalGoodsList : []
         * mailType :
         * allArrivedFlag : 0
         * cacheId : 45615aa9-db50-4dfb-aef3-0c5e2ad08ab4
         * stray : 0
         * surplusNumber :
         * surplusWeight :
         * strayDisabled : false
         * originatingStationCn : 贵阳
         */
        private int delFlag;
        private String waybillCode;
        private String waybillCodeFirst;
        private String waybillCodeSecond;
        private String tallyingTotal;
        private String tallyingWeight;
        private String warehouseArea;
        private String warehouseAreaDisplay;
        private String warehouseAreaType;
        private Object warehouseLocation;
        private String remark;
        private String transit;
        private String mailType;
        private int allArrivedFlag;
        private int stray;
        private String surplusNumber;
        private String surplusWeight;
        private boolean strayDisabled;
        private String originatingStationCn;
        private List<?> counterUbnormalGoodsList;
        private String id;
    }
}
