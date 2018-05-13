package es.test.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * Created by liuxuegang on 2017/5/18.
 */
@Document(indexName = "stocks-index")
public class StocksEntity implements Serializable {

    @Id
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
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getExchange() {
        return this.exchange;
    }

    public void setExchange(final String exchange) {
        this.exchange = exchange;
    }
}
