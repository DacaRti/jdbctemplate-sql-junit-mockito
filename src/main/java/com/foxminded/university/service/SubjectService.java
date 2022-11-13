package com.foxminded.university.service;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.model.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectDao subjectDao;

    public SubjectService(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    public void create(Subject subject) {
        subjectDao.create(subject);
    }

    public List<Subject> getAll() {
        return subjectDao.getAll();
    }

    public Subject getById(int id) {
        return subjectDao.getById(id);
    }

    public void update(Subject subject) {
        subjectDao.update(subject);
    }

    public void remove(Subject subject) {
        subjectDao.remove(subject);
    }

    public List<Subject> getByTeacherId(int id) {
        return subjectDao.getByTeacherId(id);
    }

    public List<Subject> getBySyllabusId(int id) {
        return subjectDao.getBySyllabusId(id);
    }
}
