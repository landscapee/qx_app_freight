package qx.app.freight.qxappfreight.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class InPortTallyListEntity implements Serializable {
    private String waybill;
    private List<String> flightInfoList;
    private String docName;//AWBA
    private boolean docArrived;//Y N
    private int docNumber;
    private double docWeight;
    private int manifestNumber;//舱单数量
    private double manifestWeight;//舱单重量
    private int tallyNumber;//理货数量
    private double tallyWeight;//理货重量
    private boolean customsCont;//海关监督
    private boolean transport;//是否转运
    private boolean unpackagePlate;//拆箱板
    private boolean chill;//是否冷藏
    private String storeName;//仓库名
    private int storeNumber;//库位
    private String exceptionSituation;//异常情况
    private List<Integer> errorList;
    private int numberError;//件数异常
}
