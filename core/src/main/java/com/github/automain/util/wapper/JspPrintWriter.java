package com.github.automain.util.wapper;

import com.github.automain.util.PropertiesUtil;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class JspPrintWriter extends PrintWriter {

    private ByteArrayOutputStream byteArrayOutputStream;

    public JspPrintWriter(ByteArrayOutputStream out) throws UnsupportedEncodingException {
        super(new OutputStreamWriter(out, PropertiesUtil.DEFAULT_CHARSET));
        this.byteArrayOutputStream = out;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }
}
