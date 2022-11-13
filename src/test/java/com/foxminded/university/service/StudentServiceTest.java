package com.foxminded.university.service;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.foxminded.university.service.StudentServiceTest.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDao studentDao;

    @InjectMocks
    private StudentService studentService;

    @Test
    void create_ShouldCreateStudent_WhenStudentIsCorrect() {
        studentService.create(student);
        verify(studentDao, times(1)).create(student);
    }

    @Test
    void getAll_ShouldReturnListOfStudents_WhenMethodCalled() {
        List<Student> expected = List.of(student, student);

        when(studentDao.getAll()).thenReturn(expected);

        List<Student> actual = studentService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getById_ShouldReturnStudentById_WhenStudentByIdIsExist() {
        when(studentDao.getById(1)).thenReturn(student);

        Student actual = studentService.getById(1);

        assertEquals(student, actual);
    }

    @Test
    void update_ShouldUpdateStudent_WhenStudentIsCorrect() {
        studentService.update(student);
        verify(studentDao, times(1)).update(student);
    }

    @Test
    void remove_ShouldRemoveStudent_WhenStudentExist() {
        studentService.remove(student);
        verify(studentDao, times(1)).remove(student);
    }

    interface TestData {
        Student student = Student.builder().id(1)
            .firstName("Narutoska")
            .lastName("Uzumaki")
            .gender(Gender.MALE)
            .email("DacaP64@gmail.com")
            .birthDate(LocalDate.parse("1999-05-05"))
            .phone("033333333")
            .address(Address.builder().id(1).city("a").street("a").postcode("a").district("a").build()).build();
    }
}
