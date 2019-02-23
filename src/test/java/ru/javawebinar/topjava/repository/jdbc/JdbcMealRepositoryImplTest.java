package ru.javawebinar.topjava.repository.jdbc;

import org.junit.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class JdbcMealRepositoryImplTest {
    private static ConfigurableApplicationContext appCtx;
    private static MealRestController controller;

    @BeforeClass
    public static void beforeClass() {
        appCtx = new ClassPathXmlApplicationContext(
                "spring/spring-app.xml", "spring/spring-db.xml");
        System.out.println("\n" + Arrays.toString(appCtx.getBeanDefinitionNames()) + "\n");
        controller = appCtx.getBean(MealRestController.class);
    }

    @AfterClass
    public static void afterClass() {
        appCtx.close();
    }

    @Before
    public void setUp() throws Exception {
        // re-initialize
        final JdbcMealRepositoryImpl repository = appCtx.getBean(JdbcMealRepositoryImpl.class);
        repository.init();
        MealsUtil.MEALS.forEach(x -> repository.save(x, USER_ID));
    }

    @Test
    public void get() throws Exception {
        Meal meal = controller.get(MEAL0.getId());
        assertMatch(meal, MEAL0);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        controller.get(20);
    }

    @Test
    public void getAll() throws Exception {
        List<MealTo> mealTos = controller.getAll();
        assertMatch(mealTos, MealsUtil.getWithExcess(MEALS, SecurityUtil.authUserCaloriesPerDay()));
    }

    @Test
    public void getBetween() throws Exception {
        List<MealTo> mealTos = controller.getAll();
        assertMatch(mealTos, MealsUtil.getWithExcess(Arrays.asList(MEAL0, MEAL1), SecurityUtil.authUserCaloriesPerDay()));
    }

    @Test
    public void save() throws Exception {
        Meal tmpMeal = MealsUtil.MEALS.get(0);
        Meal meal = controller.create(tmpMeal);
        tmpMeal.setId(meal.getId());
        assertMatch(meal, tmpMeal);
    }

    @Test
    public void delete() throws Exception {
        controller.delete(MEAL0.getId());
        Collection<MealTo> mealTos = controller.getAll();
        Assert.assertEquals(mealTos.size(), 5);
        Assert.assertEquals(mealTos.iterator().next(), MEAL1TO);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        controller.delete(20);
    }
}