package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int START_ID = START_SEQ + 2;
    public static final int NOT_EXISTS = START_ID + 6;

    public static final Meal MEAL0 = new Meal(START_ID, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL1 = new Meal(START_ID + 1, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL2 = new Meal(START_ID + 2, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL3 = new Meal(START_ID + 3, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal MEAL4 = new Meal(START_ID + 4, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
    public static final Meal MEAL5 = new Meal(START_ID + 5, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);

    public static <T> void assertMatch(T actual, T expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static <T> void assertMatch(List<T> actual, T... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static <T> void assertMatch(List<T> actual, List<T> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
