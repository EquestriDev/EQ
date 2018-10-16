package com.equestriworlds.util;

import com.equestriworlds.util.UtilMath;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Time difference and format utility.
 */
public class UtilTime {
    public static final String DATE_FORMAT_NOW = "MM-dd-yyyy HH:mm:ss";
    public static final String DATE_FORMAT_DAY = "MM-dd-yyyy";

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    public static String when(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(time);
    }

    public static String date() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DAY);
        return sdf.format(cal.getTime());
    }

    public static String since(long epoch) {
        return "Took " + UtilTime.convertString(System.currentTimeMillis() - epoch, 1, TimeUnit.FIT) + ".";
    }

    public static double convert(long time, int trim, TimeUnit type) {
        if (type == TimeUnit.FIT) {
            type = time < 60000L ? TimeUnit.SECONDS : (time < 3600000L ? TimeUnit.MINUTES : (time < 86400000L ? TimeUnit.HOURS : TimeUnit.DAYS));
        }
        if (type == TimeUnit.DAYS) {
            return UtilMath.trim(trim, (double)time / 8.64E7);
        }
        if (type == TimeUnit.HOURS) {
            return UtilMath.trim(trim, (double)time / 3600000.0);
        }
        if (type == TimeUnit.MINUTES) {
            return UtilMath.trim(trim, (double)time / 60000.0);
        }
        if (type == TimeUnit.SECONDS) {
            return UtilMath.trim(trim, (double)time / 1000.0);
        }
        return UtilMath.trim(trim, time);
    }

    public static String MakeStr(long time) {
        return UtilTime.convertString(time, 1, TimeUnit.FIT);
    }

    public static String MakeStr(long time, int trim) {
        return UtilTime.convertString(Math.max(0L, time), trim, TimeUnit.FIT);
    }

    public static String convertString(long time, int trim, TimeUnit type) {
        double num;
        String text;
        if (time == -1L) {
            return "Permanent";
        }
        if (type == TimeUnit.FIT) {
            type = time < 60000L ? TimeUnit.SECONDS : (time < 3600000L ? TimeUnit.MINUTES : (time < 86400000L ? TimeUnit.HOURS : TimeUnit.DAYS));
        }
        if (trim == 0) {
            if (type == TimeUnit.DAYS) {
                num = (int)UtilMath.trim(trim, (double)time / 8.64E7);
                text = (int)num + " Day";
            } else if (type == TimeUnit.HOURS) {
                num = (int)UtilMath.trim(trim, (double)time / 3600000.0);
                text = (int)num + " Hour";
            } else if (type == TimeUnit.MINUTES) {
                num = (int)UtilMath.trim(trim, (double)time / 60000.0);
                text = (int)num + " Minute";
            } else if (type == TimeUnit.SECONDS) {
                num = (int)UtilMath.trim(trim, (double)time / 1000.0);
                text = (int)num + " Second";
            } else {
                num = (int)UtilMath.trim(trim, time);
                text = (int)num + " Millisecond";
            }
        } else if (type == TimeUnit.DAYS) {
            num = UtilMath.trim(trim, (double)time / 8.64E7);
            text = num + " Day";
        } else if (type == TimeUnit.HOURS) {
            num = UtilMath.trim(trim, (double)time / 3600000.0);
            text = num + " Hour";
        } else if (type == TimeUnit.MINUTES) {
            num = UtilMath.trim(trim, (double)time / 60000.0);
            text = num + " Minute";
        } else if (type == TimeUnit.SECONDS) {
            num = UtilMath.trim(trim, (double)time / 1000.0);
            text = num + " Second";
        } else {
            num = (int)UtilMath.trim(0, time);
            text = (int)num + " Millisecond";
        }
        if (num != 1.0) {
            text = text + "s";
        }
        return text;
    }

    public static boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    public static long getSeconds(int seconds) {
        return 1000 * seconds;
    }

    public static long getMinutes(int minutes) {
        return UtilTime.getSeconds(1) * 60L * (long)minutes;
    }

    public static long getHours(int hours) {
        return UtilTime.getMinutes(1) * 60L * (long)hours;
    }

    public static long getDays(int days) {
        return UtilTime.getHours(1) * 24L * (long)days;
    }

    public static String formatDateDiff(long date, boolean future) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        GregorianCalendar now = new GregorianCalendar();
        return UtilTime.formatDateDiff(now, c, future);
    }

    public static String formatDateDiff(Calendar fromDate, Calendar toDate, boolean future) {
        if (toDate.equals(fromDate)) {
            return "now";
        }
        if (toDate.after(fromDate)) {
            future = true;
        }
        StringBuilder sb = new StringBuilder();
        int[] types = new int[]{1, 2, 5, 11, 12, 13};
        String[] names = new String[]{"year", "years", "month", "months", "day", "days", "hour", "hours", "minute", "minutes", "second", "seconds"};
        int accuracy = 0;
        for (int i = 0; i < types.length && accuracy <= 1; ++i) {
            int diff = UtilTime.dateDiff(types[i], fromDate, toDate, future);
            if (diff <= 0) continue;
            ++accuracy;
            sb.append(" ").append(diff).append(" ").append(names[i * 2 + (diff > 1 ? 1 : 0)]);
        }
        if (sb.length() == 0) {
            return "now";
        }
        return sb.toString().trim();
    }

    static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while (future && !fromDate.after(toDate) || !future && !fromDate.before(toDate)) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            ++diff;
        }
        fromDate.setTimeInMillis(savedDate);
        return --diff;
    }

    public static enum TimeUnit {
        FIT,
        DAYS,
        HOURS,
        MINUTES,
        SECONDS,
        MILLISECONDS;
        

        private TimeUnit() {
        }
    }

}
