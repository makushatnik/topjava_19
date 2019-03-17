package ru.javawebinar.topjava.web.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

@Controller
@RequestMapping("users")
public class JspUserController extends AbstractUserController {

    @GetMapping("/list")
    public String getUsers() {
        List<User> users = service.getAll();
        return "users";
    }

    @PostMapping("/")
    public String getMeals(@RequestParam("userId") int userId) {
        SecurityUtil.setAuthUserId(userId);
        return "meals";
    }
}
