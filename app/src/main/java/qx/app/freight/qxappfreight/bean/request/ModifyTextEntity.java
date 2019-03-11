package qx.app.freight.qxappfreight.bean.request;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class ModifyTextEntity {
    private String id;
    private String waybillId;
    private String insFile;
    private int insCheck;
    private int fileCheck;
    private int packaging;
    private int require;
    private int spotCheck;
    private int spotResult;
    private String unspotReson;
    private int insStatus;
}
