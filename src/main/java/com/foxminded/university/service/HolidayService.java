package com.foxminded.university.service;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.model.Holiday;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HolidayService {

    private final HolidayDao holidayDao;

    public HolidayService(HolidayDao holidayDao) {
        this.holidayDao = holidayDao;
    }

    public void create(Holiday holiday) {
        holidayDao.create(holiday);
    }

    public List<Holiday> getAll() {
        return holidayDao.getAll();
    }

    public Holiday getById(int id) {
        return holidayDao.getById(id);
    }

    public void update(Holiday holiday) {
        holidayDao.update(holiday);
    }

    public void remove(Holiday holiday) {
        holidayDao.remove(holiday);
    }
}
