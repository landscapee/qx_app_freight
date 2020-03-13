package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by Lain on 2016/9/28.
 * 登录返回实体类
 */
@Data
public class RespLoginBean implements Serializable {


    /**
     * cnname : 系统管理员
     * isSucc : true
     * loginName : SCACDM_manager
     * msg : 登录成功
     * role :SSR
     * deptcode:MaintenanceLoadBridge
     * token : eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2luZm8iOiJ7XCJpZFwiOlwiU0NBQ0RNX21hbmFnZXJcIixcImxvZ2luTmFtZVwiOlwiU0NBQ0RNX21hbmFnZXJcIixcIm5hbWVcIjpcIuezu-e7n-euoeeQhuWRmFwiLFwic3RhdGVcIjpcIjBcIn0iLCJ1c2VyX25hbWUiOiLns7vnu5_nrqHnkIblkZgiLCJ1c2VyX2tleSI6IjcxNzVkYjU2N2JhZmJiNDkyMjQwNGJlYWQ3YjNiNTQ0IiwiY3JlYXRlX3RpbWUiOjE0NzU5NzY0NDY0NDQsInRpbWVvdXQiOjE4MDB9.4KVFvgk7z0u_0FHpM4B_MELv13d0JjAoOttnwnCNDjY
     * userId : SCACDM_manager
     */
    private String cnname;
    private boolean isSucc;
    private String loginName;
    private String msg;
    private String role;
    private String token;
    private String userId;
    private String deptcode;

}
