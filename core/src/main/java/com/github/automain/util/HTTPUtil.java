package com.github.automain.util;

import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class HTTPUtil {

    private static final int DEFAULT_CONNECTION_TIME_OUT = 5000;
    private static final String POST_METHOD = "POST";
    private static final String GET_METHOD = "GET";
    private static final int COMPRESSION_MIN_SIZE = 2048;
    private static final List<String> CACHE_FILE_TYPE = Arrays.asList(".js", ".css", ".otf", ".eot", ".svg", ".ttf", ".woff", ".woff2", ".ico", ".jpg", ".png", ".gif");

    /**
     * URL编码
     *
     * @param content
     * @return
     */
    public static byte[] urlEncode(byte[] content) {
        return Base64.getUrlEncoder().encode(content);
    }

    /**
     * URL解码
     *
     * @param content
     * @return
     */
    public static byte[] urlDecode(byte[] content) {
        return Base64.getUrlDecoder().decode(content);
    }

    /**
     * 发送http请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String httpGet(String url) throws Exception {
        return commonRequest(url, "", GET_METHOD, false);
    }

    /**
     * 发送http请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String httpPost(String url) throws Exception {
        return httpPost(url, "");
    }

    /**
     * 发送http请求
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, Map<String, String> params) throws Exception {
        return httpPost(url, getDataByParams(params));
    }

    /**
     * 发送http请求
     *
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, String data) throws Exception {
        return commonRequest(url, data, POST_METHOD, false);
    }

    /**
     * 发送https请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String httpsGet(String url) throws Exception {
        return commonRequest(url, "", GET_METHOD, true);
    }

    /**
     * 发送https请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String httpsPost(String url) throws Exception {
        return httpsPost(url, "");
    }

    /**
     * 发送https请求
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String httpsPost(String url, Map<String, String> params) throws Exception {
        return httpsPost(url, getDataByParams(params));
    }

    /**
     * 发送https请求
     *
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    public static String httpsPost(String url, String data) throws Exception {
        return commonRequest(url, data, POST_METHOD, true);
    }

    private static String commonRequest(String url, String data, String requestMethod, boolean isHttps) throws Exception {
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();
        InputStreamReader streamReader = null;
        try {
            URL realUrl = new URL(url);
            if (isHttps) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) realUrl.openConnection();
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, new TrustManager[]{new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }}, new SecureRandom());
                httpsURLConnection.setSSLSocketFactory(sc.getSocketFactory());
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
                connection = httpsURLConnection;
            } else {
                connection = (HttpURLConnection) realUrl.openConnection();
            }
            if (POST_METHOD.equals(requestMethod)) {
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("Charset", PropertiesUtil.DEFAULT_CHARSET);
            }
            connection.setConnectTimeout(DEFAULT_CONNECTION_TIME_OUT);
            connection.setRequestMethod(requestMethod);
            connection.connect();
            if (POST_METHOD.equals(requestMethod)) {
                PrintWriter writer = new PrintWriter(connection.getOutputStream());
                writer.print(data);
                writer.flush();
                writer.close();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                streamReader = new InputStreamReader(connection.getInputStream(), PropertiesUtil.DEFAULT_CHARSET);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    streamReader = new InputStreamReader(connection.getErrorStream(), PropertiesUtil.DEFAULT_CHARSET);
                }
                throw e;
            } catch (Exception e1) {
                e1.printStackTrace();
                throw e1;
            }
        } finally {
            if (streamReader != null) {
                try (BufferedReader bufferedReader = new BufferedReader(streamReader)){
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return sb.toString();
    }

    /**
     * 封装post参数
     *
     * @param params
     * @return
     * @throws Exception
     */
    public static String getDataByParams(Map<String, String> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            int i = 1;
            int size = params.size();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(URLEncoder.encode(entry.getKey(), PropertiesUtil.DEFAULT_CHARSET)).append("=").append(URLEncoder.encode(entry.getValue(), PropertiesUtil.DEFAULT_CHARSET));
                if (i < size) {
                    sb.append("&");
                }
                i++;
            }
        }
        return sb.toString();
    }

    /**
     * 获取访问路径
     *
     * @param request
     * @return
     */
    public static String getRequestUri(HttpServletRequest request) {
        String projectName = request.getContextPath();
        return request.getRequestURI().replaceFirst(projectName, "");
    }

    /**
     * 静态资源文件写入到流
     *
     * @param basePath
     * @param relativePath
     * @param acceptEncoding
     * @param response
     */
    public static void writeResourceFileToResponse(String basePath, String relativePath, String acceptEncoding, HttpServletResponse response) throws Exception {
        File file = new File(basePath + relativePath);
        FileOutputStream fos = null;
        if (SystemUtil.checkFileAvailable(file)) {
            FileInputStream is = null;
            OutputStream os = null;
            try {
                long length = file.length();
                os = response.getOutputStream();
                byte[] content = null;
                if (PropertiesUtil.OPEN_CACHE && checkGzip(acceptEncoding, response, length, relativePath)) {
                    String cachePath = PropertiesUtil.CACHE_PATH + relativePath;
                    File cacheFile = new File(cachePath);
                    if (SystemUtil.checkFileAvailable(cacheFile)) {
                        is = new FileInputStream(cacheFile);
                    } else {
                        is = new FileInputStream(file);
                        content = CompressUtil.gzipCompress(is);
                        if (UploadUtil.mkdirs(cacheFile.getParentFile())) {
                            fos = new FileOutputStream(cacheFile);
                            fos.write(content);
                            fos.flush();
                        }
                    }
                }
                String fileType = relativePath.substring(relativePath.lastIndexOf("."));
                if (".js".equals(fileType)) {
                    response.setContentType("application/x-javascript");
                } else if (".css".equals(fileType)) {
                    response.setContentType("text/css");
                }
                if (CACHE_FILE_TYPE.contains(fileType)) {
                    response.addDateHeader("Expires", System.currentTimeMillis() + 2592000000L);//30 * 24 * 60 * 60 * 1000
                }
                int len = -1;
                if (content != null) {
                    os.write(content);
                } else {
                    byte[] buff = new byte[COMPRESSION_MIN_SIZE];
                    if (is == null) {
                        is = new FileInputStream(file);
                    }
                    while ((len = is.read(buff)) != -1) {
                        os.write(buff, 0, len);
                    }
                }
                os.flush();
            } finally {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            }
        }
    }


    /**
     * 文件下载
     *
     * @param request
     * @param response
     * @param path
     * @return
     * @throws Exception
     */
    public static String downloadFile(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
        File file = new File(path);
        if (SystemUtil.checkFileAvailable(file)) {
            try (InputStream is = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                String userAgent = request.getHeader("User-Agent").toUpperCase();
                String parentPath = file.getParent();
                String fileName = parentPath == null ? path : file.getPath().replace(parentPath, "");
                if (userAgent.contains("EDGE") || userAgent.contains("MSIE") || userAgent.contains("RV:11")) {
                    fileName = new String(urlEncode(fileName.getBytes(PropertiesUtil.DEFAULT_CHARSET)), PropertiesUtil.DEFAULT_CHARSET);
                } else {
                    fileName = new String(fileName.getBytes(PropertiesUtil.DEFAULT_CHARSET), "ISO8859-1");
                }
                response.setContentType("multipart/form-data");
                response.setHeader("Content-Disposition", "attachment;fileName=\"" + fileName + "\"");
                byte[] b = new byte[COMPRESSION_MIN_SIZE];
                int len;
                while ((len = is.read(b)) > 0) {
                    os.write(b, 0, len);
                }
                os.flush();
            }
        }
        return "download_file";

    }

    /**
     * 检查是否需要gzip压缩
     *
     * @param acceptEncoding
     * @param response
     * @param length
     * @param path
     * @return
     */
    public static boolean checkGzip(String acceptEncoding, HttpServletResponse response, long length, String path) {
        if (acceptEncoding != null && acceptEncoding.contains("gzip") && length > COMPRESSION_MIN_SIZE && (path.endsWith(".js") || path.endsWith(".css") || path.endsWith(".jsp"))) {
            response.addHeader("Content-Encoding", "gzip");
            return true;
        }
        return false;
    }

    /**
     * 获取访客IP
     *
     * @param request
     * @return
     */
    public static String getRequestIp(HttpServletRequest request) {
        String realIP = request.getHeader("X-Real-IP");
        return StringUtils.isBlank(realIP) || "unknown".equalsIgnoreCase(realIP) ? request.getRemoteAddr() : realIP;
    }


    /**
     * 检查是否是内网IP
     *
     * @param request
     * @return
     */
    public static boolean checkInnerIp(HttpServletRequest request) {
        return checkInnerIp(getRequestIp(request));
    }

    /**
     * 检查是否是内网IP
     *
     * @param realIP
     * @return
     */
    private static boolean checkInnerIp(String realIP) {
        if ("127.0.0.1".equals(realIP)) {
            return true;
        } else {
            int[] intIp = getIntIp(realIP);
            if (intIp != null) {
                int addr0 = intIp[0];
                int addr1 = intIp[1];
                switch (addr0) {
                    case 10:
                        return true;
                    case 172:
                        return addr1 >= 16 && addr1 <= 31;
                    case 192:
                        return addr1 == 168;
                    default:
                        return false;
                }
            }
            return false;
        }
    }

    private static int[] getIntIp(String realIP) {
        int[] ret = null;
        if (StringUtils.isNotBlank(realIP)) {
            String[] ipArr = realIP.split("\\.");
            if (ipArr.length == 4) {
                int addr0 = Integer.parseInt(ipArr[0]);
                int addr1 = Integer.parseInt(ipArr[1]);
                int addr2 = Integer.parseInt(ipArr[2]);
                int addr3 = Integer.parseInt(ipArr[3]);
                if (checkIpAddress(addr0) && checkIpAddress(addr1) && checkIpAddress(addr2) && checkIpAddress(addr3)) {
                    ret = new int[4];
                    ret[0] = addr0;
                    ret[1] = addr1;
                    ret[2] = addr2;
                    ret[3] = addr3;
                }
            }
        }
        return ret;
    }

    private static boolean checkIpAddress(int addr) {
        return addr >= 0 && addr <= 255;
    }

}
