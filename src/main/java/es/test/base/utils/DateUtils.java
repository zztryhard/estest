package es.test.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/6
 */
public class DateUtils {
    public static final String YYMMDDTHMMSSZ = "yyyy-MM-ddTHH:mm:ssZ";
    public static final String YYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMDHMS = "yyyyMMddHHmmss";
    public static final String YYMMDD = "yyyy-MM-dd";
    public static final String YMD = "yyyyMMdd";
    public static final Long SECOND = 1000L;
    public static final Long MINUTE = 60L;
    public static final Long HOUR = 24L;
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
        return calendar.get(Calendar.YEAR);
    }

    public static int currentMonth() {
        final Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static Date yearStartTime(final int year) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date yearEndTime(final int year) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year + 1);
        calendar.set(Calendar.DAY_OF_YEAR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, -1);
        return calendar.getTime();
    }

    public static Date dayStartTime(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date dayEndTime(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, -1);
        return calendar.getTime();
    }

    public static Boolean isActiveTime(final Date startTime, final Date endTime) {
        return isActiveTime(startTime, endTime, null);
    }

    public static Boolean isActiveTime(final Date startTime, final Date endTime, Date currentTime) {
        if (currentTime == null) {
            currentTime = now();
        }

        return startTime.before(currentTime) && endTime.after(currentTime) ? Boolean.valueOf(true) : Boolean.valueOf(
            false);
    }

    public static Date nextMonthNow() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    public static Date nextWeekNow(final int week) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, week);
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
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
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
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
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
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - days);
            return calendar.getTime();
        }
    }

    public static String calculatorRemainTime(final Date endTime) {
        return calculatorRemainTime(now(), endTime);
    }

    public static Date dateFormat(final Date date, final String dateFormat) {
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
                final long days = remainTime / (SECOND * MINUTE * MINUTE * HOUR);
                if (days > 0L) {
                    calculator.append(days).append("天");
                }

                final long hours = remainTime % (SECOND * MINUTE * MINUTE * HOUR) / (SECOND * MINUTE * MINUTE);
                if (hours > 0L) {
                    calculator.append(hours).append("时");
                }

                final long minutes = remainTime % (SECOND * MINUTE * MINUTE) / (SECOND * MINUTE);
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
