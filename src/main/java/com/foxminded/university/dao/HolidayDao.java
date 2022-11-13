package com.foxminded.university.dao;

import com.foxminded.university.model.Holiday;

import java.time.LocalDate;
import java.util.List;

public interface HolidayDao extends Dao<Holiday> {

    List<Holiday> getByDate(LocalDate date);
}
