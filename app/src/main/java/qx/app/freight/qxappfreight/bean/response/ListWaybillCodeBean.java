package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class ListWaybillCodeBean {

    /**
     * status : 200
     * message : 正确
     * rowCount : null
     * data : ["859-10235514","02813131313","784-10235481","111-02846464","pengpeng02","023-05326053","023-05326064","023-05326053","023-05326064","023-05326053","023-05326064","023-05326053","023-05326064","023-05326053","023-05326064","025-10315481","025-12321231","341-10235455","880-10002123"]
     */

    private String status;
    private String message;
    private Object rowCount;
    private List<String> data;
}
