package com.github.automain.util.xml;

import com.github.automain.util.PropertiesUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLUtil {

    /**
     * 对象转成XML
     *
     * @param o
     * @return
     */
    public static String convertObjectToXML(Object o) throws Exception {
        return convertObjectToXML(o, PropertiesUtil.DEFAULT_CHARSET);
    }

    /**
     * 对象转成XML
     *
     * @param o
     * @param encoding
     * @return
     */
    public static String convertObjectToXML(Object o, String encoding) throws Exception {
        StringWriter writer = null;
        CDATAXMLStreamWriter cdataStreamWriter = null;
        try {
            JAXBContext context = JAXBContext.newInstance(o.getClass());
            writer = new StringWriter();

            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter streamWriter = xof.createXMLStreamWriter(writer);
            cdataStreamWriter = new CDATAXMLStreamWriter(streamWriter);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            marshaller.marshal(o, cdataStreamWriter);
            cdataStreamWriter.flush();
            writer.flush();
            return writer.toString();
        } finally {
            if (cdataStreamWriter != null){
                cdataStreamWriter.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * XML转成对象
     *
     * @param xml
     * @param c
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertXMLToJavaBean(String xml, Class<T> c) throws Exception {
        try (StringReader reader = new StringReader(xml)) {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(reader);
        }
    }
}
