package es.test.base.mybatis.plugins;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import es.test.base.pojo.Page;
import es.test.base.utils.ReflectionUtil;

/**
 * Created by mty02 on 2017/3/14.
 */

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageHandlerInterceptor implements Interceptor, Serializable {
    private static final long serialVersionUID = 1L;
    private static final String PAGE = "page";
    private static final Pattern FROM_PATTERN = Pattern.compile("[Ff][Rr][Oo][Mm]");

    public PageHandlerInterceptor() {
    }

    public Object intercept(final Invocation invocation) throws Throwable {
        if (!(invocation.getTarget() instanceof RoutingStatementHandler)) {
            return invocation.proceed();
        } else {
            final RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
            final StatementHandler delegate = (StatementHandler) ReflectionUtil.getFieldValue(statementHandler,
                "delegate");
            if (delegate == null) {
                return invocation.proceed();
            } else {
                final MappedStatement mappedStatement = (MappedStatement) ReflectionUtil.getFieldValue(delegate,
                    "mappedStatement");
                if (mappedStatement == null) {
                    return invocation.proceed();
                } else if (!StringUtils.containsIgnoreCase(mappedStatement.getId(), PAGE)) {
                    return invocation.proceed();
                } else {
                    final BoundSql boundSql = delegate.getBoundSql();
                    if (StringUtils.isBlank(boundSql.getSql())) {
                        return invocation.proceed();
                    } else {
                        final Page<Object> page = this.findConvertPage(boundSql.getParameterObject());
                        if (page == null) {
                            return invocation.proceed();
                        } else {
                            page.setCount(
                                this.countRecord(mappedStatement, boundSql, (Connection) invocation.getArgs()[0]));
                            ReflectionUtil.setFieldValue(boundSql, "sql", this.formatSql(page, boundSql));
                            return invocation.proceed();
                        }
                    }
                }
            }
        }
    }

    public Object plugin(final Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(final Properties properties) {
    }

    private String formatSql(final Page<Object> page, final BoundSql boundSql) {
        final StringBuilder sql = new StringBuilder(boundSql.getSql());
        sql.append(" limit ");
        if (page.getBegin() > 0) {
            sql.append(page.getBegin()).append(",").append(page.getSize());
            return sql.toString();
        } else {
            sql.append(page.getSize());
            return sql.toString();
        }
    }

    private int countRecord(final MappedStatement mappedStatement, final BoundSql boundSql, final Connection connect)
        throws SQLException {
        PreparedStatement prepareStatement = null;
        ResultSet resultSet = null;
        int result = 0;
        try {
            final String countSql = this.generateCountSql(boundSql.getSql());
            prepareStatement = connect.prepareStatement(countSql);
            if (prepareStatement == null) {
                throw new SQLException("not find Statement!");
            }
            final BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql,
                boundSql.getParameterMappings(), boundSql.getParameterObject());
            final ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement,
                boundSql.getParameterObject(), countBoundSql);
            parameterHandler.setParameters(prepareStatement);
            resultSet = prepareStatement.executeQuery();
            if (!resultSet.next()) {
                return result;
            }
            result = resultSet.getInt(1);
        } catch (final Exception var13) {
            throw new SQLException("find data count faild!", var13);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (prepareStatement != null) {
                prepareStatement.close();
            }
        }
        return result;
    }

    private String generateCountSql(final String querySql) {
        final StringBuffer sb = new StringBuffer("select count(*) ");
        final Matcher matcher = FROM_PATTERN.matcher(querySql);
        if (matcher.find()) {
            sb.append(querySql.substring(matcher.start()));
            return sb.toString();
        } else {
            return querySql;
        }
    }

    private Page<Object> findConvertPage(final Object object) {
        if (object instanceof Page) {
            return (Page<Object>) object;
        } else if (!(object instanceof Map)) {
            return null;
        } else {
            final Map parameters = (Map) object;
            if (parameters.isEmpty()) {
                return null;
            } else {
                for (final Object entry : parameters.entrySet()) {
                    if (!(entry instanceof Map.Entry)) {
                        return null;
                    }
                    final Map.Entry mapEntry = (Map.Entry) entry;
                    if (mapEntry.getValue() instanceof Page) {
                        return (Page<Object>) mapEntry.getValue();
                    }
                }
                return null;
            }
        }
    }
}
