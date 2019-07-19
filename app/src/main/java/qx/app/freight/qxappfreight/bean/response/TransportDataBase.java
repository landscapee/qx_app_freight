package qx.app.freight.qxappfreight.bean.response;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by guohao on 2019/5/17 15:42 @COPYRIGHT 青霄科技
 *
 * @title：
 * @description：
 */
@Data
public class TransportDataBase implements MultiItemEntity, Serializable {

    /**
     * taskId : 522660
     * taskType : 收运
     * taskTypeCode : collection
     * taskStartTime : null
     * taskEndTime : null
     * taskResult : null
     * stepOrder : 4
     * id : b77f73826693dc6a3e6245404b7f103f
     * waybillCode : 0281616161
     * mailType : C
     * flightNumber : EU9568
     * flightDate : 1546444800000
     * originatingStation : null
     * destinationStation : null
     * consignee : 青宵科技
     * consigneePhone : 13654988874
     * consigneePostcode : jsows@foxmail.com
     * consigneeAddress : 四川省成都市双流机场1号
     * specialCargoCode : 241872
     * coldStorage : 1
     * totalNumberPackages : 100.0
     * billingWeight : 500.0
     * totalWeight : 100.0
     * refrigeratedTemperature : 0.0
     * activitiId : 530005
     * internalTransferDate : null
     * internalTransferFlight : null
     * internalTransferWaybill : null
     * exchangeWaybillId : null
     * exchangeNewWaybill : null
     * exchangeWaybillBefore : null
     * status : 3
     * shipperCompanyId : enbc55b2a4218a4261ac7083ccaa69c288
     * createUser : null
     * createTime : 1549877417822
     * lastUpdateTime : 1549878311614
     * lastUpdateOperator : null
     * delFlag : 0
     * applyTime : null
     * expectedDeliveryTime : null
     * expectedDeliveryGate : null
     * chargeFlag : 0
     * virtualFlag : 0
     * otherFlag : 0
     * flightId : null
     * declareItem : [{"itemId":null,"waybillId":"b77f73826693dc6a3e6245404b7f103f","cargoId":"7192c890c57d90b76cec8c5e53063ff7","number":100,"weight":1400,"volume":100,"packagingType":["包装","包装2"],"cargoCn":"梅花"}]
     * declareWaybillAddition : {"id":"f777b4e451ea3f633f2187c5407102de","waybillId":"b77f73826693dc6a3e6245404b7f103f","addtionInvoices":"[]"}
     * flightNo : null
     * aircraftType : null
     * associateAirport : null
     * etd : null
     * outboundNumber  0  已出库单数
     * waybillCount  3 总运单数
     */
    private String stepOwner;
    private String taskId;
    private String taskType;
    private String deptCode;
    private String taskTypeCode;//collection 收货  ；RR_collectReturn收运退货 changeApply 换单审核
    private String taskStartTime;
    private String taskEndTime;
    private Object taskResult;
    private int stepOrder;
    private String id;
    private String waybillCode;
    private String mailType;
    private String flightNumber;
    private long flightDate;
    private Object originatingStation;
    private Object destinationStation;

    private String consigneePostcode;
    private String consigneeAddress;
    private String specialCargoCode;
    //存储类型
    private String coldStorage;
    private String billingWeight;
    private String totalNumber;
    private String totalWeight;
    private String totalVolume;
    private String refrigeratedTemperature;
    private String activitiId;
    private Object internalTransferDate;
    private Object internalTransferFlight;
    private Object internalTransferWaybill;

    private String newDeclareWaybillCode; // 新运单号

    private Object exchangeWaybillId;
    private Object exchangeNewWaybill;
    private String exchangeWaybillBefore;

    //运单状态:0:待退货,-1:已退货,-2:系统数据异常,-3收验失败,-4:安检失败,1:未申报,2:已申报,3:已收验,4:已安检,5:已收货,6:待收费,7:已收费,8,已预配,9:已组板,10:已报载,11:已复重,12:已串板,13:已起飞,14:已拉货
    private String status;
    private String shipperCompanyId;
    private Object createUser;
    private String createTime;
    private String lastUpdateTime;
    private Object lastUpdateOperator;
    private String delFlag;
    private Object applyTime;
    private long expectedDeliveryTime;
    private String expectedDeliveryGate;
    private String chargeFlag;
    private String virtualFlag;
    private String otherFlag;
    private String flightId;
    private TransportListBean.DeclareWaybillAdditionBean declareWaybillAddition;
    private String flightNo;
    private int outboundNumber;
    private int waybillCount;
    private Object aircraftType;
    private Object associateAirport;
    private String spotFlag;
    private long etd;
    private long ata;
    private List<DeclareItem> declareItem;
    private boolean isExpand = false; //是否展开
    private String freightName; //货代公司名字
    private String flightName; //航空公司名字
    private String serialNumber;  //流水号

    private int totalScooterNum; //总板车数
    private int arriveWarehouseNum;//已到板车数

    private String flightYLId;// 航班尤里id

    private String role;//角色
    private String cargoCn;//品名
    private String packagingType;
    private String waybillId;

    private String consignee;
    private String consigneePhone;
    private String consigneeIdentityCard;
    /**
     * 提货人名
     * */
    private String receiverName;

    /**
     * 提货人电话
     * */
    private String receiverPhone;

    /**
     * 提货人身份证
     * */
    private String receiverIdentityCard;

    /**
     * 复重代办新添加
     * 航线   flightCourseByAndroid
     * 机位号  seat
     * 机尾号  aircraftNo
     */
    private List<String> flightCourseByAndroid;

    private String seat;

    private String aircraftNo;

    @Override
    public int getItemType() {
        if (flightCourseByAndroid !=null)
            return flightCourseByAndroid.size();
        else
            return 2;
    }

}
