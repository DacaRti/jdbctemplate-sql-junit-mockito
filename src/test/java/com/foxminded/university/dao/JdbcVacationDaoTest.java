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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.foxminded.university.dao.JdbcVacationDaoTest.TestData.*;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcVacationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private VacationDao vacationDao;

    @Test
    void create_ShouldAddVacationToTable_WhenVacationIsCorrect() {
        vacationDao.create(Vacation.builder()
            .teacher(Teacher.builder().id(1).build())
            .startDate(LocalDate.parse("1960-03-03"))
            .endDate(LocalDate.parse("1985-03-03")).build());

        assertEquals(3, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "vacations"));
    }

    @Test
    void getAll_ShouldReturnListOfVacationsFromDb_WhenMethodCalled() {
        List<Vacation> vacations = vacationDao.getAll();
        assertEquals(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "vacations"), vacations.size());
    }

    @Test
    void getById_ShouldReturnVacationFromDb_WhenVacationByIdIsExist() {
        Vacation actual = vacationDao.getById(1);
        assertEquals(standardVacation, actual);
    }

    @Test
    void getByTeacherBetweenDateRange_ShouldReturnListVacations_WhenTeacherHaveVacationsOnYear() {
        List<Vacation> actual = vacationDao.getByTeacherBetweenDateRange(Teacher.builder().id(1).build()
            , LocalDate.parse("1960-03-03"), LocalDate.parse("2000-03-03"));
        List<Vacation> expected = List.of(standardVacation);

        assertEquals(expected, actual);
    }

    @Test
    void getByTeacherBetweenDateRange_ShouldNotReturnListVacations_WhenTeacherHaveNotVacationsOnYear() {
        List<Vacation> actual = vacationDao.getByTeacherBetweenDateRange(
            Teacher.builder().id(1).build(), LocalDate.parse("1912-03-03"), LocalDate.parse("1912-03-03"));
        List<Vacation> expected = new ArrayList<>();

        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldChangeVacationInDb_WhenVacationIsCorrect() {
        vacationDao.update(Vacation.builder().id(1)
            .teacher(Teacher.builder().id(1).build())
            .startDate(LocalDate.parse("1960-03-03"))
            .endDate(LocalDate.parse("1985-03-03"))
            .isPaid(true)
            .cause("TEST")
            .build());

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, "vacations", "cause = 'TEST'"));
    }

    @Test
    void remove_ShouldRemoveVacationFromDb_WhenVacationIsExist() {
        vacationDao.remove(standardVacation);

        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "vacations"));
    }

    interface TestData {
        Teacher standardTeacher = Teacher.builder()
            .id(1)
            .firstName("Tsunade")
            .lastName("Senju")
            .gender(Gender.FEMALE)
            .email("Hokage@Gmail.com")
            .birthDate(LocalDate.parse("1900-03-03"))
            .phone("05034343434")
            .address(new Address(1, "a", "a", "a", "a"))
            .degree(Degree.DOCTOR).rank("Hokage")
            .faculty(Faculty.builder()
                .id(1).name("a")
                .syllabus(Syllabus.builder().id(1).fullTime(25).build()).build()).build();

        Vacation standardVacation = Vacation.builder().id(1)
            .teacher(standardTeacher)
            .startDate(LocalDate.parse("1960-03-03"))
            .endDate(LocalDate.parse("1985-03-03"))
            .isPaid(true)
            .cause("cause")
            .build();
    }
}
