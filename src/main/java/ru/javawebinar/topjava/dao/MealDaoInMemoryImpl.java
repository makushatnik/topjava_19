package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.Constants;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoInMemoryImpl implements MealDao {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private static final Map<LocalDateTime, MealTo> totalsMap = new ConcurrentHashMap<>();

    public MealDaoInMemoryImpl() {
        List<Meal> meals = MealsUtil.getMeals();
        meals.forEach(x -> insert(x, false));
        updateTotal();
    }

    @Override
    public List<Meal> getList() {
        return new ArrayList<>(mealsMap.values());
    }

    public Map<LocalDateTime, MealTo> getTotalsMap() {
        return totalsMap;
    }

    @Override
    public MealTo getMealToByDate(LocalDateTime date) {
        return totalsMap.get(date);
    }

    @Override
    public Meal getById(int mealId) {
        return mealsMap.get(mealId);
    }

    @Override
    public Meal insert(Meal meal, boolean updateTotal) {
        int curId = counter.incrementAndGet();
        meal.setId(curId);
        mealsMap.put(curId, meal);
        if (updateTotal) {
            updateTotal();
        }
        return meal;
    }

    @Override
    public Meal update(Meal meal) {
        if (mealsMap.containsKey(meal.getId())) {
            mealsMap.put(meal.getId(), meal);
            updateTotal();
        } else return null;
        return meal;
    }

    @Override
    public void delete(int mealId) {
        mealsMap.remove(mealId);
        updateTotal();
    }

    private void updateTotal() {
        List<MealTo> mealsWithExcess = MealsUtil.getWithExcess(new ArrayList<>(mealsMap.values()),
                Constants.NORMAL_CALORIES_PER_DAY);
        totalsMap.clear();
        mealsWithExcess.forEach(x -> {
            totalsMap.put(x.getDateTime(), x);
        });
    }
}
