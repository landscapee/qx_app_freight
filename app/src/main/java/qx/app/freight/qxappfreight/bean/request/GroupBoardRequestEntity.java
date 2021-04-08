package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;

@Data
public class GroupBoardRequestEntity {

    /**
     * stepOwner : u27f95c83a0d24f19a592d16ebdf28fe3
     * undoType : 2
     * roleCode : preplaner
     * ascs : ["ETD"]
     */

    private String stepOwner;
    private int undoType;
    private String roleCode;
    private List <String> ascs;
    private String flightNo;
}
