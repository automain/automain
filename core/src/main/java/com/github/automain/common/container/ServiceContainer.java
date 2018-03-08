package com.github.automain.common.container;

import com.github.automain.dictionary.bean.TbDictionary;
import com.github.automain.dictionary.dao.TbDictionaryDao;
import com.github.automain.dictionary.service.TbDictionaryService;
import com.github.automain.upload.bean.TbUploadFile;
import com.github.automain.upload.bean.TbUploadRelation;
import com.github.automain.upload.dao.TbUploadFileDao;
import com.github.automain.upload.dao.TbUploadRelationDao;
import com.github.automain.upload.service.TbUploadFileService;
import com.github.automain.upload.service.TbUploadRelationService;
import com.github.automain.user.bean.TbMenu;
import com.github.automain.user.bean.TbRequestMapping;
import com.github.automain.user.bean.TbRole;
import com.github.automain.user.bean.TbRoleMenu;
import com.github.automain.user.bean.TbRoleRequestMapping;
import com.github.automain.user.bean.TbUser;
import com.github.automain.user.bean.TbUserRole;
import com.github.automain.user.dao.TbMenuDao;
import com.github.automain.user.dao.TbRequestMappingDao;
import com.github.automain.user.dao.TbRoleDao;
import com.github.automain.user.dao.TbRoleMenuDao;
import com.github.automain.user.dao.TbRoleRequestMappingDao;
import com.github.automain.user.dao.TbUserDao;
import com.github.automain.user.dao.TbUserRoleDao;
import com.github.automain.user.service.TbMenuService;
import com.github.automain.user.service.TbRequestMappingService;
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
    TbRequestMappingService TB_REQUEST_MAPPING_SERVICE = new TbRequestMappingService(new TbRequestMapping(), new TbRequestMappingDao());
    TbUserRoleService TB_USER_ROLE_SERVICE = new TbUserRoleService(new TbUserRole(), new TbUserRoleDao());
    // ==================== frame end ====================

    // ==================== business start ==================


    // ==================== business end ====================
}
