package es.test.base.databind;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import es.test.base.utils.HashidsHelper;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/6
 */
public class HashidsDeserializer extends JsonDeserializer<Long> {
    @Override
    public Long deserialize(final JsonParser p, final DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
        return HashidsHelper.decode(p.getText().trim());
    }
}
