package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * TODO : 登录实体
 * Created by pr
 */
@Data
public class LoginResponseBean implements Serializable {
    private String userId;
    private String loginid;
    private String username;
    private String deptCode;
    private String orgId;
    private String orgName;
    private String depId;
    private String depName;
    private String depShortName;
    private String loginName;
    private String phone;
    private String email;
    private String idcard;
    private String postId;
    private String postName;
    private String subcenterId;
    private String subcenterName;
    private String position;
    private String sex;
    private String pwd;
    private int sort;
    private String token;
    private String userToken;
    private String code;
    private List<RoleRSBean> roleRS;

    private String currentRole;

//    public List<RoleRSBean> getRoleRS(){
//        if (roleRS == null || roleRS.size() == 0){
//            Tools.loginOut(MyApplication.getContext());
//            roleRS = new ArrayList <>();
//            return roleRS;
//        }
//        else
//            return roleRS;
//    }

    @Data
    public static class RoleRSBean implements Serializable{
        private String id;
        private String userId;
        private String roleCode;
        private String roleClassName;
        private String rolename;
        private String type;
        private int singleNumber;
        private String roleClassId;
        private List<ResourceRSBean> resourceRS;

        @Data
        public static class ResourceRSBean implements Serializable{
            /**
             * roleId : 24a8e450622146bd997c41150fbb000e
             * type : 0
             * resourceid : 0830d57302ac4979bb1ec7b63786ac10
             * applicationName : 栏目管理
             * resourceCode : lmgl
             * txttype : 菜单
             */
            private String roleId;
            private String type;
            private String resourceid;
            private String applicationName;
            private String resourceName;
            private String resourceCode;
            private String txttype;
            private String parentResourceId;
        }
    }
}
