package es.test.base.mybatis.plugins;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import es.test.base.utils.ReflectionUtil;

/**
 * @author 旺旺小学酥
 * @Time 2017/9/22
 */

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class UniqueKeyAutoReplenishInterceptor implements Interceptor, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueKeyAutoReplenishInterceptor.class);
    private static final String INSERT = "insert";
    private static final String KEY = "id";
    private UniqueKeyGenerator uniqueKey;

    public Object intercept(final Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        if (!(args[0] instanceof MappedStatement)) {
            return invocation.proceed();
        }
        final MappedStatement mappedStatement = (MappedStatement) args[0];
        if (mappedStatement.getSqlCommandType() != SqlCommandType.INSERT && !StringUtils.right(mappedStatement.getId(),
            INSERT.length()).equalsIgnoreCase(INSERT)) {
            return invocation.proceed();
        }
        final List<Object> objects = Lists.newArrayList();
        if (args[1] instanceof Map) {
            final Map param = (Map) args[1];
            final Object key = param.keySet().toArray()[0];
            if (param.get(key) instanceof Collection) {
                objects.addAll((Collection<?>) param.get(key));
            } else {
                objects.add(param.get(key));
            }
        } else {
            objects.add(args[1]);
        }
        for (final Object object : objects) {
            if (ReflectionUtil.getFieldValue(object, KEY) == null) {
                ReflectionUtil.setFieldValue(object, KEY, this.uniqueKey.getId());
            }
        }
        return invocation.proceed();
    }

    public Object plugin(final Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(final Properties properties) {
    }

    public void setUniqueKey(final UniqueKeyGenerator uniqueKey) {
        this.uniqueKey = uniqueKey;
    }
}

