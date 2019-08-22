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

    public HTTPRequestBean setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public HTTPRequestBean setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public HTTPRequestBean setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HTTPRequestBean setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public boolean getHttpsRequest() {
        return httpsRequest;
    }

    public HTTPRequestBean setHttpsRequest(boolean httpsRequest) {
        this.httpsRequest = httpsRequest;
        return this;
    }

    public Map<String, File> getFileMap() {
        return fileMap;
    }

    public HTTPRequestBean setFileMap(Map<String, File> fileMap) {
        this.fileMap = fileMap;
        return this;
    }

    @Override
    public String toString() {
        return "HTTPRequestBean{" +
                "url='" + url + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", params=" + params +
                ", headers=" + headers +
                ", httpsRequest=" + httpsRequest +
                ", fileMap=" + fileMap +
                '}';
    }
}
