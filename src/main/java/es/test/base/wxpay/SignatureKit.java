package es.test.base.wxpay;

import com.google.common.collect.Maps;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Map;
import java.util.UUID;

/**
 * @author 旺旺小学酥
 * @Time 2018/4/4
 */
public class SignatureKit {
    public static Map<String, Object> sign(final String jsapiTicket, final String url) {
        final Map<String, Object> ret = Maps.newHashMap();
        final String nonceStr = createNonceStr();
        final String timestamp = createTimestamp();
        final String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;

        try {
            final MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapiTicket);
        ret.put("nonceStr", nonceStr);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        final Formatter formatter = new Formatter();
        for (final byte b : hash) {
            formatter.format("%02x", b);
        }
        final String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
