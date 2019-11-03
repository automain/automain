package com.github.automain.common.container;

import com.github.automain.dao.SysDictionaryDao;
import com.github.automain.dao.TestDao;
import com.github.automain.service.SysDictionaryService;
import com.github.automain.service.TestService;

public interface ServiceDaoContainer {

    // ==================== frame start ==================
    SysDictionaryService SYS_DICTIONARY_SERVICE = new SysDictionaryService();
    SysDictionaryDao SYS_DICTIONARY_DAO = new SysDictionaryDao();
    TestService TEST_SERVICE = new TestService();
    TestDao TEST_DAO = new TestDao();
    // ==================== frame end ====================

    // ==================== business start ==================


    // ==================== business end ====================
}
