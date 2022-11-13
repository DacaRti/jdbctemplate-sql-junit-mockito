package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.model.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.foxminded.university.service.GroupServiceTest.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupDao groupDao;

    @InjectMocks
    private GroupService groupService;

    @Test
    void create_ShouldCreateGroup_WhenGroupIsCorrect() {
        groupService.create(firstGroup);
        verify(groupDao, times(1)).create(firstGroup);
    }

    @Test
    void create_ShouldNotCreateGroup_WhenGroupsRepeat() {
        when(groupDao.getByName("SMK")).thenReturn(secondGroup);

        groupService.create(firstGroup);

        verify(groupDao, never()).create(firstGroup);
    }

    @Test
    void getAll_ShouldReturnListOfGroups_WhenMethodCalled() {
        List<Group> expected = List.of(firstGroup, firstGroup);

        when(groupDao.getAll()).thenReturn(expected);

        List<Group> actual = groupService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getById_ShouldReturnGroupById_WhenGroupByIdIsExist() {
        when(groupDao.getById(1)).thenReturn(firstGroup);

        Group actual = groupService.getById(1);

        assertEquals(firstGroup, actual);
    }

    @Test
    void getByLessonId_ShouldReturnGroups_WhenGroupsByLessonIdAreExists() {
        List<Group> expected = List.of(firstGroup, firstGroup);

        when(groupDao.getByLessonId(1)).thenReturn(expected);

        List<Group> actual = groupService.getByLessonId(1);

        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldUpdateGroup_WhenGroupIsCorrect() {
        groupService.update(firstGroup);
        verify(groupDao, times(1)).update(firstGroup);
    }

    @Test
    void update_ShouldUpdateGroup_WhenGroupsHaveSameIdAndNames() {
        when(groupDao.getByName("SMK")).thenReturn(firstGroup);

        groupService.update(updatedFirstGroup);

        verify(groupDao, times(1)).update(updatedFirstGroup);
    }

    @Test
    void update_ShouldNotUpdateGroup_WhenGroupsRepeat() {
        when(groupDao.getByName("SMK")).thenReturn(secondGroup);

        groupService.update(firstGroup);

        verify(groupDao, never()).update(firstGroup);
    }

    @Test
    void remove_ShouldRemoveGroup_WhenGroupExist() {
        groupService.remove(firstGroup);
        verify(groupDao, times(1)).remove(firstGroup);
    }

    interface TestData {
        Group firstGroup = Group.builder().id(1).name("SMK").course(3).build();
        Group updatedFirstGroup = Group.builder().id(1).name("SMK").course(5).build();
        Group secondGroup = Group.builder().id(2).name("SMK").course(3).build();
    }
}
