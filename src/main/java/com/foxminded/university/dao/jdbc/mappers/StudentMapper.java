package com.foxminded.university.dao.jdbc.mappers;

import com.foxminded.university.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class StudentMapper implements RowMapper<Student> {

    @Autowired
    private AddressMapper addressMapper;

    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Address address = addressMapper.mapRow(rs, rowNum);

        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setGender(Gender.valueOf(rs.getString("gender")));
        student.setEmail(rs.getString("email"));
        student.setBirthDate(rs.getObject("birth_date", LocalDate.class));
        student.setPhone(rs.getString("phone"));
        student.setAddress(address);
        student.setStudyForm(StudyForm.valueOf(rs.getString("study_form")));

        return student;
    }
}
