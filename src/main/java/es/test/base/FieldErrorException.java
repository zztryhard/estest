package es.test.base;

/**
 * Created by mty02 on 2017/4/19.
 */
public class FieldErrorException extends RuntimeException {
    public static final String EXCEPTION_UI_CODE = "30001";

    public FieldErrorException(final String message) {
        super(message);
    }

    public FieldErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
