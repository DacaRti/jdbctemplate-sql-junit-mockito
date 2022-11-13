package com.foxminded.university.service;

import com.foxminded.university.dao.FacultyDao;
import com.foxminded.university.model.Faculty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyDao facultyDao;

    public FacultyService(FacultyDao facultyDao) {
        this.facultyDao = facultyDao;
    }

    public void create(Faculty faculty) {
        if (isUnique(faculty)) {
            facultyDao.create(faculty);
        }
    }

    public List<Faculty> getAll() {
        return facultyDao.getAll();
    }

    public Faculty getById(int id) {
        return facultyDao.getById(id);
    }

    public void update(Faculty faculty) {
        if (isUnique(faculty)) {
            facultyDao.update(faculty);
        }
    }

    public void remove(Faculty faculty) {
        facultyDao.remove(faculty);
    }

    private boolean isUnique(Faculty faculty) {
        Faculty facultyByName = facultyDao.getByName(faculty.getName());
        return facultyByName == null || facultyByName.getId() == faculty.getId();
    }
}
