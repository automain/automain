package com.github.automain.util.wapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class JspPrintWriter extends PrintWriter {

    private ByteArrayOutputStream byteArrayOutputStream;

    public JspPrintWriter(ByteArrayOutputStream out) {
        super(out);
        this.byteArrayOutputStream = out;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }
}
