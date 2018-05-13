package es.test.base.utils;

import org.hashids.Hashids;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/6
 */
public class HashidsHelper {

    private static final Hashids HASHIDS = new Hashids("this is my salt", 18);

    public static String encode(final long numbers) {
        return HASHIDS.encode(numbers);
    }

    public static long decode(final String hash) {
        final long[] numbers = HASHIDS.decode(hash);
        if (numbers.length < 1) {
            throw new IllegalStateException("decode id error!");
        }
        return numbers[0];
    }
}
