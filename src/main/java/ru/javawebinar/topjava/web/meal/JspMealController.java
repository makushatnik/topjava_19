package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

@Controller
@RequestMapping("/meals")
public class JspMealController {

    @Autowired
    public MealService service;

    @GetMapping
    public String getMeals(Model model) {
        model.addAttribute("meals", MealsUtil.getWithExcess(service.getAll(SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/add")
    public String getEditForm(Model model) {
        model.addAttribute("meal", new Meal());
        return "mealForm";
    }

    @GetMapping("/{id}")
    public String getEditForm(@PathVariable int id, Model model) {
        Objects.requireNonNull(id);
        model.addAttribute(service.get(id, SecurityUtil.authUserId()));
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        Objects.requireNonNull(id);
        service.delete(id, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @PostMapping(value = "/edit")
    //public String createMeal(@RequestBody final Meal meal, Errors errors) {
    public String createMeal(@ModelAttribute Meal meal, Model model) {
//        if (errors.hasErrors()) {
//            return "mealForm";
//        }

        if (meal.getId() == null) {
            if (meal.getCalories() <= 0) throw new IllegalArgumentException("Не указаны калории!");
            if (meal.getDateTime() == null) {
                meal.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            }
            service.create(meal, SecurityUtil.authUserId());
        } else {
            service.update(meal, SecurityUtil.authUserId());
        }

        return "redirect:/meals";
    }

    @PostMapping("/filter")
    public String filterByDates(@RequestParam("startDate") String startDateStr,
                                @RequestParam("endDate") String endDateStr,
                                @RequestParam("startTime") String startTimeStr,
                                @RequestParam("endTime") String endTimeStr,
                                Model model) {
        LocalDate startDate = parseLocalDate(startDateStr);
        LocalDate endDate = parseLocalDate(endDateStr);
        LocalTime startTime = parseLocalTime(startTimeStr);
        LocalTime endTime = parseLocalTime(endTimeStr);
        int userId = SecurityUtil.authUserId();
        List<Meal> meals = service.getBetweenDates(startDate, endDate, userId);
        model.addAttribute("meals", MealsUtil.getFilteredWithExcess(meals, userId, startTime, endTime));
        return "meals";
    }

//    @RequestMapping(value = "/", method = RequestMethod.PUT)
//    public String updateMeal(@RequestBody Meal meal) {
//        if (meal.getId() == null) throw new IllegalArgumentException("Не указан ИД!");
//        service.update(meal, SecurityUtil.authUserId());
//        return "redirect:meals";
//    }

//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//    public String delete(@PathVariable("id") int id) {
//        Objects.requireNonNull(id);
//        service.delete(id, SecurityUtil.authUserId());
//        return "redirect:meals";
//    }
}
