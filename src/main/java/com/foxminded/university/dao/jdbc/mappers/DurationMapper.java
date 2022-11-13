package com.foxminded.university.dao.jdbc.mappers;

import com.foxminded.university.model.Duration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

@Component
public class DurationMapper implements RowMapper<Duration> {

    public Duration mapRow(ResultSet rs, int rowNum) throws SQLException {
        Duration duration = new Duration();
        duration.setId(rs.getInt("id"));
        duration.setStartTime(rs.getObject("start_time", LocalTime.class));
        duration.setEndTime(rs.getObject("end_time", LocalTime.class));

        return duration;
    }
}
