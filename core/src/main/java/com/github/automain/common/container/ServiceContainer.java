package com.github.automain.common.container;

import com.github.automain.bean.SysDictionary;
import com.github.automain.bean.Test;
import com.github.automain.dao.SysDictionaryDao;
import com.github.automain.dao.TestDao;
import com.github.automain.service.SysDictionaryService;
import com.github.automain.service.TestService;

public interface ServiceContainer {

    // ==================== frame start ==================
    SysDictionaryService SYS_DICTIONARY_SERVICE = new SysDictionaryService(new SysDictionary(), new SysDictionaryDao());
    TestService TEST_SERVICE = new TestService(new Test(), new TestDao());
    // ==================== frame end ====================

    // ==================== business start ==================


    // ==================== business end ====================
}
