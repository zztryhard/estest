package es.test.base.datasource;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/9
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Component
@Documented
public @interface DataSource {
    String value() default DataSourceContextHolder.DEFAULT_DATA_SOURCE;

}
