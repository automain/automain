package com.github.automain.util.http;

import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.SystemUtil;
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
import java.util.Base64;
import java.util.Map;

public class HTTPUtil {

    private static final int DEFAULT_CONNECTION_TIME_OUT = 10000;
    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";
    private static final String TWO_HYPHENS = "--";
    private static final String BOUNDARY = "*****";
    private static final String END = "\r\n";
    private static final int READ_FILE_SIZE = 1024;
    private static final int COMPRESSION_MIN_SIZE = 2048;
    public static final String JSON_CONTENT_TYPE = "application/json;charset=" + PropertiesUtil.DEFAULT_CHARSET;
    public static final String FILE_CONTENT_TYPE = "multipart/form-data";

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
                        connection.setRequestProperty("Content-Type", FILE_CONTENT_TYPE + ";boundary=" + BOUNDARY);
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
     * 文件下载
     *
     * @param request
     * @param response
     * @param path
     * @return
     * @throws Exception
     */
    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
        File file = new File(path);
        if (SystemUtil.checkFileAvailable(file)) {
            try (InputStream is = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                String userAgent = request.getHeader("User-Agent").toUpperCase();
                String fileName = path.substring(path.lastIndexOf("/") + 1);
                if (userAgent.contains("EDGE") || userAgent.contains("MSIE") || userAgent.contains("RV:11")) {
                    fileName = new String(urlEncode(fileName.getBytes(PropertiesUtil.DEFAULT_CHARSET)), PropertiesUtil.DEFAULT_CHARSET);
                } else {
                    fileName = new String(fileName.getBytes(PropertiesUtil.DEFAULT_CHARSET), "ISO8859-1");
                }
                response.setContentType(FILE_CONTENT_TYPE);
                response.setHeader("Content-Disposition", "attachment;fileName=\"" + fileName + "\"");
                byte[] b = new byte[COMPRESSION_MIN_SIZE];
                int len;
                while ((len = is.read(b)) > 0) {
                    os.write(b, 0, len);
                }
                os.flush();
            }
        }
    }

    /**
     * 检查是否需要gzip压缩
     *
     * @param request
     * @param response
     * @param length
     * @return
     */
    public static boolean checkGzip(HttpServletRequest request, HttpServletResponse response, long length) {
        String acceptEncoding = request.getHeader("Accept-Encoding");
        if (acceptEncoding != null && acceptEncoding.contains("gzip") && length > COMPRESSION_MIN_SIZE) {
            response.setHeader("Content-Encoding", "gzip");
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
}
