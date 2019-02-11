package ru.javawebinar.topjava.dao;

public class MealsFactory {
    public static MealDao mealDao;

    public static MealDao getDao() {
        if (mealDao == null) {
            mealDao = new MealDaoInMemoryImpl();
        }
        return mealDao;
    }
}
