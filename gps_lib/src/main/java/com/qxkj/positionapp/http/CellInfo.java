package com.qxkj.positionapp.http;
/**
 * Created by guohao on 2019/5/29 22:04
 * @title 请求参数实体类
 */
public class CellInfo {

    private int mcc;
    private int mnc;
    private int lac;
    private int cid;

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
