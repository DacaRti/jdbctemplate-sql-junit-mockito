package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.model.Group;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupDao groupDao;

    public GroupService(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public void create(Group group) {
        if (isUnique(group)) {
            groupDao.create(group);
        }
    }

    public List<Group> getAll() {
        return groupDao.getAll();
    }

    public Group getById(int id) {
        return groupDao.getById(id);
    }

    public List<Group> getByLessonId(int id) {
        return groupDao.getByLessonId(id);
    }

    public void update(Group group) {
        if (isUnique(group)) {
            groupDao.update(group);
        }
    }

    public void remove(Group group) {
        groupDao.remove(group);
    }

    private boolean isUnique(Group group) {
        Group groupByName = groupDao.getByName(group.getName());
        return groupByName == null || groupByName.getId() == group.getId();
    }
}
