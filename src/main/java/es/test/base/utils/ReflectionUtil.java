package es.test.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/8
 */
public final class ReflectionUtil {
    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final String CGLIB_CLASS_SEPARATOR = "$$";
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    public static Object invokeGetter(final Object obj, final String propertyName) {
        Object object = obj;
        final String[] properties = StringUtils.split(propertyName, ".");

        for (final String name : properties) {
            final String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[0], new Object[0]);
        }

        return object;
    }

    public static void invokeSetter(final Object obj, final String propertyName, final Object value) {
        Object object = obj;
        final String[] names = StringUtils.split(propertyName, ".");

        for (int i = 0; i < names.length; ++i) {
            final String setterMethodName;
            if (i < names.length - 1) {
                setterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
                object = invokeMethod(object, setterMethodName, new Class[0], new Object[0]);
            } else {
                setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
                invokeMethodByName(object, setterMethodName, new Object[] {value});
            }
        }

    }

    public static Object getFieldValue(final Object obj, final String fieldName) {
        final Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        } else {
            Object result = null;

            try {
                result = field.get(obj);
            } catch (final IllegalAccessException var5) {
                LOGGER.error("不可能抛出的异常{}", var5.getMessage());
            }

            return result;
        }
    }

    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        final Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        } else {
            try {
                field.set(obj, value);
            } catch (final IllegalAccessException var5) {
                LOGGER.error("不可能抛出的异常:{}", var5.getMessage());
            }

        }
    }

    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
        final Object[] args) {
        final Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        } else {
            try {
                return method.invoke(obj, args);
            } catch (final Exception var6) {
                throw convertReflectionExceptionToUnchecked(var6);
            }
        }
    }

    public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
        final Method method = getAccessibleMethodByName(obj, methodName);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        } else {
            try {
                return method.invoke(obj, args);
            } catch (final Exception var5) {
                throw convertReflectionExceptionToUnchecked(var5);
            }
        }
    }

    public static Field getAccessibleField(final Object obj, final String fieldName) {
        Validate.notNull(obj, "object can\'t be null");
        Validate.notBlank(fieldName, "fieldName can\'t be blank");
        Class superClass = obj.getClass();

        while (superClass != Object.class) {
            try {
                final Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (final NoSuchFieldException var4) {
                superClass = superClass.getSuperclass();
            }
        }

        return null;
    }

    public static Method getAccessibleMethod(final Object obj, final String methodName, final Class... parameterTypes) {
        Validate.notNull(obj, "object can\'t be null");
        Validate.notBlank(methodName, "methodName can\'t be blank");
        Class searchType = obj.getClass();

        while (searchType != Object.class) {
            try {
                final Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            } catch (final NoSuchMethodException var5) {
                searchType = searchType.getSuperclass();
            }
        }

        return null;
    }

    public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
        Validate.notNull(obj, "object can\'t be null");
        Validate.notBlank(methodName, "methodName can\'t be blank");

        for (Class searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            final Method[] methods = searchType.getDeclaredMethods();
            final int var6 = methods.length;

            for (int var5 = 0; var5 < var6; ++var5) {
                final Method method = methods[var5];
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }

        return null;
    }

    public static void makeAccessible(final Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
            && !method.isAccessible()) {
            method.setAccessible(true);
        }

    }

    public static void makeAccessible(final Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
             || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }

    }

    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    public static Class getClassGenricType(final Class clazz, final int index) {
        final Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            LOGGER.warn(clazz.getSimpleName() + "\'s superclass not ParameterizedType");
            return Object.class;
        } else {
            final Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class)) {
                    LOGGER.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
                    return Object.class;
                } else {
                    return (Class) params[index];
                }
            } else {
                LOGGER.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "\'s Parameterized Type: "
                            + params.length);
                return Object.class;
            }
        }
    }

    public static Class<?> getUserClass(final Object instance) {
        Assert.notNull(instance, "Instance must not be null");
        final Class clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            final Class superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }

        return clazz;
    }

    public static RuntimeException convertReflectionExceptionToUnchecked(final Exception e) {
        return !(e instanceof IllegalAccessException) && !(e instanceof IllegalArgumentException)
               && !(e instanceof NoSuchMethodException) ?
               (e instanceof InvocationTargetException ? new RuntimeException(
                   ((InvocationTargetException) e).getTargetException()) :
                (e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(
                    "Unexpected Checked Exception.", e))) : new IllegalArgumentException(e);
    }
}
