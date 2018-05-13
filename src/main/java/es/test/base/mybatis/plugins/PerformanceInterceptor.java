package es.test.base.mybatis.plugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author 旺旺小学酥
 * @Time 2017/10/31
 */
@Intercepts({@Signature(type = Executor.class, method = "query",
    args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
             @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class PerformanceInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceInterceptor.class);
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String RDS_CHAR_DOLLAR = "RDS_CHAR_DOLLAR";

    public Object intercept(final Invocation invocation) throws Throwable {
        final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameterObject = null;
        if (invocation.getArgs().length > 1) {
            parameterObject = invocation.getArgs()[1];
        }

        final String statementId = mappedStatement.getId();
        final BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        final Configuration configuration = mappedStatement.getConfiguration();
        final String sql = getSql(boundSql, parameterObject, configuration);

        final long start = System.currentTimeMillis();

        try {
            final Object result = invocation.proceed();

            final long end = System.currentTimeMillis();
            final long timing = end - start;
            LOGGER.info("Elapsed time: " + timing + " ms - " + statementId + " - sql:" + sql);
            return result;
        } catch (final Throwable e) {
            LOGGER.error("Execute SQL ERROR: - sql:{}", sql);
            throw e;
        }
    }

    public Object plugin(final Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    public void setProperties(final Properties properties) {
    }

    private String getSql(final BoundSql boundSql, final Object parameterObject, final Configuration configuration) {
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ").replaceAll("\\$", RDS_CHAR_DOLLAR);
        final List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        final TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                final ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    final Object value;
                    final String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        final MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    sql = replacePlaceholder(sql, value);
                }
            }
        }
        return sql.replaceAll(RDS_CHAR_DOLLAR, "\\$");
    }

    private String replacePlaceholder(final String sql, final Object propertyValue) {
        final String result;
        if (propertyValue != null) {
            if (propertyValue instanceof String) {
                result = "'" + propertyValue + "'";
            } else if (propertyValue instanceof Date) {
                result = "'" + DATE_FORMAT.format(propertyValue) + "'";
            } else {
                result = propertyValue.toString();
            }
        } else {
            result = "null";
        }
        return sql.replaceFirst("\\?", result);
    }
}
