package com.foxminded.university.dao;

import com.foxminded.university.model.Student;

import java.util.List;

public interface StudentDao extends Dao<Student> {

    List<Student> getByGroupId(int id);
}
