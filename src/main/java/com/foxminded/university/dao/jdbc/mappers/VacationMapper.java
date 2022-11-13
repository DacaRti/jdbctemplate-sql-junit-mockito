package com.foxminded.university.dao.jdbc.mappers;

import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class VacationMapper implements RowMapper<Vacation> {

    @Autowired
    private TeacherMapper teacherMapper;

    public Vacation mapRow(ResultSet rs, int rowNum) throws SQLException {
        Teacher teacher = teacherMapper.mapRow(rs, rowNum);

        Vacation vacation = new Vacation();
        vacation.setId(rs.getInt("id"));
        vacation.setStartDate(rs.getObject("start_date", LocalDate.class));
        vacation.setEndDate(rs.getObject("end_date", LocalDate.class));
        vacation.setPaid(rs.getBoolean("is_paid"));
        vacation.setCause(rs.getString("cause"));
        vacation.setTeacher(teacher);

        return vacation;
    }
}
