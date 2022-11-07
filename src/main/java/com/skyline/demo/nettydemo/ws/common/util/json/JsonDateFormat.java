package com.skyline.demo.nettydemo.ws.common.util.json;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式化基于 joda-time
 *
 * @author : <a href="mailto:zuiwoxing@qq.com">heigong</a>
 * @version : Ver 1.0
 * @date : 2014-12-11 上午10:43:50
 */
public class JsonDateFormat extends DateFormat {

    private static final long serialVersionUID = -3121650569779268081L;

    private final static Calendar calendar = Calendar.getInstance();

    private final static NumberFormat numFormat = NumberFormat.getInstance();

    private String pattern;

    public JsonDateFormat(String pattern) {
        super.setCalendar(calendar);
        super.setNumberFormat(numFormat);
        this.pattern = pattern;
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo,
                               FieldPosition fieldPosition) {
        toAppendTo.append(new DateTime(date.getTime()).toString(pattern));
        return toAppendTo;
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        return DateTimeFormat.forPattern(pattern).parseDateTime(source)
                .toDate();
    }

    @Override
    public Date parse(String source) throws ParseException {
        return parse(source, null);
    }


//	@Override
//	public Object clone() {
//		return super.clone();
//	}

}
