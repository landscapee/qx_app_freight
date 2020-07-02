package qx.app.freight.qxappfreight.bean.request;

import lombok.Data;

/**
 * Created by zzq On 2020/7/1 15:39 & Copyright (C), 青霄科技
 *
 * @文档说明:
 */
@Data
public class QueryWaybillInfoEntity {

    /**
     * waybillCode : 784-1234311
     * flightInfoId : 5e2d5ad3734c454e87db46bc97f7bae8
     */
    private String waybillCode;
    private String flightInfoId;
}
