package com.github.automain.util.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.regex.Pattern;

public class CDATAXMLStreamWriter extends DelegatingXMLStreamWriter {

    private static final Pattern XML_CHARS = Pattern.compile("[&<>]");

    public CDATAXMLStreamWriter(XMLStreamWriter writer) {
        super(writer);
    }

    @Override
    public void writeCharacters(String text) throws XMLStreamException {
        boolean useCData = XML_CHARS.matcher(text).find();
        if (useCData) {
            super.writeCData(text);
        } else {
            super.writeCharacters(text);
        }
    }
}
