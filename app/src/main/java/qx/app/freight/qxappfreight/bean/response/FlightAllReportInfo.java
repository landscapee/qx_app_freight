package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;


@Data
public class FlightAllReportInfo {

        /**
         * id : e0a7a397d755471bebfe901eb7326812
         * flightInfoId : 3e77a12d87da41539fd10a335c756b75
         * version : 1
         * content : 舱单/装卸机单/机下建议舱单 内容
         * reviewStatus : 0
         * createTime : 1562139966026
         * createUser : system
         * updateTime : null
         * updateUser : null
         * documentType : 1
         * loadingAdvice : 0
         * loadingUser : null
         * contentObject : null
         * returnReason : null
         * preContent : null
         * cgoContent : null
         * autoLoadInstalledSingle : 0
         * flightNo : null
         */

        private String id;
        private String flightInfoId;
        private String version;
        private String content;
        private int reviewStatus;
        private long createTime;
        private String createUser;
        private Object updateTime;
        private Object updateUser;
        private int documentType;
        private int loadingAdvice;
        private Object loadingUser;
        private Object contentObject;
        private Object returnReason;
        private Object preContent;
        private Object cgoContent;
        private int autoLoadInstalledSingle;
        private Object flightNo;
        private int installedSingleConfirm;//1 是确认
        private String installedSingleConfirmUser;//确认人


}
