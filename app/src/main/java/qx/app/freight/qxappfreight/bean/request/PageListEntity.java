package qx.app.freight.qxappfreight.bean.request;

import lombok.Data;

@Data
public class PageListEntity {
    //当前用户角色
    private String role;
    //当前用户id
    private String userId;
    //当前用户公司
    private String orgId;
}
