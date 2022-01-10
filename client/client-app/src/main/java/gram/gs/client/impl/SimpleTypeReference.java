package gram.gs.client.impl;

import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.Type;

final class SimpleTypeReference<T> extends TypeReference<T> {

    public final static <T> SimpleTypeReference<T> of(Class<T> clazz) {
        return new SimpleTypeReference<>(clazz);
    }

    private final Class<T> clazz;

    public SimpleTypeReference(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Type getType() {
        return clazz;
    }
}
