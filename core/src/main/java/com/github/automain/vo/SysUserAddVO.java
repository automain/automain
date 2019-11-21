package com.github.automain.vo;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SysUserAddVO implements BaseBean<SysUserAddVO> {

    // 用户GID
    private String gid;
    // 创建时间
    private Integer createTime;
    // 更新时间
    private Integer updateTime;
    // 用户名
    private String userName;
    // 真实姓名
    private String realName;
    // 手机号
    private String phone;
    // 邮箱
    private String email;
    // 头像文件GID
    private String headImgGid;
    // 角色
    private String roleName;
    // 头像地址
    private String headImg;
    // 添加用密码
    private String password;
    // 添加用确认密码
    private String password2;
    // 角色标识
    private List<String> userRoleList;

    public String getGid() {
        return gid;
    }

    public SysUserAddVO setGid(String gid) {
        this.gid = gid;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public SysUserAddVO setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public SysUserAddVO setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public SysUserAddVO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getRealName() {
        return realName;
    }

    public SysUserAddVO setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public SysUserAddVO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public SysUserAddVO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getHeadImgGid() {
        return headImgGid;
    }

    public SysUserAddVO setHeadImgGid(String headImgGid) {
        this.headImgGid = headImgGid;
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public SysUserAddVO setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public String getHeadImg() {
        return headImg;
    }

    public SysUserAddVO setHeadImg(String headImg) {
        this.headImg = headImg;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SysUserAddVO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPassword2() {
        return password2;
    }

    public SysUserAddVO setPassword2(String password2) {
        this.password2 = password2;
        return this;
    }

    public List<String> getUserRoleList() {
        return userRoleList;
    }

    public SysUserAddVO setUserRoleList(List<String> userRoleList) {
        this.userRoleList = userRoleList;
        return this;
    }

    @Override
    public String tableName() {
        return null;
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        return null;
    }

    @Override
    public SysUserAddVO beanFromResultSet(ResultSet rs) throws SQLException {
        return new SysUserAddVO()
                .setGid(rs.getString("gid"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setUserName(rs.getString("user_name"))
                .setRealName(rs.getString("real_name"))
                .setPhone(rs.getString("phone"))
                .setEmail(rs.getString("email"))
                .setHeadImgGid(rs.getString("head_img_gid"))
                .setHeadImg(rs.getString("head_img"))
                .setRoleName(rs.getString("role_name"));
    }

    @Override
    public String toString() {
        return "SysUserAddVO{" +
                "gid='" + gid + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", headImgGid='" + headImgGid + '\'' +
                ", roleName='" + roleName + '\'' +
                ", headImg='" + headImg + '\'' +
                ", password='" + password + '\'' +
                ", password2='" + password2 + '\'' +
                ", userRoleList=" + userRoleList +
                '}';
    }
}
