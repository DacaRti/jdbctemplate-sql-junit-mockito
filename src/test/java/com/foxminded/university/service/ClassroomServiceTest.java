package com.foxminded.university.service;

import com.foxminded.university.dao.ClassroomDao;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import static com.foxminded.university.service.ClassroomServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {

    @Mock
    private ClassroomDao classroomDao;

    @InjectMocks
    private ClassroomService classroomService;

    @Test
    void create_ShouldCreateClassroom_WhenClassroomIsCorrect() {
        classroomService.create(firstClassroom);
        verify(classroomDao, times(1)).create(firstClassroom);
    }

    @Test
    void create_ShouldNotCreateClassroom_WhenClassroomWithWrongCapacity() {
        classroomService.create(classroomWithWrongCapacity);
        verify(classroomDao, never()).create(classroomWithWrongCapacity);
    }

    @Test
    void create_ShouldNotCreateClassroom_WhenClassroomsRepeat() {
        when(classroomDao.getByNumber(101)).thenReturn(secondClassroom);

        classroomService.create(firstClassroom);

        verify(classroomDao, never()).create(firstClassroom);
    }

    @Test
    void getAll_ShouldReturnListOfClassrooms_WhenMethodCalled() {
        List<Classroom> expected = List.of(firstClassroom, firstClassroom);

        when(classroomDao.getAll()).thenReturn(expected);

        List<Classroom> actual = classroomService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getById_ShouldReturnClassroom_WhenClassroomByIdIsExist() {
        when(classroomDao.getById(1)).thenReturn(firstClassroom);

        Classroom actual = classroomService.getById(1);

        assertEquals(firstClassroom, actual);
    }

    @Test
    void update_ShouldUpdateClassroom_WhenClassroomIsCorrect() {
        classroomService.update(firstClassroom);
        verify(classroomDao, times(1)).update(firstClassroom);
    }

    @Test
    void update_ShouldUpdateClassroom_WhenClassroomWithSameId() {
        when(classroomDao.getByNumber(101)).thenReturn(firstClassroom);

        classroomService.update(updatedFirstClassroom);

        verify(classroomDao, times(1)).update(updatedFirstClassroom);
    }

    @Test
    void update_ShouldNotUpdateClassroom_WhenClassroomsRepeat() {
        when(classroomDao.getByNumber(101)).thenReturn(secondClassroom);

        classroomService.update(firstClassroom);

        verify(classroomDao, never()).update(firstClassroom);
    }

    @Test
    void update_ShouldNotCreateClassroom_WhenClassroomWithWrongCapacity() {
        classroomService.update(classroomWithWrongCapacity);
        verify(classroomDao, never()).update(classroomWithWrongCapacity);
    }

    @Test
    void remove_ShouldRemoveClassroom_WhenClassroomExist() {
        classroomService.remove(firstClassroom);
        verify(classroomDao, times(1)).remove(firstClassroom);
    }

    interface TestData {
        Classroom firstClassroom = Classroom.builder().id(1).number(101).floor(1).capacity(20).build();
        Classroom updatedFirstClassroom = Classroom.builder().id(1).number(101).floor(5).capacity(60).build();

        Classroom secondClassroom = Classroom.builder().id(2).number(101).floor(1).capacity(20).build();
        Classroom classroomWithWrongCapacity = Classroom.builder().id(2).number(1).floor(1).capacity(0).build();
    }
}
