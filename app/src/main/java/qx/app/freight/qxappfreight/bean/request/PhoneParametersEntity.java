package qx.app.freight.qxappfreight.bean.request;


import lombok.Data;

@Data
public class PhoneParametersEntity {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 设备编号
     */
    private String deviceId;
//    /**
//     * 设备类型
//     */
//    private String deviceType;
//    /**
//     * 设备型号
//     */
//    private String eptModel;
//    /**
//     * 系统名称
//     */
//    private String systemName;
//    /**
//     * 系统版本
//     */
//    private String systemCode;
    /**
     * 手机号
     */
    private String phoneNumber;
}
