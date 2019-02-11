package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    List<Meal> getList();
    Meal getById(int mealId);
    Meal insert(Meal meal);
    Meal update(Meal meal);
    void delete(int mealId);
}
