package com.github.automain.upload.service;

import com.github.automain.upload.bean.TbUploadRelation;
import com.github.automain.upload.dao.TbUploadRelationDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class TbUploadRelationService extends BaseService<TbUploadRelation, TbUploadRelationDao> {

    public TbUploadRelationService(TbUploadRelation bean, TbUploadRelationDao dao) {
        super(bean, dao);
    }

    public PageBean<TbUploadRelation> selectTableForCustomPage(ConnectionBean connection, TbUploadRelation bean, HttpServletRequest request) throws Exception {
        int page = getInt("page", request, 1);
        int limit = getInt("limit", request, 1);
        return getDao().selectTableForCustomPage(connection, bean, page, limit);
    }

    public TbUploadRelation selectMaxOrderNumberRelationByBean(ConnectionBean connection, TbUploadRelation bean) throws SQLException {
        return getDao().selectMaxOrderNumberRelationByBean(connection, bean);
    }

}