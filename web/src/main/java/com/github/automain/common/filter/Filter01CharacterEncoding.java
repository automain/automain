package com.github.automain.common.filter;

import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.SystemUtil;

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

@WebFilter(filterName = "characterEncoding", urlPatterns = "/*", asyncSupported = true)
public class Filter01CharacterEncoding implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(PropertiesUtil.DEFAULT_CHARSET);
        servletResponse.setCharacterEncoding(PropertiesUtil.DEFAULT_CHARSET);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String origin = request.getHeader("Origin");
        boolean allow = origin != null && SystemUtil.ALLOW_ORIGIN.contains(origin);
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (allow) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Connection, User-Agent, Authorization");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition, Authorization");
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }
        filterChain.doFilter(servletRequest, response);
    }

    @Override
    public void destroy() {

    }
}
