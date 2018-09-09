package com.github.automain.filter;

import com.github.automain.util.http.HTTPUtil;
import com.github.automain.util.PropertiesUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "uploadsFile", urlPatterns = "/uploads/*", asyncSupported = true)
public class Filter03UploadsFile implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = HTTPUtil.getRequestUri(request);
        uri = uri.substring(uri.indexOf("/uploads") + 8);
        String acceptEncoding = request.getHeader("Accept-Encoding");
        try {
            HTTPUtil.writeResourceFileToResponse(PropertiesUtil.UPLOADS_PATH, uri, acceptEncoding, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }
}
