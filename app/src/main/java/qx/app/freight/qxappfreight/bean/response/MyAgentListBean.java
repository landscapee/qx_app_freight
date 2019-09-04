package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class MyAgentListBean implements Serializable {



    /**
     * status : 200
     * message : null
     * rowCount : 2
     * data : [{"id":"e93160b2c5e9882a3949084d9c17090c","waybillId":"8b2f73d2afaec8294641425ec2abb5cf","cargoId":"12","number":100,"weight":200,"volume":300,"packagingType":["纸盒","口袋"],"scooterId":"bc173702badd96203810962954351d79","repType":"0","repPlaceId":"a75aefb68020d5176bc9b7837839025c","uldId":"e273839e8c53e2d072774b80bc31c180","overWeight":100,"delFlag":0,"isPrint":0,"scooterCode":"01020421123521","scooterWeight":"1200","scooterType":null,"uldCode":"11124124","uldType":"大","iata":"3U","uldWeight":"1200","repPlaceNum":"001","repPlaceStatus":"0"},{"id":"c3ab6fec9f51eae1bec69cb8a45e949c","waybillId":"8b2f73d2afaec8294641425ec2abb5cf","cargoId":"12","number":100,"weight":200,"volume":300,"packagingType":["纸盒","口袋"],"scooterId":"7c9dce61cd6c7ba5ee9acd9ba59772d9","repType":"0","repPlaceId":"6c41dc99241dd92624769d9c7aad3936","uldId":"e273839e8c53e2d072774b80bc31c180","overWeight":100,"delFlag":0,"isPrint":0,"scooterCode":"0102042112313521","scooterWeight":"1200","scooterType":null,"uldCode":"11124124","uldType":"大","iata":"3U","uldWeight":"1200","repPlaceNum":"005","repPlaceStatus":"0"}]
     */
    /**
     * id : e93160b2c5e9882a3949084d9c17090c
     * waybillId : 8b2f73d2afaec8294641425ec2abb5cf
     * cargoId : 12
     * number : 100
     * weight : 200
     * volume : 300
     * packagingType : ["纸盒","口袋"]
     * scooterId : bc173702badd96203810962954351d79
     * repType : 0
     * repPlaceId : a75aefb68020d5176bc9b7837839025c
     * uldId : e273839e8c53e2d072774b80bc31c180
     * overWeight : 100
     * delFlag : 0
     * isPrint : 0
     * scooterCode : 01020421123521
     * scooterWeight : 1200
     * scooterType : null
     * uldCode : 11124124
     * uldType : 大
     * iata : 3U
     * uldWeight : 1200
     * repPlaceNum : 001
     * repPlaceStatus : 0
     *
     */


    private String id;
    private String waybillId;
    private String waybillCode;
    private String []cargoId;
    private String cargoCn;
    private int number;
    private double weight;
    private double volume;
    private String scooterId;
    private String repType;
    private String repPlaceId;
    private int overWeight;
    private int delFlag;
    private int isPrint;
    private String scooterCode;
    private String scooterWeight;
    private String scooterType;

    private String uldCode;
    private String uldType;
    private String uldId;
    private int uldWeight;

    private String iata;
    private String repName;
    private String repPlaceNum;
    private String repPlaceStatus;
    private String reservoirName;
    private List<String> packagingType;
    private  List<RcInfoOverweight> spOverweight;
    private String taskTypeCode;
    private String addOrderId;
}
