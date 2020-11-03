package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;

import lombok.Data;

/**
 * @ProjectName: qx_app_pad
 * @Package: com.qxkj.smartservice.bean.response
 * @ClassName: GroundAgentBean
 * @Description: java类作用描述
 * @Author: 张耀
 * @CreateDate: 2020/7/28 16:04
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/7/28 16:04
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
@Data
public class GroundAgentBean implements Serializable {
    /**
     * id : 123
     * agentCode : 地面代理code
     * fullName : 地面代理全程
     * shortName : 地面代理简称
     * abbreviation : 地面代理缩写
     * agentRank : 0
     * delFlag : 0
     * createUserId : u2551b4c7dcc24ddb845c73c19955278d
     * createUserName : 超级管理员
     * createTime : 1595921644305
     */

    private String id;
    private String agentCode;
    private String fullName;
    private String shortName;
    private String abbreviation;
    private int agentRank;//0为默认, 10为非默认
    private int delFlag;
    private String createUserId;
    private String createUserName;
    private long createTime;


}
