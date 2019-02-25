package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void getAll() throws Exception {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, MealsUtil.getSortedDatetimeDesc(Arrays.asList(MEAL0, MEAL1, MEAL2, MEAL3, MEAL4, MEAL5)));
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        List<Meal> all = service.getBetweenDateTimes(
                LocalDateTime.of(2015, Month.MAY, 30, 10, 0),
                LocalDateTime.of(2015, Month.MAY, 30, 13, 0), USER_ID);
        assertMatch(all, MealsUtil.getSortedDatetimeDesc(Arrays.asList(MEAL0, MEAL1)));
    }

    @Test
    public void get() throws Exception {
        Meal meal = service.get(START_ID, USER_ID);
        assertMatch(meal, MEAL0);
    }

    @Test(expected = NotFoundException.class)
    public void getAlien() {
        service.get(START_ID + 6, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(START_ID + 11, USER_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = new Meal(null, LocalDateTime.now(), "New", 1400);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.get(newMeal.getId(), USER_ID), newMeal);
    }

    @Test
    public void saveAlien() {
        Meal alien = new Meal(START_ID + 6, LocalDateTime.of(2017, Month.MAY, 30, 10, 0),
                "This is Changed Description", 555);
        Meal updated = service.create(alien, USER_ID);
        assertMatch(updated, null);
    }

    @Test(expected = DuplicateKeyException.class)
    public void theSameDate() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0),
                "Some Description", 14623);
        service.create(meal, USER_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = new Meal(MEAL0);
        updated.setDateTime(LocalDateTime.now());
        updated.setDescription("UpdatedName");
        updated.setCalories(880);
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test
    public void delete() throws Exception {
        service.delete(START_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MealsUtil.getSortedDatetimeDesc(MEALS_DELETED));
    }

    @Test(expected = NotFoundException.class)
    public void deleteAlien() {
        service.delete(START_ID + 6, USER_ID);
    }
}