package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.Constants;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        List<Meal> meals = service.getAll(authUserId());
        return MealsUtil.getWithExcess(meals, authUserCaloriesPerDay());
    }

    public List<MealTo> getAllFilter(String startDateStr, String startTimeStr, String endDateStr, String endTimeStr) {
        log.info("getAllFilter");
        LocalDate startDate = LocalDate.parse(startDateStr.isEmpty() ? Constants.MIN_DATE : startDateStr);
        LocalTime startTime = LocalTime.parse(startTimeStr.isEmpty() ? Constants.MIN_TIME : startTimeStr);
        LocalDate endDate = LocalDate.parse(endDateStr.isEmpty() ? Constants.MAX_DATE : endDateStr);
        LocalTime endTime = LocalTime.parse(endTimeStr.isEmpty() ? Constants.MAX_TIME : endTimeStr);
        if (startDate.getYear() < Constants.MIN_YEAR) startDate = startDate.withYear(Constants.MIN_YEAR);
        if (endDate.getYear() > Constants.MAX_YEAR) endDate = endDate.withYear(Constants.MAX_YEAR);
        List<Meal> meals = service.getAllFilter(authUserId(), startDate, startTime, endDate, endTime);
        return MealsUtil.getWithExcess(meals, authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }
}