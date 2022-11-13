package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.DurationDao;
import com.foxminded.university.dao.jdbc.mappers.DurationMapper;
import com.foxminded.university.model.Duration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcDurationDao implements DurationDao {

    private static final String CREATE_DURATION_QUERY = "INSERT INTO durations(start_time, end_time) VALUES (?, ?)";
    private static final String GET_DURATIONS_QUERY = "SELECT * FROM durations";
    private static final String GET_DURATION_BY_ID_QUERY = "SELECT * FROM durations WHERE id=?";
    private static final String UPDATE_DURATION_QUERY = "UPDATE durations SET start_time=?, end_time=? WHERE id=?";
    private static final String DELETE_DURATION_QUERY = "DELETE from durations WHERE id=?";

    private final JdbcTemplate jdbcTemplate;

    private final DurationMapper durationMapper;

    public JdbcDurationDao(JdbcTemplate jdbcTemplate, DurationMapper durationMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.durationMapper = durationMapper;
    }

    @Override
    public void create(Duration duration) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_DURATION_QUERY, new String[]{"id"});
            statement.setObject(1, duration.getStartTime());
            statement.setObject(2, duration.getEndTime());

            return statement;
        }, keyHolder);

        duration.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public List<Duration> getAll() {
        return jdbcTemplate.query(GET_DURATIONS_QUERY, durationMapper);
    }

    @Override
    public Duration getById(int id) {
        return jdbcTemplate.queryForObject(GET_DURATION_BY_ID_QUERY, new Object[]{id}, durationMapper);
    }

    @Override
    public void update(Duration duration) {
        jdbcTemplate.update(UPDATE_DURATION_QUERY, duration.getStartTime(), duration.getEndTime(), duration.getId());
    }

    @Override
    public void remove(Duration duration) {
        jdbcTemplate.update(DELETE_DURATION_QUERY, duration.getId());
    }
}
