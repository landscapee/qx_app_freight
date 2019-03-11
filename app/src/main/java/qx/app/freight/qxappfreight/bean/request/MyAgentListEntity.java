package qx.app.freight.qxappfreight.bean.request;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class MyAgentListEntity {
    private int pageNum;    //页数
    private int PageSize; //每页数量
    private String filter;  //过滤条件
}
