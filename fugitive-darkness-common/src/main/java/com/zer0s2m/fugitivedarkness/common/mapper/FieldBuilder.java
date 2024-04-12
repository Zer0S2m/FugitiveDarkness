package com.zer0s2m.fugitivedarkness.common.mapper;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * An interface for implementing the functionality of embedding source data into a target object.
 * <p>Based on the annotation {@link Field}.</p>
 *
 * @param <T> The source object from where the embedding will take place.
 */
public interface FieldBuilder<T> {

    FieldObject build(T source, Class<? extends FieldObject> target, Collection<String> fields)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

}
