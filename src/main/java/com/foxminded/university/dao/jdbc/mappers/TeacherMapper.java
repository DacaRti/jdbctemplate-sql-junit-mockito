package com.foxminded.university.dao.jdbc.mappers;

import com.foxminded.university.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class TeacherMapper implements RowMapper<Teacher> {

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private FacultyMapper facultyMapper;

    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        Address address = addressMapper.mapRow(rs, rowNum);
        Faculty faculty = facultyMapper.mapRow(rs, rowNum);

        Teacher teacher = new Teacher();
        teacher.setId(rs.getInt("id"));
        teacher.setFirstName(rs.getString("first_name"));
        teacher.setLastName(rs.getString("last_name"));
        teacher.setGender(Gender.valueOf(rs.getString("gender")));
        teacher.setEmail(rs.getString("email"));
        teacher.setBirthDate(rs.getObject("birth_date", LocalDate.class));
        teacher.setPhone(rs.getString("phone"));
        teacher.setAddress(address);
        teacher.setDegree(Degree.valueOf(rs.getString("degree")));
        teacher.setRank(rs.getString("rank"));
        teacher.setFaculty(faculty);

        return teacher;
    }
}
