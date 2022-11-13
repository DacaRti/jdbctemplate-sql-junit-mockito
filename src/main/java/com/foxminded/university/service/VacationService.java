package com.foxminded.university.service;

import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Vacation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
public class VacationService {

    @Value("#{${vacation.days}}")
    private Map<Degree, Integer> vacationDays;

    private final VacationDao vacationDao;

    public VacationService(VacationDao vacationDao) {
        this.vacationDao = vacationDao;
    }

    public void create(Vacation vacation) {
        if (isVacationValid(vacation)) {
            vacationDao.create(vacation);
        }
    }

    public List<Vacation> getAll() {
        return vacationDao.getAll();
    }

    public Vacation getById(int id) {
        return vacationDao.getById(id);
    }

    public void update(Vacation vacation) {
        if (isVacationValid(vacation)) {
            vacationDao.update(vacation);
        }
    }

    public void remove(Vacation vacation) {
        vacationDao.remove(vacation);
    }

    private boolean isVacationValid(Vacation vacation) {
        return isVacationStartDateBeforeEndDate(vacation) && isTeacherGoToVacation(vacation);
    }

    private boolean isTeacherGoToVacation(Vacation vacation) {
        List<Vacation> vacationsOverLap = vacationDao.getByTeacherBetweenDateRange(vacation.getTeacher(), vacation.getStartDate(), vacation.getEndDate());
        return (vacationsOverLap.isEmpty() || vacationDao.getById(vacation.getId()) != null) && isVacationDaysLeft(vacation);
    }

    private boolean isVacationDaysLeft(Vacation vacation) {
        int totalVacationDays = vacationDao.getByTeacherBetweenDateRange(vacation.getTeacher(),
                vacation.getStartDate().with(firstDayOfYear()), vacation.getStartDate().with(lastDayOfYear()))
            .stream().filter(v -> v.getId() != vacation.getId()).map(Vacation::getCountOfVacationDays)
            .reduce(vacation.getCountOfVacationDays(), Integer::sum);
        int maxVacationDays = vacationDays.get(vacation.getTeacher().getDegree());

        return totalVacationDays <= maxVacationDays;
    }

    private boolean isVacationStartDateBeforeEndDate(Vacation vacation) {
        return vacation.getStartDate().isBefore(vacation.getEndDate());
    }
}
