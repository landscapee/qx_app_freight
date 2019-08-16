package qx.app.freight.qxappfreight.bean.loadinglist;

import lombok.Data;

/**
 * 用于判断装机单数据是否修改的数据model
 */
@Data
public class CompareInfoBean {
    private String id;
    private int pullStatus;
}
