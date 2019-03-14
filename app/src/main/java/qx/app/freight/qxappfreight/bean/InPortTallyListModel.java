package qx.app.freight.qxappfreight.bean;

import lombok.Data;

@Data
public class InPortTallyListModel {
    private String flightName;
    private String startPlace;
    private String middlePlace;
    private String endPlace;
    private String docName;//AWBA
    private boolean docArrived;//Y N
    private int docNumber;
    private int docWeight;
    private int manifestNumber;//舱单数量
    private int manifestWeight;//舱单重量
    private int tallyNumber;//理货数量
    private int tallyWeight;//理货重量
    private boolean customsCont;//海关监督
    private boolean transport;//是否转运
    private boolean unpackagePlate;//拆箱板
    private boolean chill;//是否冷藏
    private String storeName;//仓库名
    private int storeNumber;//库位
    private String exceptionSituation;//异常情况
}
