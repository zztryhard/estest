package es.test.base.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 旺旺小学酥
 * @Time 2017/6/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Component
@Target(ElementType.TYPE)
@Documented
public @interface MyBatisDao {
    String value() default "";
}