package com.foxminded.university.service;

import com.foxminded.university.dao.FacultyDao;
import com.foxminded.university.model.Faculty;
import com.foxminded.university.model.Syllabus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static com.foxminded.university.service.FacultyServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {

    @Mock
    private FacultyDao facultyDao;

    @InjectMocks
    private FacultyService facultyService;

    @Test
    void create_ShouldCreateFaculty_WhenFacultyIsCorrect() {
        facultyService.create(firstFaculty);
        verify(facultyDao, times(1)).create(firstFaculty);
    }

    @Test
    void create_ShouldNotCreateFaculty_WhenFacultyRepeat() {
        when(facultyDao.getByName("AA")).thenReturn(secondFaculty);

        facultyService.create(firstFaculty);

        verify(facultyDao, never()).create(firstFaculty);
    }

    @Test
    void getAll_ShouldReturnListOfFaculties_WhenMethodCalled() {
        List<Faculty> expected = List.of(firstFaculty, firstFaculty);

        when(facultyDao.getAll()).thenReturn(expected);

        List<Faculty> actual = facultyService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getById_ShouldReturnFacultyById_WhenFacultyByIdIsCorrect() {
        when(facultyDao.getById(1)).thenReturn(firstFaculty);

        Faculty actual = facultyService.getById(1);

        assertEquals(firstFaculty, actual);
    }

    @Test
    void update_ShouldUpdateFaculty_WhenFacultyIsCorrect() {
        facultyService.update(firstFaculty);
        verify(facultyDao, times(1)).update(firstFaculty);
    }

    @Test
    void update_ShouldNotUpdateFaculty_WhenFacultyRepeat() {
        when(facultyDao.getByName("AA")).thenReturn(secondFaculty);

        facultyService.update(firstFaculty);

        verify(facultyDao, never()).update(firstFaculty);
    }

    @Test
    void update_ShouldUpdateFaculty_WhenFacultyWithSameNameAndId() {
        when(facultyDao.getByName("AA")).thenReturn(firstFaculty);

        facultyService.update(updatedFirstFaculty);

        verify(facultyDao, times(1)).update(updatedFirstFaculty);
    }

    @Test
    void remove_ShouldRemoveFaculty_WhenFacultyExist() {
        facultyService.remove(firstFaculty);
        verify(facultyDao, times(1)).remove(firstFaculty);
    }

    interface TestData {
        Faculty firstFaculty = Faculty.builder().id(1).name("AA").build();
        Faculty updatedFirstFaculty = Faculty.builder().id(1).name("AA").syllabus(Syllabus.builder().id(2).build()).build();
        Faculty secondFaculty = Faculty.builder().id(2).name("AA").build();
    }
}
