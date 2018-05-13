package es.test.base.datasource.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import es.test.base.datasource.DataSource;
import es.test.base.datasource.DataSourceContextHolder;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/9
 */
@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Pointcut("@annotation(com.mutongyun.investor_education_wechat.base.datasource.DataSource)")
    public void pointcut() {
        System.out.println("pointcut");
    }

    @Before("pointcut()")
    public void beforeSwitchDataSource(final JoinPoint point) {

        //获得当前访问的class
        final Class<?> className = point.getTarget().getClass();

        //获得访问的方法名
        final String methodName = point.getSignature().getName();
        //得到方法的参数的类型
        final Class[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
        String dataSource = DataSourceContextHolder.DEFAULT_DATA_SOURCE;
        try {
            // 得到访问的方法对象
            final Method method = className.getMethod(methodName, argClass);

            // 判断是否存在@DataSource注解
            if (method.isAnnotationPresent(DataSource.class)) {
                final DataSource annotation = method.getAnnotation(DataSource.class);
                // 取出注解中的数据源名
                dataSource = annotation.value();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        // 切换数据源
        DataSourceContextHolder.setDataSource(dataSource);
    }

    @After("pointcut()")
    public void afterSwitchDataSource(final JoinPoint point) {

        DataSourceContextHolder.clearDataSource();

    }
}
