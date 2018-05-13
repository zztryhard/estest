package es.test.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.test.base.AbstractCustomController;
import es.test.base.AjaxResultVo;
import es.test.service.ElasticsearchService;
import es.test.service.StocksService;

/**
 * Created by liuxuegang on 2017/5/17.
 */
@Controller
@RequestMapping
public class ElasticsearchController extends AbstractCustomController {

    @Autowired
    private StocksService stocksService;
    @Autowired
    private ElasticsearchService elasticsearchService;

    @ResponseBody
    @RequestMapping(value = "/refresh_stocks", produces = JSON)
    public AjaxResultVo refreshStocks() {
        if (this.request.getMethod().equalsIgnoreCase("PUT")) {
            return new AjaxResultVo(this.stocksService.refreshStocks());
        }
        return new AjaxResultVo();
    }

    @ResponseBody
    @RequestMapping(value = "/refresh_user", produces = JSON)
    public AjaxResultVo refreshUser() {
        if (this.request.getMethod().equalsIgnoreCase("PUT")) {
            return new AjaxResultVo(this.elasticsearchService.refreshUser());
        }
        return new AjaxResultVo();
    }

    @ResponseBody
    @RequestMapping(value = "/refresh_article", produces = JSON)
    public AjaxResultVo refreshArticle() throws IOException {
        if (this.request.getMethod().equalsIgnoreCase("PUT")) {
            return new AjaxResultVo(this.elasticsearchService.refreshArticle());
        }
        return new AjaxResultVo();
    }

    @ResponseBody
    @RequestMapping(value = "/refresh_formula", produces = JSON)
    public AjaxResultVo refreshFormula() {
        if (this.request.getMethod().equalsIgnoreCase("PUT")) {
            return new AjaxResultVo(this.elasticsearchService.refreshFormula());
        }
        return new AjaxResultVo();
    }

   /* @ResponseBody
    @RequestMapping(value = "/auto-complete-formula", method = RequestMethod.GET, produces = JSON)
    public AjaxResultVo autoCompleteFormula(@RequestParam("wd") final String keyword) {
        return new AjaxResultVo(this.elasticsearchService.autoCompleteFormula(keyword));
    }*/
}
