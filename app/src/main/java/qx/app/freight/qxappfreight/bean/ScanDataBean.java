package qx.app.freight.qxappfreight.bean;

import lombok.Data;

/**
 * 扫码数据封装类
 */
@Data
public class ScanDataBean {

    private int id;

    private String functionFlag;

    private String data;

    private boolean isLaser;//true 激光扫码  默认 为false 普通扫码
}
