package com.github.automain.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class CompressUtil {

    private static final int BUF_SIZE = 2048;

    /**
     * zip压缩
     *
     * @param filePath
     * @param zipPath
     */
    public static void zipCompress(String filePath, String zipPath) {
        ZipOutputStream zos = null;
        try {
            File file = new File(filePath);
            File zipFile = new File(zipPath);
            String parentPath = "";
            if (file.exists()) {
                parentPath = file.getParentFile().getPath();
                if (!parentPath.endsWith(File.separator)) {
                    parentPath += File.separator;
                }
            }
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            zipDirectory(parentPath, file, zos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.finish();
                    zos.close();
                    zos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void zipDirectory(String rootPath, File file, ZipOutputStream zos) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                zos.putNextEntry(new ZipEntry(file.getPath().replace(rootPath, "") + "/"));
                zos.closeEntry();
            } else {
                for (File fileName : files) {
                    if (fileName.isDirectory()) {
                        zipDirectory(rootPath, fileName, zos);
                    } else {
                        zipFile(rootPath, zos, fileName);
                    }
                }
            }
        } else {
            zipFile(rootPath, zos, file);
        }
    }

    private static void zipFile(String rootPath, ZipOutputStream zos, File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            zos.putNextEntry(new ZipEntry(file.getPath().replace(rootPath, "")));
            int readBytes = -1;
            byte[] buf = new byte[BUF_SIZE];
            while ((readBytes = fis.read(buf)) > 0) {
                zos.write(buf, 0, readBytes);
            }
            zos.closeEntry();
        }
    }

    /**
     * zip解压
     *
     * @param zipFileName
     * @param unCompressPath
     * @throws IOException
     */
    public static void zipUnCompress(String zipFileName, String unCompressPath) throws IOException {
        try (FileInputStream fis = new FileInputStream(zipFileName);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ZipInputStream zis = new ZipInputStream(bis)) {
            ZipEntry zipEntry = null;
            if (!unCompressPath.endsWith(File.separator)) {
                unCompressPath += File.separator;
            }
            File unCompressFolder = new File(unCompressPath);
            if (SystemUtil.mkdirs(unCompressFolder)) {
                while ((zipEntry = zis.getNextEntry()) != null) {
                    File file = new File(unCompressPath + zipEntry.getName());
                    if (zipEntry.isDirectory()) {
                        if (!SystemUtil.mkdirs(file)) {
                            break;
                        }
                    } else {
                        File parent = file.getParentFile();
                        if (SystemUtil.mkdirs(parent)) {
                            FileOutputStream fos = new FileOutputStream(file);
                            int readBytes = -1;
                            byte[] buf = new byte[BUF_SIZE];
                            while ((readBytes = zis.read(buf)) > 0) {
                                fos.write(buf, 0, readBytes);
                            }
                            fos.flush();
                            fos.close();
                        }
                    }
                    zis.closeEntry();
                }
            }
        }
    }

    /**
     * gzip压缩
     *
     * @param content
     * @return
     */
    public static byte[] gzipCompress(byte[] content) throws IOException {
        if (content != null) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 GZIPOutputStream os = new GZIPOutputStream(bos)) {
                os.write(content);
                os.finish();
                os.flush();
                return bos.toByteArray();
            }
        }
        return null;
    }

    /**
     * gzip压缩
     *
     * @param is
     * @return
     */
    public static byte[] gzipCompress(FileInputStream is) throws IOException {
        if (is != null) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 GZIPOutputStream os = new GZIPOutputStream(bos)) {
                int readBytes = -1;
                byte[] buf = new byte[BUF_SIZE];
                while ((readBytes = is.read(buf)) != -1) {
                    os.write(buf, 0, readBytes);
                }
                os.finish();
                os.flush();
                return bos.toByteArray();
            }
        }
        return null;
    }

    /**
     * gzip解压缩
     *
     * @param gzip
     * @return
     */
    public static byte[] gzipUnCompress(byte[] gzip) throws IOException {
        if (gzip != null) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ByteArrayInputStream bis = new ByteArrayInputStream(gzip);
                 GZIPInputStream gis = new GZIPInputStream(bis)) {
                byte[] buf = new byte[BUF_SIZE];
                int readBytes = -1;
                while ((readBytes = gis.read(buf)) > 0) {
                    bos.write(buf, 0, readBytes);
                }
                bos.flush();
                return bos.toByteArray();
            }
        }
        return null;
    }

}
