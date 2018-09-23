package com.github.automain.util.wapper;

import com.github.automain.util.PropertiesUtil;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class JspResponseWrapper extends HttpServletResponseWrapper {

    private JspPrintWriter printWriter;
    private ByteArrayOutputStream bos;

    public JspResponseWrapper(HttpServletResponse response) throws UnsupportedEncodingException {
        super(response);
        this.bos = new ByteArrayOutputStream();
        this.printWriter = new JspPrintWriter(bos);
    }

    public String getContent() throws IOException {
        try {
            this.printWriter.flush();
            return new String(this.printWriter.getByteArrayOutputStream().toByteArray(), PropertiesUtil.DEFAULT_CHARSET);
        } finally {
            if (this.bos != null) {
                this.bos.close();
            }
            if (this.printWriter != null) {
                this.printWriter.close();
            }
        }
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return this.printWriter;
    }

}
