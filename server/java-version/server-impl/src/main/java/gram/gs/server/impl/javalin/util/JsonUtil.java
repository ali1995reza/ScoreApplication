package gram.gs.server.impl.javalin.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtil {

    public final static ObjectMapper MAPPER = new ObjectMapper();

    public static String toJsonString(Object o) {
        try {
            return MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] toJsonBytes(Object o) {
        try {
            return MAPPER.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T fromBytes(byte[] data, Class<T> cls) {
        try {
            return MAPPER.readValue(data, cls);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T fromString(byte[] data, Class<T> cls) {
        try {
            return MAPPER.readValue(data, cls);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
