package com.foxminded.university.dao;

import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;

import java.time.LocalDate;
import java.util.List;

public interface VacationDao extends Dao<Vacation> {

    List<Vacation> getByTeacherBetweenDateRange(Teacher teacher, LocalDate startDate, LocalDate endDate);

    Vacation getByTeacherAndDate(Teacher teacher, LocalDate date);
}
