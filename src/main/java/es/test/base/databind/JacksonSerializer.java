package es.test.base.databind;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import redis.clients.util.SafeEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jarvis.cache.serializer.ISerializer;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/7
 */
public class JacksonSerializer implements ISerializer {

    public static final ISerializer me = new JacksonSerializer();

    public byte[] keyToBytes(final String key) {
        return SafeEncoder.encode(key);
    }

    public String keyFromBytes(final byte[] bytes) {
        return SafeEncoder.encode(bytes);
    }

    public byte[] fieldToBytes(final Object field) {
        return valueToBytes(field);
    }

    public Object fieldFromBytes(final byte[] bytes) {
        return valueFromBytes(bytes);
    }

    public byte[] valueToBytes(final Object value) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsBytes(value);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Object valueFromBytes(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(bytes, Object.class);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * @see com.jarvis.cache.clone.ICloner#deepClone(java.lang.Object, java.lang.reflect.Type)
     */
    public Object deepClone(Object obj, Type type) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.jarvis.cache.clone.ICloner#deepCloneMethodArgs(java.lang.reflect.Method, java.lang.Object[])
     */
    public Object[] deepCloneMethodArgs(Method method, Object[] args)
            throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.jarvis.cache.serializer.ISerializer#serialize(java.lang.Object)
     */
    public byte[] serialize(Object obj) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.jarvis.cache.serializer.ISerializer#deserialize(byte[], java.lang.reflect.Type)
     */
    public Object deserialize(byte[] bytes, Type returnType) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
