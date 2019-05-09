package qx.app.freight.qxappfreight.bean.response;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import lombok.Data;

@Data
public class InventoryQueryBean implements MultiItemEntity {

    private InWaybillBean inWaybill;
    private FlightBean flight;

    @Override
    public int getItemType() {
        return flight.flightCourseByAndroid.size();
    }

    @Data
    public static class InWaybillBean {
        private String id;
        private String waybillCode;
        private String indicator;
        private String flightNo;
        private String flightInfoId;
        private int flightId;
        private String mailType;
        private int documentDelivery;
        private int totalNumberPackages;
        private int totalWeight;
        private int chargeableWeight;
        private String specialCargoCode;
        private String commodityName;
        private String consignee;
        private String consigneePhone;
        private String consigneeCarid;
        private String forwarderId;
        private String forwarderCode;
        private String forwarderName;
        private String forwarderPhone;
        private int forwarderPayType;
        private int priority;
        private int buckleGoods;
        private int transit;
        private int refrigerated;
        private int precious;
        private int living;
        private int quick;
        private int devanning;
        private Object toPay;
        private int toPayFee;
        private Object currency;
        private Object exchangeRate;
        private int customsSupervision;
        private int transship;
        private Object originatingStation;
        private Object originatingStationCn;
        private Object destinationStation;
        private Object destinationStationCn;
        private String route;
        private int manifestTotal;
        private int manifestWeight;
        private String warehouseArea;
        private Object warehouseLocation;
        private long putStorageTime;
        private Object putStorageUser;
        private Object outStorageTime;
        private Object outStorageUser;
        private int tallyingTotal;
        private int tallyingWeight;
        private int waybillStatus;
        private Object waybillCodeScanUrl;
        private Object commodityScanUrl;
        private Object waybillScanUrl;
        private long createDate;
        private String createUser;
        private long updateDate;
        private String updateUser;
        private int returnOutTransportFlag;
        private List<Integer> ubnormalType;
        private int ubnormalNum;
        private Object noticeNum;
        private int amountOfMoney;
        private int outboundNumber;
        private long flightAta;
        private Object flightSta;
        private Object defermentCharge;
        private Object nowAmountOfMoney;
        private Object searchStartTime;
        private Object searchEndTime;
    }

    @Data
    public static class FlightBean  {

        private String id;
        private int flightId;
        private String airlineCode;
        private String flightNo;
        private String aircraftNo;
        private long scheduleTime;
        private long estimateTime;
        private long actualTime;
        private String movement;
        private String flightType;
        private String flightIndicator;
        private String aircraftType;
        private String terminal;
        private long sta;
        private long eta;
        private long ata;
        private Object std;
        private Object etd;
        private Object atd;
        private String seat;
        private String flightStatus;
        private String flightExtStatus;
        private String flightExtRemark;
        private Object cancelTime;
        private long createDate;
        private String succession;
        private String successionFlightNo;
        private int successionId;
        private Object carrier;
        private int masterFlightId;
        private int delay;
        private long estimateInSeat;
        private Object vipMark;
        private int vipNo;
        private Object destChangeReason;
        private Object destChangeDirection;
        private Object destChangeAirport;
        private Object returnReason;
        private Object returnCode;
        private Object landAbortedReason;
        private Object landAbortedCode;
        private long aircraftIn;
        private Object aircraftOut;
        private Object preEtd;
        private long preAtd;
        private Object nxtEta;
        private Object nxtAta;
        private Object keyMaintaince;
        private Object boardingTime;
        private String associateAirport;
        private String runway;
        private Object runwayTime;
        private int delFlag;
        private Object delData;
        private String iacoAirlineCode;
        private Object tobt;
        private Object cobt;
        private Object ctot;
        private Object acdmSeat;
        private int transportStatus;
        private Object taskId;
        private int totalScooterNum;
        private int arriveWarehouseNum;
        private Object scooters;
        private List<String> flightCourseByAndroid;
        private List<String> flightCourseCn;
        private Object associateAirportCn;
        private Object luggageScanningUser;
        private Object flightBody;
        private Object inWaybillRecords;
        private List<String> airportCn;


    }

}
