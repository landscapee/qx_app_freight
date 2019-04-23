package qx.app.freight.qxappfreight.bean.request;

import java.io.Serializable;

import lombok.Data;

/**
 * 换单审核 请求类，只需要一个id就够了
 *
 * create by guohao - 2019/4/22
 */

@Data
public class DeclareWaybillEntity implements Serializable {

    private String id;
}
