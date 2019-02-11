package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class MealExcessServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealExcessServlet.class);
    private static final String EXCESS_PAGE = "/mealExcess.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("mealServlet Get method");

        List<Meal> meals = MealsUtil.getMeals();
        List<MealTo> mealsWithExcess = MealsUtil.getFilteredWithExcess(meals,
                LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        request.setAttribute("meals", mealsWithExcess);
        request.getRequestDispatcher(EXCESS_PAGE).forward(request, response);
    }
}
