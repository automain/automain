package com.github.automain.bean;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SysFileRelation implements BaseBean<SysFileRelation> {

    // 主键
    private Integer id;
    // 创建时间
    private Integer createTime;
    // 更新时间
    private Integer updateTime;
    // 是否有效(0:否,1:是)
    private Integer isValid;
    // sys_file表gid
    private String fileGid;
    // 记录表名
    private String recordTableName;
    // 记录ID
    private Integer recordId;
    // 记录GID
    private String recordGid;
    // 记录标识
    private String recordLabel;
    // 展示顺序
    private Integer sequenceNumber;
    // 关联的文件表表名
    private String fileTableName;

    public Integer getId() {
        return id;
    }

    public SysFileRelation setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public SysFileRelation setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public SysFileRelation setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public SysFileRelation setIsValid(Integer isValid) {
        this.isValid = isValid;
        return this;
    }

    public String getFileGid() {
        return fileGid;
    }

    public SysFileRelation setFileGid(String fileGid) {
        this.fileGid = fileGid;
        return this;
    }

    public String getRecordTableName() {
        return recordTableName;
    }

    public SysFileRelation setRecordTableName(String recordTableName) {
        this.recordTableName = recordTableName;
        return this;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public SysFileRelation setRecordId(Integer recordId) {
        this.recordId = recordId;
        return this;
    }

    public String getRecordGid() {
        return recordGid;
    }

    public SysFileRelation setRecordGid(String recordGid) {
        this.recordGid = recordGid;
        return this;
    }

    public String getRecordLabel() {
        return recordLabel;
    }

    public SysFileRelation setRecordLabel(String recordLabel) {
        this.recordLabel = recordLabel;
        return this;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public SysFileRelation setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    public String getFileTableName() {
        return fileTableName;
    }

    public SysFileRelation setFileTableName(String fileTableName) {
        this.fileTableName = fileTableName;
        return this;
    }

    @Override
    public String tableName() {
        return "sys_file_relation";
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>(11);
        if (all || this.getId() != null) {
            map.put("id", this.getId());
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
        if (all || this.getFileGid() != null) {
            map.put("file_gid", this.getFileGid());
        }
        if (all || this.getRecordTableName() != null) {
            map.put("record_table_name", this.getRecordTableName());
        }
        if (all || this.getRecordId() != null) {
            map.put("record_id", this.getRecordId());
        }
        if (all || this.getRecordGid() != null) {
            map.put("record_gid", this.getRecordGid());
        }
        if (all || this.getRecordLabel() != null) {
            map.put("record_label", this.getRecordLabel());
        }
        if (all || this.getSequenceNumber() != null) {
            map.put("sequence_number", this.getSequenceNumber());
        }
        if (all || this.getFileTableName() != null) {
            map.put("file_table_name", this.getFileTableName());
        }
        return map;
    }

    @Override
    public SysFileRelation beanFromResultSet(ResultSet rs) throws SQLException {
        return new SysFileRelation()
                .setId(rs.getInt("id"))
                .setCreateTime(rs.getInt("create_time"))
                .setUpdateTime(rs.getInt("update_time"))
                .setIsValid(rs.getInt("is_valid"))
                .setFileGid(rs.getString("file_gid"))
                .setRecordTableName(rs.getString("record_table_name"))
                .setRecordId(rs.getInt("record_id"))
                .setRecordGid(rs.getString("record_gid"))
                .setRecordLabel(rs.getString("record_label"))
                .setSequenceNumber(rs.getInt("sequence_number"))
                .setFileTableName(rs.getString("file_table_name"));
    }

    @Override
    public String toString() {
        return "SysFileRelation{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isValid=" + isValid +
                ", fileGid='" + fileGid + '\'' +
                ", recordTableName='" + recordTableName + '\'' +
                ", recordId=" + recordId +
                ", recordGid='" + recordGid + '\'' +
                ", recordLabel='" + recordLabel + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", fileTableName='" + fileTableName + '\'' +
                '}';
    }
}