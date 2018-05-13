package es.test.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Random;

/**
 * 生成一个18位订单号
 * 日期6位，商品类型2位，当天毫秒数+自增数(二进制10位)
 *
 * @author 旺旺小学酥
 * @Time 2017/9/22
 */
public class OrderNumberCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderNumberCreator.class);

    private static long sequence;
    private static long lastTimestamp = -1L;
    private static long lastDay = -1L;
    private static final long sequenceMask = 4095L;

    synchronized public static String next(final int type) {
        long timestamp = timeGen();
        final long today = LocalDate.now().getLong(ChronoField.DAY_OF_YEAR);
        if (OrderNumberCreator.lastDay != today) {
            LOGGER.info("新一天的第一笔订单");
            OrderNumberCreator.lastDay = today;
            OrderNumberCreator.lastTimestamp = -1L;
        }
        if (timestamp < OrderNumberCreator.lastTimestamp) {
            throw new IllegalStateException(
                String.format("时间早于最低时间:%d【%d】", OrderNumberCreator.lastTimestamp, timestamp));
        } else {
            if (OrderNumberCreator.lastTimestamp == timestamp) {
                OrderNumberCreator.sequence = OrderNumberCreator.sequence + 1L & OrderNumberCreator.sequenceMask;
                if (OrderNumberCreator.sequence == 0L) {
                    timestamp = tilNextNano(OrderNumberCreator.lastTimestamp);
                }
            } else {
                OrderNumberCreator.sequence = 0L;
                OrderNumberCreator.lastTimestamp = timestamp;
            }
            // 日期6位
            final StringBuilder number = new StringBuilder();
            final LocalDate data = LocalDate.now();
            number.append(data.format(DateTimeFormatter.ofPattern("yyMMdd")));
            final int t = type & 99;
            if (t < 10) {
                number.append(0);
            }
            number.append(t);
            // 随机数1位+当天毫秒数+自增数(二进制12位)，共10位
            number.append((timestamp << 12) + OrderNumberCreator.sequence + 1000000000L * (new Random().nextInt(9)
                                                                                           + 1));
            return number.toString().toUpperCase();
        }
    }

    private static long tilNextNano(final long lastTimestamp) {
        long timestamp;
        for (timestamp = timeGen(); timestamp <= lastTimestamp; timestamp = timeGen()) {
        }

        return timestamp;
    }

    private static long timeGen() {
        return LocalTime.now().getLong(ChronoField.SECOND_OF_DAY);
    }

}
