package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class WebSocketResultBean {
    /**
     * chgData : [{"chargeFlag":"0","refrigeratedTemperature":"0","shipperCompanyId":"enbc55b2a4218a4261ac7083ccaa69c288","totalVolume":"100","spotFlag":"1","consigneePostcode":"jsows@foxmail.com","expectedDeliveryGate":"D口","virtualFlag":"0","delFlag":"0","totalNumberPackages":"100.0","taskType":"收验","consigneePhone":"13654988874","id":"12ed01f95297891a1531ff2d605abe6d","applyTime":"1553342848932","consigneeAddress":"四川省成都市双流机场1号","activitiId":"1932541","declareItem":[{"volume":100,"number":100,"cargoId":"b856f9932ec34a3bb2cd320c5ca136e3","cargoCn":"钛棒","waybillId":"12ed01f95297891a1531ff2d605abe6d","weight":100,"packagingType":["纸盒","编织袋"]}],"otherFlag":"0","specialCargoCode":"241872","consignee":"青宵科技","flightDate":"1553315440000","stepOrder":2,"taskTypeCode":"receive","flightNumber":"EU9564","coldStorage":"0","mailType":"C","billingWeight":"500.0","createTime":"1553343105594","totalWeight":"100.0","exceptionFlag":"0","waybillCode":"02810000028","createUser":"u90eab98bad7d407ca9ff7537f5e4367a","declareWaybillAddition":{"addtionInvoices":"[]","waybillId":"12ed01f95297891a1531ff2d605abe6d","id":"4bc92587e86924d1cfddec3e1fd45be7"},"expectedDeliveryTime":"1553343105594","taskId":"1932547","status":"2"}]
     * flag : N
     */
    private String flag; //N是新增, D是删除
    private List <TransportListBean.TransportDataBean> chgData;

}
