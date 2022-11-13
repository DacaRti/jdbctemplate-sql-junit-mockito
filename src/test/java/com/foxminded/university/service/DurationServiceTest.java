package com.foxminded.university.service;

import com.foxminded.university.dao.DurationDao;
import com.foxminded.university.model.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static com.foxminded.university.service.DurationServiceTest.TestData.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class DurationServiceTest {

    @Mock
    private DurationDao durationDao;

    @InjectMocks
    private DurationService durationService;

    @BeforeEach
    public void setFieldForDaysMap() {
        setField(durationService, "lessonMinDuration", 30);
    }

    @Test
    void create_ShouldCreateDuration_WhenDurationIsCorrect() {
        durationService.create(duration);
        verify(durationDao, times(1)).create(duration);
    }

    @Test
    void create_ShouldNotCreateDuration_WhenDurationIsNotCorrect() {
        durationService.create(durationWithWrongTime);
        verify(durationDao, never()).create(durationWithWrongTime);
    }

    @Test
    void create_ShouldNotCreateDuration_WhenDurationLessThenThirtyMinutes() {
        durationService.create(smallDuration);
        verify(durationDao, never()).create(smallDuration);
    }

    @Test
    void getAll_ShouldReturnListOfDurations_WhenMethodCalled() {
        List<Duration> expected = List.of(duration, duration);

        when(durationDao.getAll()).thenReturn(expected);

        List<Duration> actual = durationService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getById_ShouldReturnDurationById_WhenDurationByIdIsExist() {
        when(durationDao.getById(1)).thenReturn(duration);

        Duration actual = durationService.getById(1);

        assertEquals(duration, actual);
    }

    @Test
    void update_ShouldUpdateDuration_WhenDurationIsCorrect() {
        durationService.update(duration);
        verify(durationDao, times(1)).update(duration);
    }

    @Test
    void update_ShouldNotUpdateDuration_WhenDurationIsNotCorrect() {
        durationService.update(durationWithWrongTime);
        verify(durationDao, never()).update(durationWithWrongTime);
    }

    @Test
    void update_ShouldNotUpdateDuration_WhenDurationLessThenThirtyMinutes() {
        durationService.update(smallDuration);
        verify(durationDao, never()).update(smallDuration);
    }

    @Test
    void remove_ShouldRemoveDuration_WhenDurationExist() {
        durationService.remove(duration);
        verify(durationDao, times(1)).remove(duration);
    }

    interface TestData {
        Duration duration = Duration.builder().id(1)
            .startTime(LocalTime.parse("08:30"))
            .endTime(LocalTime.parse("10:10"))
            .build();
        Duration durationWithWrongTime = Duration.builder().id(2)
            .startTime(LocalTime.parse("10:10"))
            .endTime(LocalTime.parse("06:30")).build();
        Duration smallDuration = Duration.builder().id(2)
            .startTime(LocalTime.parse("01:00"))
            .endTime(LocalTime.parse("01:05")).build();
    }
}
