package es.test.base;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * Created by mty02 on 2017/5/3.
 */
public interface BaseSearchRepository<E, ID extends Serializable>
    extends ElasticsearchRepository<E, ID>, PagingAndSortingRepository<E, ID> {
}
