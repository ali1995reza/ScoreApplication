package gram.gs.server.impl.javalin.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    public final static ObjectMapper MAPPER = new ObjectMapper();

    public static String toJsonString(Object o) {
        try {
            return MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] toJsonByte(Object o) {
        try {
            return MAPPER.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
