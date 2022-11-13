package com.foxminded.university.service;

import com.foxminded.university.dao.SyllabusDao;
import com.foxminded.university.model.Syllabus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyllabusService {

    private final SyllabusDao syllabusDao;

    public SyllabusService(SyllabusDao syllabusDao) {
        this.syllabusDao = syllabusDao;
    }

    public void create(Syllabus syllabus) {
        if (isSyllabusValid(syllabus)) {
            syllabusDao.create(syllabus);
        }
    }

    public List<Syllabus> getAll() {
        return syllabusDao.getAll();
    }

    public Syllabus getById(int id) {
        return syllabusDao.getById(id);
    }

    public void update(Syllabus syllabus) {
        if (isSyllabusValid(syllabus)) {
            syllabusDao.update(syllabus);
        }
    }

    public void remove(Syllabus syllabus) {
        syllabusDao.remove(syllabus);
    }

    private boolean isSyllabusValid(Syllabus syllabus) {
        return syllabus.getFullTime() >= 0;
    }
}
