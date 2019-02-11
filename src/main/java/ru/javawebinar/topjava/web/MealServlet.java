package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoInMemoryImpl;
import ru.javawebinar.topjava.dao.MealsFactory;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static final String EDIT_PAGE = "/meal.jsp";
    private static final String LIST_PAGE = "/mealList.jsp";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final MealDao dao = MealsFactory.getDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean redirect = false;
        String page = "";
        int mealId;
        String action = request.getParameter("action");
        if (action == null) {
            page = LIST_PAGE;
            request.setAttribute("meals", dao.getList());
            request.getRequestDispatcher(page).forward(request, response);
        } else action = action.toLowerCase();
        switch (action) {
            case "list":
                page = LIST_PAGE;
                request.setAttribute("meals", dao.getList());
                break;
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
            default:
                page = LIST_PAGE;
                request.setAttribute("meals", dao.getList());
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
            dao.insert(meal);
        } else {
            meal.setId(Integer.parseInt(mealId));
            dao.update(meal);
        }
        request.setAttribute("meals", dao.getList());
        request.getRequestDispatcher(LIST_PAGE).forward(request, response);
    }
}
