package es.test.base.databind;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import es.test.base.utils.HashidsHelper;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/6
 */
public class HashidsSerializer extends JsonSerializer<Long> {
    @Override
    public void serialize(final Long value, final JsonGenerator gen, final SerializerProvider serializers)
        throws IOException, JsonProcessingException {
        gen.writeString(HashidsHelper.encode(value));
    }
}
