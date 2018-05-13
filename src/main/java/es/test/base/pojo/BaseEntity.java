package es.test.base.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/6
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseEntity<T> implements Serializable {

    /**
     * 为删除
     */
    public static final Boolean NOT_DELETED = false;
    /**
     * 已删除
     */
    public static final Boolean DELETED = true;

    public BaseEntity() {
    }

    public BaseEntity(final Long id) {
        this();
        this.setId(id);
    }

    /**
     * get id
     *
     * @return
     */
    public abstract Long getId();

    /**
     * set id
     *
     * @param id id
     */
    public abstract void setId(Long id);

    /**
     * get deleted
     *
     * @return
     */
    public abstract Boolean getDeleted();

    /**
     * set deleted
     *
     * @param deleted deleted
     */
    public abstract void setDeleted(Boolean deleted);
}
