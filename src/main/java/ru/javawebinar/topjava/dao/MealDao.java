package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    List<Meal> getMeals();
    Meal getById(int mealId);
    void insert(Meal meal);
    void update(Meal meal);
    void delete(int mealId);
}
