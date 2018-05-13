package es.test.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/6
 */
public final class HttpContextUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpContextUtils.class);

    public static String getSessionValue(final String key, final String token) {
        final HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String value = (String) request.getSession().getAttribute(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        final String sessionKey = getCookieValue(token);
        if (StringUtils.isBlank(sessionKey)) {
            return null;
        }
        final RedisTemplate<String, Object> redisTemplate =
            (RedisTemplate<String, Object>) SpringContextUtils.getBeanById("redisTemplate");
        final String sessionValue = (String) redisTemplate.opsForValue().get(sessionKey);
        if (StringUtils.isBlank(sessionValue)) {
            return null;
        }
        return sessionValue;
    }

    public static void removeSessionValue(final String key, final String token) {
        final HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute(key, null);
        request.getSession().removeAttribute(key);
        final String sessionKey = getCookieValue(token);
        if (StringUtils.isBlank(sessionKey)) {
            return;
        }
        removeCookieValue(token);
        final RedisTemplate<String, Object> redisTemplate =
            (RedisTemplate<String, Object>) SpringContextUtils.getBeanById("redisTemplate");
        redisTemplate.opsForValue().set(sessionKey, null, 1, TimeUnit.SECONDS);
    }

    public static String getCookieValue(final String key) {
        final HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            LOGGER.debug("not find cookie {}!", key);
            return null;
        }
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        LOGGER.debug("not find cookie {}!", key);
        return null;
    }

    public static void removeCookieValue(final String key) {
        final HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final HttpServletResponse response =
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        final Cookie[] cookies = request.getCookies();
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                cookie.setValue(null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
    }

    public static String getRemoteAddr() {
        final HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        final String[] ips = ip.split(",");
        if (ips.length == 1) {
            return ips[0];
        }
        for (final String i : ips) {
            if (!"unknown".equalsIgnoreCase(i)) {
                return i;
            }
        }
        return ip;
    }

    public static Integer getRemoteAddrInt() {
        final String addr = getRemoteAddr();
        Integer ip = 0;
        for (final String a : addr.split("\\.")) {
            ip = ip << 8 + Integer.parseInt(a);
        }
        return ip;
    }

}
