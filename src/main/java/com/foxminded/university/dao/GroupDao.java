package com.foxminded.university.dao;

import com.foxminded.university.model.Group;

import java.util.List;

public interface GroupDao extends Dao<Group> {

    List<Group> getByLessonId(int id);

    Group getByName(String name);
}
