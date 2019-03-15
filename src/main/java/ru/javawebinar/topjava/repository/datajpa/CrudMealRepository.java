package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
    @Query(name = Meal.DELETE)
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query(name = Meal.ALL_SORTED)
    List<Meal> findAll(@Param("userId") int userId);

    @Query(name = Meal.GET_BETWEEN)
    List<Meal> findAllBetween(@Param("startDate") LocalDateTime startDate,
          @Param("endDate") LocalDateTime endDate,
          @Param("userId") int userId);

    Meal findByIdAndUserId(int id, int userId);

    @Query(name = Meal.GET_BY_ID_WITH_USER)
    Meal findWithUser(int id);
}
