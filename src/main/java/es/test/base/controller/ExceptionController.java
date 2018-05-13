package es.test.base.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import es.test.base.exception.CommonException;
import es.test.base.pojo.AjaxResult;

@RestControllerAdvice
@RestController
public class ExceptionController {

    /**
     * 捕捉其他所有异常
     *
     * @param request
     * @param ex
     *
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public AjaxResult globalException(final HttpServletRequest request, final Throwable ex) {
        return new AjaxResult(getStatus(request).value(), "服务器异常");
    }

    @ExceptionHandler(CommonException.class)
    @ResponseStatus(HttpStatus.OK)
    public AjaxResult commonException(final HttpServletRequest request, final Throwable ex) {
        return new AjaxResult(((CommonException) ex).getCode(), ex.getMessage());
    }

    @RequestMapping("/**")
    public AjaxResult notFound() {
        return new AjaxResult(404, "请求未找到");
    }

    private HttpStatus getStatus(final HttpServletRequest request) {
        final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}

