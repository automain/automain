package com.github.automain.upload.dao;

import com.github.automain.upload.bean.TbUploadFile;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class TbUploadFileDao extends BaseDao<TbUploadFile> {

    @SuppressWarnings("unchecked")
    public PageBean<TbUploadFile> selectTableForCustomPage(ConnectionBean connection, TbUploadFile bean, int page, int limit) throws Exception {
        List<Object> parameterList = new ArrayList<Object>();
        String sql = setSearchCondition(bean, parameterList);
        return selectTableForPage(connection, bean, sql, parameterList, page, limit);
    }

    private String setSearchCondition(TbUploadFile bean, List<Object> parameterList) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_upload_file WHERE 1 = 1 ");
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