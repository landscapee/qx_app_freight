package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;

import lombok.Data;

/**
 * 推送消息bean类
 * Created by mm on 2016/10/21.
 */
@Data
public class PushBaseBean implements Serializable {


    /**
     * content : gu
     * msgid : faefawe
     * isforcedispose : guy
     * msgtype : ugu
     * extra : {"taskId":"guy","tasknodename":"guy","planemodel":"uyg","craftno":"gyg","stepid":"gyu","craftseat":"yu","preposittime":"guy","flightno":"guy"}
     */

    private String content;
    private String msgid;
    private boolean isforcedispose;
    private String msgtype;
    private int flightState;//航班状态参数 2：已达  1：已飞  4：前方起飞 5：本场登机
    private boolean background;//是否后台刷新数据，但不给通知默认 false
    private String extra;
    private String other;
    private String title;
}
