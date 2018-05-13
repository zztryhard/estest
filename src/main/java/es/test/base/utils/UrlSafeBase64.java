package es.test.base.utils;

import es.test.base.common.Constants;


/**
 * @author 旺旺小学酥
 * @Time 2017/12/6
 */
public final class UrlSafeBase64 {

    private UrlSafeBase64() {
    }   // don't instantiate

    /**
     * 编码字符串
     *
     * @param data 待编码字符串
     *
     * @return 结果字符串
     */
    public static String encodeToString(final String data) {
        return encodeToString(data.getBytes(Constants.UTF_8));
    }

    /**
     * 编码数据
     *
     * @param data 字节数组
     *
     * @return 结果字符串
     */
    public static String encodeToString(final byte[] data) {
        return Base64.encodeToString(data, Base64.URL_SAFE | Base64.NO_WRAP);
    }

    /**
     * 解码数据
     *
     * @param data 编码过的字符串
     *
     * @return 原始数据
     */
    public static byte[] decode(final String data) {
        return Base64.decode(data, Base64.URL_SAFE | Base64.NO_WRAP);
    }
}
