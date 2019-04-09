package qx.app.freight.qxappfreight.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import lombok.Data;

@Data
public class MultiStepEntity implements MultiItemEntity {
    public static final int TYPE_STEP_OVER = 0;
    public static final int TYPE_STEP_NOW = 1;
    public static final int TYPE_STEP_NEXT = 2;
    private int itemType;
    private int loadUnloadType;
    private String stepName;
    private String stepDoneDate;
    private String planeInfo;
    private String flightType;
}
