package com.github.automain.util;

import com.github.automain.common.bean.TbUploadFile;
import com.github.automain.common.bean.TbUploadRelation;
import com.github.automain.common.container.ServiceContainer;
import com.github.fastjdbc.bean.ConnectionBean;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UploadUtil implements ServiceContainer {

    public static final List<String> IMG_TYPES = Collections.unmodifiableList(Arrays.asList("bmp", "jpg", "jpeg", "png", "gif"));

    private static final String CDN_PATH = PropertiesUtil.CONFIG_PROPERTIES.getProperty("cdnPath");

    /**
     * 保存base64到文件
     *
     * @param connection
     * @param base64
     * @param fileExtension
     * @return
     * @throws SQLException
     */
    public static Long saveFileByBase64(ConnectionBean connection, String base64, String fileExtension) throws Exception {
        String newFileName = SystemUtil.randomUUID();
        if (fileExtension != null) {
            newFileName = newFileName + "." + fileExtension;
            String filePath = "/" + DateUtil.getNow(DateUtil.SIMPLE_DATE_PATTERN) + "/" + newFileName;
            String absolutePath = PropertiesUtil.UPLOADS_PATH + filePath;
            base6ToFile(base64, absolutePath);
            File file = new File(absolutePath);
            String fileMd5 = EncryptUtil.MD5(file);
            long fileSize = file.length();
            int imageWidth = 0;
            int imageHeight = 0;
            if (IMG_TYPES.contains(fileExtension)) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    BufferedImage image = ImageIO.read(fis);
                    imageWidth = image.getWidth();
                    imageHeight = image.getHeight();
                }
            }
            TbUploadFile uploadFile = new TbUploadFile();
            uploadFile.setFileExtension(fileExtension);
            uploadFile.setFilePath(filePath);
            uploadFile.setFileSize(fileSize);
            uploadFile.setImageHeight(imageHeight);
            uploadFile.setImageWidth(imageWidth);
            uploadFile.setUploadTime(new Timestamp(System.currentTimeMillis()));
            uploadFile.setFileMd5(fileMd5);
            return TB_UPLOAD_FILE_SERVICE.insertIntoTableReturnId(connection, uploadFile);
        }
        return 0L;
    }

    /**
     * 保存文件和数据对应关系
     *
     * @param connection
     * @param uploadFileId
     * @param recordId
     * @param recordTableName
     * @param recordLabel
     * @param sequenceNumber
     * @throws SQLException
     */
    public static void saveFileRelation(ConnectionBean connection, Long uploadFileId, Long recordId, String recordTableName, String recordLabel, int sequenceNumber) throws SQLException {
        TbUploadRelation relation = new TbUploadRelation();
        relation.setRecordTableName(recordTableName);
        relation.setRecordId(recordId);
        relation.setRecordLabel(recordLabel);
        if (sequenceNumber == 0) {
            TbUploadRelation uploadRelation = TB_UPLOAD_RELATION_SERVICE.selectMaxOrderNumberRelationByBean(connection, relation);
            if (uploadRelation != null && uploadRelation.getSequenceNumber() != null) {
                sequenceNumber = uploadRelation.getSequenceNumber() + 1;
            }
        }
        relation.setUploadFileId(uploadFileId);
        relation.setSequenceNumber(sequenceNumber);
        relation.setIsDelete(0);
        TB_UPLOAD_RELATION_SERVICE.insertIntoTable(connection, relation);
    }

    /**
     * 获取文件下载路径
     *
     * @param request
     * @param filePath
     * @return
     */
    private static String concatPathForFile(HttpServletRequest request, String filePath) {
        return StringUtils.isNotBlank(CDN_PATH) ? CDN_PATH + filePath : request.getContextPath() + "/uploads" + filePath;
    }

    /**
     * 获取最大排序号文件
     *
     * @param connection
     * @param request
     * @param relation
     * @return
     * @throws SQLException
     */
    public static String getLastFile(ConnectionBean connection, HttpServletRequest request, TbUploadRelation relation) throws SQLException {
        TbUploadRelation uploadRelation = TB_UPLOAD_RELATION_SERVICE.selectMaxOrderNumberRelationByBean(connection, relation);
        if (uploadRelation != null && uploadRelation.getUploadFileId() != null) {
            TbUploadFile file = TB_UPLOAD_FILE_SERVICE.selectTableById(connection, uploadRelation.getUploadFileId());
            if (file != null && file.getFilePath() != null) {
                return concatPathForFile(request, file.getFilePath());
            }
        }
        return null;
    }

    /**
     * 获取单条记录所有关联文件路径
     *
     * @param connection
     * @param request
     * @param relation
     * @return
     * @throws SQLException
     */
    public static List<String> getFilePathList(ConnectionBean connection, HttpServletRequest request, TbUploadRelation relation) throws SQLException {
        List<TbUploadRelation> relationList = TB_UPLOAD_RELATION_SERVICE.selectTableByBean(connection, relation);
        List<String> retList = new ArrayList<String>();
        for (TbUploadRelation uploadRelation : relationList) {
            if (uploadRelation.getUploadFileId() != null) {
                TbUploadFile file = TB_UPLOAD_FILE_SERVICE.selectTableById(connection, uploadRelation.getUploadFileId());
                if (file != null && file.getFilePath() != null) {
                    retList.add(concatPathForFile(request, file.getFilePath()));
                }
            }
        }
        return retList;
    }

    /**
     * 文件转为base64
     *
     * @param relativeFilePath
     * @return
     */
    public static String fileToBase64(String relativeFilePath) throws IOException {
        String filePath = PropertiesUtil.UPLOADS_PATH + relativeFilePath;
        File file = new File(filePath);
        if (SystemUtil.checkFileAvailable(file)) {
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[(int) file.length()];
                if (fis.read(buffer) > -1) {
                    return EncryptUtil.BASE64Encode(buffer);
                }
            }
        }
        return null;
    }

    /**
     * base64转文件
     *
     * @param base64
     * @param filePath
     */
    private static void base6ToFile(String base64, String filePath) throws IOException {
        File file = new File(filePath);
        if (mkdirs(file.getParentFile())) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] bytes = EncryptUtil.BASE64Decode(base64);
                fos.write(bytes);
            }
        }
    }

    /**
     * 创建目录
     *
     * @param file
     * @return
     */
    public static boolean mkdirs(File file) {
        return file != null && ((file.exists() && file.isDirectory()) || file.mkdirs());
    }

}
