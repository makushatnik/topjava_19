package ru.javawebinar.topjava.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.*;

@RestController
@RequestMapping("/ajax/admin/users")
public class AdminUIController extends AbstractUserController {

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createOrUpdate(@RequestParam Integer id,
                               @RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam(required = false) Boolean enabled) {

        User user = new User(id, name, email, password, DEFAULT_CALORIES_PER_DAY,
                enabled == null ? false : enabled, new Date(), Arrays.asList(Role.ROLE_USER));
        if (user.isNew()) {
            super.create(user);
        } else {
            super.update(user, id);
        }
    }

    @PostMapping("/switch")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void switchEnabled(@RequestParam Integer id,
                              @RequestParam boolean enabled) {
        System.out.println("ID: " + id);
        System.out.println("Enabled: " + enabled);
        User user = get(id);
        if (user == null) throw new NotFoundException("That user not found!\n404");

        user.setEnabled(enabled);
        super.update(user, id);
    }
}
