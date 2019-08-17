package com.github.automain.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.automain.common.container.ServiceContainer;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.SystemUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class BaseController implements ServiceContainer {

    public static final Logger LOGGER = SystemUtil.getLogger();

    public static <T> T getRequestParam(HttpServletRequest request, Class<T> clazz) {
        try (InputStreamReader isr = new InputStreamReader(request.getInputStream(), PropertiesUtil.DEFAULT_CHARSET);
             BufferedReader br = new BufferedReader(isr)) {
            StringBuilder sb = new StringBuilder();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            return JSONObject.parseObject(sb.toString(), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
