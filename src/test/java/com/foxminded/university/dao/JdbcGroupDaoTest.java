package com.foxminded.university.dao;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcGroupDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private GroupDao groupDao;

    @Test
    void create_ShouldAddGroupToTable_WhenGroupIsCorrect() {
        groupDao.create(Group.builder().name("TEST").faculty(Faculty.builder().id(1).build()).build());

        assertEquals(4, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "groups"));
    }

    @Test
    void getAll_ShouldReturnListOfGroupsFromDB_WhenMethodCalled() {
        List<Group> groups = groupDao.getAll();

        assertEquals(JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "groups"), groups.size());
    }

    @Test
    void getById_ShouldReturnGroupFromDB_WhenGroupByIdIsExist() {
        Group actual = groupDao.getById(1);
        Group expected = Group.builder().id(1).name("SMK-16").course(1).build();

        assertEquals(expected, actual);
    }

    @Test
    void getByName_ShouldReturnGroupFromDB_WhenGroupByNameIsExist() {
        Group actual = groupDao.getByName("SMK-16");
        Group expected = Group.builder().id(1).name("SMK-16").course(1).build();

        assertEquals(expected, actual);
    }

    @Test
    void getByLessonId_ShouldReturnGroupsFromDB_WhenGroupByLessonIdIsExist() {
        List<Group> actual = groupDao.getByLessonId(1);
        List<Group> expected = List.of(Group.builder().id(1).name("SMK-16").course(1).build());

        assertEquals(expected, actual);
    }

    @Test
    void update_ShouldChangeGroupInDB_WhenGroupIsCorrect() {
        groupDao.update(Group.builder().id(1).name("TEST").course(1)
            .faculty(Faculty.builder().id(1).build()).build());

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, "groups", "name = 'TEST'"));
    }

    @Test
    void remove_ShouldRemoveGroupFromDb_WhenGroupIsExist() {
        groupDao.remove(Group.builder().id(1).name("SMK-16").course(1).build());

        assertEquals(2, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "groups"));
    }
}
