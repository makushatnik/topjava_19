package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MealsUtil {
    public static void main(String[] args) {
        List<Meal> mealList = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<MealWithExceed> list = getFilteredWithExceeded(mealList,
                LocalTime.of(7, 0), LocalTime.of(23,0), 2000);
        list.forEach(meal -> {
            System.out.println(meal);
        });
    }

    public static List<MealWithExceed>  getFilteredWithExceeded(List<Meal> meals, LocalTime startTime,
            LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> caloriesSumPerDay = new HashMap<>();
        final Map<LocalDate, AtomicBoolean> exceededMap = new HashMap<>();

        final List<MealWithExceed> mealsExceeded = new ArrayList<>();
        meals.forEach(meal -> {
            AtomicBoolean wrapBoolean = exceededMap.computeIfAbsent(meal.getDate(), date -> new AtomicBoolean());
            Integer dailyCalories = caloriesSumPerDay.merge(meal.getDate(), meal.getCalories(), Integer::sum);
            if (dailyCalories > caloriesPerDay) {
                wrapBoolean.set(true);
            }
            if (TimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
                mealsExceeded.add(createWithExceeded(meal, wrapBoolean));
            }
        });

        return mealsExceeded;
    }

    private static MealWithExceed createWithExceeded(Meal meal, AtomicBoolean exceeded) {
        return new MealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded);
    }
}
