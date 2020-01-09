package qx.app.freight.qxappfreight.bean.response;


import lombok.Data;

@Data
public class WebSocketMessageBean {
    /**
     * id : facf214c1a9c26ce82149fa2c25b72af
     * messageType : 0
     * consumerType : 1
     * role : receive
     * userId : null
     * orgId : null
     * content : MT测试消息！！
     * createDate : 1553580116792
     * createUser : null
     * readingStatus : 0
     */

    private String id;
    private int messageType;
    private int consumerType;
    private String role;
    private Object userId;
    private Object orgId;
    private String content;
    private long createDate;
    private Object createUser;
    private int readingStatus;

    private int specialFlag;// /1强提醒 2 特殊
    private String messageName;// BEFOREHAND_IN_SCOOTER_NUMBER 代表 分拣板车入库数量变化 //CLIPPING_WEIGHTER_ERROR_NOTICE 负重异常

}
