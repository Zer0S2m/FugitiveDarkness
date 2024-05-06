package com.zer0s2m.fugitivedarkness.models;

import java.time.LocalDateTime;

abstract class Job<T> {

    abstract T getType();

    abstract String getCron();

    abstract LocalDateTime getNextRunAt();

}
