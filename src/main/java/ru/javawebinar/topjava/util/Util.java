package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.StringJoiner;

public class Util {
    private Util() {
    }

    public static <T extends Comparable<? super T>> boolean isBetween(T value, @Nullable T start, @Nullable T end) {
        return (start == null || value.compareTo(start) >= 0) && (end == null || value.compareTo(end) <= 0);
    }

    public static String getErrors(List<FieldError> list) {
        final StringJoiner joiner = new StringJoiner("<br>");
        list.forEach(
                fe -> {
                    String msg = fe.getDefaultMessage();
                    if (msg != null) {
                        if (!msg.startsWith(fe.getField())) {
                            msg = fe.getField() + ' ' + msg;
                        }
                        joiner.add(msg);
                    }
                });
        return joiner.toString();
    }
}
