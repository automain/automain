package com.github.automain.upload.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TbUploadRelation extends RequestUtil implements BaseBean<TbUploadRelation> {

    // 关系ID
    private Long uploadRelationId;

    // 文件ID
    private Long uploadFileId;

    // 记录ID
    private Long recordId;

    // 记录表名
    private String recordTableName;

    // 记录标记
    private String recordLabel;

    // 展示顺序
    private Integer sequenceNumber;

    // 是否删除(0:否,1:是)
    private Integer isDelete;

    // ========== additional column begin ==========


    // ========== additional column end ==========

    public Long getUploadRelationId() {
        return uploadRelationId;
    }

    public void setUploadRelationId(Long uploadRelationId) {
        this.uploadRelationId = uploadRelationId;
    }

    public Long getUploadFileId() {
        return uploadFileId;
    }

    public void setUploadFileId(Long uploadFileId) {
        this.uploadFileId = uploadFileId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getRecordTableName() {
        return recordTableName;
    }

    public void setRecordTableName(String recordTableName) {
        this.recordTableName = recordTableName;
    }

    public String getRecordLabel() {
        return recordLabel;
    }

    public void setRecordLabel(String recordLabel) {
        this.recordLabel = recordLabel;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String tableName() {
        return "tb_upload_relation";
    }

    @Override
    public String primaryKey() {
        return "upload_relation_id";
    }

    @Override
    public Long primaryValue() {
        return this.getUploadRelationId();
    }

    @Override
    public Map<String, Object> notNullColumnMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (this.getUploadFileId() != null) {
            map.put("upload_file_id", this.getUploadFileId());
        }
        if (this.getRecordId() != null) {
            map.put("record_id", this.getRecordId());
        }
        if (this.getRecordTableName() != null) {
            map.put("record_table_name", this.getRecordTableName());
        }
        if (this.getRecordLabel() != null) {
            map.put("record_label", this.getRecordLabel());
        }
        if (this.getSequenceNumber() != null) {
            map.put("sequence_number", this.getSequenceNumber());
        }
        if (this.getIsDelete() != null) {
            map.put("is_delete", this.getIsDelete());
        }
        return map;
    }

    @Override
    public TbUploadRelation beanFromResultSet(ResultSet rs) throws SQLException {
        TbUploadRelation bean = new TbUploadRelation();
        bean.setUploadRelationId(rs.getLong("upload_relation_id"));
        bean.setUploadFileId(rs.getLong("upload_file_id"));
        bean.setRecordId(rs.getLong("record_id"));
        bean.setRecordTableName(rs.getString("record_table_name"));
        bean.setRecordLabel(rs.getString("record_label"));
        bean.setSequenceNumber(rs.getInt("sequence_number"));
        bean.setIsDelete(rs.getInt("is_delete"));
        return bean;
    }

    @Override
    public TbUploadRelation beanFromRequest(HttpServletRequest request) {
        TbUploadRelation bean = new TbUploadRelation();
        bean.setUploadRelationId(getLong("uploadRelationId", request));
        bean.setUploadFileId(getLong("uploadFileId", request));
        bean.setRecordId(getLong("recordId", request));
        bean.setRecordTableName(getString("recordTableName", request));
        bean.setRecordLabel(getString("recordLabel", request));
        bean.setSequenceNumber(getInt("sequenceNumber", request));
        bean.setIsDelete(getInt("isDelete", request, 0));
        return bean;
    }
}