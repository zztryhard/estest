package es.test.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.test.base.pojo.Page;
import es.test.dao.DictStockMapper;
import es.test.dao.StocksRepository;
import es.test.pojo.StocksEntity;
import es.test.pojo.StocksVo;
import es.test.service.StocksService;

/**
 * Created by liuxuegang on 2017/5/18.
 */
@Service
@Transactional
public class StocksServiceImpl implements StocksService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(StocksServiceImpl.class);

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private DictStockMapper stockMapper;
    @Autowired
    private StocksRepository stocksRepository;
    

    private org.springframework.data.domain.Page<StocksEntity> searchFromES(
            final String keyword, final Page page)
            throws UnsupportedEncodingException {
        final Document annotation = StocksVo.class
                .getAnnotation(Document.class);
        String index = "stocks-index";
        String type = "stocks";
        if (annotation != null) {
            index = annotation.indexName();
            type = annotation.type();
        }
        final Integer current = page.getIndex() == 0 ? 0
                : page.getIndex() - 1;
        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(index)
                .withTypes(type)
                .withQuery(
                        QueryBuilders.queryStringQuery(URLDecoder.decode(
                                keyword, "UTF-8")))
                .withPageable(new PageRequest(current, page.getSize()))
                .build();
        return this.elasticsearchTemplate.queryForPage(searchQuery,
                StocksEntity.class);
    }

    public void search(final String keyword, final Page<StocksEntity> page)
            throws UnsupportedEncodingException {
        final org.springframework.data.domain.Page<StocksEntity> stocks = searchFromES(
                keyword, page);
        page.setResults(stocks.getContent());
        page.setIndex(stocks.getNumber() + 1);
        page.setCount((int) stocks.getTotalElements());
        page.setPageCount(stocks.getTotalPages());
    }

    public Integer refreshStocks() {
        final List<StocksVo> stocksVos = this.stockMapper.findAll();
        // this.stocksRepository.delete(stocksVos);
        this.stocksRepository.deleteAll();
        //this.stocksRepository.save(stocksVos);
        stocksRepository.saveAll(stocksVos);
        return stocksVos.size();
    }

    
}
