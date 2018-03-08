package com.github.automain.upload.service;

import com.github.automain.upload.bean.TbUploadFile;
import com.github.automain.upload.dao.TbUploadFileDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;

public class TbUploadFileService extends BaseService<TbUploadFile, TbUploadFileDao> {

    public TbUploadFileService(TbUploadFile bean, TbUploadFileDao dao) {
        super(bean, dao);
    }

    public PageBean<TbUploadFile> selectTableForCustomPage(ConnectionBean connection, TbUploadFile bean, HttpServletRequest request) throws Exception {
        int page = getInt("page", request, 1);
        int limit = getInt("limit", request, 1);
        return getDao().selectTableForCustomPage(connection, bean, page, limit);
    }
}