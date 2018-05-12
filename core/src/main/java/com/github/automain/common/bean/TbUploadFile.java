package com.github.automain.common.bean;

import com.github.fastjdbc.common.BaseBean;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class TbUploadFile extends RequestUtil implements BaseBean<TbUploadFile> {

    // 文件ID
    private Long uploadFileId;

    // 文件扩展名
    private String fileExtension;

    // 文件相对路径
    private String filePath;

    // 文件大小(字节)
    private Long fileSize;

    // 上传日期
    private Timestamp uploadTime;

    // 文件MD5值
    private String fileMd5;

    // 图片文件宽度
    private Integer imageWidth;

    // 图片文件高度
    private Integer imageHeight;

    // ========== additional column begin ==========

    // 上传日期
    private String uploadTimeRange;

    public String getUploadTimeRange() {
        return uploadTimeRange;
    }

    public void setUploadTimeRange(String uploadTimeRange) {
        this.uploadTimeRange = uploadTimeRange;
    }

    // ========== additional column end ==========

    public Long getUploadFileId() {
        return uploadFileId;
    }

    public void setUploadFileId(Long uploadFileId) {
        this.uploadFileId = uploadFileId;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    @Override
    public String tableName() {
        return "tb_upload_file";
    }

    @Override
    public String primaryKey() {
        return "upload_file_id";
    }

    @Override
    public Long primaryValue() {
        return this.getUploadFileId();
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (all || this.getFileExtension() != null) {
            map.put("file_extension", this.getFileExtension());
        }
        if (all || this.getFileMd5() != null) {
            map.put("file_md5", this.getFileMd5());
        }
        if (all || this.getFilePath() != null) {
            map.put("file_path", this.getFilePath());
        }
        if (all || this.getFileSize() != null) {
            map.put("file_size", this.getFileSize());
        }
        if (all || this.getImageHeight() != null) {
            map.put("image_height", this.getImageHeight());
        }
        if (all || this.getImageWidth() != null) {
            map.put("image_width", this.getImageWidth());
        }
        if (all || this.getUploadTime() != null) {
            map.put("upload_time", this.getUploadTime());
        }
        return map;
    }

    @Override
    public TbUploadFile beanFromResultSet(ResultSet rs) throws SQLException {
        TbUploadFile bean = new TbUploadFile();
        bean.setUploadFileId(rs.getLong("upload_file_id"));
        bean.setFileExtension(rs.getString("file_extension"));
        bean.setFilePath(rs.getString("file_path"));
        bean.setFileSize(rs.getLong("file_size"));
        bean.setUploadTime(rs.getTimestamp("upload_time"));
        bean.setFileMd5(rs.getString("file_md5"));
        bean.setImageWidth(rs.getInt("image_width"));
        bean.setImageHeight(rs.getInt("image_height"));
        return bean;
    }

    @Override
    public TbUploadFile beanFromRequest(HttpServletRequest request) {
        TbUploadFile bean = new TbUploadFile();
        bean.setUploadFileId(getLong("uploadFileId", request));
        bean.setFileExtension(getString("fileExtension", request));
        bean.setFilePath(getString("filePath", request));
        bean.setFileSize(getLong("fileSize", request));
        bean.setUploadTime(getTimestamp("uploadTime", request));
        bean.setFileMd5(getString("fileMd5", request));
        bean.setImageWidth(getInt("imageWidth", request));
        bean.setImageHeight(getInt("imageHeight", request));
        bean.setUploadTimeRange(getString("uploadTimeRange", request));
        return bean;
    }
}