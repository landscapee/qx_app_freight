package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;

/**
 * TODO : xxx
 */
@Data
public class LoginEntity {
    private String username;
    private String password;
    private String type;
    private List<String> sysCode;
}
