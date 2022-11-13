package com.foxminded.university.service;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import static com.foxminded.university.service.LessonServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonDao lessonDao;

    @Mock
    private HolidayDao holidayDao;

    @Mock
    private VacationDao vacationDao;

    @InjectMocks
    private LessonService lessonService;

    @Test
    void create_ShouldCreateLesson_WhenLessonIsCorrect() {
        lessonService.create(lesson);
        verify(lessonDao, times(1)).create(lesson);
    }

    @Test
    void create_ShouldNotCreateLesson_WhenTeacherOnVacation() {
        when(vacationDao.getByTeacherAndDate(teacherOnVacation,
            lessonWithTeacherOnVacation.getDate())).thenReturn(firstVacation);

        lessonService.create(lessonWithTeacherOnVacation);

        verify(lessonDao, never()).create(lessonWithTeacherOnVacation);
    }

    @Test
    void create_ShouldNotCreateLesson_WhenTeacherOnAnotherLesson() {
        when(lessonDao.getByTeacherAndDateTime(teacherWithLessonOnSameDate, firstDate, duration)).thenReturn(firstSameLesson);

        lessonService.create(secondSameLesson);

        verify(lessonDao, never()).create(secondSameLesson);
    }

    @Test
    void create_ShouldNotCreateLesson_WhenGroupOnAnotherLesson() {
        when(lessonDao.getByGroupAndDateTime(group, firstDate, duration)).thenReturn(firstSameLesson);

        lessonService.create(secondSameLesson);

        verify(lessonDao, never()).create(secondSameLesson);
    }

    @Test
    void create_ShouldNotCreateLesson_WhenClassroomHaveAnotherLesson() {
        when(lessonDao.getByClassroomAndDateTime(classroom, firstDate, duration)).thenReturn(firstSameLesson);

        lessonService.create(secondSameLesson);

        verify(lessonDao, never()).create(secondSameLesson);
    }

    @Test
    void create_ShouldNotCreateLesson_WhenTeacherWithoutNecessarySkill() {
        lessonService.create(lessonWithTeacherWithoutNecessarySkill);
        verify(lessonDao, never()).create(lessonWithTeacherWithoutNecessarySkill);
    }

    @Test
    void create_ShouldNotCreateLesson_WhenClassroomWithoutEnoughSeats() {
        lessonService.create(lessonWithClassroomWithoutEnoughSeats);
        verify(lessonDao, never()).create(lessonWithClassroomWithoutEnoughSeats);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2022-07-23", "2022-07-24"})
    void create_ShouldNotCreateLesson_WhenDateOnWeekends(String day) {
        weekendLesson.setDate(LocalDate.parse(day));
        lessonService.create(weekendLesson);
        verify(lessonDao, never()).create(weekendLesson);
    }

    @Test
    void getAll_ShouldReturnListOfLessons_WhenMethodCalled() {
        List<Lesson> expected = List.of(lesson, lesson);

        when(lessonDao.getAll()).thenReturn(expected);

        List<Lesson> actual = lessonService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getById_ShouldReturnLesson_WhenLessonByIdIsExist() {
        when(lessonDao.getById(1)).thenReturn(lesson);

        Lesson actual = lessonService.getById(1);

        assertEquals(lesson, actual);
    }

    @Test
    void getByGroupAndDate_ShouldReturnDayTimeTable_WhenLessonsByDateAndGroupAreExists() {
        List<Lesson> expected = List.of(lesson, lesson);

        when(lessonDao.getByGroupAndDate(group,
            LocalDate.parse("2000-05-05"))).thenReturn(expected);

        List<Lesson> actual = lessonService.getByGroupAndDate(group,
            LocalDate.parse("2000-05-05"));

        assertEquals(expected, actual);
    }

    @Test
    void getByGroupAndDateRange_ShouldReturnMonthTimeTable_WhenLessonsByMonthAndGroupAreExists() {
        List<Lesson> expected = List.of(lesson, lesson);

        when(lessonDao.getByGroupAndDateRange(group, LocalDate.parse("2000-05-05"),
            LocalDate.parse("2000-06-06"))).thenReturn(expected);

        List<Lesson> actual = lessonService.getByGroupAndDateRange(group,
            LocalDate.parse("2000-05-05"), LocalDate.parse("2000-06-06"));

        assertEquals(expected, actual);
    }

    @Test
    void getByTeacherAndDate_ShouldReturnDayTimeTable_WhenLessonsByDateAndTeacherAreExists() {
        List<Lesson> expected = List.of(lesson, lesson);

        when(lessonDao.getByTeacherAndDate(teacher, LocalDate.parse("2000-05-05"))).thenReturn(expected);

        List<Lesson> actual = lessonService.getByTeacherAndDate(teacher, LocalDate.parse("2000-05-05"));

        assertEquals(expected, actual);
    }

    @Test
    void getByTeacherAndDateRange_ShouldReturnMonthTimeTable_WhenLessonsByMonthAndTeacherAreExists() {
        List<Lesson> expected = List.of(lesson, lesson);

        when(lessonDao.getByTeacherAndDateRange(teacher, LocalDate.parse("2000-05-05"),
            LocalDate.parse("2000-06-06"))).thenReturn(expected);

        List<Lesson> actual = lessonService.getByTeacherAndDateRange(teacher,
            LocalDate.parse("2000-05-05"), LocalDate.parse("2000-06-06"));

        assertEquals(expected, actual);
    }

    @Test
    void getByClassroomAndDate_ShouldReturnDayTimeTable_WhenLessonsByDateAndClassroomAreExists() {
        List<Lesson> expected = List.of(lesson, lesson);

        when(lessonDao.getByClassroomAndDate(classroom, LocalDate.parse("2000-05-05"))).thenReturn(expected);

        List<Lesson> actual = lessonService.getByClassroomAndDate(classroom, LocalDate.parse("2000-05-05"));

        assertEquals(expected, actual);
    }

    @Test
    void getByClassroomAndDateRange_ShouldReturnMonthTimeTable_WhenLessonsInTheClassroomAreExists() {
        List<Lesson> expected = List.of(lesson, lesson);

        when(lessonDao.getByClassroomAndDateRange(classroom, LocalDate.parse("2000-05-05"), LocalDate.parse("2000-06-06"))).thenReturn(expected);

        List<Lesson> actual = lessonService.getByClassroomAndDateRange(classroom, LocalDate.parse("2000-05-05"), LocalDate.parse("2000-06-06"));

        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldUpdateLesson_WhenLessonIsCorrect() {
        lessonService.update(lesson);
        verify(lessonDao, times(1)).update(lesson);
    }

    @Test
    void update_ShouldUpdateLesson_WhenGroupWithSameId() {
        when(lessonDao.getByGroupAndDateTime(group, firstDate, duration)).thenReturn(lesson);

        lessonService.update(lesson);

        verify(lessonDao, times(1)).update(lesson);
    }

    @Test
    void update_ShouldUpdateLesson_WhenClassroomWithSameId() {
        when(lessonDao.getByTeacherAndDateTime(teacher, firstDate, duration)).thenReturn(lesson);

        lessonService.update(lesson);

        verify(lessonDao, times(1)).update(lesson);
    }

    @Test
    void update_ShouldNotUpdateLesson_WhenTeacherOnVacation() {
        when(vacationDao.getByTeacherAndDate(teacherOnVacation, lessonWithTeacherOnVacation.getDate())).thenReturn(firstVacation);

        lessonService.update(lessonWithTeacherOnVacation);

        verify(lessonDao, never()).update(lessonWithTeacherOnVacation);
    }

    @Test
    void update_ShouldNotUpdateLesson_WhenTeacherOnAnotherLesson() {
        when(lessonDao.getByTeacherAndDateTime(teacherWithLessonOnSameDate, firstDate, duration)).thenReturn(firstSameLesson);

        lessonService.update(secondSameLesson);

        verify(lessonDao, never()).update(secondSameLesson);
    }

    @Test
    void update_ShouldNotUpdateLesson_WhenGroupOnAnotherLesson() {
        when(lessonDao.getByGroupAndDateTime(group, firstDate, duration)).thenReturn(firstSameLesson);

        lessonService.update(secondSameLesson);

        verify(lessonDao, never()).update(secondSameLesson);
    }

    @Test
    void update_ShouldNotUpdateLesson_WhenClassroomHaveAnotherLesson() {
        when(lessonDao.getByClassroomAndDateTime(classroom, firstDate, duration)).thenReturn(firstSameLesson);

        lessonService.create(secondSameLesson);

        verify(lessonDao, never()).create(secondSameLesson);
    }

    @Test
    void update_ShouldNotUpdateLesson_WhenTeacherWithoutNecessarySkill() {
        lessonService.update(lessonWithTeacherWithoutNecessarySkill);
        verify(lessonDao, never()).update(lessonWithTeacherWithoutNecessarySkill);
    }

    @Test
    void update_ShouldNotUpdateLesson_WhenClassroomWithoutEnoughSeats() {
        lessonService.update(lessonWithClassroomWithoutEnoughSeats);
        verify(lessonDao, never()).update(lessonWithClassroomWithoutEnoughSeats);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2022-07-23", "2022-07-24"})
    void update_ShouldNotUpdateLesson_WhenDateOnWeekends(String day) {
        weekendLesson.setDate(LocalDate.parse(day));
        lessonService.update(weekendLesson);
        verify(lessonDao, never()).update(weekendLesson);
    }

    @Test
    void remove_ShouldRemoveLesson_WhenLessonExist() {
        lessonService.remove(lesson);
        verify(lessonDao, times(1)).remove(lesson);
    }

    interface TestData {
        Subject math = Subject.builder().id(1).name("MATH").build();
        Subject literature = Subject.builder().id(2).name("LITERATURE").build();

        LocalDate today = LocalDate.now();
        LocalDate firstDate = LocalDate.parse("2000-05-05");

        Classroom classroom = Classroom.builder().id(1).number(101).floor(1).capacity(20).build();
        Classroom smallClassroom = Classroom.builder().id(1).number(1).floor(1).capacity(0).build();

        Duration duration = Duration.builder().id(1)
            .startTime(LocalTime.parse("08:30"))
            .endTime(LocalTime.parse("10:10"))
            .build();

        Student student = Student.builder()
            .firstName("FirstName")
            .lastName("LastName")
            .birthDate(LocalDate.parse("1980-05-05"))
            .address(Address.builder().city("City").build())
            .email("student@gmail.com")
            .gender(Gender.MALE)
            .phone("000000000000")
            .studyForm(StudyForm.EXTRAMURAL)
            .build();

        Group group = Group.builder()
            .name("KV4")
            .students(List.of(student))
            .build();

        Teacher teacher = Teacher.builder()
            .skills(List.of(math))
            .vacations(List.of(Vacation.builder().id(1)
                .startDate(LocalDate.parse("2000-05-05"))
                .endDate(LocalDate.parse("2000-06-06")).build())).build();

        Teacher teacherOnVacation = Teacher.builder()
            .skills(List.of(math))
            .vacations(List.of(Vacation.builder().id(1)
                .startDate(LocalDate.parse("2000-01-01"))
                .endDate(LocalDate.parse("2100-01-15")).build())).build();

        Teacher teacherWithLessonOnSameDate = Teacher.builder()
            .skills(List.of(math))
            .vacations(List.of(Vacation.builder().id(1)
                .startDate(LocalDate.parse("2000-05-05"))
                .endDate(LocalDate.parse("2000-06-06")).build())).build();

        Vacation firstVacation = Vacation.builder().id(1).startDate(LocalDate.parse("2000-05-05"))
            .endDate(LocalDate.parse("2000-06-16")).build();

        Lesson lesson = Lesson.builder()
            .subject(math)
            .classroom(classroom)
            .date(firstDate)
            .groups(List.of(group))
            .teacher(teacher)
            .duration(duration)
            .build();

        Lesson lessonWithTeacherOnVacation = Lesson.builder()
            .subject(math)
            .classroom(classroom)
            .date(firstDate)
            .groups(List.of(group))
            .teacher(teacherOnVacation)
            .build();

        Lesson lessonWithTeacherWithoutNecessarySkill = Lesson.builder()
            .subject(literature)
            .classroom(classroom)
            .date(today)
            .groups(List.of(group))
            .teacher(teacher)
            .build();

        Lesson lessonWithClassroomWithoutEnoughSeats = Lesson.builder()
            .subject(math)
            .classroom(smallClassroom)
            .date(firstDate)
            .duration(duration)
            .groups(List.of(group))
            .teacher(teacher)
            .build();

        Lesson firstSameLesson = Lesson.builder()
            .id(1)
            .subject(math)
            .classroom(classroom)
            .date(firstDate)
            .duration(duration)
            .groups(List.of(group))
            .teacher(teacherWithLessonOnSameDate)
            .build();

        Lesson secondSameLesson = Lesson.builder()
            .id(2)
            .subject(math)
            .classroom(classroom)
            .date(firstDate)
            .duration(duration)
            .groups(List.of(group))
            .teacher(teacherWithLessonOnSameDate)
            .build();

        Lesson weekendLesson = Lesson.builder()
            .subject(math)
            .classroom(classroom)
            .duration(duration)
            .groups(List.of(group))
            .teacher(teacherWithLessonOnSameDate)
            .build();
    }
}
