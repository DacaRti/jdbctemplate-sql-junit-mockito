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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcTeacherDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TeacherDao teacherDao;

    @Test
    void create_ShouldAddTeacherToTable_WhenTeacherIsCorrect() {
        teacherDao.create(Teacher.builder().firstName("TEST").lastName("TEST")
            .address(Address.builder().city("TEST").build())
            .faculty(Faculty.builder().id(1).build())
            .skills(List.of(Subject.builder().id(1).build())).build());

        assertEquals(3, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "teachers"));
    }

    @Test
    void getAll_ShouldReturnListOfTeachersFromDb_WhenMethodCalled() {
        List<Teacher> teachers = teacherDao.getAll();
        assertEquals(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "teachers"), teachers.size());
    }

    @Test
    void getById_ShouldReturnTeacherFromDb_WhenTeacherByIdIsExist() {
        Teacher actual = teacherDao.getById(1);
        Teacher expected = Teacher.builder().firstName("Tsunade").lastName("Senju").gender(Gender.FEMALE)
            .email("").birthDate(LocalDate.parse("1900-03-03")).phone("05034343434")
            .address(Address.builder().id(1).city("a").street("a").postcode("a").district("a").build())
            .degree(Degree.DOCTOR).rank("Hokage").faculty(Faculty.builder().id(1).name("a").syllabus(
                Syllabus.builder().id(1).fullTime(25).build()).build()).build();

        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldChangeTeacherInDb_WhenTeacherIsCorrect() {
        teacherDao.update(Teacher.builder().id(1).firstName("Rebbit").lastName("").gender(Gender.FEMALE)
            .email("").birthDate(LocalDate.parse("1900-03-03")).phone("")
            .address(Address.builder().id(1).city("a").street("a").postcode("a").district("a").build())
            .degree(Degree.DOCTOR).rank("Hokage").faculty(Faculty.builder().id(1).name("a").syllabus(
                Syllabus.builder().id(1).fullTime(25).build()).build())
            .skills(List.of(Subject.builder().id(1).name("Math").build())).build());

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, "teachers", "first_name = 'Rebbit'"));
    }

    @Test
    void remove_ShouldRemoveTeacherFromDb_WhenTeacherIsExist() {
        teacherDao.remove(Teacher.builder().id(1).build());
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "teachers"));
    }
}
