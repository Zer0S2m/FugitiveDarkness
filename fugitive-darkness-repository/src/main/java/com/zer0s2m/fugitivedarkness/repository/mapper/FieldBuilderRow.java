package com.zer0s2m.fugitivedarkness.repository.mapper;

import com.zer0s2m.fugitivedarkness.common.mapper.Field;
import com.zer0s2m.fugitivedarkness.common.mapper.FieldBuilder;
import com.zer0s2m.fugitivedarkness.common.mapper.FieldObject;
import io.vertx.sqlclient.Row;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class FieldBuilderRow implements FieldBuilder<Row> {

    @Override
    public FieldObject build(Row source, Class<? extends FieldObject> target, Collection<String> fields)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final FieldObject targetInit = target.getDeclaredConstructor().newInstance();

        for (java.lang.reflect.Field field : targetInit.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Field.class)) {
                Field annotation = field.getAnnotation(Field.class);
                if (fields.contains(annotation.value())) {
                    field.setAccessible(true);
                    field.set(targetInit, source.get(Card.get(field.getType()), annotation.value()));
                    field.setAccessible(false);
                }
            }
        }

        return targetInit;
    }

    static private class Card {

        public static Class<?> get(Class<?> clazz) {
            if (clazz.equals(long.class)) {
                return Long.class;
            } else if (clazz.equals(int.class)) {
                return Integer.class;
            } else if (clazz.equals(boolean.class)) {
                return Boolean.class;
            }

            return clazz;
        }

    }

}
