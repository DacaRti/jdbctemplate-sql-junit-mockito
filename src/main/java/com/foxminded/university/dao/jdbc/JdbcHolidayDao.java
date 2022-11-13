package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.dao.jdbc.mappers.HolidayMapper;
import com.foxminded.university.model.Holiday;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcHolidayDao implements HolidayDao {

    private static final String CREATE_HOLIDAY_QUERY = "INSERT INTO holidays (name, date) VALUES (?, ?)";
    private static final String GET_HOLIDAY_QUERY = "SELECT * FROM holidays";
    private static final String GET_HOLIDAY_BY_ID_QUERY = "SELECT * FROM holidays WHERE id = ?";
    private static final String UPDATE_HOLIDAY_QUERY = "UPDATE holidays SET name=?, date=? WHERE id = ?";
    private static final String DELETE_HOLIDAY_QUERY = "DELETE from holidays WHERE id=?";
    private static final String GET_HOLIDAYS_ON_LESSON_DATE = "SELECT * FROM holidays h WHERE h.date = ?";

    private final JdbcTemplate jdbcTemplate;

    private final HolidayMapper holidayMapper;

    public JdbcHolidayDao(JdbcTemplate jdbcTemplate, HolidayMapper holidayMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.holidayMapper = holidayMapper;
    }

    @Override
    public void create(Holiday holiday) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_HOLIDAY_QUERY, new String[]{"id"});
            statement.setString(1, holiday.getName());
            statement.setObject(2, holiday.getDate());

            return statement;
        }, keyHolder);

        holiday.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public List<Holiday> getAll() {
        return jdbcTemplate.query(GET_HOLIDAY_QUERY, holidayMapper);
    }

    @Override
    public Holiday getById(int id) {
        return jdbcTemplate.queryForObject(GET_HOLIDAY_BY_ID_QUERY, new Object[]{id}, holidayMapper);
    }

    @Override
    public List<Holiday> getByDate(LocalDate date) {
        return jdbcTemplate.query(GET_HOLIDAYS_ON_LESSON_DATE, new Object[]{date}, holidayMapper);
    }

    @Override
    public void update(Holiday holiday) {
        jdbcTemplate.update(UPDATE_HOLIDAY_QUERY, holiday.getName(), holiday.getDate(), holiday.getId());
    }

    @Override
    public void remove(Holiday holiday) {
        jdbcTemplate.update(DELETE_HOLIDAY_QUERY, holiday.getId());
    }
}
