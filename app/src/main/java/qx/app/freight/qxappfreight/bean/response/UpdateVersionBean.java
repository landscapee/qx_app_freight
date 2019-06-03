package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class UpdateVersionBean {
    /**
     * downloadUrl : http://192.168.50.50:8980/acdm-api/sys/api/download/todownloadflie.json?name=f45ab1de483e41ae92077655693746fd&filePath=/root/uploadfile/1b05ee180aa34c74a57b2491e112295f
     * updateMsg : null
     * updateTime : null
     * versionCode : null
     * versionName : null
     * versionCodeRS : 2
     */
    private String updateMsg;

    private String id;
    /**
     * 版本号
     */
    private String versionCode;

    /**
     * app下载地址
     */
    private String downloadPath;
    /**
     * 更新时间
     */
//    private long updateTime;
    /**
     * 是否强制更新 0：否 1：是
     */
    private int updateFlag;
    /**
     * 是否是当前版本 0：否 1：是
     */
    private int isCurrentVersion;
}
