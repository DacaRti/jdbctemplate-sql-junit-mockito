package com.foxminded.university.dao.jdbc.mappers;

import com.foxminded.university.model.Syllabus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SyllabusMapper implements RowMapper<Syllabus> {

    public Syllabus mapRow(ResultSet rs, int rowNum) throws SQLException {
        Syllabus syllabus = new Syllabus();
        syllabus.setId(rs.getInt("id"));
        syllabus.setFullTime(rs.getInt("full_time"));

        return syllabus;
    }
}
