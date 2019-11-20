package com.github.automain.bean;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SysFile implements BaseBean<SysFile> {

    // 主键
    private Integer id;
    // 文件GID
    private String gid;
    // 创建时间
    private Integer createTime;
    // 文件扩展名
    private String fileExtension;
    // 文件相对路径
    private String filePath;
    // 文件大小(字节)
    private Integer fileSize;
    // 文件MD5值
    private String fileMd5;
    // 图片宽度
    private Integer imageWidth;
    // 图片高度
    private Integer imageHeight;

    public Integer getId() {
        return id;
    }

    public SysFile setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getGid() {
        return gid;
    }

    public SysFile setGid(String gid) {
        this.gid = gid;
        return this;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public SysFile setCreateTime(Integer createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public SysFile setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public SysFile setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public SysFile setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public SysFile setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
        return this;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public SysFile setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
        return this;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public SysFile setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
        return this;
    }

    @Override
    public String tableName() {
        return "sys_file";
    }

    @Override
    public Map<String, Object> columnMap(boolean all) {
        Map<String, Object> map = new HashMap<String, Object>(9);
        if (all || this.getId() != null) {
            map.put("id", this.getId());
        }
        if (all || this.getGid() != null) {
            map.put("gid", this.getGid());
        }
        if (all || this.getCreateTime() != null) {
            map.put("create_time", this.getCreateTime());
        }
        if (all || this.getFileExtension() != null) {
            map.put("file_extension", this.getFileExtension());
        }
        if (all || this.getFilePath() != null) {
            map.put("file_path", this.getFilePath());
        }
        if (all || this.getFileSize() != null) {
            map.put("file_size", this.getFileSize());
        }
        if (all || this.getFileMd5() != null) {
            map.put("file_md5", this.getFileMd5());
        }
        if (all || this.getImageWidth() != null) {
            map.put("image_width", this.getImageWidth());
        }
        if (all || this.getImageHeight() != null) {
            map.put("image_height", this.getImageHeight());
        }
        return map;
    }

    @Override
    public SysFile beanFromResultSet(ResultSet rs) throws SQLException {
        return new SysFile()
                .setId(rs.getInt("id"))
                .setGid(rs.getString("gid"))
                .setCreateTime(rs.getInt("create_time"))
                .setFileExtension(rs.getString("file_extension"))
                .setFilePath(rs.getString("file_path"))
                .setFileSize(rs.getInt("file_size"))
                .setFileMd5(rs.getString("file_md5"))
                .setImageWidth(rs.getInt("image_width"))
                .setImageHeight(rs.getInt("image_height"));
    }

    @Override
    public String toString() {
        return "SysFile{" +
                "id=" + id +
                ", gid='" + gid + '\'' +
                ", createTime=" + createTime +
                ", fileExtension='" + fileExtension + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", fileMd5='" + fileMd5 + '\'' +
                ", imageWidth=" + imageWidth +
                ", imageHeight=" + imageHeight +
                '}';
    }
}