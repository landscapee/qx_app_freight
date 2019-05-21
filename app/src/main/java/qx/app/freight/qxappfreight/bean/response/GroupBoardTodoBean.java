package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class GroupBoardTodoBean {

    /**
     * status : 200
     * message : 正确
     * rowCount : null
     * data : [{"taskId":"72874538","taskType":"预配","taskTypeCode":"prematching","taskStartTime":null,"taskEndTime":null,"taskResult":null,"stepOrder":6,"id":null,"waybillCode":null,"mailType":null,"flightNumber":null,"flightDate":null,"originatingStation":null,"originatingStationCn":null,"destinationStation":"CZX","destinationStationCn":"常州","consignee":null,"consigneePhone":null,"consigneePostcode":null,"consigneeAddress":null,"specialCargoCode":null,"coldStorage":null,"totalNumberPackages":null,"billingWeight":null,"totalWeight":null,"refrigeratedTemperature":null,"activitiId":null,"internalTransferDate":null,"internalTransferFlight":null,"internalTransferWaybill":null,"exchangeWaybillId":null,"exchangeNewWaybill":null,"exchangeWaybillBefore":null,"status":null,"shipperCompanyId":null,"createUser":null,"createTime":null,"lastUpdateTime":null,"lastUpdateOperator":null,"delFlag":null,"applyTime":null,"expectedDeliveryTime":null,"expectedDeliveryGate":null,"chargeFlag":null,"virtualFlag":null,"otherFlag":null,"flightId":"57004eb97ee041deadf7762982abf18b","flightYLId":"12242330","declareItem":null,"declareWaybillAddition":null,"flightNo":"MU2936","aircraftType":"320","associateAirport":null,"spotFlag":null,"storageTime":null,"serialNumber":null,"consigneeIdentityCard":null,"outboundNumber":null,"waybillCount":null,"totalVolume":null,"exceptionFlag":null,"transferStation":null,"freightName":null,"flightName":null,"ata":null,"totalScooterNum":0,"arriveWarehouseNum":0,"etd":1558449900000}]
     */

    private String status;
    private String message;
    private Object rowCount;
    private List<TransportDataBean> data;
    @Data
    @EqualsAndHashCode
    public static class TransportDataBean extends TransportDataBase {

    }
}
