package ru.javawebinar.topjava.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.LocalTime;

final public class LocalTimeConverter implements Converter<String, LocalTime> {
    @Nullable
    @Override
    public LocalTime convert(String s) {
        return s.isEmpty() ? null : LocalTime.parse(s);
    }
}
