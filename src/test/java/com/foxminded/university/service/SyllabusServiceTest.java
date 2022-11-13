package com.foxminded.university.service;

import com.foxminded.university.dao.SyllabusDao;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Syllabus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.foxminded.university.service.SyllabusServiceTest.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyllabusServiceTest {

    @Mock
    private SyllabusDao syllabusDao;

    @InjectMocks
    private SyllabusService syllabusService;

    @Test
    void create_ShouldCreateSyllabus_WhenSyllabusIsCorrect() {
        syllabusService.create(syllabus);
        verify(syllabusDao, times(1)).create(syllabus);
    }

    @Test
    void create_ShouldNotCreateSyllabus_WhenFullTimeIsNegative() {
        syllabusService.create(syllabusWithWrongFullTime);
        verify(syllabusDao, times(0)).create(syllabusWithWrongFullTime);
    }

    @Test
    void getAll_ShouldReturnListOfSyllabuses_WhenMethodCalled() {
        List<Syllabus> expected = List.of(syllabus, syllabus);

        when(syllabusDao.getAll()).thenReturn(expected);

        List<Syllabus> actual = syllabusService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getById_ShouldReturnSyllabusById_WhenSyllabusByIdIsExist() {
        when(syllabusDao.getById(1)).thenReturn(syllabus);

        Syllabus actual = syllabusService.getById(1);

        assertEquals(syllabus, actual);
    }

    @Test
    void update_ShouldUpdateSyllabus_WhenSyllabusIsCorrect() {
        syllabusService.update(syllabus);
        verify(syllabusDao, times(1)).update(syllabus);
    }

    @Test
    void update_ShouldNotUpdateSyllabus_WhenFullTimeIsNegative() {
        syllabusService.update(syllabusWithWrongFullTime);
        verify(syllabusDao, times(0)).update(syllabusWithWrongFullTime);
    }

    @Test
    void remove_ShouldRemoveSyllabus_WhenSyllabusExist() {
        syllabusService.remove(syllabus);
        verify(syllabusDao, times(1)).remove(syllabus);
    }

    interface TestData {
       Syllabus syllabus = Syllabus.builder().id(1).fullTime(5000)
           .subjects(List.of(Subject.builder().id(1).build())).build();
       Syllabus syllabusWithWrongFullTime = Syllabus.builder().id(1).fullTime(-1)
           .subjects(List.of(Subject.builder().id(1).build())).build();
    }
}
