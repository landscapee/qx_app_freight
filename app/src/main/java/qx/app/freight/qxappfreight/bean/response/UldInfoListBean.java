package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class UldInfoListBean {
    /**
     * id :
     * uldCode :
     * iata :
     * uldType :
     * applyType :
     * bulkingType :
     * uldWeight :
     * weight :
     * airType : ["TXT1","TXT2","TXT3"]
     */

    private String id;
    private String uldCode;
    private String iata;
    private String uldType;
    private String applyType;
    private String bulkingType;
    private String uldWeight;
    private String weight;
    private List<String> airType;


}
