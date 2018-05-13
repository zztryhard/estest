/**
 * Copyright (c) 2018, www.mutongyun.cn
 * All Rights Reserved.
 */
package es.test.base.utils;

import java.util.Map;

import com.alibaba.fastjson.JSON;

import es.test.base.pojo.AjaxResult;
import es.test.base.utils.OKHttpClient.Builder;

/**
 * 功能: 请求commonservice接口工具;
 * date: 2018年4月9日 上午11:59:56 ;
 *
 * @author qli31
 * @version
 * @since JDK 1.8
 */
public class HttpCommonService {

    public static AjaxResult httpCommonGet(Map<String, String> params,String url) throws Exception{
        final Builder builder = OKHttpClient.getBuilder();
        String result = builder.url(url, params).get().execute();
        final AjaxResult ajaxResult = JSON.parseObject(result, AjaxResult.class);
        ajaxResult.setErrorMessage(null);
        return ajaxResult;
    }
    public static AjaxResult httpCommonPost(Map<String, String> params,String url) throws Exception{
        final Builder builder = OKHttpClient.getBuilder();
        String result = builder.buildFormBody(params).url(url).post().execute();
        final AjaxResult ajaxResult = JSON.parseObject(result, AjaxResult.class);
        //ajaxResult.setErrorMessage(null);
        return ajaxResult;
    }
    
}
