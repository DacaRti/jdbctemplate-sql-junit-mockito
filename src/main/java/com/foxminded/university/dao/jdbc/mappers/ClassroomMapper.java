package com.foxminded.university.dao.jdbc.mappers;

import com.foxminded.university.model.Classroom;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ClassroomMapper implements RowMapper<Classroom> {

    public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {
        Classroom classroom = new Classroom();
        classroom.setId(rs.getInt("id"));
        classroom.setNumber(rs.getInt("number"));
        classroom.setFloor(rs.getInt("floor"));
        classroom.setCapacity(rs.getInt("capacity"));

        return classroom;
    }
}
