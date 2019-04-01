package ru.javawebinar.topjava.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

final public class LocalDateConverter implements Converter<String, LocalDate> {
    @Nullable
    @Override
    public LocalDate convert(String s) {
        return s.isEmpty() ? null : LocalDate.parse(s);
    }
}
