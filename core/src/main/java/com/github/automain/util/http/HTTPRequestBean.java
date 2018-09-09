package com.github.automain.util.http;

import java.io.File;
import java.util.Map;

public class HTTPRequestBean {

    // 地址
    private String url;

    // 请求方法，可选值(POST,GET)
    private String requestMethod;

    // 请求参数
    private Map<String, String> params;

    // 请求头参数
    private Map<String, String> headers;

    // 是否是https请求
    private boolean httpsRequest;

    // 请求文件参数名->文件
    private Map<String, File> fileMap;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public boolean getHttpsRequest() {
        return httpsRequest;
    }

    public void setHttpsRequest(boolean httpsRequest) {
        this.httpsRequest = httpsRequest;
    }

    public Map<String, File> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<String, File> fileMap) {
        this.fileMap = fileMap;
    }
}
