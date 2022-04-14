package com.bank.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public String getDate(long t) {
        return format.format(new Date(t));
    }

}
