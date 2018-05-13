package es.test.base.service;

import java.io.Serializable;

import es.test.base.pojo.Page;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/6
 */
public interface BaseService<T, ID extends Serializable> {

    Page<T> findPage(final Page<T> page);

    Page<T> findPage(final Page<T> page, T t);
}
