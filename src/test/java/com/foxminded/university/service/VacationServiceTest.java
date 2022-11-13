package com.foxminded.university.service;

import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import static com.foxminded.university.service.VacationServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class VacationServiceTest {

    @Mock
    private VacationDao vacationDao;

    @InjectMocks
    private VacationService vacationService;

    @BeforeEach
    public void setFieldForDaysMap() {
        setField(vacationService, "vacationDays", getVacationDaysMap());
    }

    @Test
    void create_ShouldCreateVacation_WhenVacationIsCorrect() {
        vacationService.create(vacation);
        verify(vacationDao, atLeastOnce()).create(vacation);
    }

    @Test
    void create_ShouldNotCreateVacation_WhenVacationsOverlap() {
        when(vacationDao.getByTeacherBetweenDateRange(teacher, vacation.getStartDate(),
            vacation.getEndDate())).thenReturn(List.of(vacation));

        vacationService.create(vacation);

        verify(vacationDao, never()).create(vacation);
    }

    @Test
    void create_ShouldCreateVacation_WhenVacationsNotOverlap() {
        when(vacationDao.getByTeacherBetweenDateRange(teacher, vacation.getStartDate(),
            vacation.getEndDate())).thenReturn(List.of());

        vacationService.create(vacation);

        verify(vacationDao, times(1)).create(vacation);
    }

    @Test
    void create_ShouldNotCreateVacation_WhenVacationWithWrongDuration() {
        vacationService.create(vacationWithWrongDuration);
        verify(vacationDao, never()).create(vacationWithWrongDuration);
    }

    @Test
    void create_ShouldNotCreateVacation_WhenVacationWithExtraDays() {
        when(vacationDao.getByTeacherBetweenDateRange(teacher,
            vacationWithExtraDays.getStartDate(), vacationWithExtraDays.getEndDate())).thenReturn(List.of(vacation));

        vacationService.create(vacationWithExtraDays);

        verify(vacationDao, never()).create(vacationWithExtraDays);
    }

    @Test
    void create_ShouldCreateVacation_WhenTeacherHasVacationDays() {
        when(vacationDao.getByTeacherBetweenDateRange(teacher, vacation.getStartDate(),
            vacation.getEndDate())).thenReturn(List.of());

        vacationService.create(vacation);

        verify(vacationDao, times(1)).create(vacation);
    }

    @Test
    void create_ShouldNotCreateVacation_WhenTeacherHasNotVacationDays() {
        when(vacationDao.getByTeacherBetweenDateRange(teacher, vacation.getStartDate(),
            vacation.getEndDate())).thenReturn(List.of());

        vacationService.create(vacation);

        verify(vacationDao, never()).update(vacation);
    }

    @Test
    void getAll_ShouldReturnListOfVacations_WhenMethodCalled() {
        List<Vacation> expected = List.of(vacation, vacation);

        when(vacationDao.getAll()).thenReturn(expected);

        List<Vacation> actual = vacationService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getById_ShouldReturnVacationById_WhenVacationByIdIsExist() {
        when(vacationDao.getById(1)).thenReturn(vacation);

        Vacation actual = vacationService.getById(1);

        assertEquals(vacation, actual);
    }

    @Test
    void update_ShouldUpdateVacation_WhenVacationIsCorrect() {
        vacationService.update(vacation);
        verify(vacationDao, times(1)).update(vacation);
    }

    @Test
    void update_ShouldUpdateVacation_WhenVacationHaveSameId() {
        when(vacationDao.getByTeacherBetweenDateRange(teacher, updatedVacation.getStartDate(),
            updatedVacation.getEndDate())).thenReturn(List.of(vacation));
        when(vacationDao.getById(updatedVacation.getId())).thenReturn(vacation);

        vacationService.update(updatedVacation);

        verify(vacationDao, times(1)).update(updatedVacation);
    }

    @Test
    void update_ShouldUpdateVacation_WhenTeacherHasVacationDays() {
        when(vacationDao.getById(updatedVacation.getId())).thenReturn(vacation);
        when(vacationDao.getByTeacherBetweenDateRange(teacher, vacation.getStartDate(), vacation.getEndDate()))
            .thenReturn(List.of(vacationWithOneDay));

        vacationService.update(vacation);

        verify(vacationDao, times(1)).update(vacation);
    }

    @Test
    void update_ShouldNotUpdateVacation_WhenVacationWithWrongDuration() {
        vacationService.update(vacationWithWrongDuration);

        verify(vacationDao, never()).update(vacationWithWrongDuration);
    }

    @Test
    void update_ShouldNotUpdateVacation_WhenVacationWithExtraDays() {
        when(vacationDao.getByTeacherBetweenDateRange(teacher,
            vacationWithExtraDays.getStartDate(), vacationWithExtraDays.getEndDate())).thenReturn(List.of(vacation));

        vacationService.update(vacationWithExtraDays);

        verify(vacationDao, never()).update(vacationWithExtraDays);
    }

    @Test
    void update_ShouldNotUpdateVacation_WhenVacationsOverlap() {
        when(vacationDao.getByTeacherBetweenDateRange(teacher,
            vacation.getStartDate(), vacation.getEndDate())).thenReturn(List.of(vacation));

        vacationService.update(vacation);

        verify(vacationDao, never()).update(vacation);
    }

    @Test
    void update_ShouldCreateVacation_WhenVacationsOverlap() {
        when(vacationDao.getByTeacherBetweenDateRange(teacher,
            vacation.getStartDate(), vacation.getEndDate())).thenReturn(List.of());

        vacationService.update(vacation);

        verify(vacationDao, times(1)).update(vacation);
    }

    @Test
    void remove_ShouldRemoveVacation_WhenVacationExist() {
        vacationService.remove(Vacation.builder().build());

        verify(vacationDao, times(1)).remove(Vacation.builder().build());
    }

    interface TestData {
        Teacher teacher = Teacher.builder()
            .firstName("First name")
            .lastName("Last name")
            .gender(Gender.FEMALE)
            .email("email@gmail.com")
            .birthDate(LocalDate.parse("1900-03-03"))
            .phone("05000000000")
            .address(new Address(1, "a", "a", "a", "a"))
            .degree(Degree.ASSOCIATE)
            .rank("Hokage")
            .faculty(Faculty.builder().id(1).name("MATH").syllabus(Syllabus.builder().id(1).fullTime(25).build()).build())
            .vacations(List.of(Vacation.builder().id(1)
                .startDate(LocalDate.parse("2000-05-07"))
                .endDate(LocalDate.parse("2000-05-08")).build())).build();

        Vacation vacation = Vacation.builder().id(1)
            .teacher(teacher)
            .startDate(LocalDate.parse("2000-05-05"))
            .endDate(LocalDate.parse("2000-05-06")).build();

        Vacation updatedVacation = Vacation.builder().id(1)
            .teacher(teacher)
            .startDate(LocalDate.parse("2000-06-05"))
            .endDate(LocalDate.parse("2000-06-10")).build();

        Vacation vacationWithOneDay = Vacation.builder().id(1)
            .teacher(teacher)
            .startDate(LocalDate.parse("2020-05-05"))
            .endDate(LocalDate.parse("2000-05-06")).build();

        Vacation vacationWithWrongDuration = Vacation.builder().id(1)
            .teacher(teacher)
            .startDate(LocalDate.parse("2020-05-05"))
            .endDate(LocalDate.parse("2000-06-06")).build();

        Vacation vacationWithExtraDays = Vacation.builder().id(1)
            .teacher(teacher)
            .startDate(LocalDate.parse("2000-05-05"))
            .endDate(LocalDate.parse("2000-10-06")).build();
    }

    Map<Degree, Integer> getVacationDaysMap() {
        Map<Degree, Integer> vacationDays = new HashMap<>();
        vacationDays.put(Degree.ASSOCIATE, 10);
        vacationDays.put(Degree.BACHELOR, 20);
        vacationDays.put(Degree.MASTER, 30);
        vacationDays.put(Degree.DOCTOR, 40);
        return vacationDays;
    }
}
