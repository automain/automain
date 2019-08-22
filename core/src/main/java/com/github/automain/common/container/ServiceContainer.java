package com.github.automain.common.container;

import com.github.automain.bean.Test;
import com.github.automain.dao.TestDao;
import com.github.automain.service.TestService;

public interface ServiceContainer {

    // ==================== frame start ==================
    TestService TEST_SERVICE = new TestService(new Test(), new TestDao());
    // ==================== frame end ====================

    // ==================== business start ==================


    // ==================== business end ====================
}
