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
class JdbcStudentDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StudentDao studentDao;

    @Test
    void create_ShouldAddStudentToTable_WhenStudentsIsCorrect() {
        studentDao.create(Student.builder().firstName("TEST").lastName("TEST")
            .address(Address.builder().city("TEST").build()).build());

        assertEquals(3, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "students"));
    }

    @Test
    void getAll_ShouldReturnListOfStudentsFromDb_WhenMethodCalled() {
        List<Student> students = studentDao.getAll();
        assertEquals(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "students"), students.size());
    }

    @Test
    void getById_ShouldReturnStudentFromDb_WhenStudentByIdIsExist() {
        Student actual = studentDao.getById(1);
        Student expected = Student.builder().id(1).firstName("Naruto").lastName("Uzumaki").gender(Gender.MALE)
            .email("DacaP64@gmail.com").birthDate(LocalDate.parse("1999-05-05"))
            .phone("033333333").address(Address.builder().id(1).city("a").street("a").postcode("a").district("a").build())
            .studyForm(StudyForm.EXTRAMURAL).build();

        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldChangeStudentInDb_WhenStudentIsCorrect() {
        Student student = Student.builder().id(1)
            .firstName("Narutoska").lastName("Uzumaki").gender(Gender.MALE)
            .email("DacaP64@gmail.com").birthDate(LocalDate.parse("1999-05-05"))
            .phone("033333333").address(Address.builder().id(1).city("a").street("a").postcode("a").district("a").build()).build();

        studentDao.update(student);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, "students", "first_name = 'Narutoska'"));

        student.setGroup(Group.builder().id(1).name("SMK-16").build());

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, "students", "group_id = 1"));
    }

    @Test
    void remove_ShouldRemoveAddressFromTable_WhenStudentsIsExist() {
        studentDao.remove(Student.builder().id(1).build());
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "students"));
    }
}
