package com.foxminded.university.dao;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.model.Holiday;
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
public class JdbcHolidayDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HolidayDao holidayDao;

    @Test
    void create_ShouldAddHolidayToTable_WhenHolidayIsCorrect() {
        holidayDao.create(Holiday.builder().name("Cristmas").date(LocalDate.parse("2022-05-06")).build());
        assertEquals(3, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "holidays"));
    }

    @Test
    void getAll_ShouldReturnListOfHolidaysFromDb_WhenMethodCalled() {
        List<Holiday> holidays = holidayDao.getAll();
        assertEquals(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "holidays"), holidays.size());
    }

    @Test
    void getById_ShouldReturnHolidayFromDb_WhenHolidayByIdIsExist() {
        Holiday actual = holidayDao.getById(1);
        Holiday expected = Holiday.builder().id(1).name("New Year").date(LocalDate.parse("2022-03-03")).build();
        assertEquals(expected, actual);
    }

    @Test
    void getByDate_ShouldReturnHolidayFromDb_WhenHolidayByDateIsExist() {
        List<Holiday> actual = holidayDao.getByDate(LocalDate.parse("2022-03-03"));
        Holiday holiday = Holiday.builder().id(1).name("New Year").date(LocalDate.parse("2022-03-03")).build();
        List<Holiday> expected = List.of(holiday);

        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldChangeHolidayInDb_WhenHolidayIsCorrect() {
        holidayDao.update(Holiday.builder().id(1).name("TEST").date(LocalDate.parse("2022-03-03")).build());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, "holidays", "name = 'TEST'"));
    }

    @Test
    void remove_ShouldRemoveHolidayFromDb_WhenHolidayIsExist() {
        holidayDao.remove(Holiday.builder().build());
        assertEquals(2, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "holidays"));
    }
}
