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
    private String downloadUrl;
    private String updateMsg;
    private String updateTime;
    private String versionCode;
    private String versionName;
    private int versionCodeRS;
}
