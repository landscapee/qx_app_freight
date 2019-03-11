package qx.app.freight.qxappfreight.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 装机版本数据model
 */
@Data
public class UnloadPlaneVersionEntity implements Serializable {
    private int version;
    private List<UnloadPlaneEntity> list;
    private boolean showDetail;
}
