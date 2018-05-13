package es.test.base.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/5
 */
@Data
public class Page<T> implements Serializable {
    private List<T> results;
    private int size = 20;
    private int index = 1;
    private long count;
    @JsonIgnore
    private long begin;
    private int pageCount;

    public long getBegin() {
        final long begin = this.size * (this.index - 1);
        this.begin = begin < 0 ? 0 : begin;
        return this.begin;
    }
    public int getPageCount(){
        if (this.count == 0) {
            this.pageCount = 1;
        } else {
            this.pageCount = (int) (this.count / this.size);
            this.pageCount += this.count % this.size == 0 ? 0 : 1;
            if (this.pageCount == 0) {
                this.pageCount = 1;
            }
        }
        return this.pageCount;
    }
    
}
