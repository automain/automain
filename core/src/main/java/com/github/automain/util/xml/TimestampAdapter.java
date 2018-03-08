package com.github.automain.util.xml;

import com.github.automain.util.DateUtil;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Timestamp;

public class TimestampAdapter extends XmlAdapter<String,Timestamp> {
    @Override
    public Timestamp unmarshal(String v) throws Exception {
        return DateUtil.convertStringToTimestamp(v,DateUtil.SIMPLE_DATE_TIME_PATTERN);
    }

    @Override
    public String marshal(Timestamp v) throws Exception {
        return DateUtil.convertDateToString(v,DateUtil.SIMPLE_DATE_TIME_PATTERN);
    }
}
