package com.github.automain.vo;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class UserVO implements BaseBean<UserVO> {

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
    // 角色
    private String roleName;
    // 头像地址
    private String headImg;

    public String getGid() {
        return gid;
    }

    public UserVO setGid(String gid) {
        this.gid = gid;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public UserVO setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public UserVO setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public UserVO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getRealName() {
        return realName;
    }

    public UserVO setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserVO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserVO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public UserVO setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public String getHeadImg() {
        return headImg;
    }

    public UserVO setHeadImg(String headImg) {
        this.headImg = headImg;
        return this;
    }

    @Override
    public String tableName() {
        return null;
    }

    @Override
    public Map<String, Object> columnMap(boolean b) {
        return null;
    }

    @Override
    public UserVO beanFromResultSet(ResultSet rs) throws SQLException {
        return new UserVO()
                .setGid(rs.getString("gid"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setUserName(rs.getString("user_name"))
                .setRealName(rs.getString("real_name"))
                .setPhone(rs.getString("phone"))
                .setEmail(rs.getString("email"))
                .setRoleName(rs.getString("role_name"))
                .setHeadImg(rs.getString("head_img"));
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "gid='" + gid + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", roleName='" + roleName + '\'' +
                ", headImg='" + headImg + '\'' +
                '}';
    }
}
