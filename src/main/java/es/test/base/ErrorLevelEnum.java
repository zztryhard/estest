package es.test.base;

/**
 * Created by mty02 on 2017/3/28.
 */
public enum ErrorLevelEnum {
    SUCCESS(0, "请求成功"),
    FIELD_ERROR(30000, "参数有误"),
    USER_EXISTED(30001, "用户已存在"),
    INVALID_PASSWORD(30002, "密码无效"),
    NOT_FOUND(40000, "请求不存在"),
    USER_NOT_EXIST(40001, "用户不存在"),
    USER_SESSION_TIMEOUT(50001, "用户状态超时"),
    TOKEN_TIMEOUT(50002, "TOKEN超时"),
    IMAGE_CAPTCHA_INVALID(50003, "验证码有误"),
    SMS_CAPTCHA_INVALID(50004, "短信验证码有误"),
    SERVER_ERROR(50999, "服务器错误"),
    OTHER(70999, "未知异常"),
    REQUEST_TOO_FREQUENT(90001, "请求过于频繁"),
    INVALID_PARAMETER(90002, "参数无效");
    private final Integer errorCode;
    private final String errorMessage;

    private ErrorLevelEnum(final Integer errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
