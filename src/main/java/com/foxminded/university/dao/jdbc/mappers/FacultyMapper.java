package com.foxminded.university.dao.jdbc.mappers;

import com.foxminded.university.model.Faculty;
import com.foxminded.university.model.Syllabus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FacultyMapper implements RowMapper<Faculty> {

    @Autowired
    private SyllabusMapper syllabusMapper;

    public Faculty mapRow(ResultSet rs, int rowNum) throws SQLException {
        Syllabus syllabus = syllabusMapper.mapRow(rs, rowNum);

        Faculty faculty = new Faculty();
        faculty.setId(rs.getInt("id"));
        faculty.setName(rs.getString("name"));
        faculty.setSyllabus(syllabus);

        return faculty;
    }
}
