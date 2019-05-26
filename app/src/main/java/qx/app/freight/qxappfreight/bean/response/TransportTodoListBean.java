package qx.app.freight.qxappfreight.bean.response;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class TransportTodoListBean implements MultiItemEntity , Serializable {
    /**
     * id : aec574f5254c20c7fa9c04685293e581
     * tpScooterId : 123
     * tpScooterType : 大板车
     * tpScooterCode : JFGD
     * tpCargoType : N
     * tpCargoNumber : 200
     * tpCargoWeight : 300
     * tpCargoVolume : 5.5
     * tpFlightId : 6ghg3
     * tpFlightNumber : 3U9999
     * tpFlightLocate : 101
     * tpFlightTime : 2019-02-26 12:00:00
     * tpFregihtSpace : 1H
     * tpStartLocate : 库区
     * tpDesticationLocate : 待运区
     * tpType : 离
     * tpState : 0
     * tpOperator : null
     * dtoType : 0
     * newId : null
     * flightIndicator : 国内D 或者 国际I
     * warehouseId :  库区id（新加）
     */
    private String id;
    private String tpScooterId;
    private String tpScooterType;
    private String tpScooterCode;
    private String tpCargoType;
    private Integer tpCargoNumber;
    private Double tpCargoWeight;
    private Double tpCargoVolume;
    private String tpFlightId;
    private String tpFlightNumber;
    private String tpFlightLocate;
    private Long tpFlightTime;
    private String tpFregihtSpace;
    private String tpStartLocate;
    private String tpDestinationLocate;
    private String tpType;
    private Integer tpState;//1 锁定（被扫描了）
    private String tpOperator;
    /**
     * 作用一:用来对MQ接收的数据进行判断,判断值由MQ发送方填写
     * 作用二:手持端结束运输时传入该字段对应的值,根据值修改该板车的下个起始地以及运输目的地
     * 1-库区中的正常货物,需要设置该数据的起始位置为库区,目的地为待运区
     * 2-待运区中的正常货物,需要设置该数据的起始位置为待运区,目的地为机下
     * 3-库区中的拉下货物
     * 4-待运区中的拉下货物
     * 8-拉货上报的货物
     * 9-卸机的货物
     */
    private Integer dtoType;
    private Object newId;
    private String flightIndicator;
    private String warehouseId;
    /**
     * 是否到机位
     */
    private Integer inSeat;
    /**
     * 开始区域扫码位置类型
     */
    private String beginAreaType;
    /**
     * 开始区域扫码位置编号
     */
    private String beginAreaId;
    /**
     * 结束位置类型
     */
    private String endAreaType;
    /**
     * 结束位置编号
     */
    private String endAreaId;
    /**
     * 上报时间
     */
    private Long reportTime;
    private String taskId;
    private List<String> waybillIds;

    private String acdmDtoId;

    /**
     * 用于显示板存在于某航班下
     */
    private String flightNo;
    private String planePlace;
    private String planeType;
    private Long etd;
    private List<String> flightRoute;
    private Integer num;//板车数量
    private String carType;//板车类型


    /*************行李区数据******************/
    // 出港行李数据保存时需要的行李转盘标识
    private String baggageTurntable;
    //出港行李数据上传用户ID
    private String baggageSubOperator;
    //出港行李数据上传用户名称
    private String baggageSubUserName;
    //出港行李数据上传终端ID
    private String baggageSubTerminal;
    //
    private String tpAsFlightId;
    //业务id
    private String tpFlightBusId;
    //
    private int tpFlightType; //航班类型,0 国内出港,1,国内进港 2国外出港 3国内进港
    //仓位
    private String tpFreightSpace;
    //运单号码
    private String billNumber;
    //是板车下拉还是运单下拉type值
    private Integer infoType;
    private Integer maxBillNumber;
    private Double maxBillWeight;
    private String billCode;
    private String waybillId;
    private Integer pullInNumber;//拉货上报输入的板车拉的件数
    private Double pullInWeight;//拉货上报输入的板车拉的重量

    private String taskPk;//子任务id

    @Override
    public int getItemType() {
        return infoType;
    }
}
