package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class TransportTodoListBean {
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
         */
        private String id;
        private String tpScooterId;
        private String tpScooterType;
        private String tpScooterCode;
        private String tpCargoType;
        private int tpCargoNumber;
        private int tpCargoWeight;
        private double tpCargoVolume;
        private String tpFlightId;
        private String tpFlightNumber;
        private String tpFlightLocate;
        private long tpFlightTime;
        private String tpFregihtSpace;
        private String tpStartLocate;
        private String tpDestinationLocate;
        private String tpType;
        private int tpState;
        private String tpOperator;
        private int dtoType;
        private Object newId;
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
        private long reportTime;
        private String taskId;
        private List<String> waybillIds;

        private String acdmDtoId;

        /**
         * 用于显示板存在于某航班下
         */
        private String flightNo;
        private String planePlace;
        private String planeType;
        private long etd;
        private String flightRoute;
        private int num;//板车数量
        private String carType;//板车类型


}
