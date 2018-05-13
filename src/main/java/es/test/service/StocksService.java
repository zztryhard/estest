package es.test.service;

import java.io.UnsupportedEncodingException;




import es.test.base.pojo.Page;
import es.test.pojo.StocksEntity;

/**
 * Created by liuxuegang on 2017/5/18.
 */
public interface StocksService {

    void search(final String keyword, final Page<StocksEntity> page) throws UnsupportedEncodingException;
    Integer refreshStocks();
    
  
}
