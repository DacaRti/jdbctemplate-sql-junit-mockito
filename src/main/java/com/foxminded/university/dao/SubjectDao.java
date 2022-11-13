package com.foxminded.university.dao;

import com.foxminded.university.model.Subject;

import java.util.List;

public interface SubjectDao extends Dao<Subject> {

    List<Subject> getByTeacherId(int id);

    List<Subject> getBySyllabusId(int id);
}
