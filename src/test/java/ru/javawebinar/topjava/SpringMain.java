package ru.javawebinar.topjava;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (XmlWebApplicationContext appCtx = new XmlWebApplicationContext()) {
            DispatcherServlet dispatcher =  new DispatcherServlet(appCtx);
            //dispatcher.setContextConfigLocation("WEB-INF/web.xml");
            dispatcher.init(new ServletConfig() {
                @Override
                public String getServletName() {
                    return "SpringMain Servlet";
                }

                @Override
                public ServletContext getServletContext() {
                    return null;
                }

                @Override
                public String getInitParameter(String s) {
                    return null;
                }

                @Override
                public Enumeration<String> getInitParameterNames() {
                    return null;
                }
            });
            appCtx.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile(), Profiles.REPOSITORY_IMPLEMENTATION);
            //appCtx.setConfigLocation("spring/");
            appCtx.setConfigLocations("spring/spring-app.xml", "spring/spring-db.xml", "spring/spring-mvc.xml");
            //appCtx.load("spring/spring-app.xml", "spring/spring-db.xml", "spring/spring-mvc.xml");
            appCtx.setServletContext(dispatcher.getServletContext());
            appCtx.refresh();
            //servletContext.addListener(new ContextLoaderListener(appCtx));

            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));
            System.out.println();

            MealRestController mealController = appCtx.getBean(MealRestController.class);
            List<MealTo> filteredMealsWithExceeded =
                    mealController.getBetween(
                            LocalDate.of(2015, Month.MAY, 30), LocalTime.of(7, 0),
                            LocalDate.of(2015, Month.MAY, 31), LocalTime.of(11, 0));
            filteredMealsWithExceeded.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Err: " + e);
        }
    }
}
