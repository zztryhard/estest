package es.test.base.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import es.test.base.databind.HashidsDeserializer;
import es.test.base.databind.HashidsSerializer;
import es.test.base.utils.HashidsHelper;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/6
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseEntitySerializeCode<T> extends BaseEntity<T> {

    /**
     * 序列化成 cid
     *
     * @return id
     */
    @Override
    @JsonProperty("cid")
    @JsonSerialize(using = HashidsSerializer.class)
    public abstract Long getId();

    /**
     * cid 反序列化成id
     *
     * @param id id
     */
    @Override
    @JsonProperty("cid")
    @JsonDeserialize(using = HashidsDeserializer.class)
    public abstract void setId(Long id);

    public void setCid(final String cid) {
        this.setId(HashidsHelper.decode(cid));
    }
}
