package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final Comparator<User> COMPARE_BY_NAME_EMAIL = (a, b) -> a.getName().compareTo(b.getName()) +
            a.getEmail().compareTo(b.getEmail());

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    //private final DataSourceTransactionManager transactionManager;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate
            //DataSourceTransactionManager transactionManager
    ) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        //this.transactionManager = transactionManager;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            int userId = newKey.intValue();
            if (user.getRoles() != null && user.getRoles().size() > 0 &&
                    !batchInsertRoles(new ArrayList<>(user.getRoles()), userId)) {
                return null;
            }
            user.setId(userId);
        //update user
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        //update roles
        } else if (deleteRoles(user.getId()) && !batchInsertRoles(new ArrayList<>(user.getRoles()), user.getId())) {
            return null;
        }
        return user;
    }

    private boolean batchInsertRoles(final List<Role> roles, final int userId) {
        final String sql = "INSERT INTO user_roles (user_id, role) VALUES (?,?)";
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Role role = roles.get(i);
                ps.setInt(1, userId);
                ps.setString(2, role.toString());
            }

            //only 100 roles for a batch
            @Override
            public int getBatchSize() {
                return roles.size();
            }
        }).length > 0;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    private boolean deleteRoles(int id) {
        return jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.id, u.name, u.email, u.enabled, u.calories_per_day, " +
                "u.password, u.registered, r.role " +
                "FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE id=?", new UserWithRolesExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE email=?",
                new UserWithRolesExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id = r.user_id " +
                "ORDER BY name, email", new UserWithRolesExtractor());
    }

    private static final class UserWithRolesExtractor implements ResultSetExtractor<List<User>> {
        @Nullable
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> map = new HashMap<>();
            User user = null;
            while (rs.next()) {
                Integer id = rs.getInt("id");
                user = map.get(id);
                if (user == null) {
                    user = new User();
                    user.setId(id);
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    user.setRegistered(rs.getDate("registered"));
                    Set<Role> roles = new HashSet<>();
                    user.setRoles(roles);

                    map.put(id, user);
                }

                String roleStr = rs.getString("role");
                if (roleStr != null && !roleStr.isEmpty()) {
                    Role role = Role.valueOf(roleStr);
                    user.getRoles().add(role);
                }
            }
            List<User> res = new ArrayList<>(map.values());
            res.sort(COMPARE_BY_NAME_EMAIL);
            return res;
        }
    }
}
