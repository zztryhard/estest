package es.test.dao;

import java.util.List;

import es.test.base.annotation.MyBatisDao;
import es.test.pojo.DictStock;
import es.test.pojo.StocksVo;

@MyBatisDao
public interface DictStockMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DictStock record);

    int insertSelective(DictStock record);

    DictStock selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictStock record);

    int updateByPrimaryKey(DictStock record);

    //List<StockVo> findPageStock(@Param("page") Page<StockVo> page, @Param("stock") StockVo stock);

    //@Cache(expire = 3600, key = "'dictStock.findAll'")
    List<StocksVo> findAll();

    //@Cache(expire = 3600, key = "'dictStock.findTreeNode'")
    //List<StockTreeVo> findTreeNode();

    //@Cache(expire = 3600, key = "'dictStock.findStockName'+#args[0]+#args[1]")
    //DictStock findStockName(@Param("symbol") String symbol, @Param("market") String market);
}