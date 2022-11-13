package com.foxminded.university.service;

import com.foxminded.university.dao.ClassroomDao;
import com.foxminded.university.model.Classroom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomService {

    private final ClassroomDao classroomDao;

    public ClassroomService(ClassroomDao classroomDao) {
        this.classroomDao = classroomDao;
    }

    public void create(Classroom classroom) {
        if (isEnoughCapacity(classroom) && isUnique(classroom)) {
            classroomDao.create(classroom);
        }
    }

    public List<Classroom> getAll() {
        return classroomDao.getAll();
    }

    public Classroom getById(int id) {
        return classroomDao.getById(id);
    }

    public void update(Classroom classroom) {
        if (isEnoughCapacity(classroom) && isUnique(classroom)) {
            classroomDao.update(classroom);
        }
    }

    public void remove(Classroom classroom) {
        classroomDao.remove(classroom);
    }

    private boolean isUnique(Classroom classroom) {
        Classroom classroomByNumber = classroomDao.getByNumber(classroom.getNumber());
        return classroomByNumber == null || classroomByNumber.getId() == classroom.getId();
    }

    private boolean isEnoughCapacity(Classroom classroom) {
        return classroom.getCapacity() > 0;
    }
}
