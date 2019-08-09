package qx.app.freight.qxappfreight.bean.response;

/**
 * TODO : 结果返回
 * Created by owen
 * on 2016-09-29.
 */

public class RespBean {
    private boolean isSucc;
    private String msg;

    public boolean isSucc() {
        return isSucc;
    }

    public void setSucc(boolean succ) {
        isSucc = succ;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
