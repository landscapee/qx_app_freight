package qx.app.freight.qxappfreight.bean.request;

import lombok.Data;

@Data
public class UpdatePwdEntity {
    //用户名
    private String username;
    //新密码
    private String password;
    private String userId;
    //旧密码
    private String oldpassword;
}
