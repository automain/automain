package com.github.automain.common.container;

import com.github.automain.bean.TbConfig;
import com.github.automain.bean.TbDictionary;
import com.github.automain.bean.TbSchedule;
import com.github.automain.bean.TbUploadFile;
import com.github.automain.bean.TbUploadRelation;
import com.github.automain.dao.TbConfigDao;
import com.github.automain.dao.TbDictionaryDao;
import com.github.automain.dao.TbScheduleDao;
import com.github.automain.dao.TbUploadFileDao;
import com.github.automain.dao.TbUploadRelationDao;
import com.github.automain.service.TbConfigService;
import com.github.automain.service.TbDictionaryService;
import com.github.automain.service.TbScheduleService;
import com.github.automain.service.TbUploadFileService;
import com.github.automain.service.TbUploadRelationService;
import com.github.automain.bean.TbMenu;
import com.github.automain.bean.TbPrivilege;
import com.github.automain.bean.TbRole;
import com.github.automain.bean.TbRoleMenu;
import com.github.automain.bean.TbRolePrivilege;
import com.github.automain.bean.TbUser;
import com.github.automain.bean.TbUserRole;
import com.github.automain.dao.TbMenuDao;
import com.github.automain.dao.TbPrivilegeDao;
import com.github.automain.dao.TbRoleDao;
import com.github.automain.dao.TbRoleMenuDao;
import com.github.automain.dao.TbRolePrivilegeDao;
import com.github.automain.dao.TbUserDao;
import com.github.automain.dao.TbUserRoleDao;
import com.github.automain.service.TbMenuService;
import com.github.automain.service.TbPrivilegeService;
import com.github.automain.service.TbRoleMenuService;
import com.github.automain.service.TbRolePrivilegeService;
import com.github.automain.service.TbRoleService;
import com.github.automain.service.TbUserRoleService;
import com.github.automain.service.TbUserService;

public interface ServiceContainer {

    // ==================== frame start ==================
    TbUploadFileService TB_UPLOAD_FILE_SERVICE = new TbUploadFileService(new TbUploadFile(), new TbUploadFileDao());
    TbUploadRelationService TB_UPLOAD_RELATION_SERVICE = new TbUploadRelationService(new TbUploadRelation(), new TbUploadRelationDao());
    TbDictionaryService TB_DICTIONARY_SERVICE = new TbDictionaryService(new TbDictionary(), new TbDictionaryDao());
    TbUserService TB_USER_SERVICE = new TbUserService(new TbUser(), new TbUserDao());
    TbRoleService TB_ROLE_SERVICE = new TbRoleService(new TbRole(), new TbRoleDao());
    TbRoleMenuService TB_ROLE_MENU_SERVICE = new TbRoleMenuService(new TbRoleMenu(), new TbRoleMenuDao());
    TbMenuService TB_MENU_SERVICE = new TbMenuService(new TbMenu(), new TbMenuDao());
    TbUserRoleService TB_USER_ROLE_SERVICE = new TbUserRoleService(new TbUserRole(), new TbUserRoleDao());
    TbConfigService TB_CONFIG_SERVICE = new TbConfigService(new TbConfig(), new TbConfigDao());
    TbScheduleService TB_SCHEDULE_SERVICE = new TbScheduleService(new TbSchedule(), new TbScheduleDao());
    TbPrivilegeService TB_PRIVILEGE_SERVICE = new TbPrivilegeService(new TbPrivilege(), new TbPrivilegeDao());
    TbRolePrivilegeService TB_ROLE_PRIVILEGE_SERVICE = new TbRolePrivilegeService(new TbRolePrivilege(), new TbRolePrivilegeDao());
    // ==================== frame end ====================

    // ==================== business start ==================


    // ==================== business end ====================
}
