package com.foxminded.university.service;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.model.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class LessonService {

    private final LessonDao lessonDao;
    private final HolidayDao holidayDao;
    private final VacationDao vacationDao;

    public LessonService(LessonDao lessonDao, HolidayDao holidayDao, VacationDao vacationDao) {
        this.lessonDao = lessonDao;
        this.holidayDao = holidayDao;
        this.vacationDao = vacationDao;
    }

    public void create(Lesson lesson) {
        if (isLessonValid(lesson)) {
            lessonDao.create(lesson);
        }
    }

    public List<Lesson> getAll() {
        return lessonDao.getAll();
    }

    public Lesson getById(int id) {
        return lessonDao.getById(id);
    }

    public void update(Lesson lesson) {
        if (isLessonValid(lesson)) {
            lessonDao.update(lesson);
        }
    }

    public void remove(Lesson lesson) {
        lessonDao.remove(lesson);
    }

    public List<Lesson> getByTeacherAndDate(Teacher teacher, LocalDate date) {
        return lessonDao.getByTeacherAndDate(teacher, date);
    }

    public List<Lesson> getByTeacherAndDateRange(Teacher teacher, LocalDate startDate, LocalDate endDate) {
        return lessonDao.getByTeacherAndDateRange(teacher, startDate, endDate);
    }

    public List<Lesson> getByGroupAndDate(Group group, LocalDate date) {
        return lessonDao.getByGroupAndDate(group, date);
    }

    public List<Lesson> getByGroupAndDateRange(Group group, LocalDate startDate, LocalDate endDate) {
        return lessonDao.getByGroupAndDateRange(group, startDate, endDate);
    }

    public List<Lesson> getByClassroomAndDate(Classroom classroom, LocalDate date) {
        return lessonDao.getByClassroomAndDate(classroom, date);
    }

    public List<Lesson> getByClassroomAndDateRange(Classroom classroom, LocalDate startDate, LocalDate endDate) {
        return lessonDao.getByClassroomAndDateRange(classroom, startDate, endDate);
    }

    private boolean isLessonValid(Lesson lesson) {
        return isTeacherValid(lesson) && isClassroomFree(lesson)
            && isGroupsFree(lesson) && isNotHolidayLesson(lesson)
            && isNotWeekendLesson(lesson);
    }

    private boolean isNotWeekendLesson(Lesson lesson) {
        return lesson.getDate().getDayOfWeek() != DayOfWeek.SUNDAY
            && lesson.getDate().getDayOfWeek() != DayOfWeek.SATURDAY;
    }

    private boolean isNotHolidayLesson(Lesson lesson) {
        return holidayDao.getByDate(lesson.getDate()).isEmpty();
    }

    private boolean isGroupsFree(Lesson lesson) {
        return lesson.getGroups().stream().map(group -> lessonDao.getByGroupAndDateTime(group, lesson.getDate(), lesson.getDuration()))
            .anyMatch(l -> l == null || l.getId() == lesson.getId());
    }

    private boolean isClassroomFree(Lesson lesson) {
        int countOfStudents = lesson.getGroups().stream().map(Group::getStudents).mapToInt(List::size).sum();
        Lesson existingLesson = lessonDao.getByClassroomAndDateTime(lesson.getClassroom(), lesson.getDate(), lesson.getDuration());
        return (existingLesson == null || existingLesson.getId() == lesson.getId())
            && lesson.getClassroom().getCapacity() >= countOfStudents;
    }

    private boolean isTeacherValid(Lesson lesson) {
        return lesson.getTeacher().getSkills().contains(lesson.getSubject()) && isTeacherFree(lesson) && !isTeacherOnVacation(lesson);
    }

    private boolean isTeacherFree(Lesson lesson) {
        Lesson existingLesson = lessonDao.getByTeacherAndDateTime(lesson.getTeacher(), lesson.getDate(), lesson.getDuration());
        return existingLesson == null || existingLesson.getId() == lesson.getId();
    }

    private boolean isTeacherOnVacation(Lesson lesson) {
        return vacationDao.getByTeacherAndDate(lesson.getTeacher(), lesson.getDate()) != null;
    }
}
