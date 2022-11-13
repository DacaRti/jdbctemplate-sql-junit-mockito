package com.foxminded.university.service;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Teacher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherDao teacherDao;

    public TeacherService(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    public void create(Teacher teacher) {
        teacherDao.create(teacher);
    }

    public List<Teacher> getAll() {
        return teacherDao.getAll();
    }

    public Teacher getById(int id) {
        return teacherDao.getById(id);
    }

    public void update(Teacher teacher) {
        teacherDao.update(teacher);
    }

    public void remove(Teacher teacher) {
        teacherDao.remove(teacher);
    }
}
