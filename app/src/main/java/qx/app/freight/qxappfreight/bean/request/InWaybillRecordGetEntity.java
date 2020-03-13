package qx.app.freight.qxappfreight.bean.request;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by guohao on 2019/5/23 16:27
 * 进港理货 分拣，请求实体类
 */
@Data
public class InWaybillRecordGetEntity implements Serializable {
    String flightInfoId;
    /**
     * 1：已办， 0：待办
     */
    Integer taskFlag;
}
