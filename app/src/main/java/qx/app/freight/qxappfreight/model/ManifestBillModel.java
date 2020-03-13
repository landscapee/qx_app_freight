package qx.app.freight.qxappfreight.model;

import java.util.List;

import lombok.Data;

/**
 * created by swd
 * 2019/7/3 16:40
 */
@Data
public class ManifestBillModel {

    /**
     * flightNo : EU2257
     * flightDate : 1562044200000
     * origin : CTU
     * destination : SJW
     * aircraftType : 320
     * airlineEn : EU
     * airlineCn : 成都航空有限公司
     * aircraftNo : B8345
     * waybills : [{"id":null,"waybillId":"2e1657a507244d40883bc7536b110d50","waybillCode":"911-19047405","number":0,"weight":null,"volume":null,"runTimeScooterId":null,"scooterId":null,"scooterType":0,"scooterCode":"RCX,VAL","scooterWeight":0,"repPlaceId":null,"repName":null,"repPlaceNum":null,"repPlaceStatus":null,"inFlight":0,"groupScooterStatus":0,"destinationStation":null,"cargoCn":"黄金,白金,贵重金属","routeEn":null,"cargoType":null,"inflightnum":30000,"totalnum":30000,"inflightweight":3000,"totalweight":3000,"inFlightVolume":7,"totalVolume":7,"remark":null,"updateWeight":null,"mailType":null,"updateFlag":1},{"id":null,"waybillId":"ad026f52d73e490fa0af1bd1e859d018","waybillCode":"911-20927675","number":0,"weight":null,"volume":null,"runTimeScooterId":null,"scooterId":null,"scooterType":0,"scooterCode":"","scooterWeight":0,"repPlaceId":null,"repName":null,"repPlaceNum":null,"repPlaceStatus":null,"inFlight":0,"groupScooterStatus":0,"destinationStation":null,"cargoCn":"土豆粉","routeEn":null,"cargoType":null,"inflightnum":7000,"totalnum":7000,"inflightweight":4000,"totalweight":4000,"inFlightVolume":3,"totalVolume":3,"remark":null,"updateWeight":null,"mailType":null,"updateFlag":1},{"id":null,"waybillId":"4aaf18de3a6048d6b01a4724e91ec698","waybillCode":"911-52669584","number":0,"weight":null,"volume":null,"runTimeScooterId":null,"scooterId":null,"scooterType":0,"scooterCode":"","scooterWeight":0,"repPlaceId":null,"repName":null,"repPlaceNum":null,"repPlaceStatus":null,"inFlight":0,"groupScooterStatus":0,"destinationStation":null,"cargoCn":"玉米","routeEn":null,"cargoType":null,"inflightnum":6000,"totalnum":6000,"inflightweight":4000,"totalweight":4000,"inFlightVolume":7,"totalVolume":7,"remark":null,"updateWeight":null,"mailType":null,"updateFlag":1}]
     */

    private String flightNo;
    private long flightDate;
    private String origin;
    private String destination;
    private String aircraftType;
    private String airlineEn;
    private String airlineCn;
    private String aircraftNo;
    private List<WaybillsBean> waybills;
}
