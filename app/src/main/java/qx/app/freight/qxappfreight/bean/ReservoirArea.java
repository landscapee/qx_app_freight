package qx.app.freight.qxappfreight.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by guohao on 2019/5/21 22:45
 *
 * 库区实体类
 */
@Data
public class ReservoirArea implements Serializable {


    private String id;
    private String depId;
    private String code;
    private String reservoirName;
    private String reservoirType;
    private String reservoirSaveType;

}
