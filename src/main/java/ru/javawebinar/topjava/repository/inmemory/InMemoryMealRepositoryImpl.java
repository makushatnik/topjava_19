package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(x -> save(x, SecurityUtil.ADMIN_ID));
        save(new Meal(LocalDateTime.of(2016, Month.APRIL, 5, 11, 0), "Завтрак", 250), SecurityUtil.USER_ID);
        save(new Meal(LocalDateTime.of(2016, Month.APRIL, 5, 11, 0), "Брекфаст", 350), SecurityUtil.USER_ID);
        save(new Meal(LocalDateTime.of(2016, Month.APRIL, 5, 11, 0), "Обед", 500), SecurityUtil.USER_ID);
        save(new Meal(LocalDateTime.of(2016, Month.APRIL, 5, 16, 0), "Ланч", 350), SecurityUtil.USER_ID);
        save(new Meal(LocalDateTime.of(2016, Month.APRIL, 5, 20, 0), "Ужин", 750), SecurityUtil.USER_ID);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} for userId - {}", meal, userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> userMeal = repository.get(userId);
            if (userMeal == null) {
                userMeal = new ConcurrentHashMap<>();
            }
            userMeal.put(meal.getId(), meal);
            repository.put(userId, userMeal);
            return meal;
        }
        // treat case: update, but absent in storage
        Map<Integer, Meal> userMeal = repository.get(userId);
        if (userMeal != null) {
            userMeal.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
            repository.put(userId, userMeal);
            return meal;
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete by id {} for userId - {}", id, userId);
        Map<Integer, Meal> userMeal = repository.get(userId);
        if (userMeal != null) {
            Meal meal = userMeal.remove(id);
            repository.put(userId, userMeal);
            return meal != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get by id {} for userId - {}", id, userId);
        Map<Integer, Meal> userMeal = repository.get(userId);
        if (userMeal != null) {
            return userMeal.get(id);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll for userId - {}", userId);
        Map<Integer, Meal> userMeal = repository.get(userId);
        if (userMeal != null) {
            List<Meal> meals = new ArrayList<>(userMeal.values());
            Comparator<Meal> mealComparator
                    = Comparator.comparing(Meal::getDateTime);
            mealComparator = mealComparator.reversed();
            meals.sort(mealComparator);

            return meals;
        }
        return Collections.emptyList();
    }

    @Override
    public List<Meal> getAllFilter(int userId, LocalDate startDate, LocalTime startTime, LocalDate endDate,
           LocalTime endTime) {
        log.info("getAllFilter for userId - {}. filter - {}. {}. {}. {}", userId, startDate, startTime, endDate, endTime);
        Map<Integer, Meal> userMeal = repository.get(userId);
        if (userMeal != null) {
            return userMeal.values().stream()
                .filter(x -> DateTimeUtil.isBetween(x.getDate(), startDate, endDate))
                .filter(x -> DateTimeUtil.isBetween(x.getTime(), startTime, endTime))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}

