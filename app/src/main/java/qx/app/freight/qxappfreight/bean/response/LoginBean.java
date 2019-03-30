package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

public class LoginBean {
    /**
     * /**
     *      "userId": "u58f07a49719446a188b8687cd0da6c8c",
     *      "username": "裴丹",
     *      "deptCode": "jpyxzh",
     *      "orgId": "en6ec51d07ecb24b3285789a616610da3a",
     *      "orgName": "成都双流国际机场股份有限公司",
     *      "orgShortName": null,
     *      "depId": "depf6706a7ceaea445daf7bc80486d31888",
     *      "depName": "梦之翼班组",
     *      "depShortName": "梦之翼班组",
     *      "loginName": "peidan",
     *      "phone": "",
     *      "email": "",
     *      "idcard": "",
     *      "postId": "09b259eff55f48cc829cfaa163f41446",
     *      "postName": "无",
     *      "subcenterId": "bbcd4f7d51f3432796c235ea534197c2",
     *      "subcenterName": "股份分中心",
     *      "position": "",
     *      "sex": "0",
     *      "pwd": "111111",
     *      "sort": 0,
     *      "token": "dTU4ZjA3YTQ5NzE5NDQ2YTE4OGI4Njg3Y2QwZGE2YzhjOmRlcGY2NzA2YTdjZWFlYTQ0NWRhZjdiYzgwNDg2ZDMxODg4OmpweXh6aA==",
     *      "roleRS": [{
     *      "id": "a8af0c1cb14d4896a78f6313a259c019",
     *      "userId": null,
     *      "roleCode": "followMe_schdule",
     *      "roleClassName": "智能调度",
     *      "rolename": "引导车调度",
     *      "type": "2",
     *      "singleNumber": 0,
     *      "roleClassId": "1380fcdfd58649af855025ad6c9e6b5f"
     *      */

    private String userId;
    private String username;
    private String deptCode;
    private String orgId;
    private String orgName;
    private String orgShortName;
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
    private List<LoginResponseBean.RoleRSBean.ResourceRSBean> roleRS;

    public List<LoginResponseBean.RoleRSBean.ResourceRSBean> getRoleRS() {
        return roleRS;
    }

    public void setRoleRS(List<LoginResponseBean.RoleRSBean.ResourceRSBean> roleRS) {
        this.roleRS = roleRS;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgShortName() {
        return orgShortName;
    }

    public void setOrgShortName(String orgShortName) {
        this.orgShortName = orgShortName;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getDepShortName() {
        return depShortName;
    }

    public void setDepShortName(String depShortName) {
        this.depShortName = depShortName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getSubcenterId() {
        return subcenterId;
    }

    public void setSubcenterId(String subcenterId) {
        this.subcenterId = subcenterId;
    }

    public String getSubcenterName() {
        return subcenterName;
    }

    public void setSubcenterName(String subcenterName) {
        this.subcenterName = subcenterName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



}
