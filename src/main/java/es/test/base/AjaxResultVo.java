package es.test.base;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;


/**
 * Created by mty02 on 2017/3/27.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AjaxResultVo implements Serializable {

    @SerializedName("ec")
    @JsonProperty("ec")
    private Integer errorCode;
    @SerializedName("em")
    @JsonProperty("em")
    private String errorMessage;
    @SerializedName("data")
    @JsonProperty("data")
    private Object data;

    public AjaxResultVo() {
        this.errorCode = ErrorLevelEnum.SUCCESS.getErrorCode();
        this.errorMessage = ErrorLevelEnum.SUCCESS.getErrorMessage();
    }

    public AjaxResultVo(final ErrorLevelEnum errorLevel) {
        this.errorCode = errorLevel.getErrorCode();
        this.errorMessage = errorLevel.getErrorMessage();
    }

    /**
     * 请求成功
     *
     * @param data
     */
    public AjaxResultVo(final Object data) {
        this.errorCode = 0;
        this.data = data;
    }

    /**
     * 请求错误
     *
     * @param errorCode
     * @param errorMessage
     */
    public AjaxResultVo(final Integer errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public AjaxResultVo(final String errorMessage) {
        this.errorCode = ErrorLevelEnum.OTHER.getErrorCode();
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(final Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(final Object data) {
        this.data = data;
    }
}
