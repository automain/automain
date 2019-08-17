package com.github.automain.dao;

import com.github.automain.bean.TbUploadFile;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.bean.PageParameterBean;
import com.github.fastjdbc.common.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class TbUploadFileDao extends BaseDao<TbUploadFile> {

    @SuppressWarnings("unchecked")
    public PageBean<TbUploadFile> selectTableForCustomPage(ConnectionBean connection, TbUploadFile bean, int page, int limit) throws Exception {
        List<Object> countParameterList = new ArrayList<Object>();
        List<Object> parameterList = new ArrayList<Object>();
        String countSql = setSearchCondition(bean, countParameterList, true);
        String sql = setSearchCondition(bean, parameterList, false);
        PageParameterBean<TbUploadFile> pageParameterBean = new PageParameterBean<TbUploadFile>();
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

    private String setSearchCondition(TbUploadFile bean, List<Object> parameterList, boolean isCountSql) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(isCountSql ? "COUNT(1)" : "*").append(" FROM tb_upload_file WHERE 1 = 1 ");
        if (bean.getFileExtension() != null) {
            sql.append(" AND file_extension = ?");
            parameterList.add(bean.getFileExtension());
        }
        if (bean.getFilePath() != null) {
            sql.append(" AND file_path = ?");
            parameterList.add(bean.getFilePath());
        }
        if (bean.getFileSize() != null) {
            sql.append(" AND file_size = ?");
            parameterList.add(bean.getFileSize());
        }
        if (bean.getUploadTimeRange() != null) {
            sql.append(" AND upload_time >= ? AND upload_time <= ?");
            setTimeRange(bean.getUploadTimeRange(), parameterList);
        }
        if (bean.getFileMd5() != null) {
            sql.append(" AND file_md5 = ?");
            parameterList.add(bean.getFileMd5());
        }
        if (bean.getImageWidth() != null) {
            sql.append(" AND image_width = ?");
            parameterList.add(bean.getImageWidth());
        }
        if (bean.getImageHeight() != null) {
            sql.append(" AND image_height = ?");
            parameterList.add(bean.getImageHeight());
        }
        return sql.toString();
    }
}