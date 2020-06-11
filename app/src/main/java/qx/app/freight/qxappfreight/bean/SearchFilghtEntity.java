package qx.app.freight.qxappfreight.bean;

import lombok.Data;

/**
 * Created by zzq On 2020/6/11 11:45 & Copyright (C), 青霄科技
 *
 * @文档说明: 模糊搜索航班数据的请求实体类
 */
@Data
public class SearchFilghtEntity {
    private String movement = "D";
    private String flightIndicator = "D";
    private String flightNo;
    private String flightDate;
}
