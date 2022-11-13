package com.foxminded.university.service;

import com.foxminded.university.dao.DurationDao;
import com.foxminded.university.model.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.Duration.between;

@Service
public class DurationService {

    @Value("${lesson.min.duration}")
    private int lessonMinDuration;

    private final DurationDao durationDao;

    public DurationService(DurationDao durationDao) {
        this.durationDao = durationDao;
    }

    public void create(Duration duration) {
        if (isValidTimeInterval(duration)) {
            durationDao.create(duration);
        }
    }

    public List<Duration> getAll() {
        return durationDao.getAll();
    }

    public Duration getById(int id) {
        return durationDao.getById(id);
    }

    public void update(Duration duration) {
        if (isValidTimeInterval(duration)) {
            durationDao.update(duration);
        }
    }

    public void remove(Duration duration) {
        durationDao.remove(duration);
    }

    private boolean isValidTimeInterval(Duration duration) {
        return duration.getStartTime().isBefore(duration.getEndTime()) &&
            between(duration.getStartTime(), duration.getEndTime()).toMinutes() >= lessonMinDuration;
    }
}
