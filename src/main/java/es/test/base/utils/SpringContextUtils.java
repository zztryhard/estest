package es.test.base.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/6
 */
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 获取applicationContext对象
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 根据bean的id来查找对象
     *
     * @param id
     *
     * @return
     */
    public static Object getBeanById(final String id) {
        return applicationContext.getBean(id);
    }

    /**
     * 根据bean的class来查找对象
     *
     * @param c
     *
     * @return
     */
    public static Object getBeanByClass(final Class c) {
        return applicationContext.getBean(c);
    }

    /**
     * 根据bean的class来查找所有的对象(包括子类)
     *
     * @param c
     *
     * @return
     */
    public static Map getBeansByClass(final Class c) {
        return applicationContext.getBeansOfType(c);
    }

    /**
     * 清除applicationContext静态变量.
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextUtils");
        }
    }
}
