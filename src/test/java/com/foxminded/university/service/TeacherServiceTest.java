package com.foxminded.university.service;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.foxminded.university.service.TeacherServiceTest.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherDao teacherDao;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void create_ShouldCreateTeacher_WhenTeacherIsCorrect() {
        teacherService.create(teacher);
        verify(teacherDao, times(1)).create(teacher);
    }

    @Test
    void getAll_ShouldReturnListOfTeachers_WhenMethodCalled() {
        List<Teacher> expected = List.of(teacher, teacher);

        when(teacherDao.getAll()).thenReturn(expected);

        List<Teacher> actual = teacherService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getById_ShouldReturnTeacherById_WhenTeacherByIdIsCorrect() {
        when(teacherDao.getById(1)).thenReturn(teacher);

        Teacher actual = teacherService.getById(1);

        assertEquals(teacher, actual);
    }

    @Test
    void update_ShouldUpdateTeacher_WhenTeacherIsCorrect() {
        teacherService.update(teacher);
        verify(teacherDao, times(1)).update(teacher);
    }

    @Test
    void remove_ShouldRemoveTeacher_WhenTeacherExist() {
        teacherService.remove(teacher);
        verify(teacherDao, times(1)).remove(teacher);
    }

    interface TestData {
        Teacher teacher = Teacher.builder()
            .id(1)
            .firstName("Rebbit")
            .lastName("")
            .gender(Gender.FEMALE)
            .email("")
            .birthDate(LocalDate.parse("1900-03-03"))
            .phone("")
            .address(Address.builder().id(1).city("a").street("a").postcode("a").district("a").build())
            .degree(Degree.DOCTOR)
            .rank("Hokage").faculty(Faculty.builder().id(1).name("a").syllabus(Syllabus.builder().id(1).fullTime(25).build()).build())
            .skills(List.of(Subject.builder().id(1).name("Math").build())).build();
    }
}
