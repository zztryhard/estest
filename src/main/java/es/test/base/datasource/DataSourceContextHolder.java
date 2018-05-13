package es.test.base.datasource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/9
 */
public class DataSourceContextHolder {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataSourceContextHolder.class);

    /**
     * 默认数据源
     */
    public static final String DEFAULT_DATA_SOURCE = "primary";

    private static final ThreadLocal<String> DATA_SOURCE_HOLDER = new ThreadLocal<String>();

    /**
     * 设置数据源名
     *
     * @param dbType
     */
    public static void setDataSource(final String dbType) {
        LOGGER.debug("切换到数据源:{}", dbType);
        DATA_SOURCE_HOLDER.set(dbType);
    }

    /**
     * 获取数据源名
     *
     * @return
     */
    public static String getDataSource() {
        final String dataSource = DATA_SOURCE_HOLDER.get();
        if (StringUtils.isNotBlank(dataSource)) {
            return dataSource;
        }
        return DEFAULT_DATA_SOURCE;
    }

    /**
     * 清除数据源名
     */
    public static void clearDataSource() {
        DATA_SOURCE_HOLDER.remove();
    }
}
