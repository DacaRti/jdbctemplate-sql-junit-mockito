package com.foxminded.university.dao;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcDurationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DurationDao durationDao;

    @Test
    void create_ShouldAddDurationToTable_WhenDurationIsCorrect() {
        durationDao.create(Duration.builder()
            .startTime(LocalTime.of(1, 1, 1))
            .endTime(LocalTime.of(2, 2, 2)).build());
        assertEquals(3, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "durations"));
    }

    @Test
    void getAll_ShouldReturnListOfStudentsFromDb_WhenMethodCalled() {
        List<Duration> durations = durationDao.getAll();
        assertEquals(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "durations"), durations.size());
    }

    @Test
    void getById_ShouldReturnStudentFromDb_WhenGetId() {
        Duration actual = durationDao.getById(1);
        Duration expected = new Duration(1, LocalTime.of(8, 0), LocalTime.of(10, 0));
        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldUpdateDurationInDb_WhenDurationIsCorrect() {
        durationDao.update(Duration.builder().id(1)
            .startTime(LocalTime.of(1, 1))
            .endTime(LocalTime.of(2, 2)).build());

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
            this.jdbcTemplate, "durations", "start_time = '01:01'"));
    }

    @Test
    void remove_ShouldRemoveDurationFromDb_WhenDurationIsExist() {
        durationDao.remove(new Duration(1, LocalTime.of(8, 0, 0), LocalTime.of(10, 0, 0)));
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "durations"));
    }
}
