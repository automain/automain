package com.github.automain.util.http;

import com.github.automain.util.CompressUtil;
import com.github.automain.util.EncryptUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.util.UploadUtil;
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
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HTTPUtil {

    private static final int DEFAULT_CONNECTION_TIME_OUT = 5000;
    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";
    private static final String TWO_HYPHENS = "--";
    private static final String BOUNDARY = "*****";
    private static final String END = "\r\n";
    private static final int READ_FILE_SIZE = 1024;
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
     * 发送请求
     *
     * @param url           地址
     * @param requestMethod 请求方法，本类中常量GET_METHOD和POST_METHOD
     * @param httpsRequest  是否是https请求
     * @return
     * @throws Exception
     */
    public static String sendRequest(String url, String requestMethod, boolean httpsRequest) throws Exception {
        HTTPRequestBean bean = new HTTPRequestBean();
        bean.setUrl(url);
        bean.setRequestMethod(requestMethod);
        bean.setHttpsRequest(httpsRequest);
        return sendRequest(bean);
    }

    /**
     * 发送请求
     *
     * @param requestBean
     * @return
     * @throws Exception
     */
    public static String sendRequest(HTTPRequestBean requestBean) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (requestBean != null && requestBean.getUrl() != null) {
            HttpURLConnection connection = null;
            InputStreamReader streamReader = null;
            try {
                URL realUrl = new URL(requestBean.getUrl());
                if (requestBean.getHttpsRequest()) {
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
                String requestMethod = requestBean.getRequestMethod();
                requestMethod = requestMethod == null ? POST_METHOD : requestMethod;
                boolean postFile = false;
                if (requestBean.getFileMap() != null && !requestBean.getFileMap().isEmpty()) {
                    postFile = true;
                    requestMethod = POST_METHOD;
                }
                if (POST_METHOD.equals(requestMethod)) {
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    if (postFile) {
                        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
                    } else {
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    }
                    connection.setRequestProperty("accept", "*/*");
                    connection.setRequestProperty("Charset", PropertiesUtil.DEFAULT_CHARSET);
                }
                Map<String, String> headers = requestBean.getHeaders();
                if (headers != null && !headers.isEmpty()) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                connection.setConnectTimeout(DEFAULT_CONNECTION_TIME_OUT);
                connection.setRequestMethod(requestMethod);
                connection.connect();
                if (POST_METHOD.equals(requestMethod)) {
                    if (postFile) {
                        try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
                            byte[] buffer = null;
                            for (Map.Entry<String, File> entry : requestBean.getFileMap().entrySet()) {
                                File file = entry.getValue();
                                dos.writeBytes(TWO_HYPHENS + BOUNDARY + END);
                                dos.writeBytes("Content-Disposition: form-data; " + "name=\"" + entry.getKey() + "\";filename=\"" + file.getName() + "\"" + END);
                                dos.writeBytes(END);
                                try (FileInputStream fis = new FileInputStream(file)) {
                                    buffer = new byte[READ_FILE_SIZE];
                                    int length = -1;
                                    while ((length = fis.read(buffer)) != -1) {
                                        dos.write(buffer, 0, length);
                                    }
                                    dos.writeBytes(END);
                                }
                            }
                            dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + END);
                            dos.flush();
                        }
                    } else {
                        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), PropertiesUtil.DEFAULT_CHARSET))) {
                            writer.print(getDataByParams(requestBean.getParams()));
                            writer.flush();
                        }
                    }
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
                    try (BufferedReader bufferedReader = new BufferedReader(streamReader)) {
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
        }
        return sb.toString();
    }

    /**
     * 封装http请求参数
     *
     * @param params
     * @return
     * @throws Exception
     */
    private static String getDataByParams(Map<String, String> params) throws Exception {
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
        relativePath = relativePath.toLowerCase();
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
     * 生成api接口签名
     *
     * @param appKey
     * @param apiUrl
     * @param params
     * @param expireTime
     * @return
     * @throws Exception
     */
    public static Map<String, String> generateAPIToken(String appKey, String apiUrl, TreeMap<String, String> params, int expireTime) throws Exception {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("token", generateToken(appKey, apiUrl, params, expireTime));
        return headers;
    }

    private static String generateToken(String appKey, String apiUrl, TreeMap<String, String> params, int expireTime) throws Exception {
        return expireTime + "_" + EncryptUtil.MD5((PropertiesUtil.API_KEY_MAP.get(appKey) + "_" + apiUrl + "_" + expireTime + "_" + getDataByParams(params)).getBytes(PropertiesUtil.DEFAULT_CHARSET));
    }

    /**
     * 检查API接口签名
     *
     * @param request
     * @param appkey
     * @param params
     * @return
     */
    public static boolean checkAPIToken(HttpServletRequest request, String appkey, TreeMap<String, String> params) {
        boolean result = false;
        String token = request.getHeader("token");
        if (token.contains("_")) {
            String expireTime = token.split("_")[0];
            String generateToken = null;
            try {
                generateToken = HTTPUtil.generateToken(appkey, HTTPUtil.getRequestUri(request), params, Integer.parseInt(expireTime));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            result = token.equals(generateToken);
        }
        return result;
    }

    public static void setResponseHeader(HttpServletResponse response, String key, String Value) throws Exception {
        response.setHeader(key, new String(HTTPUtil.urlEncode(Value.getBytes(PropertiesUtil.DEFAULT_CHARSET)), PropertiesUtil.DEFAULT_CHARSET));
    }

}
