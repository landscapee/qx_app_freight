package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.response.FtGroupScooter;
import qx.app.freight.qxappfreight.bean.response.FtRuntimeFlightScooter;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class FightScooterSubmitEntity {


    /**
     * userId : userId
     * taskId : taskId
     * flightId : flightId
     * scooters : [{"id":"e414cc77a0d04913a62270230364210d","scooterId":"5dab37031dbf467a8765d362379d9c68","flightId":"b0bf4cb543ed4304a3bb4d3639eedac2","total":null,"weight":123,"volume":123,"flightDestination":null,"suggestRepository":"H1","uldId":null,"createDate":null,"createUser":null,"updateDate":null,"updateUser":null,"delFlag":null,"reWeight":null,"reDifference":null,"reDifferenceRate":null,"reWeightFinish":null,"personUpdateValue":null,"rcInfoList":[{"id":"9e09063610a7aaa748b1dc0ae14427dd","waybillId":"1b8dfe1108eeef0d6e484d18a3625709","waybillCode":"姚秋实666","cargoId":"0b574bd87d8e823d7f04a48876771488","cargoCn":"玫瑰花","number":123,"weight":123,"volume":123,"scooterId":"5dab37031dbf467a8765d362379d9c68","scooterType":1,"scooterCode":"12661","scooterWeight":575,"repPlaceId":null,"repName":null,"repPlaceNum":"系统分配","repPlaceStatus":null,"uldId":null,"uldCode":"","uldType":null,"uldWeight":null,"iata":null,"overWeight":null,"delFlag":0,"createUser":null,"createDate":1550842005554,"updateUser":null,"updateDate":null,"updateStatus":0,"inFlight":0}],"updateStatus":null,"scooterCode":"12661","scooterType":1,"uldCode":"","uldType":null,"iata":null,"scooterWeight":575,"uldWeight":null,"inFlight":1}]
     * withoutScootereRcInfos : [{"id":"9e09063610a7aaa748b1dc0ae14427dd","waybillId":"1b8dfe1108eeef0d6e484d18a3625709","waybillCode":"姚秋实666","cargoId":"0b574bd87d8e823d7f04a48876771488","cargoCn":"玫瑰花","number":123,"weight":123,"volume":123,"scooterId":"5dab37031dbf467a8765d362379d9c68","scooterType":1,"scooterCode":"12661","scooterWeight":575,"repPlaceId":null,"repName":null,"repPlaceNum":"系统分配","repPlaceStatus":null,"uldId":null,"uldCode":"","uldType":null,"uldWeight":null,"iata":null,"overWeight":null,"delFlag":0,"createUser":null,"createDate":1550842005554,"updateUser":null,"updateDate":null,"updateStatus":0,"inFlight":0}]
     * deleteScooters : [{"id":"9e09063610a7aaa748b1dc0ae14427dd","waybillId":"1b8dfe1108eeef0d6e484d18a3625709","waybillCode":"姚秋实666","cargoId":"0b574bd87d8e823d7f04a48876771488","cargoCn":"玫瑰花","number":123,"weight":123,"volume":123,"scooterId":"5dab37031dbf467a8765d362379d9c68","scooterType":1,"scooterCode":"12661","scooterWeight":575,"repPlaceId":null,"repName":null,"repPlaceNum":"系统分配","repPlaceStatus":null,"uldId":null,"uldCode":"","uldType":null,"uldWeight":null,"iata":null,"overWeight":null,"delFlag":0,"createUser":null,"createDate":1550842005554,"updateUser":null,"updateDate":null,"updateStatus":0,"inFlight":0}]
     * deleteRedRcInfos : [{"id":"9e09063610a7aaa748b1dc0ae14427dd","waybillId":"1b8dfe1108eeef0d6e484d18a3625709","waybillCode":"姚秋实666","cargoId":"0b574bd87d8e823d7f04a48876771488","cargoCn":"玫瑰花","number":123,"weight":123,"volume":123,"scooterId":"5dab37031dbf467a8765d362379d9c68","scooterType":1,"scooterCode":"12661","scooterWeight":575,"repPlaceId":null,"repName":null,"repPlaceNum":"系统分配","repPlaceStatus":null,"uldId":null,"uldCode":"","uldType":null,"uldWeight":null,"iata":null,"overWeight":null,"delFlag":0,"createUser":null,"createDate":1550842005554,"updateUser":null,"updateDate":null,"updateStatus":0,"inFlight":0}]
     */

    private String userId;
    private String taskId;
    private String flightId;
    private List<FtRuntimeFlightScooter> scooters; //板车列表
    private List<FtGroupScooter> withoutScootereRcInfos; //无板收运记录
    private List<FtGroupScooter> deleteRcInfos;//删除收运记录
    private List<FtGroupScooter> deleteRedRcInfos;//删除非本航班收运记录

}
