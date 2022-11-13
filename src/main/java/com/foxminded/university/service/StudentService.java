package com.foxminded.university.service;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void create(Student student) {
        studentDao.create(student);
    }

    public List<Student> getAll() {
        return studentDao.getAll();
    }

    public Student getById(int id) {
        return studentDao.getById(id);
    }

    public void update(Student student) {
        studentDao.update(student);
    }

    public void remove(Student student) {
        studentDao.remove(student);
    }

    public List<Student> getByGroupId(int id) {
        return studentDao.getByGroupId(id);
    }
}
