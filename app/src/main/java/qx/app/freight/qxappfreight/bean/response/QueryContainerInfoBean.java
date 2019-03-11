package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class QueryContainerInfoBean {
    /**
     * id : e273839e8c53e2d072774b80bc31c180
     * uldCode : 11124124
     * uldType : 大
     * applyType : 应用类型
     * bulkingType : 集装箱类型
     * uldWeight : 1200
     * weight : 9000
     * airType : ["TXT1","TXT2","TXT3"]
     */
    private String id;
    private String uldCode;
    private String uldType;
    private String applyType;
    private String bulkingType;
    private int uldWeight;
    private int weight;
    private List<String> airType;
}
