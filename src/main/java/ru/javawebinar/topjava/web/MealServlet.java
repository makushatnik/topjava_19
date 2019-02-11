package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoInMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static final String EDIT_PAGE = "/meal.jsp";
    private static final String LIST_PAGE = "/mealList.jsp";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final MealDao dao = new MealDaoInMemoryImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean redirect = false;
        String page = "";
        int mealId;
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        } else action = action.toLowerCase();
        switch (action) {
            case "delete":
                mealId = Integer.parseInt(request.getParameter("mealId"));
                dao.delete(mealId);
                redirect = true;
                response.sendRedirect("meals");
                break;
            case "edit":
                mealId = Integer.parseInt(request.getParameter("mealId"));
                Meal meal = dao.getById(mealId);
                if (meal == null) {
                    throw new RuntimeException("404! Meal Not Found!");
                }
                request.setAttribute("meal", meal);
            case "insert":
                page = EDIT_PAGE;
                break;
            case "list":
            default:
                page = LIST_PAGE;
                List<Meal> list = dao.getList();
                final List<MealTo> res = new ArrayList<>();
                list.forEach(x -> {
                    boolean excess = false;
                    MealTo mealTo = dao.getMealToByDate(x.getDateTime());
                    res.add(new MealTo(x.getId(), x.getDateTime(), x.getDescription(), x.getCalories(),
                            mealTo != null && mealTo.isExcess()));
                });
                request.setAttribute("meals", res);
        }

        if (!redirect) {
            request.getRequestDispatcher(page).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("mealServlet Post method");
        request.setCharacterEncoding("UTF-8");

        String mealId = request.getParameter("mealId");
        String dateTimeStr = request.getParameter("dateTime");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(
                dateTime,
                request.getParameter("description"),
                calories
        );

        if(mealId == null || mealId.isEmpty()) {
            dao.insert(meal, true);
        } else {
            meal.setId(Integer.parseInt(mealId));
            dao.update(meal);
        }

        response.sendRedirect("meals");
    }
}
