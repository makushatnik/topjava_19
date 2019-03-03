package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.AccessDeniedException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    public Meal get(int id) {
        return em.createQuery("from Meal m where m.id = :id", Meal.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Meal get(int id, int userId) {
        return em.createQuery("from Meal m where m.id = :id and m.user.id = :userId", Meal.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createQuery("from Meal m where m.user.id = :userId ORDER BY m.dateTime DESC", Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createQuery("from Meal m where m.dateTime between :startDate and :endDate " +
                "and m.user.id = :userId ORDER BY m.dateTime DESC", Meal.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            User user = em.find(User.class, userId);
            meal.setUser(user);
            em.persist(meal);
            return meal;
        } else {
            //Проверяем, что принадлежит юзеру
            Meal that = get(meal.getId(), userId);
            Meal exists = get(meal.getId());
            if (that != null) {
                meal.setUser(that.getUser());
                return em.merge(meal);
            } else if (exists != null) {
                throw new AccessDeniedException("Вам не разрешено изменять эту еду!");
            } else throw new NotFoundException("Запрашиваемая еда не существует!");
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createQuery("DELETE FROM Meal m WHERE m.id = :id AND m.user.id = :userId")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }
}