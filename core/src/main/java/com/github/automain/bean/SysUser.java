package com.github.automain.bean;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SysUser implements BaseBean<SysUser> {

    // 主键
    private Integer id;
    // 用户GID
    private String gid;
    // 创建时间
    private Integer createTime;
    // 更新时间
    private Integer updateTime;
    // 是否有效(0:无效,1:有效)
    private Integer isValid;
    // 用户名
    private String userName;
    // 密码MD5值
    private String passwordMd5;
    // 真实姓名
    private String realName;
    // 手机号
    private String phone;
    // 邮箱
    private String email;

    public Integer getId() {
        return id;
    }

    public SysUser setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getGid() {
        return gid;
    }

    public SysUser setGid(String gid) {
        this.gid = gid;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public SysUser setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public SysUser setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public SysUser setIsValid(Integer isValid) {
        this.isValid = isValid;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public SysUser setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPasswordMd5() {
        return passwordMd5;
    }

    public SysUser setPasswordMd5(String passwordMd5) {
        this.passwordMd5 = passwordMd5;
        return this;
    }

    public String getRealName() {
        return realName;
    }

    public SysUser setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public SysUser setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public SysUser setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public String tableName() {
        return "sys_user";
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>(10);
        if (all || this.getId() != null) {
            map.put("id", this.getId());
        }
        if (all || this.getGid() != null) {
            map.put("gid", this.getGid());
        }
        if (all || this.getCreateTime() != null) {
            map.put("create_time", this.getCreateTime());
        }
        if (all || this.getUpdateTime() != null) {
            map.put("update_time", this.getUpdateTime());
        }
        if (all || this.getIsValid() != null) {
            map.put("is_valid", this.getIsValid());
        }
        if (all || this.getUserName() != null) {
            map.put("user_name", this.getUserName());
        }
        if (all || this.getPasswordMd5() != null) {
            map.put("password_md5", this.getPasswordMd5());
        }
        if (all || this.getRealName() != null) {
            map.put("real_name", this.getRealName());
        }
        if (all || this.getPhone() != null) {
            map.put("phone", this.getPhone());
        }
        if (all || this.getEmail() != null) {
            map.put("email", this.getEmail());
        }
        return map;
    }

    @Override
    public SysUser beanFromResultSet(ResultSet rs) throws SQLException {
        return new SysUser()
                .setId(rs.getInt("id"))
                .setGid(rs.getString("gid"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setIsValid(rs.getInt("is_valid"))
                .setUserName(rs.getString("user_name"))
                .setPasswordMd5(rs.getString("password_md5"))
                .setRealName(rs.getString("real_name"))
                .setPhone(rs.getString("phone"))
                .setEmail(rs.getString("email"));
    }

    @Override
    public String toString() {
        return "SysUser{" +
                "id=" + id +
                ", gid='" + gid + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isValid=" + isValid +
                ", userName='" + userName + '\'' +
                ", passwordMd5='" + passwordMd5 + '\'' +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}