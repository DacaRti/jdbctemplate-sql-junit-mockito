package com.foxminded.university.service;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.model.Holiday;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.foxminded.university.service.HolidayServiceTest.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayDao holidayDao;

    @InjectMocks
    private HolidayService holidayService;

    @Test
    void create_ShouldCreateHoliday_WhenHolidayIsCorrect() {
        holidayService.create(holiday);
        verify(holidayDao, times(1)).create(holiday);
    }

    @Test
    void getAll_ShouldReturnListOfHolidays_WhenMethodCalled() {
        List<Holiday> expected = List.of(holiday, holiday);

        when(holidayDao.getAll()).thenReturn(expected);

        List<Holiday> actual = holidayService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getById_ShouldReturnHoliday_WhenHolidayByIdIsExist() {
        when(holidayDao.getById(1)).thenReturn(holiday);

        Holiday actual = holidayService.getById(1);

        assertEquals(holiday, actual);
    }

    @Test
    void update_ShouldUpdateHoliday_WhenHolidayIsCorrect() {
        holidayService.update(holiday);
        verify(holidayDao, times(1)).update(holiday);
    }

    @Test
    void remove_ShouldRemoveHoliday_WhenHolidayExist() {
        holidayService.remove(holiday);
        verify(holidayDao, times(1)).remove(holiday);
    }

    interface TestData {
        Holiday holiday = Holiday.builder().id(1).date(LocalDate.parse("2020-12-12")).build();
    }
}

