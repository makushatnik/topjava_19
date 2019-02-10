package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoImpl implements MealDao {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();

    public MealDaoImpl() {
        List<Meal> meals = MealsUtil.getMeals();
        meals.forEach(x -> {
            int curId = counter.incrementAndGet();
            x.setId(curId);
            mealsMap.put(curId, x);
        });
    }

    @Override
    public List<Meal> getMeals() {
        return new ArrayList<>(mealsMap.values());
    }

    @Override
    public Meal getById(int mealId) {
        return mealsMap.getOrDefault(mealId, null);
    }

    @Override
    public void insert(Meal meal) {
        int curId = counter.incrementAndGet();
        meal.setId(curId);
        mealsMap.put(curId, meal);
    }

    @Override
    public void update(Meal meal) {
        mealsMap.put(meal.getId(), meal);
    }

    @Override
    public void delete(int mealId) {
        mealsMap.remove(mealId);
    }
}
