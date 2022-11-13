package com.foxminded.university.dao;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.model.Subject;
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
class JdbcSubjectDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SubjectDao subjectDao;

    @Test
    void create_ShouldAddSubjectToTable_WhenSubjectIsCorrect() {
        subjectDao.create(Subject.builder().name("z").build());
        assertEquals(4, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "subjects"));
    }

    @Test
    void getAll_ShouldReturnListOfSubjectsFromDb_WhenMethodCalled() {
        List<Subject> subjects = subjectDao.getAll();
        assertEquals(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "subjects"), subjects.size());
    }

    @Test
    void getById_ShouldReturnSubjectFromDb_WhenSubjectByIdIsExist() {
        Subject actual = subjectDao.getById(1);
        Subject expected = new Subject(1, "MATH");
        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldChangeSubjectInDb_WhenSubjectIsCorrect() {
        subjectDao.update(new Subject(1, "HAPPY"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, "subjects", "name = 'HAPPY'"));
    }

    @Test
    void remove_ShouldRemoveSubjectFromDb_WhenSubjectIsExist() {
        subjectDao.remove(new Subject(2, "LITERATURE"));
        assertEquals(2, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "subjects"));
    }
}
