package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;

@Data
public class ErrorFilingEntity {

    /**
     * flightNum : 3U888
     * ubnormalSource : 安检1
     * ubnormalType : [1,3]
     * ubnormalPic : ["pic"]
     * ubnormalDescription : 货物太小
     * ubnormalNum : 3
     * remark : 备注3
     * createUser : sdfwwerffggergerg
     * createUserName : 张三
     * waybillId : ["sdsfsdf","gergerger"]
     */

    private String flightNum;
    private String ubnormalSource;
    private String ubnormalDescription;
    private int ubnormalNum;
    private String remark;
    private String createUser;
    private String createUserName;
    private List<Integer> ubnormalType;
    private List<String> ubnormalPic;
    private List<String> waybillId;
}
