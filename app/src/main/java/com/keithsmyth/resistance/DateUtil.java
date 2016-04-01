package com.keithsmyth.resistance;

import java.util.Calendar;

public class DateUtil {

    public static boolean isAprilFools() {
        final Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) == Calendar.APRIL && c.get(Calendar.DAY_OF_MONTH) == 1;
    }
}
