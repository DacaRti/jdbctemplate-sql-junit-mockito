package com.foxminded.university.dao;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcLessonDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LessonDao lessonDao;

    @Test
    public void create_ShouldAddLessonToTable_WhenLessonIsCorrect() {
        lessonDao.create(Lesson.builder().subject(Subject.builder().id(1).build())
            .classroom(Classroom.builder().id(1).build())
            .duration(Duration.builder().id(1).build())
            .date(LocalDate.parse("1900-03-03"))
            .teacher(Teacher.builder().id(1).build())
            .groups(List.of(Group.builder().id(1).build())).build());

        assertEquals(4, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "lessons"));
    }

    @Test
    void getAll_ShouldReturnListOfLessonsFromDB_WhenMethodCalled() {
        List<Lesson> lessons = lessonDao.getAll();
        assertEquals(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "lessons"), lessons.size());
    }

    @Test
    void getById_ShouldReturnLessonFromDB_WhenLessonByIdIsExist() {
        Lesson actual = lessonDao.getById(1);
        Lesson expected = createLesson();

        assertEquals(expected, actual);
    }

    @Test
    void getByGroupAndDate_ShouldReturnDayTimeTable_WhenLessonsByDateAndGroupAreExists() {
        List<Lesson> actual = lessonDao.getByGroupAndDate(Group.builder().id(1).name("SMK-16").course(1).build(),
            LocalDate.parse("2022-05-05"));
        List<Lesson> expected = new ArrayList<>();
        expected.add(createLesson());

        assertEquals(expected, actual);
    }

    @Test
    void getByGroupAndDateRange_ShouldReturnMonthTimeTable_WhenLessonsByMonthAndGroupAreExists() {
        List<Lesson> actual = lessonDao.getByGroupAndDateRange(Group.builder().id(1).name("SMK-16").course(1).build(),
            LocalDate.parse("2022-05-01"),
            LocalDate.parse("2022-05-30"));
        List<Lesson> expected = new ArrayList<>();
        expected.add(createLesson());

        assertEquals(expected, actual);
    }

    @Test
    void getByTeacherAndDate_ShouldReturnDayTimeTable_WhenLessonsByDateAndTeacherAreExists() {
        List<Lesson> actual = lessonDao.getByTeacherAndDate(
            Teacher.builder().id(1).firstName("Tsunade").lastName("Senju").build(), LocalDate.parse("2022-05-05"));
        List<Lesson> expected = new ArrayList<>();
        expected.add(createLesson());

        assertEquals(expected, actual);
    }

    @Test
    void getByTeacherAndDateRange_ShouldReturnMonthTimeTable_WhenLessonsByMonthAndTeacherAreExists() {
        List<Lesson> actual = lessonDao.getByTeacherAndDateRange(
            Teacher.builder().id(1).firstName("Tsunade").lastName("Senju").build(),
            LocalDate.parse("2022-05-01"),
            LocalDate.parse("2022-05-30"));
        List<Lesson> expected = new ArrayList<>();
        expected.add(createLesson());

        assertEquals(expected, actual);
    }

    @Test
    void getByClassroomAndDate_ShouldReturnDayTimeTable_WhenLessonsByDateAndClassroomAreExists() {
        List<Lesson> actual = lessonDao.getByClassroomAndDate(
            Classroom.builder().id(1).number(101).floor(1).build(),
            LocalDate.parse("2022-05-05"));
        List<Lesson> expected = new ArrayList<>();
        expected.add(createLesson());

        assertEquals(expected, actual);
    }

    @Test
    void getByClassroomAndDateRange_ShouldReturnMonthTimeTable_WhenClassroomHaveLessons() {
        List<Lesson> actual = lessonDao.getByClassroomAndDateRange(
            Classroom.builder().id(1).number(101).floor(1).build(),
            LocalDate.parse("2022-05-01"),
            LocalDate.parse("2022-05-30"));
        List<Lesson> expected = new ArrayList<>();
        expected.add(createLesson());

        assertEquals(expected, actual);
    }

    @Test
    void getByTeacherAndDateTime_ShouldReturnListOfLessons_WhenLessonsWithSameTeacherAndDateTimeAreExists() {
        Lesson lesson = Lesson.builder()
            .teacher(Teacher.builder().id(1).build())
            .date(LocalDate.parse("2022-05-05"))
            .duration(Duration.builder().id(1)
                .startTime(LocalTime.of(8, 0, 0))
                .endTime(LocalTime.of(10, 0, 0)).build()).build();
        Lesson actual = lessonDao.getByTeacherAndDateTime(lesson.getTeacher(), lesson.getDate(), lesson.getDuration());
        Lesson expected = createLesson();

        assertEquals(expected, actual);
    }

    @Test
    void getByClassroomAndDateTime_ShouldReturnListOfLessons_WhenLessonsWithSameClassroomAndDateTimeAreExists() {
        Lesson lesson = Lesson.builder()
            .classroom(Classroom.builder().id(1).build())
            .date(LocalDate.parse("2022-05-05"))
            .duration(Duration.builder().id(1)
                .startTime(LocalTime.of(8, 0, 0))
                .endTime(LocalTime.of(10, 0, 0)).build()).build();
        Lesson actual = lessonDao.getByClassroomAndDateTime(lesson.getClassroom(), lesson.getDate(), lesson.getDuration());
        Lesson expected = createLesson();

        assertEquals(expected, actual);
    }

    @Test
    void getByGroupAndDateTime_ShouldReturnListOfLessons_WhenLessonsWithSameGroupAndDateTimeAreExists() {
        Lesson lesson = Lesson.builder()
            .classroom(Classroom.builder().id(1).build())
            .date(LocalDate.parse("2022-05-05"))
            .duration(Duration.builder().id(1)
                .startTime(LocalTime.of(8, 0, 0))
                .endTime(LocalTime.of(10, 0, 0)).build()).build();

        Lesson actual = lessonDao.getByGroupAndDateTime(Group.builder().id(1).build(), lesson.getDate(), lesson.getDuration());
        Lesson expected = createLesson();

        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldChangeLessonInDB_WhenLessonIsCorrect() {
        lessonDao.update(Lesson.builder().id(1).subject(Subject.builder().id(1).build())
            .classroom(Classroom.builder().id(3).build())
            .duration(Duration.builder().id(1).build())
            .date(LocalDate.parse("1900-03-03"))
            .teacher(Teacher.builder().id(1).build())
            .groups(List.of(Group.builder().id(1).build())).build());

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, "lessons", "classroom_id = 3"));
    }

    @Test
    void remove_ShouldRemoveLessonFromTable_WhenLessonIsExist() {
        lessonDao.remove(Lesson.builder().id(2).build());

        assertEquals(2, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "lessons"));
    }

    private Lesson createLesson() {
        return Lesson.builder().id(1)
            .subject(Subject.builder().id(1).name("MATH").build())
            .classroom(Classroom.builder().id(1).number(101).floor(1).capacity(20).build())
            .duration(Duration.builder().id(1)
                .startTime(LocalTime.of(8, 0, 0))
                .endTime(LocalTime.of(10, 0, 0)).build())
            .date(LocalDate.parse("2022-05-05"))
            .teacher(Teacher.builder().id(1).firstName("Tsunade").lastName("Senju").gender(Gender.FEMALE)
                .email("Hokage@Gmail.com").birthDate(LocalDate.of(1900, 3, 3))
                .phone("05034343434").address(new Address(1, "a", "a", "a", "a"))
                .degree(Degree.DOCTOR).rank("Hokage")
                .faculty(Faculty.builder().id(1).name("MATH").syllabus(Syllabus.builder().id(1).fullTime(25)
                    .build()).build()).build()).build();
    }
}
