package com.foxminded.university.dao;

import com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.List;

public interface LessonDao extends Dao<Lesson> {

    List<Lesson> getByTeacherAndDate(Teacher teacher, LocalDate date);

    List<Lesson> getByTeacherAndDateRange(Teacher teacher, LocalDate startDate, LocalDate endDate);

    List<Lesson> getByGroupAndDate(Group group, LocalDate date);

    List<Lesson> getByGroupAndDateRange(Group group, LocalDate startDate, LocalDate endDate);

    List<Lesson> getByClassroomAndDate(Classroom classroom, LocalDate date);

    List<Lesson> getByClassroomAndDateRange(Classroom classroom, LocalDate startDate, LocalDate endDate);

    Lesson getByTeacherAndDateTime(Teacher teacher, LocalDate date, Duration duration);

    Lesson getByClassroomAndDateTime(Classroom classroom, LocalDate date, Duration duration);

    Lesson getByGroupAndDateTime(Group group, LocalDate date, Duration duration);
}
