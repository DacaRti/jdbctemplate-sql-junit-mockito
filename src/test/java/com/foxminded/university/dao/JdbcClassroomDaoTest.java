package com.foxminded.university.dao;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.model.Classroom;
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
class JdbcClassroomDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ClassroomDao classroomDao;

    @Test
    void create_ShouldCreateClassroom_WhenClassroomIsCorrect() {
        classroomDao.create(Classroom.builder().number(1).floor(1).build());
        assertEquals(5, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "classrooms"));
    }

    @Test
    void getAll_ShouldReturnListOfClassroomsFromDb_WhenMethodCalled() {
        List<Classroom> classrooms = classroomDao.getAll();
        assertEquals(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "classrooms"), classrooms.size());
    }

    @Test
    void getById_ShouldReturnClassroomFromDb_WhenClassroomByIdIsExist() {
        Classroom actual = classroomDao.getById(1);
        Classroom expected = new Classroom(1, 101, 1, 20);
        assertEquals(expected, actual);
    }

    @Test
    void getByNumber_ShouldReturnClassroomFromDb_WhenClassroomByNumberIsExist() {
        Classroom actual = classroomDao.getByNumber(101);
        Classroom expected = new Classroom(1, 101, 1, 20);
        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldUpdateClassroomInDb_WhenClassroomIsCorrect() {
        classroomDao.update(new Classroom(1, 1, 100000, 1));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, "classrooms", "floor = 100000"));
    }

    @Test
    void remove_ShouldRemoveClassroomFromDb_WhenClassroomIsExist() {
        classroomDao.remove(new Classroom(1, 101, 1, 20));
        assertEquals(3, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "classrooms"));
    }
}
