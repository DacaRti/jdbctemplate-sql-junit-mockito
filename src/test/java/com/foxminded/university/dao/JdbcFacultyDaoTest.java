package com.foxminded.university.dao;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcFacultyDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FacultyDao facultyDao;

    @Test
    void create_ShouldAddFacultyToTable_WhenFacultyIsCorrect() {
        facultyDao.create(Faculty.builder().name("TEST").syllabus(Syllabus.builder().id(1).build()).build());
        assertEquals(3, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "faculties"));
    }

    @Test
    void getAll_ShouldReturnListOfFacultiesFromDB_WhenMethodCalled() {
        List<Faculty> faculties = facultyDao.getAll();
        assertEquals(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "faculties"), faculties.size());
    }

    @Test
    void getById_ShouldReturnFacultyFromDB_WhenFacultyByIdIsExist() {
        Faculty actual = facultyDao.getById(1);
        Faculty expected = Faculty.builder().id(1).name("a")
            .syllabus(Syllabus.builder().id(1).fullTime(25).build()).build();
        assertEquals(expected, actual);
    }

    @Test
    void getByName_ShouldReturnFacultyFromDB_WhenFacultyByNameIsExist() {
        Faculty actual = facultyDao.getByName("a");
        Faculty expected = Faculty.builder().id(1).name("a")
            .syllabus(Syllabus.builder().id(1).fullTime(25).build()).build();
        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldChangeFacultyInDB_WhenFacultyIsCorrect() {
        facultyDao.update(Faculty.builder().id(1).name("TOP")
            .syllabus(Syllabus.builder().id(1).fullTime(25).build())
            .groups(List.of(Group.builder().id(1).name("SMK-16").course(1)
                .faculty(Faculty.builder().id(1).name("a")
                    .syllabus(Syllabus.builder().id(1).fullTime(25).build()).build()).build())).build());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, "faculties", "name = 'TOP'"));
    }

    @Test
    void remove_ShouldRemoveFacultyFromDb_WhenFacultyIsExist() {
        facultyDao.remove(Faculty.builder().id(1).name("a")
            .syllabus(Syllabus.builder().id(1).fullTime(25).build()).build());
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "faculties"));
    }
}
