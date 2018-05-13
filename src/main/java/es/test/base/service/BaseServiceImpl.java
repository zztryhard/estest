/*package es.test.base.service;

import com.mutongyun.investor_education_wechat.base.pojo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;

*//**
 * @author 旺旺小学酥
 * @Time 2017/12/6
 *//*
public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

    @Autowired
    protected MongoRepository<T, ID> repository;

    @Override
    public Page<T> findPage(final Page<T> page) {
        page.setCount(this.repository.count());
        final org.springframework.data.domain.Page<T> mpage = this.repository.findAll(
            new PageRequest(page.getIndex(), page.getSize()));
        page.setResults(mpage.getContent());
        return page;
    }

    @Override
    public Page<T> findPage(final Page<T> page, final T t) {
        page.setCount(this.repository.count());
        final org.springframework.data.domain.Page<T> mpage = this.repository.findAll(
            new PageRequest(page.getIndex(), page.getSize()));
        page.setResults(mpage.getContent());
        return page;
    }
}
*/