package qx.app.freight.qxappfreight.bean.response;

/**
 * created by swd
 * 2019/6/3 18:02
 */

public class UpdateVersionBean2 {

    /**
     * responseCode : 1000
     * responseMessage : 请求服务器成功！
     * time : 1559556088703
     * data : {"downloadUrl":"http://10.16.23.156:9082/acdm-api/sys/api/download/todownloadflie.json?name=84ed15bf10e641d186fd562b62610796&filePath=/root/uploadfile/3426b22648f348a6987b0796cdb716e7","updateMsg":"牵引车测试版本更新接口修改","updateTime":null,"versionCode":"82","versionName":"84ed15bf10e641d186fd562b62610796","versionCodeRS":82}
     */

    private int responseCode;
    private String responseMessage;
    private long time;
    private DataBean data;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * downloadUrl : http://10.16.23.156:9082/acdm-api/sys/api/download/todownloadflie.json?name=84ed15bf10e641d186fd562b62610796&filePath=/root/uploadfile/3426b22648f348a6987b0796cdb716e7
         * updateMsg : 牵引车测试版本更新接口修改
         * updateTime : null
         * versionCode : 82
         * versionName : 84ed15bf10e641d186fd562b62610796
         * versionCodeRS : 82
         */

        private String downloadUrl;
        private String updateMsg;
        private Object updateTime;
        private String versionCode;
        private String versionName;
        private int versionCodeRS;

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public String getUpdateMsg() {
            return updateMsg;
        }

        public void setUpdateMsg(String updateMsg) {
            this.updateMsg = updateMsg;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionCodeRS() {
            return versionCodeRS;
        }

        public void setVersionCodeRS(int versionCodeRS) {
            this.versionCodeRS = versionCodeRS;
        }
    }
}
