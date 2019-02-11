package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoInMemoryImpl implements MealDao {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();

    public MealDaoInMemoryImpl() {
        List<Meal> meals = MealsUtil.getMeals();
        meals.forEach(x -> {
            insertInner(x);
        });
    }

    @Override
    public List<Meal> getList() {
        return new ArrayList<>(mealsMap.values());
    }

    @Override
    public Meal getById(int mealId) {
        return mealsMap.get(mealId);
    }

    @Override
    public Meal insert(Meal meal) {
        insertInner(meal);
        return meal;
    }

    private void insertInner(Meal meal) {
        int curId = counter.incrementAndGet();
        meal.setId(curId);
        mealsMap.put(curId, meal);
    }

    @Override
    public Meal update(Meal meal) {
        if (mealsMap.containsKey(meal.getId())) {
            mealsMap.put(meal.getId(), meal);
        } else throw new RuntimeException("404! Еды с таким ИД не существует!");
        return meal;
    }

    @Override
    public void delete(int mealId) {
        mealsMap.remove(mealId);
    }
}
