package com.zer0s2m.fugitivedarkness.common.mapper;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * An annotation for marking a class property for further embedding the value in the property.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface Field {

    /**
     * The name of the property from where the value will be taken from the sources.
     */
    String value();

}
