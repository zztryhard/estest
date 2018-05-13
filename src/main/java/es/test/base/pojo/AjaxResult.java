package es.test.base.pojo;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import es.test.base.enums.ErrorLevelEnum;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/8
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "xml")
public class AjaxResult implements Serializable {

    @JsonProperty("code")
    private Integer errorCode;
    @JsonProperty("msg")
    private String errorMessage;
    @JacksonXmlProperty(localName = "return_code")
    private String returnCode;
    @JacksonXmlProperty(localName = "return_msg")
    private String returnMsg;
    @JsonProperty("data")
    private Object data;

    public AjaxResult() {
        this.errorCode = ErrorLevelEnum.SUCCESS.getCode();
        this.errorMessage = ErrorLevelEnum.SUCCESS.getMessage();
    }

    public AjaxResult(final ErrorLevelEnum errorLevel) {
        this.errorCode = errorLevel.getCode();
        this.errorMessage = errorLevel.getMessage();
    }

    /**
     * 请求成功
     *
     * @param data
     */
    public AjaxResult(final Object data) {
        this.errorCode = 0;
        this.data = data;
    }

    /**
     * 请求错误
     *
     * @param errorCode
     * @param errorMessage
     */
    public AjaxResult(final Integer errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public AjaxResult(final String errorMessage) {
        this.errorCode = ErrorLevelEnum.OTHER.getCode();
        this.errorMessage = errorMessage;
    }

    public AjaxResult(final String returnCode, final String returnMsg) {
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
    }
}
