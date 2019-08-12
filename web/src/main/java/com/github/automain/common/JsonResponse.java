package com.github.automain.common;

public class JsonResponse {

    private Integer status;
    private String message;
    private Object data;

    private JsonResponse() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static JsonResponse getSuccessJson(String message) {
        return getSuccessJson(message, null);
    }

    public static JsonResponse getSuccessJson(String message, Object data) {
        return getJson(0, message, data);
    }

    public static JsonResponse getFailedJson(String message) {
        return getFailedJson(message, null);
    }

    public static JsonResponse getFailedJson(String message, Object data) {
        return getJson(1, message, data);
    }

    public static JsonResponse getJson(Integer status, String message) {
        return getJson(status, message, null);
    }

    public static JsonResponse getJson(Integer status, String message, Object data) {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setStatus(status);
        jsonResponse.setMessage(message);
        jsonResponse.setData(data);
        return jsonResponse;
    }

    @Override
    public String toString() {
        return "JsonResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
