package com.github.automain.common.dao;

import com.github.automain.common.bean.TbUploadRelation;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.bean.PageParameterBean;
import com.github.fastjdbc.common.BaseDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TbUploadRelationDao extends BaseDao<TbUploadRelation> {

    @SuppressWarnings("unchecked")
    public PageBean<TbUploadRelation> selectTableForCustomPage(ConnectionBean connection, TbUploadRelation bean, int page, int limit) throws Exception {
        List<Object> countParameterList = new ArrayList<Object>();
        List<Object> parameterList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParameterList, true);
        String sql = setSearchCondition(bean, parameterList, false);
        PageParameterBean<TbUploadRelation> pageParameterBean = new PageParameterBean<TbUploadRelation>();
        pageParameterBean.setConnection(connection);
        pageParameterBean.setBean(bean);
        pageParameterBean.setCountSql(countSql);
        pageParameterBean.setCountParameterList(countParameterList);
        pageParameterBean.setSql(sql);
        pageParameterBean.setParameterList(parameterList);
        pageParameterBean.setPage(page);
        pageParameterBean.setLimit(limit);
        return selectTableForPage(pageParameterBean);
    }

    private String setSearchCondition(TbUploadRelation bean, List<Object> parameterList, boolean isCountSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM tb_upload_relation WHERE is_delete = 0 ");
        if (bean.getRecordId() != null) {
            sql.append(" AND record_id = ?");
            parameterList.add(bean.getRecordId());
        }
        if (bean.getRecordTableName() != null) {
            sql.append(" AND record_table_name = ?");
            parameterList.add(bean.getRecordTableName());
        }
        if (bean.getRecordLabel() != null) {
            sql.append(" AND record_label = ?");
            parameterList.add(bean.getRecordLabel());
        }
        if (bean.getSequenceNumber() != null) {
            sql.append(" AND sequence_number = ?");
            parameterList.add(bean.getSequenceNumber());
        }
        if (bean.getUploadFileId() != null) {
            sql.append(" AND upload_file_id = ?");
            parameterList.add(bean.getUploadFileId());
        }
        return sql.toString();
    }

    public TbUploadRelation selectMaxOrderNumberRelationByBean(ConnectionBean connection, TbUploadRelation bean) throws SQLException {
        String sql = "SELECT * FROM tb_upload_relation WHERE is_delete = 0 AND record_id = ? AND record_table_name = ? ";
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(bean.getRecordId());
        paramList.add(bean.getRecordTableName());
        if (bean.getRecordLabel() != null) {
            sql += " AND record_label = ?";
            paramList.add(bean.getRecordLabel());
        }
        sql += " ORDER BY sequence_number DESC LIMIT 1";
        return executeSelectReturnBean(connection, sql, paramList, bean);
    }

}