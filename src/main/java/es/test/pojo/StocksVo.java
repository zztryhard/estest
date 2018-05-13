package es.test.pojo;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuxuegang on 2017/5/18.
 */
@Document(indexName = "stocks-index-pinyin", type = "stocks-pinyin", shards = 1, replicas = 0)
public class StocksVo implements Serializable {

    @Id
    private Long id;

    @Field(type = FieldType.keyword, store = true, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word")
    private String code;

    @Field(type = FieldType.keyword, store = true, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.keyword, store = true, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word")
    private String exchange;

    @Field(type = FieldType.keyword, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word")
    private String key;

    @Field(type = FieldType.keyword, store = true, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word")
    private String stockCode;
    @Field(type = FieldType.Double)
    private Double sort = 0D;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code == null ? null : this.code.trim();
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return this.name == null ? null : this.name.trim();
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getExchange() {
        return this.exchange == null ? null : this.exchange.trim();
    }

    public void setExchange(final String exchange) {
        this.exchange = exchange;
    }

    public String getKey() {
        if (StringUtils.isBlank(this.key)) {
            final List<String> keys = Lists.newArrayList();
            for (int i = 0; i < this.code.length(); i++) {
                keys.add(this.code.substring(0, i));
            }
            final String key = StringUtils.join(keys, " ");
            this.key = key;
            return key;
        }
        return this.key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getStockCode() {
        return StringUtils.isBlank(this.stockCode) ? this.exchange + this.code : this.stockCode;
    }

    public void setStockCode(final String stockCode) {
        this.stockCode = stockCode;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(final Double sort) {
        this.sort = sort;
    }
}
