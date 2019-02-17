package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundMeal;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    public List<Meal> getAllFilter(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.getAllFilter(userId, startDate.toLocalDate(), startDate.toLocalTime(),
                endDate.toLocalDate(), endDate.toLocalTime());
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        return checkNotFoundMeal(repository.get(id, userId), id, userId);
    }

    @Override
    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    @Override
    public void update(Meal meal, int userId) {
        checkNotFoundMeal(repository.save(meal, userId), meal.getId(), userId);
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        checkNotFoundMeal(repository.delete(id, userId), id, userId);
    }
}