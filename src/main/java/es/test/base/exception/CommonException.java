package es.test.base.exception;

import lombok.Getter;
import es.test.base.enums.ErrorLevelEnum;

/**
 * @author 旺旺小学酥
 * @Time 2018/4/2
 */

public class CommonException extends RuntimeException {

    @Getter
    private final Integer code;

    public CommonException(final ErrorLevelEnum errorLevel) {
        super(errorLevel.getMessage());
        this.code = errorLevel.getCode();
    }

    public CommonException(final Integer code) {
        super();
        this.code = code;
    }

    public CommonException(final Integer code, final String message) {
        super(message);
        this.code = code;
    }

    public CommonException(final Integer code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CommonException(final Integer code, final Throwable cause) {
        super(cause);
        this.code = code;
    }

    protected CommonException(final Integer code, final String message, final Throwable cause,
        final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
