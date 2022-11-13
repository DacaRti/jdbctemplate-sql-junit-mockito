package com.foxminded.university.dao.jdbc.mappers;

import com.foxminded.university.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class LessonMapper implements RowMapper<Lesson> {

    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private DurationMapper durationMapper;
    @Autowired
    private ClassroomMapper classroomMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
        Subject subject = subjectMapper.mapRow(rs, rowNum);
        Classroom classroom = classroomMapper.mapRow(rs, rowNum);
        Teacher teacher = teacherMapper.mapRow(rs, rowNum);
        Duration duration = durationMapper.mapRow(rs, rowNum);

        Lesson lesson = new Lesson();
        lesson.setId(rs.getInt("id"));
        lesson.setSubject(subject);
        lesson.setClassroom(classroom);
        lesson.setDate(rs.getObject("date", LocalDate.class));
        lesson.setDuration(duration);
        lesson.setTeacher(teacher);

        return lesson;
    }
}
