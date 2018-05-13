package es.test.pojo;

import java.io.Serializable;

import es.test.base.pojo.BaseEntity;

public class DictStock implements Serializable{
    private Long id;

    private String code;

    private String name;

    private String exchange;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getExchange() {
        return this.exchange;
    }

    public void setExchange(final String exchange) {
        this.exchange = exchange == null ? null : exchange.trim();
    }
}