package es.test.dao;


import org.springframework.stereotype.Repository;

import es.test.base.BaseSearchRepository;
import es.test.pojo.StocksVo;

/**
 * Created by liuxuegang on 2017/5/18.
 */
@Repository
public interface StocksRepository extends BaseSearchRepository<StocksVo, Long> {
}
