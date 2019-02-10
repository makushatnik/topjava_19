package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletConfig;
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
    private static final String EXCESS_PAGE = "/mealExcess.jsp";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final MealDao dao = new MealDaoImpl();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("mealServlet Get method");
        //--- For Common HomeWork 1
//        List<Meal> meals = MealsUtil.getMeals();
//        List<MealTo> mealsWithExcess = MealsUtil.getFilteredWithExcess(meals,
//                LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        request.setAttribute("meals", mealsWithExcess);
//        request.getRequestDispatcher(EXCESS_PAGE).forward(request, response);
        //---

        //For Optional HomeWork 1
        boolean redirect = false;
        String page = "";
        String action = request.getParameter("action");
        if (action == null) {
            page = LIST_PAGE;
            request.setAttribute("meals", dao.getMeals());
        } else if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            dao.delete(mealId);
            redirect = true;
            response.sendRedirect("meals");
        } else if (action.equalsIgnoreCase("edit")) {
            page = EDIT_PAGE;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = dao.getById(mealId);
            if (meal == null) {
                throw new RuntimeException("404! Meal Not Found!");
            }
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("insert")) {
            page = EDIT_PAGE;
        } else if (action.equalsIgnoreCase("list")){
            page = LIST_PAGE;
            request.setAttribute("meals", dao.getMeals());
        } else {
            page = LIST_PAGE;
            request.setAttribute("meals", dao.getMeals());
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
            request.getParameter("descr"),
            calories
        );

        if(mealId == null || mealId.isEmpty()) {
            dao.insert(meal);
        } else {
            int id = Integer.parseInt(mealId);
            if (id <= 0) {
                dao.insert(meal);
            } else {
                meal.setId(id);
                dao.update(meal);
            }
        }
        request.setAttribute("meals", dao.getMeals());
        request.getRequestDispatcher(LIST_PAGE).forward(request, response);
    }
}
