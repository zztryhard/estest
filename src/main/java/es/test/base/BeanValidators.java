package es.test.base;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mty02 on 2017/3/14.
 */
public class BeanValidators {
    public BeanValidators() {
    }

    public static void validateWithException(final Validator validator, final Object object, final Class... groups) throws ConstraintViolationException {
        final Set constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public static List<String> extractMessage(final ConstraintViolationException e) {
        return extractMessage(e.getConstraintViolations());
    }

    public static List<String> extractMessage(final Set<? extends ConstraintViolation> constraintViolations) {
        final ArrayList errorMessages = Lists.newArrayList();
        final Iterator var3 = constraintViolations.iterator();

        while (var3.hasNext()) {
            final ConstraintViolation violation = (ConstraintViolation) var3.next();
            errorMessages.add(violation.getMessage());
        }

        return errorMessages;
    }

    public static Map<String, String> extractPropertyAndMessage(final ConstraintViolationException e) {
        return extractPropertyAndMessage(e.getConstraintViolations());
    }

    public static Map<String, String> extractPropertyAndMessage(final Set<? extends ConstraintViolation> constraintViolations) {
        final HashMap errorMessages = Maps.newHashMap();
        final Iterator var3 = constraintViolations.iterator();

        while (var3.hasNext()) {
            final ConstraintViolation violation = (ConstraintViolation) var3.next();
            errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return errorMessages;
    }

    public static List<String> extractPropertyAndMessageAsList(final ConstraintViolationException e) {
        return extractPropertyAndMessageAsList(e.getConstraintViolations(), " ");
    }

    public static List<String> extractPropertyAndMessageAsList(final Set<? extends ConstraintViolation> constraintViolations) {
        return extractPropertyAndMessageAsList(constraintViolations, " ");
    }

    public static List<String> extractPropertyAndMessageAsList(final ConstraintViolationException e, final String separator) {
        return extractPropertyAndMessageAsList(e.getConstraintViolations(), separator);
    }

    public static List<String> extractPropertyAndMessageAsList(final Set<? extends ConstraintViolation> constraintViolations, final String separator) {
        final ArrayList errorMessages = Lists.newArrayList();
        final Iterator var4 = constraintViolations.iterator();

        while (var4.hasNext()) {
            final ConstraintViolation violation = (ConstraintViolation) var4.next();
            errorMessages.add(violation.getPropertyPath() + separator + violation.getMessage());
        }

        return errorMessages;
    }
}
