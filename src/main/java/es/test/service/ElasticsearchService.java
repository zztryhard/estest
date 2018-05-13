package es.test.service;

import java.io.IOException;

/**
 * Created by liuxuegang on 2017/5/18.
 */
public interface ElasticsearchService {
    Integer refreshUser();

    /*@Async
    void updateUser(UserESVo user);*/

    Integer refreshArticle() throws IOException;

    /*@Async
    void updateArticle(ArticleESVo article) throws IOException;*/

    Integer refreshFormula();

    /*@Async
    void updateFormula(FormulaESVo formula);

    List<AutoCompleteVo> autoCompleteFormula(String keyword);*/
}
