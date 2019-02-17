package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface MealService {

    List<Meal> getAll(int userId);

    List<Meal> getAllFilter(int userId, LocalDateTime startDate, LocalDateTime endDate);

    Meal get(int id, int userId) throws NotFoundException;

    Meal create(Meal meal, int userId);

    void update(Meal meal, int userId);

    void delete(int id, int userId) throws NotFoundException;
}