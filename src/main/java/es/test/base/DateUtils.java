package es.test.base;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mty02 on 2017/3/14.
 */
public class DateUtils {
    public static final String YYMMDDTHMMSSZ = "yyyy-MM-ddTHH:mm:ssZ";
    public static final String YYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMDHMS = "yyyyMMddHHmmss";
    public static final String YYMMDD = "yyyy-MM-dd";
    public static final String YMD = "yyyyMMdd";
    public static final Long SECOND = Long.valueOf(1000L);
    public static final Long MINUTE = Long.valueOf(60L);
    public static final Long HOUR = Long.valueOf(24L);
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    public DateUtils() {
    }

    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    public static String nowToString() {
        return dateToString(now());
    }

    public static String dateToString(final Date dateTime, final String dateFormat) {
        if (dateTime == null) {
            return "";
        } else {
            final SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            return formatter.format(dateTime);
        }
    }

    public static String dateToString(final Date datetime) {
        return dateToString(datetime, "yyyyMMddHHmmss");
    }

    public static Date stringToDate(final String dateTime, final String dateFormat) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        } else {
            final SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

            try {
                return formatter.parse(dateTime);
            } catch (final ParseException var4) {
                LOGGER.error("convert date error!", var4);
                return null;
            }
        }
    }

    public static Date stringToDate(final String dateTime) {
        return stringToDate(dateTime, "yyyyMMddHHmmss");
    }

    public static int currentYear() {
        final Calendar calendar = Calendar.getInstance();
        return calendar.get(1);
    }

    public static int currentMonth() {
        final Calendar calendar = Calendar.getInstance();
        return calendar.get(2) + 1;
    }

    public static Date yearStartTime(final int year) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(1, year);
        calendar.set(6, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTime();
    }

    public static Date yearEndTime(final int year) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(1, year + 1);
        calendar.set(6, 0);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, -1);
        return calendar.getTime();
    }

    public static Date dayStartTime(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTime();
    }

    public static Date dayEndTime(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(6, calendar.get(6) + 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, -1);
        return calendar.getTime();
    }

    public static Boolean isActiveTime(final Date startTime, final Date endTime) {
        return isActiveTime(startTime, endTime, (Date) null);
    }

    public static Boolean isActiveTime(final Date startTime, final Date endTime, Date currentTime) {
        if (currentTime == null) {
            currentTime = now();
        }

        return startTime.before(currentTime) && endTime.after(currentTime) ? Boolean.valueOf(true) : Boolean.valueOf(false);
    }

    public static Date nextMonthNow() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(2, 1);
        return calendar.getTime();
    }

    public static Date nextWeekNow(final int week) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(3, week);
        return calendar.getTime();
    }

    public static Date lateMonth() {
        return lateMonth(now());
    }

    public static Date lateMonth(final Date date) {
        if (date == null) {
            return now();
        } else {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(2, calendar.get(2) - 1);
            return calendar.getTime();
        }
    }

    public static Date lateYear() {
        return lateYear(now());
    }

    private static Date lateYear(final Date date) {
        if (date == null) {
            return now();
        } else {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(1, calendar.get(1) - 1);
            return calendar.getTime();
        }
    }

    public static Date lateDay(final int days) {
        return lateDay(now(), days);
    }

    public static Date lateDay(final Date date, final int days) {
        if (date == null) {
            return now();
        } else {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(5, calendar.get(5) - days);
            return calendar.getTime();
        }
    }

    public static String calculatorRemainTime(final Date endTime) {
        return calculatorRemainTime(now(), endTime);
    }

    public static Date DateFormat(final Date date, final String dateFormat) {
        return stringToDate(dateToString(date, dateFormat), dateFormat);
    }

    public static String calculatorRemainTime(final Date startTime, final Date endTime) {
        if (startTime == null) {
            return "";
        } else if (endTime == null) {
            return "";
        } else {
            final long remainTime = endTime.getTime() - startTime.getTime();
            if (remainTime != 0L && remainTime >= 0L) {
                final StringBuilder calculator = new StringBuilder();
                final long days = remainTime / (SECOND.longValue() * MINUTE.longValue() * MINUTE.longValue() * HOUR.longValue());
                if (days > 0L) {
                    calculator.append(days).append("天");
                }

                final long hours = remainTime % (SECOND.longValue() * MINUTE.longValue() * MINUTE.longValue() * HOUR.longValue()) / (SECOND.longValue() * MINUTE.longValue() * MINUTE.longValue());
                if (hours > 0L) {
                    calculator.append(hours).append("时");
                }

                final long minutes = remainTime % (SECOND.longValue() * MINUTE.longValue() * MINUTE.longValue()) / (SECOND.longValue() * MINUTE.longValue());
                if (minutes > 0L) {
                    calculator.append(minutes).append("分");
                }

                return StringUtils.isBlank(calculator.toString()) ? "0分" : calculator.toString();
            } else {
                return "0分";
            }
        }
    }
}
