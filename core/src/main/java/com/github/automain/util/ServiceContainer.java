package com.github.automain.util;

import com.github.automain.service.SysUserService;

public interface ServiceContainer {

    // ==================== frame start ==================
    SysUserService SYS_USER_SERVICE = new SysUserService();
    // ==================== frame end ====================

    // ==================== business start ==================


    // ==================== business end ====================
}
