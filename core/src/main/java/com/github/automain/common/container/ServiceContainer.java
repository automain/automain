package com.github.automain.common.container;

import com.github.automain.common.bean.TbConfig;
import com.github.automain.common.bean.TbDictionary;
import com.github.automain.common.bean.TbInnerIpPort;
import com.github.automain.common.bean.TbSchedule;
import com.github.automain.common.bean.TbUploadFile;
import com.github.automain.common.bean.TbUploadRelation;
import com.github.automain.common.dao.TbConfigDao;
import com.github.automain.common.dao.TbDictionaryDao;
import com.github.automain.common.dao.TbInnerIpPortDao;
import com.github.automain.common.dao.TbScheduleDao;
import com.github.automain.common.dao.TbUploadFileDao;
import com.github.automain.common.dao.TbUploadRelationDao;
import com.github.automain.common.service.TbConfigService;
import com.github.automain.common.service.TbDictionaryService;
import com.github.automain.common.service.TbInnerIpPortService;
import com.github.automain.common.service.TbScheduleService;
import com.github.automain.common.service.TbUploadFileService;
import com.github.automain.common.service.TbUploadRelationService;
import com.github.automain.monitor.bean.DbSlowLog;
import com.github.automain.monitor.bean.DbStatus;
import com.github.automain.monitor.dao.DbSlowLogDao;
import com.github.automain.monitor.dao.DbStatusDao;
import com.github.automain.monitor.service.DbSlowLogService;
import com.github.automain.monitor.service.DbStatusService;
import com.github.automain.user.bean.TbMenu;
import com.github.automain.user.bean.TbRole;
import com.github.automain.user.bean.TbRoleMenu;
import com.github.automain.user.bean.TbRoleRequestMapping;
import com.github.automain.user.bean.TbUser;
import com.github.automain.user.bean.TbUserRole;
import com.github.automain.user.dao.TbMenuDao;
import com.github.automain.user.dao.TbRoleDao;
import com.github.automain.user.dao.TbRoleMenuDao;
import com.github.automain.user.dao.TbRoleRequestMappingDao;
import com.github.automain.user.dao.TbUserDao;
import com.github.automain.user.dao.TbUserRoleDao;
import com.github.automain.user.service.TbMenuService;
import com.github.automain.user.service.TbRoleMenuService;
import com.github.automain.user.service.TbRoleRequestMappingService;
import com.github.automain.user.service.TbRoleService;
import com.github.automain.user.service.TbUserRoleService;
import com.github.automain.user.service.TbUserService;

public interface ServiceContainer {

    // ==================== frame start ==================
    TbUploadFileService TB_UPLOAD_FILE_SERVICE = new TbUploadFileService(new TbUploadFile(), new TbUploadFileDao());
    TbUploadRelationService TB_UPLOAD_RELATION_SERVICE = new TbUploadRelationService(new TbUploadRelation(), new TbUploadRelationDao());
    TbDictionaryService TB_DICTIONARY_SERVICE = new TbDictionaryService(new TbDictionary(), new TbDictionaryDao());
    TbUserService TB_USER_SERVICE = new TbUserService(new TbUser(), new TbUserDao());
    TbRoleService TB_ROLE_SERVICE = new TbRoleService(new TbRole(), new TbRoleDao());
    TbRoleMenuService TB_ROLE_MENU_SERVICE = new TbRoleMenuService(new TbRoleMenu(), new TbRoleMenuDao());
    TbRoleRequestMappingService TB_ROLE_REQUEST_MAPPING_SERVICE = new TbRoleRequestMappingService(new TbRoleRequestMapping(), new TbRoleRequestMappingDao());
    TbMenuService TB_MENU_SERVICE = new TbMenuService(new TbMenu(), new TbMenuDao());
    TbUserRoleService TB_USER_ROLE_SERVICE = new TbUserRoleService(new TbUserRole(), new TbUserRoleDao());
    TbConfigService TB_CONFIG_SERVICE = new TbConfigService(new TbConfig(), new TbConfigDao());
    DbSlowLogService DB_SLOW_LOG_SERVICE = new DbSlowLogService(new DbSlowLog(), new DbSlowLogDao());
    DbStatusService DB_STATUS_SERVICE = new DbStatusService(new DbStatus(), new DbStatusDao());
    TbInnerIpPortService TB_INNER_IP_PORT_SERVICE = new TbInnerIpPortService(new TbInnerIpPort(), new TbInnerIpPortDao());
    TbScheduleService TB_SCHEDULE_SERVICE = new TbScheduleService(new TbSchedule(), new TbScheduleDao());
    // ==================== frame end ====================

    // ==================== business start ==================


    // ==================== business end ====================
}
