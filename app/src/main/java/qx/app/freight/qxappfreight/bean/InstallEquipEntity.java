package qx.app.freight.qxappfreight.bean;

import java.util.List;

import lombok.Data;

/**
 * 装机列表数据model
 */
@Data
public class InstallEquipEntity {
    private boolean showDetail;
    private String flightInfo;//航班号
    private String airCraftNo;//航班机型号
    private List<String> flightInfoList;//航线信息列表
    private String seat;//座位数
    private String scheduleTime;//计划时间（13：:14（4））4代表4号
    private String actualTime;//实际时间（13：:14（4））4代表4号
    private List<MultiStepEntity> list;
    private int loadUnloadType;
    private int taskTpye;
    private String flightType;
    private String id;
    private long flightId;
    private String taskId;
    private int taskType;
    private String workerName;
    private List<String> stepCodeList;
    private boolean widePlane;//是否是宽体机
}
