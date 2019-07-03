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
    private String timeForShow;//时间：实际到达（离港）时间  >  预计到达（离港）时间  >  计划到达（离港）时间
    private int timeType;//时间显示类型：3，实际时间；2，预计时间；1，计划时间
    private List<MultiStepEntity> list;
    private int loadUnloadType;
    private String flightType;
    private String id;
    private long flightId;
    private String taskId;
    private int taskType;
    private String workerName;
    private List<String> stepCodeList;
    private boolean widePlane;//是否是宽体机
    private boolean acceptTask=false;//是否已经领受任务
}
