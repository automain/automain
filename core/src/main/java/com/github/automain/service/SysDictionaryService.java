package com.github.automain.service;

import com.github.automain.bean.SysDictionary;
import com.github.automain.dao.SysDictionaryDao;
import com.github.automain.vo.DictionaryVO;
import com.github.automain.vo.SysDictionaryVO;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import java.util.List;

public class SysDictionaryService extends BaseService<SysDictionary, SysDictionaryDao> {

    public SysDictionaryService(SysDictionary bean, SysDictionaryDao dao) {
        super(bean, dao);
    }

    public PageBean<SysDictionary> selectTableForCustomPage(ConnectionBean connection, SysDictionaryVO bean) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean);
    }

    public List<DictionaryVO> selectAllDictionaryVO(ConnectionBean connection) throws Exception{
        return getDao().selectAllDictionaryVO(connection);
    }
}