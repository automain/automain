package com.github.automain.vo;

import com.github.automain.bean.SysUser;

import java.util.List;

public class SysUserAddVO extends SysUser {

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

    public SysUserAddVO setId(Integer id) {
        super.setId(id);
        return this;
    }

    public SysUserAddVO setGid(String gid) {
        super.setGid(gid);
        return this;
    }

    public SysUserAddVO setCreateTime(Integer createTime) {
        super.setCreateTime(createTime);
        return this;
    }

    public SysUserAddVO setUpdateTime(Integer updateTime) {
        super.setUpdateTime(updateTime);
        return this;
    }

    public SysUserAddVO setIsValid(Integer isValid) {
        super.setIsValid(isValid);
        return this;
    }

    public SysUserAddVO setUserName(String userName) {
        super.setUserName(userName);
        return this;
    }

    public SysUserAddVO setPasswordMd5(String passwordMd5) {
        super.setPasswordMd5(passwordMd5);
        return this;
    }

    public SysUserAddVO setRealName(String realName) {
        super.setRealName(realName);
        return this;
    }

    public SysUserAddVO setPhone(String phone) {
        super.setPhone(phone);
        return this;
    }

    public SysUserAddVO setEmail(String email) {
        super.setEmail(email);
        return this;
    }

    public SysUserAddVO setHeadImgGid(String headImgGid) {
        super.setHeadImgGid(headImgGid);
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
    public String toString() {
        return "SysUserAddVO{" +
                "roleName='" + roleName + '\'' +
                ", headImg='" + headImg + '\'' +
                ", password='" + password + '\'' +
                ", password2='" + password2 + '\'' +
                ", userRoleList=" + userRoleList +
                '}';
    }
}
