package com.github.automain.common.controller;

import com.github.automain.common.bean.JsonResponse;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class UploadExecutor extends BaseExecutor {

    @Override
    protected JsonResponse execute(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonResponse jsonResponse = null;
//        try {
//            Collection<Part> parts = request.getParts();
//            if (parts == null) {
//                jsonResponse = JsonResponse.getFailedJson("请选择上传文件");
//            } else {
//                Iterator<Part> iterator = parts.iterator();
//                Part part = null;
//                if (iterator.hasNext()) {
//                    part = iterator.next();
//                }
//                if (part != null && part.getSubmittedFileName() != null) {
//                    String originalName = part.getSubmittedFileName();
//                    String fileExtension = null;
//                    int index = originalName.lastIndexOf(".");
//                    String newFileName = UUID.randomUUID().toString();
//                    if (index > 0) {
//                        fileExtension = originalName.substring(index + 1).toLowerCase();
//                        newFileName = newFileName + "." + fileExtension;
//                    }
//                    String filePath = "/" + DateUtil.getNow(DateUtil.DATE_FORMATTER) + "/" + newFileName;
//                    long fileSize = part.getSize();
//                    int imageWidth = 0;
//                    int imageHeight = 0;
//                    if (UploadUtil.IMG_TYPES.contains(fileExtension)) {
//                        BufferedImage image = ImageIO.read(part.getInputStream());
//                        imageWidth = image.getWidth();
//                        imageHeight = image.getHeight();
//                    }
//                    String absolutePath = UploadUtil.UPLOADS_PATH + filePath;
//                    File file = new File(absolutePath);
//                    if (SystemUtil.mkdirs(file.getParentFile())) {
//                        part.write(absolutePath);
//                        String fileMd5 = EncryptUtil.MD5(file);
//                        TbUploadFile uploadFile = new TbUploadFile();
//                        uploadFile.setFileExtension(fileExtension);
//                        uploadFile.setFilePath(filePath);
//                        uploadFile.setFileSize(fileSize);
//                        uploadFile.setImageHeight(imageHeight);
//                        uploadFile.setImageWidth(imageWidth);
//                        uploadFile.setUploadTime(new Timestamp(System.currentTimeMillis()));
//                        uploadFile.setFileMd5(fileMd5);
//                        Long uploadFileId = TB_UPLOAD_FILE_SERVICE.insertIntoTableReturnId(connection, uploadFile);
//                        if (!uploadFileId.equals(0L)) {
//                            request.setAttribute("uploadFileId", uploadFileId);
//                            if (imageWidth > 0 && imageHeight > 0) {
//                                request.setAttribute("imagePath", request.getContextPath() + "/uploads" + filePath);
//                            }
//                        }
//                        jsonResponse = JsonResponse.getFailedJson("上传成功");
//                    } else {
//                        jsonResponse = JsonResponse.getFailedJson("上传失败");
//                    }
//                } else {
//                    jsonResponse = JsonResponse.getFailedJson("请选择上传文件");
//                }
//            }
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//            jsonResponse = JsonResponse.getFailedJson("上传文件过大");
//        }
        return jsonResponse;
    }
}
