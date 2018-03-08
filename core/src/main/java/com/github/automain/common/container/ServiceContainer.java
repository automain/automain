package com.github.automain.common.container;

import com.github.automain.upload.bean.TbUploadFile;
import com.github.automain.upload.bean.TbUploadRelation;
import com.github.automain.upload.dao.TbUploadFileDao;
import com.github.automain.upload.dao.TbUploadRelationDao;
import com.github.automain.upload.service.TbUploadFileService;
import com.github.automain.upload.service.TbUploadRelationService;

public interface ServiceContainer {

    // ==================== frame start ==================
    TbUploadFileService TB_UPLOAD_FILE_SERVICE = new TbUploadFileService(new TbUploadFile(), new TbUploadFileDao());
    TbUploadRelationService TB_UPLOAD_RELATION_SERVICE = new TbUploadRelationService(new TbUploadRelation(), new TbUploadRelationDao());
    // ==================== frame end ====================

    // ==================== business start ==================


    // ==================== business end ====================
}
