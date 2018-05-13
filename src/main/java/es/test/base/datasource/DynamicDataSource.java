package es.test.base.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/9
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        LOGGER.debug("数据源为:{}", DataSourceContextHolder.getDataSource());

        return DataSourceContextHolder.getDataSource();
    }
}
