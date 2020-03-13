package com.github.automain.common.controller;

import com.github.automain.bean.SysFile;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.dao.SysFileDao;
import com.github.automain.util.DateUtil;
import com.github.automain.util.EncryptUtil;
import com.github.automain.util.SystemUtil;
import redis.clients.jedis.Jedis;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class UploadExecutor extends BaseExecutor {

    @Override
    protected JsonResponse execute(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Collection<Part> parts = request.getParts();
            if (parts == null) {
                return JsonResponse.getFailedJson("请选择上传文件");
            } else {
                Iterator<Part> iterator = parts.iterator();
                Part part = null;
                if (iterator.hasNext()) {
                    part = iterator.next();
                }
                if (part != null && part.getSubmittedFileName() != null) {
                    String originalName = part.getSubmittedFileName();
                    String fileExtension = null;
                    int index = originalName.lastIndexOf(".");
                    String newFileName = UUID.randomUUID().toString();
                    if (index > 0) {
                        fileExtension = originalName.substring(index + 1).toLowerCase();
                        newFileName = newFileName + "." + fileExtension;
                    }
                    String filePath = "/" + DateUtil.getNow(DateUtil.DATE_FORMATTER) + "/" + newFileName;
                    int fileSize = (int) part.getSize();
                    int imageWidth = 0;
                    int imageHeight = 0;
                    if (SystemUtil.IMG_TYPES.contains(fileExtension)) {
                        BufferedImage image = ImageIO.read(part.getInputStream());
                        imageWidth = image.getWidth();
                        imageHeight = image.getHeight();
                    }
                    String absolutePath = SystemUtil.UPLOADS_PATH + filePath;
                    File file = new File(absolutePath);
                    if (SystemUtil.mkdirs(file.getParentFile())) {
                        part.write(absolutePath);
                        String fileMd5 = EncryptUtil.MD5(file);
                        int now = DateUtil.getNow();
                        SysFile uploadFile = new SysFile()
                                .setGid(UUID.randomUUID().toString())
                                .setCreateTime(now)
                                .setUpdateTime(now)
                                .setIsValid(1)
                                .setFileExtension(fileExtension)
                                .setFilePath(filePath)
                                .setFileSize(fileSize)
                                .setFileMd5(fileMd5)
                                .setImageHeight(imageHeight)
                                .setImageWidth(imageWidth);
                        SysFileDao.insertIntoTable(uploadFile);
                        Map<String, String> map = new HashMap<String, String>(2);
                        map.put("uploadFileGid", uploadFile.getGid());
                        if (imageWidth > 0 && imageHeight > 0) {
                            map.put("imagePath", request.getContextPath() + "/uploads" + filePath);
                        }
                        return JsonResponse.getSuccessJson("上传成功", map);
                    } else {
                        return JsonResponse.getFailedJson("上传失败");
                    }
                } else {
                    return JsonResponse.getFailedJson("请选择上传文件");
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return JsonResponse.getFailedJson("上传文件过大");
        }
    }
}
