package com.github.automain.util.xml;

import com.github.automain.util.DateUtil;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

public class DateAdapter extends XmlAdapter<String,Date> {
    @Override
    public Date unmarshal(String v) throws Exception {
        return DateUtil.convertStringToDate(v,DateUtil.SIMPLE_DATE_TIME_PATTERN);
    }

    @Override
    public String marshal(Date v) throws Exception {
        return DateUtil.convertDateToString(v,DateUtil.SIMPLE_DATE_TIME_PATTERN);
    }
}
