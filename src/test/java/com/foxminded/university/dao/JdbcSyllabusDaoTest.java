package com.foxminded.university.dao;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Syllabus;
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
class JdbcSyllabusDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SyllabusDao syllabusDao;

    @Test
    void create_ShouldAddSyllabusToTable_WhenSyllabusIsCorrect() {
        syllabusDao.create(Syllabus.builder().subjects(List.of(Subject.builder().id(1).build())).build());

        assertEquals(3, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "syllabuses"));
    }

    @Test
    void getAll_ShouldReturnListOfSyllabusesFromDb_WhenMethodCalled() {
        List<Syllabus> syllabuses = syllabusDao.getAll();
        assertEquals(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "syllabuses"), syllabuses.size());
    }

    @Test
    void getById_ShouldReturnSyllabusFromDb_WhenSyllabusByIdIsExist() {
        Syllabus actual = syllabusDao.getById(1);
        Syllabus expected = Syllabus.builder().id(1).fullTime(25).build();
        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldChangeSyllabusInDB_WhenSyllabusIsCorrect() {
        syllabusDao.update(Syllabus.builder().id(1).fullTime(5000)
            .subjects(List.of(Subject.builder().id(1).build())).build());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, "syllabuses", "full_time = 5000"));
    }

    @Test
    void remove_ShouldRemoveSyllabusFromTable_WhenSyllabusIsExist() {
        syllabusDao.remove(Syllabus.builder().id(2).build());
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "syllabuses"));
    }
}
