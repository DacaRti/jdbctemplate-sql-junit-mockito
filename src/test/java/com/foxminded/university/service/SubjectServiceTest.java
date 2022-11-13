package com.foxminded.university.service;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.model.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.foxminded.university.service.SubjectServiceTest.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectDao subjectDao;

    @InjectMocks
    private SubjectService subjectService;

    @Test
    void create_ShouldCreateSubject_WhenSubjectIsCorrect() {
        subjectService.create(subject);
        verify(subjectDao, times(1)).create(subject);
    }

    @Test
    void getAll_ShouldReturnListOfSubjects_WhenMethodCalled() {
        List<Subject> expected = List.of(subject, subject);

        when(subjectDao.getAll()).thenReturn(expected);

        List<Subject> actual = subjectService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getById_ShouldReturnSubjectById_WhenSubjectByIdIsExist() {
        when(subjectDao.getById(1)).thenReturn(subject);

        Subject actual = subjectService.getById(1);

        assertEquals(subject, actual);
    }

    @Test
    void update_ShouldChangeSubject_WhenSubjectIsCorrect() {
        subjectService.update(subject);
        verify(subjectDao, times(1)).update(subject);
    }

    @Test
    void remove_ShouldRemoveSubject_WhenSubjectExist() {
        subjectService.remove(subject);
        verify(subjectDao, times(1)).remove(subject);
    }

    interface TestData {
        Subject subject = Subject.builder().id(1).name("Math").build();
    }
}
