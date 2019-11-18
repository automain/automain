package com.github.automain.common.container;

import com.github.automain.service.SysDictionaryService;
import com.github.automain.service.SysUserService;
import com.github.automain.service.TestService;

public interface ServiceContainer {

    // ==================== frame start ==================
    SysDictionaryService SYS_DICTIONARY_SERVICE = new SysDictionaryService();
    TestService TEST_SERVICE = new TestService();
    SysUserService SYS_USER_SERVICE = new SysUserService();
    // ==================== frame end ====================

    // ==================== business start ==================


    // ==================== business end ====================
}
