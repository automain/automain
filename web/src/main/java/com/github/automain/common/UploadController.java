package com.github.automain.common;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 最大上传10M文件
 */
@MultipartConfig(maxFileSize = 10485760)
@WebServlet(urlPatterns = "/upload", asyncSupported = true, loadOnStartup = 1)
public class UploadController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Boolean filtered = (Boolean) req.getAttribute("resourceFiltered");
        if (filtered == null || !filtered) {
            BaseExecutor executor = new UploadExecutor();
            final AsyncContext asyncContext = req.startAsync(req, resp);
            asyncContext.setTimeout(10000L);
            executor.setAsyncContext(asyncContext);
            asyncContext.start(executor);
        }
    }
}
